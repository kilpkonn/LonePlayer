package ee.taltech.iti0202.gui.game.desktop.entities.weapons2;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

public class Deagle extends Weapon {
    public Deagle(Body body, SpriteBatch sb) {
        super(body, sb, Type.DEAGLE.getAnimationFile());
        setScale(0.1f);
        setAnimationSpeed(50);
    }
}