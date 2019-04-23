package ee.taltech.iti0202.gui.game.desktop.entities.bosses.snowman;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

import ee.taltech.iti0202.gui.game.desktop.states.Play;

public class SnowManBuilder {
    private Body body;
    private SpriteBatch sb;
    private String type;
    private Play play;
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

    public SnowManBuilder setPlay(Play play) {
        this.play = play;
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
        return new SnowMan(body, sb, type, play, x, y);
    }
}