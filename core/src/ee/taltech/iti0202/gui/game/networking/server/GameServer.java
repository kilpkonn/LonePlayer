package ee.taltech.iti0202.gui.game.networking.server;

import net.corpwar.lib.corpnet.Server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import ee.taltech.iti0202.gui.game.networking.server.listeners.ServerListener;

public class GameServer {
    Server server;
    String connect = "";

    private Set<UUID> players = new HashSet<>();

    public GameServer() {
        server = new Server();
        server.setKeepAlive(true);


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

    public Set<UUID> getPlayers() {
        return players;
    }
}
