package ee.taltech.iti0202.gui.game.desktop.entities;

import com.badlogic.gdx.physics.box2d.Body;


public abstract class Boss extends B2DSprite {

    private Body body;
    private String type;

    Boss(Body body, String type) {

        super(body);

        this.body = body;
        this.type = type;

    }

}
