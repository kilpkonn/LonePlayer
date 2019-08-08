package ee.taltech.iti0202.gui.game.desktop.entities.projectile2;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

import java.io.Serializable;

import ee.taltech.iti0202.gui.game.desktop.entities.animations.SpriteAnimation;
import ee.taltech.iti0202.gui.game.desktop.entities.animations.loader.MultiplayerAnimation;
import ee.taltech.iti0202.gui.game.desktop.entities.projectile.bullet.Bullet;

public abstract class WeaponProjectile extends SpriteAnimation {

    public WeaponProjectile(Body body, SpriteBatch sb, String path) {
        super(body, sb, path);
    }

    public void setAnimation(MultiplayerAnimation animation) {
        setAnimation(animation.getName(), animation.isToPlayOnce());
        if (animation.getName().equals(Bullet.Animation.HIT.getName())) hardSetAnimation(animation.getName(), animation.isToPlayOnce());  //TODO: Something better here
        setAnimationSpeed(animation.getSpeed());
        setScale(animation.getScale());
    }

    public enum Type implements Serializable {
        BULLET ("images/bullets/bullet_default/bullet.scml");

        private final String animationFile;

        Type(String t) {
            animationFile = t;
        }

        public String getAnimationFile() {
            return animationFile;
        }
    }
}
