package ee.taltech.iti0202.gui.game.desktop.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

import ee.taltech.iti0202.gui.game.desktop.entities.animated.SpriteAnimation;
import ee.taltech.iti0202.gui.game.desktop.states.Play;


public abstract class Boss extends SpriteAnimation {

    private String type;

    public Play getPlay() {
        return play;
    }

    protected Play play;

    Boss(Body body, SpriteBatch sb, String type, Play play) {
        super(body, sb, "images/bosses/magmaworm/magmaworm.scml"); //TODO: Type...
        body.setUserData(type);
        this.type = type;
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
