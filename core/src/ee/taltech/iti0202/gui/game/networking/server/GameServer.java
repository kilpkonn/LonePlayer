package ee.taltech.iti0202.gui.game.networking.server;

import net.corpwar.lib.corpnet.Server;

import java.net.Inet4Address;
import java.net.UnknownHostException;

import ee.taltech.iti0202.gui.game.networking.server.listeners.ServerListener;

public class GameServer {
    Server server;
    String connect = "";

    public GameServer() {
        server = new Server();
        server.setKeepAlive(true);

        try {
            String address = Inet4Address.getLocalHost().getHostAddress();
            int port = 55000 + (int) Math.round(Math.random() * 1000);
            server.setPortAndIp(55778, address);
            connect = address + ":" + port;
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
            //TODO: Some actual error handling
        }
        server.registerServerListerner(new ServerListener());
        server.startServer();
    }

    public String getConnect() {
        return connect;
    }
}
