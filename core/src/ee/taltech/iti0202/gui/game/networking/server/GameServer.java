package ee.taltech.iti0202.gui.game.networking.server;

import net.corpwar.lib.corpnet.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
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
        server.setWaitingQue(true);
        server.setMaxConnections(6);
        server.setMilisecoundToTimeout(20000);
        server.keepConnectionsAlive();

        try {
            URL url_name = new URL("http://bot.whatismyipaddress.com");

            BufferedReader sc =
                    new BufferedReader(new InputStreamReader(url_name.openStream()));
            String address = sc.readLine().trim(); //InetAddress.getLocalHost().getHostAddress();
            int port = 55000; // + (int) Math.round(Math.random() * 1000);
            server.setPortAndIp(port, "192.168.0.254"); //address);
            connect = address + ":" + port;
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
            //TODO: Some actual error handling
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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

    public void updatePlayerName(UUID uuid, Lobby.NameChange nameChange) {
        Player player = players.get(uuid);
        player.name = nameChange.newName;
        players.remove(uuid);
        players.put(uuid, player);
        updateLobbyDetails();
    }

    public void kickPlayer(Lobby.Kick player) {
        players.remove(player.playerToBeKicked.uuid);
        updateLobbyDetails();
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
