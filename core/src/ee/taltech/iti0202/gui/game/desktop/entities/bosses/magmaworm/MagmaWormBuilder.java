package ee.taltech.iti0202.gui.game.desktop.entities.bosses.magmaworm;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

import ee.taltech.iti0202.gui.game.desktop.states.Play;

public class MagmaWormBuilder {
    private Body body;
    private SpriteBatch sb;
    private String type;
    private Play play;
    private MagmaWorm.Part part;
    private float size;
    private float x = 0;
    private float y = 0;

    public MagmaWormBuilder setBody(Body body) {
        this.body = body;
        return this;
    }

    public MagmaWormBuilder setSb(SpriteBatch sb) {
        this.sb = sb;
        return this;
    }

    public MagmaWormBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public MagmaWormBuilder setPlay(Play play) {
        this.play = play;
        return this;
    }

    public MagmaWormBuilder setPart(MagmaWorm.Part part) {
        this.part = part;
        return this;
    }

    public MagmaWormBuilder setSize(float size) {
        this.size = size;
        return this;
    }

    public MagmaWormBuilder setX(float x) {
        this.x = x;
        return this;
    }

    public MagmaWormBuilder setY(float y) {
        this.y = y;
        return this;
    }

    public MagmaWorm createMagmaWorm() {
        return new MagmaWorm(body, sb, type, play, part, size, x, y);
    }
}