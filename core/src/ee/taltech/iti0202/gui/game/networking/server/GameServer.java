package ee.taltech.iti0202.gui.game.networking.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

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

import ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.networking.serializable.Handshake;
import ee.taltech.iti0202.gui.game.networking.serializable.Lobby;
import ee.taltech.iti0202.gui.game.networking.server.listeners.ServerListener;
import ee.taltech.iti0202.gui.game.networking.server.player.Player;

public class GameServer {
    private int tcpPort = 55000;
    private int udpPort = 55001;
    private Server server;
    private String connect = "";

    private String act;
    private String map;
    private B2DVars.GameDifficulty difficulty;

    private Map<Integer, Player> players = new HashMap<>();

    public GameServer() {
        server = new Server();
        server.start();

        Kryo kryo = server.getKryo();
        kryo.register(Handshake.Request.class);
        kryo.register(Handshake.Response.class);
        kryo.register(Lobby.ActMapDifficulty.class);
        kryo.register(Lobby.Kick.class);
        kryo.register(Lobby.NameChange.class);
        kryo.register(Lobby.Details.class);
        kryo.register(HashSet.class);
        kryo.register(B2DVars.GameDifficulty.class);
        kryo.register(Player.class);

        try {
            URL url_name = new URL("http://bot.whatismyipaddress.com");

            BufferedReader sc =
                    new BufferedReader(new InputStreamReader(url_name.openStream()));
            String address = sc.readLine().trim(); //InetAddress.getLocalHost().getHostAddress();
            server.bind(tcpPort, udpPort);
            //server.setPortAndIp(port, "192.168.0.254"); //address);
            address = "192.168.0.254";
            connect = address + ":" + tcpPort + "|" + udpPort;
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
            //TODO: Some actual error handling
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.addListener(new ServerListener(this));
    }

    public String getConnect() {
        return connect;
    }

    public Set<Player> getPlayers() {
        return new HashSet<>(players.values());
    }

    public void updatePlayerName(int id, Lobby.NameChange nameChange) {
        Player player = players.get(id);
        player.name = nameChange.newName;
        players.remove(id);
        players.put(id, player);
        updateLobbyDetails();
    }

    public void kickPlayer(Lobby.Kick player) {
        players.remove(player.playerToBeKicked.id);
        updateLobbyDetails();
    }

    public void updateActMapDifficulty(Lobby.ActMapDifficulty obj) {
        this.act = obj.act;
        this.map = obj.map;
        this.difficulty = obj.difficulty;
        updateLobbyDetails();
    }

    public void updateConnection(int id, Player player) {
        players.put(id, player);
        updateLobbyDetails();
    }

    public void performHandshake(int id) {
        Handshake.Request request = new Handshake.Request();
        for (Player player : players.values()) {
            request.names.add(player.name);
        }
        server.sendToTCP(id, request);
    }

    public void onDisconnected(int id) {
        players.remove(id);
        updateLobbyDetails();
    }

    private void updateLobbyDetails() {
        Lobby.Details details = new Lobby.Details();
        details.act = act;
        details.map = map;
        details.difficulty = difficulty;
        for (Connection con : server.getConnections()) {
            Player player = players.get(con.getID());
            player.latency = con.getReturnTripTime();
            details.players.add(player);
        }
        server.sendToAllTCP(details);
    }

    public Set<String> getNames() {
        Set<String> names = new HashSet<>();
        for (Player player : players.values()) {
            names.add(player.name);
        }
        return names;
    }
}
