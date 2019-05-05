package ee.taltech.iti0202.gui.game.desktop.entities.player.weapons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars;

public class Deagle extends Weapon {

    public Deagle(World world, SpriteBatch sb, Body body) {
        super(world, sb, body, "images/bullets/deagle/deagle.scml");
        setScale(0.1f);
        setAnimationSpeed(200);
    }
}
