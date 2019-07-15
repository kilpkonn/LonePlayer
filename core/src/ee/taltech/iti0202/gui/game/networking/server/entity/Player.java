package ee.taltech.iti0202.gui.game.networking.server.entity;

import java.io.Serializable;

public class Player extends Entity implements Serializable {

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
    public boolean dash = false;
    public boolean onGround = false;
    public boolean doubleJump = false;

    public ee.taltech.iti0202.gui.game.desktop.entities.player.Player.PlayerAnimation animation;
    public boolean flippedAnimation = false;

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                '}';
    }
}
