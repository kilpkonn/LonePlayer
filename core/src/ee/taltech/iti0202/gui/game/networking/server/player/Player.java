package ee.taltech.iti0202.gui.game.networking.server.player;

import java.io.Serializable;

public class Player implements Serializable {

    public Player () {}

    public Player(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String name;
    public int id;
    public long latency;

    public short health = 100;
    public short wallJump = 0;

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                '}';
    }
}
