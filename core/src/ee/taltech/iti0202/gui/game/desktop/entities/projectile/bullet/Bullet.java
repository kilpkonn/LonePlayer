package ee.taltech.iti0202.gui.game.desktop.entities.projectile.bullet;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import java.io.Serializable;

import ee.taltech.iti0202.gui.game.desktop.entities.animations.loader.MultiplayerAnimation;
import ee.taltech.iti0202.gui.game.desktop.entities.projectile.WeaponProjectile;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Bullet extends WeaponProjectile {

    private World world;
    private Vector2 position;
    private Body body;
    private SpriteBatch spriteBatch;
    private boolean hit = false;

    public Bullet(World world, SpriteBatch sb, Body body) {
        super(body, sb, "images/bullets/bullet_default/bullet.scml");
        this.world = world;
        this.body = body;
        this.spriteBatch = sb;
        setAnimation(Animation.FLY.name, false);
        setScale(0.03f);
        setAnimationSpeed(50);
        body.setUserData("bullet");
    }

    @Override
    public void update(float dt) {
        super.update(dt);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        super.render(spriteBatch);
    }

    public void onHit() {
        hardSetAnimation(Animation.HIT.name, true);
        setStopOnAnimationOver(true);
        setScale(1f);
        setAnimationSpeed(10);
        hit = true;
        super.update(0);
    }

    public boolean toBeRemoved() {
        return hit && isAnimationOver();
    }

    public enum Animation implements MultiplayerAnimation, Serializable {
        FLY("fly", false, 50, 0.03f),
        HIT("hit", true, 10, 1f);

        private final String name;
        private final float scale;
        private final int speed;
        private final boolean isToPlayOnce;

        Animation(String s, boolean toPlayOnce, int sp, float sca) {
            name = s;
            isToPlayOnce = toPlayOnce;
            speed = sp;
            scale = sca;
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
        public String getName() {
            return name;
        }
    }
}
