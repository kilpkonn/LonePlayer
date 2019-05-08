package ee.taltech.iti0202.gui.game.desktop.entities.projectile.bullet;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

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
    public void update(float dt) { //initial velocity must be set after constructing the bullet
        super.update(dt);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        super.render(spriteBatch);
    }

    public void onHit() { //TODO: Call this on bullet hit, make bullets sticky
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

    public enum Animation {
        FLY("fly"),
        HIT("hit");

        private final String name;

        Animation(String s) {
            name = s;
        }

        public String toString() {
            return this.name;
        }
    }
}
