package ee.taltech.iti0202.gui.game.desktop.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

import ee.taltech.iti0202.gui.game.desktop.entities.animated.SpriteAnimation;
import ee.taltech.iti0202.gui.game.desktop.states.Play;


public abstract class Boss extends SpriteAnimation {

    public Play getPlay() {
        return play;
    }

    protected Play play;

    Boss(Body body, SpriteBatch sb, Play play, String path, String entity) {
        super(body, sb, path, entity);
        this.play = play;
    }

    Boss(Body body, SpriteBatch sb, Play play, String path, String entity, float x, float y) {
        super(body, sb, path, entity, x, y);
        this.play = play;
    }

    @Override
    public void update(float dt) {
        super.update(dt);
    }

    public void updateHeadSmall(float dt) {
        super.update(dt);
    }

    public void updateHeadBig(float dt) {
        super.update(dt);
    }
}
