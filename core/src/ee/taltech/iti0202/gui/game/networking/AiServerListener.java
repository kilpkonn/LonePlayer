package ee.taltech.iti0202.gui.game.networking;

import net.corpwar.lib.corpnet.Connection;
import net.corpwar.lib.corpnet.DataReceivedListener;
import net.corpwar.lib.corpnet.Message;

import java.util.UUID;

public class AiServerListener implements DataReceivedListener {
    @Override
    public void connected(Connection connection) {
        System.out.println(connection);
    }

    @Override
    public void receivedMessage(Message message) {
        System.out.println(new String(message.getData()));
    }

    @Override
    public void disconnected(UUID connectionId) {
        System.out.println(connectionId);
    }
}
