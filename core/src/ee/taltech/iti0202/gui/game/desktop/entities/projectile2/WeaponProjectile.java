package ee.taltech.iti0202.gui.game.desktop.entities.projectile2;

import com.badlogic.gdx.physics.box2d.Body;

import java.io.Serializable;

import ee.taltech.iti0202.gui.game.desktop.entities.animations.SpriteAnimation2;
import ee.taltech.iti0202.gui.game.desktop.entities.animations.loader.MultiplayerAnimation;

public abstract class WeaponProjectile extends SpriteAnimation2 {

    public WeaponProjectile(Body body, String path) {
        super(body, path);
        setAnimation(Animation.FLY);
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

    public enum Animation implements MultiplayerAnimation, Serializable {
        FLY("fly", false, 50, 0.03f, true),
        HIT("hit", true, 10, 1f, true);

        private final String name;
        private final boolean isToPlayOnce;
        private final float scale;
        private final int speed;
        private final boolean isHardSet;

        Animation(String s, boolean toPlayOnce, int sp, float sca, boolean hardSet) {
            name = s;
            isToPlayOnce = toPlayOnce;
            speed = sp;
            scale = sca;
            isHardSet = hardSet;
        }

        public String toString() {
            return this.name;
        }

        @Override
        public float getScale() {
            return scale;
        }

        @Override
        public int getSpeed() {
            return speed;
        }

        @Override
        public boolean isToPlayOnce() {
            return isToPlayOnce;
        }

        @Override
        public boolean isHardSet() {
            return isHardSet;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
