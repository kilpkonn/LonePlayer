package ee.taltech.iti0202.gui.game.networking.client;

import net.corpwar.lib.corpnet.Client;

public class GameClient {

    private Client client;

    public GameClient(String connect) {
        client = new Client();
        String address = connect.substring(0, connect.indexOf(":"));
        int port = Integer.parseInt(connect.substring(connect.indexOf(":") + 1));
        System.out.println(address);
        System.out.println(port);
        System.out.println(connect);
        client.setPortAndIp(port, address);
        client.startClient();
    }

    public void sendMessage(String message) {
        client.sendReliableData(message.getBytes());
    }
}
