package ee.taltech.iti0202.gui.game.networking.server;

import net.corpwar.lib.corpnet.Server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import ee.taltech.iti0202.gui.game.networking.serializable.Handshake;
import ee.taltech.iti0202.gui.game.networking.server.listeners.ServerListener;
import ee.taltech.iti0202.gui.game.networking.server.player.Player;

public class GameServer {
    private Server server;
    private String connect = "";

    private Map<UUID, Player> players = new HashMap<>();

    public GameServer() {
        server = new Server();
        server.setKeepAlive(true);
        server.keepConnectionsAlive();

        try {
            String address = InetAddress.getLocalHost().getHostAddress();
            int port = 55000 + (int) Math.round(Math.random() * 1000);
            server.setPortAndIp(port, address);
            connect = address + ":" + port;
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
            //TODO: Some actual error handling
        }
        server.registerServerListerner(new ServerListener(this));
        server.startServer();
    }

    public String getConnect() {
        return connect;
    }

    public Set<Player> getPlayers() {
        return new HashSet<>(players.values());
    }

    public void addPlayer(Player player) {
        if (!players.values().contains(player)) {
            updateConnection(player.uuid, player);
        }
    }

    public void updateConnection(UUID uuid, Player player) {
        players.put(uuid, player);
    }

    public void performHandshake(UUID uuid) {
        Handshake.Request request = new Handshake.Request();
        server.sendReliableObjectToClient(request, uuid);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!players.get(uuid).handshakeDone) {
                    players.remove(uuid);
                }
                cancel();  // Cleans up thread
            }
        }, 2 * 1000);
    }
}
