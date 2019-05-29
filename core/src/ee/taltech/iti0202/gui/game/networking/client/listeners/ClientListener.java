package ee.taltech.iti0202.gui.game.networking.client.listeners;

import net.corpwar.lib.corpnet.Connection;
import net.corpwar.lib.corpnet.DataReceivedListener;
import net.corpwar.lib.corpnet.Message;
import net.corpwar.lib.corpnet.util.SerializationUtils;

import java.util.UUID;

import ee.taltech.iti0202.gui.game.networking.client.GameClient;
import ee.taltech.iti0202.gui.game.networking.serializable.Handshake;

public class ClientListener implements DataReceivedListener {

    private GameClient client;

    public ClientListener(GameClient client) {
        this.client = client;
    }

    @Override
    public void connected(Connection connection) {
        System.out.println("Connected to: " + connection.getAddress().getHostAddress());
    }

    @Override
    public void receivedMessage(Message message) {
        Object obj = SerializationUtils.getInstance().deserialize(message.getData());

        if (obj instanceof Handshake.Request) {
            client.performHandshake((Handshake.Request) obj);
        }
    }

    @Override
    public void disconnected(UUID connectionId) {}
}
