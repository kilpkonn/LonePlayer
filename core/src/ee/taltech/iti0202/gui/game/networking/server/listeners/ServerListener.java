package ee.taltech.iti0202.gui.game.networking.server.listeners;

import net.corpwar.lib.corpnet.Connection;
import net.corpwar.lib.corpnet.DataReceivedListener;
import net.corpwar.lib.corpnet.Message;
import net.corpwar.lib.corpnet.Server;

import java.util.UUID;

import ee.taltech.iti0202.gui.game.networking.server.GameServer;

public class ServerListener implements DataReceivedListener {

    private GameServer server;

    public ServerListener(GameServer server) {
        this.server = server;
    }

    @Override
    public void connected(Connection connection) {
        System.out.println("Connected from: " + connection.getAddress().getHostAddress());
        server.getPlayers().add(connection.getConnectionId());
    }

    @Override
    public void receivedMessage(Message message) {
        System.out.println(new String(message.getData()));
    }

    @Override
    public void disconnected(UUID connectionId) {}
}
