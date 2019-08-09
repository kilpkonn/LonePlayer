package ee.taltech.iti0202.gui.game.desktop.entities.weapons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import java.io.Serializable;

import ee.taltech.iti0202.gui.game.desktop.entities.animations.SpriteAnimation;
import ee.taltech.iti0202.gui.game.desktop.entities.animations.loader.MultiplayerAnimation;
import ee.taltech.iti0202.gui.game.desktop.entities.projectile.bullet.Bullet;
import ee.taltech.iti0202.gui.game.desktop.entities.projectile.bullet.loader.BulletLoader;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.canvas.Draw;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.V_WIDTH;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class Weapon extends SpriteAnimation {

    protected World world;
    protected Draw draw;
    protected float coolDown = 0;
    protected float bulletHeat = 0;
    protected double offRadius = 0;
    private SpriteBatch sb;

    public Weapon(World world, SpriteBatch sb, Body body, String path, Draw draw) {
        super(body, sb, path);
        this.sb = sb;
        this.world = world;
        this.draw = draw;
        setAnimation(Animation.DEFAULT.name, false);
        setAnimationSpeed(50);
    }

    @Override
    public void update(float dt) {
        if (bulletHeat > 0) {
            bulletHeat -= dt;
        } else {
            setAnimation(Animation.DEFAULT.name, false);
        }
        super.update(dt);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        super.render(spriteBatch);
    }

    public void fire(Vector2 destination) {
        bulletHeat = coolDown;
        setAnimation(Animation.FIRE.name, true);
        Bullet bullet =
                BulletLoader.bulletLoader(
                        sb,
                        world,
                        new Vector2(V_WIDTH >> 1, V_HEIGHT >> 1),
                        destination,
                        body.getPosition(),
                        offRadius);

        draw.getBulletHandler().getBulletArray().add(bullet);
    }

    public void setAnimation(Animation animation) {
        setAnimation(animation.getName(), animation.isToPlayOnce());
    }

    public boolean canFire() {
        return bulletHeat <= 0;
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
