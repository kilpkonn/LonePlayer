package ee.taltech.iti0202.gui.game.desktop.entities.weapons2;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.io.Serializable;

import ee.taltech.iti0202.gui.game.desktop.entities.animations.SpriteAnimation2;
import ee.taltech.iti0202.gui.game.desktop.entities.animations.loader.MultiplayerAnimation;
import ee.taltech.iti0202.gui.game.desktop.entities.projectile2.bullet.Bullet;

public class Weapon extends SpriteAnimation2 {

    public boolean isDropped;

    public Weapon(Body body, String path) {
        super(body, path);
        setAnimation(Animation.DEFAULT);
    }

    public void setAnimation(MultiplayerAnimation animation) {
        playerTweener.setAnimation(animation);
    }

    @Override
    public void render(SpriteBatch sb) {
        if (isDropped) {
            super.render(sb);
        }
    }

    public void forceRender(SpriteBatch sb) {
        super.render(sb);
    }

    public static class BulletInitData {
        public BulletInitData(Vector2 pos, Vector2 velocity, float angle, Bullet.Type type) {
            this.pos = pos;
            this.velocity = velocity;
            this.angle = angle;
            this.type = type;
        }

        public Vector2 pos;
        public Vector2 velocity;
        public float angle;
        public Bullet.Type type;
    }

    public enum Type implements Serializable {
        DEAGLE ("images/bullets/deagle/deagle.scml", WeaponData.DEAGLE),
        SHOTGUN ("images/bullets/shotgun/shotgun.scml", WeaponData.SHOTGUN),
        M4 ("images/bullets/m4/m4.scml", WeaponData.M4);

        private final String animationFile;
        private final WeaponData data;

        Type(String t, WeaponData d) {
            animationFile = t;
            data = d;
        }

        public String getAnimationFile() {
            return animationFile;
        }

        public WeaponData getData() {
            return data;
        }
    }

    public enum Animation implements MultiplayerAnimation, Serializable {
        DEFAULT("default", false, 100, 0.1f, false),
        FIRE("fire", true, 100, 0.1f, true);

        private final String name;
        private boolean isToPlayOnce;
        private final float scale;
        private final int speed;
        private final boolean isHardSet;

        Animation(String s, boolean playOnce, int sp, float sca, boolean hardSet) {
            name = s;
            isToPlayOnce = playOnce;
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
