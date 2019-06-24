package ee.taltech.iti0202.gui.game.networking.server.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import ee.taltech.iti0202.gui.game.networking.serializable.Handshake;
import ee.taltech.iti0202.gui.game.networking.serializable.Lobby;
import ee.taltech.iti0202.gui.game.networking.server.GameServer;
import ee.taltech.iti0202.gui.game.networking.server.player.Player;

public class ServerListener extends Listener {

    private GameServer server;

    public ServerListener(GameServer server) {
        this.server = server;
    }

    @Override
    public void connected(Connection connection) {
        System.out.println("Player connected from: " + connection.getRemoteAddressTCP());
        server.performHandshake(connection.getID());
    }

    @Override
    public void disconnected(Connection connection) {
        server.onDisconnected(connection.getID());
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof Handshake.Response) {
            Handshake.Response response = (Handshake.Response) object;
            if (server.getNames().contains(response.name)) {
                server.performHandshake(connection.getID());
                return;
            }
            Player player = new Player(response.name, connection.getID());
            server.updateConnection(connection.getID(), player);
        } else if (object instanceof Lobby.NameChange) {
            server.updatePlayerName(connection.getID(), (Lobby.NameChange) object);
        } else if (object instanceof Lobby.Kick) {
            server.kickPlayer((Lobby.Kick) object);
        } else if (object instanceof Lobby.ActMapDifficulty) {
            server.updateActMapDifficulty((Lobby.ActMapDifficulty) object);
        }
    }

    @Override
    public void idle(Connection connection) {
        super.idle(connection);
    }
}
