package ee.taltech.iti0202.gui.game.networking.client;

import net.corpwar.lib.corpnet.Client;
import net.corpwar.lib.corpnet.util.SerializationUtils;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.networking.client.listeners.ClientListener;
import ee.taltech.iti0202.gui.game.networking.serializable.Handshake;

public class GameClient {

    private Client client;

    public GameClient(String connect) {
        client = new Client();
        String address = connect.substring(0, connect.indexOf(":"));
        int port = Integer.parseInt(connect.substring(connect.indexOf(":") + 1));
        client.setMillisecondToTimeout(10 * 1000);
        client.setPortAndIp(port, address);
        client.registerClientListerner(new ClientListener(this));
        client.startClient();
    }

    public void performHandshake(Handshake.Request request) {
        Handshake.Response response = new Handshake.Response();
        if (request.names.contains(Game.settings.NAME)) {
            Game.settings.NAME += Math.round(Math.random() * 100);
        }
        response.name = Game.settings.NAME;
        client.sendReliableData(SerializationUtils.getInstance().serialize(response));
    }

    @Deprecated
    public void sendMessage(String message) {
        client.sendReliableData(message.getBytes());
    }
}
