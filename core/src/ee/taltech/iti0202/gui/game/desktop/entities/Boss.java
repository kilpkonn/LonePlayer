package ee.taltech.iti0202.gui.game.desktop.entities;

import com.badlogic.gdx.physics.box2d.Body;

import ee.taltech.iti0202.gui.game.desktop.states.Play;


public abstract class Boss extends B2DSprite {

    private Body body;
    private String type;

    public Play getPlay() {
        return play;
    }

    private Play play;

    Boss(Body body, String type, Play play) {
        super(body);
        body.setUserData(type);
        this.body = body;
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
