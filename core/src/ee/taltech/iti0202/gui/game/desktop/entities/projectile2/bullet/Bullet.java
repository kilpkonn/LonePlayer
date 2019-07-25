package ee.taltech.iti0202.gui.game.desktop.entities.projectile2.bullet;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

import ee.taltech.iti0202.gui.game.desktop.entities.projectile2.WeaponProjectile;

public class Bullet extends WeaponProjectile {

    public Bullet(Body body, SpriteBatch sb) {
        super(body, sb, Type.BULLET.getAnimationFile());
        setScale(0.03f);
    }
}
