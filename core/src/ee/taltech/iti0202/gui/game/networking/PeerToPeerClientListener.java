package ee.taltech.iti0202.gui.game.networking;

import net.corpwar.lib.corpnet.Connection;
import net.corpwar.lib.corpnet.Message;
import net.corpwar.lib.corpnet.PeerReceiverListener;

import java.util.UUID;

public class PeerToPeerClientListener implements PeerReceiverListener {

    @Override
    public void connected(Connection connection) {}

    @Override
    public void receivedMessage(Message message) {}

    @Override
    public void disconnected(UUID connectionId) {}
}
