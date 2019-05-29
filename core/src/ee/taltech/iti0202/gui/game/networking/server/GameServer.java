package ee.taltech.iti0202.gui.game.networking.server;

import net.corpwar.lib.corpnet.Server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import ee.taltech.iti0202.gui.game.networking.serializable.Handshake;
import ee.taltech.iti0202.gui.game.networking.serializable.Lobby;
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

    public void updateConnection(UUID uuid, Player player) {
        players.put(uuid, player);
        updateLobbyDetails();
    }

    public void performHandshake(UUID uuid) {
        Handshake.Request request = new Handshake.Request();
        for (Player player : players.values()) {
            request.names.add(player.name);
        }
        server.sendReliableObjectToClient(request, uuid);
    }

    public void onDisconnected(UUID uuid) {
        players.remove(uuid);
        updateLobbyDetails();
    }

    private void updateLobbyDetails() {
        Lobby.Details details = new Lobby.Details();
        details.players = getPlayers();
        for (Player player : details.players) {
            player.latency = server.getConnectionFromUUID(player.uuid).getLastPingTime();
        }
        server.sendReliableObjectToAllClients(details);
    }

    public Set<String> getNames() {
        Set<String> names = new HashSet<>();
        for (Player player : players.values()) {
            names.add(player.name);
        }
        return names;
    }
}
