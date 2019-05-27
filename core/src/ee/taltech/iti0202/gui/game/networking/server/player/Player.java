package ee.taltech.iti0202.gui.game.networking.server.player;

import java.util.UUID;

public class Player {

    public Player(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public String name;
    public UUID uuid;
    public boolean handshakeDone = false;

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                '}';
    }
}
