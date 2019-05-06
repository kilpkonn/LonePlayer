package ee.taltech.iti0202.gui.game.desktop.entities.weapons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import ee.taltech.iti0202.gui.game.desktop.entities.animations.SpriteAnimation;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class Weapon extends SpriteAnimation {

    protected World world;
    protected Body body;
    protected float coolDown;
    protected float bulletHeat;

    public Weapon(World world, SpriteBatch sb, Body body, String path) {
        super(body, sb, path);
        this.world = world;
        this.body = body;
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

    public void fire() {
        bulletHeat = coolDown;
        setAnimation(Animation.FIRE.name, true);
    }

    public boolean canFire() {
        return bulletHeat <= 0;
    }

    public enum Animation {
        DEFAULT("default"),
        FIRE("fire");

        private final String name;

        Animation(String s) {
            name = s;
        }

        public String toString() {
            return this.name;
        }
    }
}
