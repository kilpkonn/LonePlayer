package ee.taltech.iti0202.gui.game.desktop.entities.bosses.snowman;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

import ee.taltech.iti0202.gui.game.desktop.entities.player.handler.PlayerHandler;

public class SnowManBuilder {
    private Body body;
    private SpriteBatch sb;
    private String type;
    private PlayerHandler playerHandler;
    private float size;
    private float x;
    private float y;

    public SnowManBuilder setBody(Body body) {
        this.body = body;
        return this;
    }

    public SnowManBuilder setSb(SpriteBatch sb) {
        this.sb = sb;
        return this;
    }

    public SnowManBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public SnowManBuilder setPlayerHandler(PlayerHandler playerHandler) {
        this.playerHandler = playerHandler;
        return this;
    }

    public SnowManBuilder setSize(float size) {
        this.size = size;
        return this;
    }

    public SnowManBuilder setX(float x) {
        this.x = x;
        return this;
    }

    public SnowManBuilder setY(float y) {
        this.y = y;
        return this;
    }

    public SnowMan createSnowMan() {
        return new SnowMan(body, sb, type, playerHandler, size, x, y);
    }
}