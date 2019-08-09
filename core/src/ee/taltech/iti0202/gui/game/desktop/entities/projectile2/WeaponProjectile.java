package ee.taltech.iti0202.gui.game.desktop.entities.projectile2;

import com.badlogic.gdx.physics.box2d.Body;

import java.io.Serializable;

import ee.taltech.iti0202.gui.game.desktop.entities.animations.SpriteAnimation2;
import ee.taltech.iti0202.gui.game.desktop.entities.animations.loader.MultiplayerAnimation;
import ee.taltech.iti0202.gui.game.desktop.entities.projectile.bullet.Bullet;

public abstract class WeaponProjectile extends SpriteAnimation2 {

    public WeaponProjectile(Body body, String path) {
        super(body, path);
        setAnimation(Bullet.Animation.FLY);
    }

    public void setAnimation(MultiplayerAnimation animation) {
        playerTweener.setAnimation(animation);
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
