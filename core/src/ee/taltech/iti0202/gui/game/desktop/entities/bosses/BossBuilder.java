package ee.taltech.iti0202.gui.game.desktop.entities.bosses;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

import ee.taltech.iti0202.gui.game.desktop.states.Play;

public class BossBuilder {
    private Body body;
    private SpriteBatch sb;
    private Play play;
    private String path;
    private String entity;
    private float x = 0;
    private float y = 0;

    public BossBuilder setBody(Body body) {
        this.body = body;
        return this;
    }

    public BossBuilder setSb(SpriteBatch sb) {
        this.sb = sb;
        return this;
    }

    public BossBuilder setPlay(Play play) {
        this.play = play;
        return this;
    }

    public BossBuilder setPath(String path) {
        this.path = path;
        return this;
    }

    public BossBuilder setEntity(String entity) {
        this.entity = entity;
        return this;
    }

    public BossBuilder setX(float x) {
        this.x = x;
        return this;
    }

    public BossBuilder setY(float y) {
        this.y = y;
        return this;
    }

    public Boss createBoss() {
        if (x == 0 || y == 0) {
            return new Boss(body, sb, play, path, entity);
        }
        return new Boss(body, sb, play, path, entity, x, y);
    }
}