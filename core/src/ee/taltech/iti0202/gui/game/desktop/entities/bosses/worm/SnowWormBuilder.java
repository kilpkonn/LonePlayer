package ee.taltech.iti0202.gui.game.desktop.entities.bosses.worm;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

import ee.taltech.iti0202.gui.game.desktop.entities.bosses.Boss;
import ee.taltech.iti0202.gui.game.desktop.entities.player.handler.PlayerHandler;

public class SnowWormBuilder {
    private Body body;
    private SpriteBatch sb;
    private String type;
    private PlayerHandler playerHandler;
    private Boss.Part part;
    private float size;
    private float x = 0;
    private float y = 0;

    public SnowWormBuilder setBody(Body body) {
        this.body = body;
        return this;
    }

    public SnowWormBuilder setSb(SpriteBatch sb) {
        this.sb = sb;
        return this;
    }

    public SnowWormBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public SnowWormBuilder setPlayerHandler(PlayerHandler playerHandler) {
        this.playerHandler = playerHandler;
        return this;
    }

    public SnowWormBuilder setPart(Boss.Part part) {
        this.part = part;
        return this;
    }

    public SnowWormBuilder setSize(float size) {
        this.size = size;
        return this;
    }

    public SnowWormBuilder setX(float x) {
        this.x = x;
        return this;
    }

    public SnowWormBuilder setY(float y) {
        this.y = y;
        return this;
    }

    public SnowWorm createSnowWorm() {
        return new SnowWorm(body, sb, type, playerHandler, part, size, x, y);
    }
}