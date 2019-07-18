package ee.taltech.iti0202.gui.game.desktop.entities.weapons2;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

public class M4 extends Weapon {

    public M4(Body body, SpriteBatch sb) {
        super(body, sb, ee.taltech.iti0202.gui.game.desktop.entities.weapons.Weapon.Type.M4.getAnimationFile());
    }
}
