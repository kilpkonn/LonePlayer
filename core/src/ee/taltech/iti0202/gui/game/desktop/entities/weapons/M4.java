package ee.taltech.iti0202.gui.game.desktop.entities.weapons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.canvas.Draw;

public class M4 extends Weapon {

    public M4(World world, SpriteBatch sb, Body body, Draw draw) {
        super(world, sb, body, Type.M4.getAnimationFile(), draw);
        setScale(0.1f);
        setAnimationSpeed(50);
        coolDown = 0.2f;
        offRadius = 50d;
    }
}
