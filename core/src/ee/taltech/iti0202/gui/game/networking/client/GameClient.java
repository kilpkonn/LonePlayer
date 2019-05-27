package ee.taltech.iti0202.gui.game.networking.client;

import net.corpwar.lib.corpnet.Client;

public class GameClient {

    private Client client;

    public GameClient() {
        client = new Client();
        client.setPortAndIp(55778, "127.0.0.1");
        client.startClient();
    }

    public void sendMessage(String message) {
        client.sendReliableData(message.getBytes());
    }
}
