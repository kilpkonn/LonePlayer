package ee.taltech.iti0202.gui.game.desktop.entities.weapons2;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

public class M4 extends Weapon {

    public M4(Body body, SpriteBatch sb) {
        super(body, sb, Type.M4.getAnimationFile());
        setScale(0.1f);
        setAnimationSpeed(Type.M4.getAnimationSpeed());
    }
}
