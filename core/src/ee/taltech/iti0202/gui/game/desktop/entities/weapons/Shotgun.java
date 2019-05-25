package ee.taltech.iti0202.gui.game.desktop.entities.weapons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class Shotgun extends Weapon {

    public Shotgun(World world, SpriteBatch sb, Body body) {
        super(world, sb, body, "images/bullets/shotgun/shotgun.scml");
        setScale(0.1f);
        setAnimationSpeed(50);
        coolDown = 1.5f;
    }
}
