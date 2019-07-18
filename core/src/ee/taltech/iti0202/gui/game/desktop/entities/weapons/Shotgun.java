package ee.taltech.iti0202.gui.game.desktop.entities.weapons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.canvas.Draw;

public class Shotgun extends Weapon {
    private int bulletsPerShot;

    public Shotgun(World world, SpriteBatch sb, Body body, Draw draw) {
        super(world, sb, body, Type.SHOTGUN.getAnimationFile(), draw);
        setScale(0.1f);
        setAnimationSpeed(50);
        coolDown = 1.5f;
        bulletsPerShot = 5;
    }

    @Override
    public void fire(Vector2 destination) {
        for (int i = 0; i < bulletsPerShot; i++) {
            super.fire(destination.add(0, (float) -Math.floor(bulletsPerShot * 20 / 2f) + i * 20));
        }
    }
}
