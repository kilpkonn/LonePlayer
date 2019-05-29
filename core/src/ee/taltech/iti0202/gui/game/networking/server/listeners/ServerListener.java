package ee.taltech.iti0202.gui.game.networking.server.listeners;

import net.corpwar.lib.corpnet.Connection;
import net.corpwar.lib.corpnet.DataReceivedListener;
import net.corpwar.lib.corpnet.Message;
import net.corpwar.lib.corpnet.Server;
import net.corpwar.lib.corpnet.util.SerializationUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import ee.taltech.iti0202.gui.game.networking.serializable.Handshake;
import ee.taltech.iti0202.gui.game.networking.server.GameServer;
import ee.taltech.iti0202.gui.game.networking.server.player.Player;

public class ServerListener implements DataReceivedListener {

    private GameServer server;

    public ServerListener(GameServer server) {
        this.server = server;
    }

    @Override
    public void connected(Connection connection) {
        System.out.println("Player connected from: " + connection.getAddress().getHostAddress() + " -> " + connection.getConnectionId());
        server.performHandshake(connection.getConnectionId());
    }

    @Override
    public void receivedMessage(Message message) {
        Object obj = SerializationUtils.getInstance().deserialize(message.getData());

        if (obj instanceof Handshake.Response) {
            Handshake.Response response = (Handshake.Response) obj;
            if (server.getNames().contains(response.name)) {
                server.performHandshake(message.getConnectionID());
                return;
            }
            Player player = new Player(response.name, message.getConnectionID());
            server.updateConnection(message.getConnectionID(), player);
        }
    }

    @Override
    public void disconnected(UUID connectionId) {
        server.onDisconnected(connectionId);
    }
}
