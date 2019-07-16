package ee.taltech.iti0202.gui.game.networking.client.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import ee.taltech.iti0202.gui.game.networking.client.GameClient;
import ee.taltech.iti0202.gui.game.networking.serializable.Handshake;
import ee.taltech.iti0202.gui.game.networking.serializable.Lobby;
import ee.taltech.iti0202.gui.game.networking.serializable.Play;

public class ClientListener extends Listener {

    private GameClient client;

    public ClientListener(GameClient client) {
        this.client = client;
    }

    @Override
    public void connected(Connection connection) {
        System.out.println("Connected to: " + connection.getRemoteAddressTCP().getHostName());
    }

    @Override
    public void disconnected(Connection connection) {
        super.disconnected(connection);
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof Handshake.Request) {
            client.performHandshake((Handshake.Request) object);
        } else if (object instanceof Lobby.Details) {
            client.updateLobbyDetails((Lobby.Details) object);
        } else if (object instanceof Lobby.StartGame) {
            client.onStartGame((Lobby.StartGame) object);
        } else if (object instanceof Play.Players) {
            client.onUpdatePlayers((Play.Players) object);
        } else if (object instanceof Play.Weapons) {
            client.onUpdateWeapons((Play.Weapons) object);
        }
    }

    @Override
    public void idle(Connection connection) {
        super.idle(connection);
    }

}
