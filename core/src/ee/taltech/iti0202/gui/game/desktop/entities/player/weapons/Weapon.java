package ee.taltech.iti0202.gui.game.desktop.entities.player.weapons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import ee.taltech.iti0202.gui.game.desktop.entities.animations.SpriteAnimation;
import lombok.Data;

@Data
public abstract class Weapon extends SpriteAnimation {

    protected World world;
    protected Body body;

    public Weapon(World world, SpriteBatch sb, Body body, String path) {
        super(body, sb, path);
        this.world = world;
        this.body = body;
        setAnimation(Animation.DEFAULT.name, false);
        setAnimationSpeed(50);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        super.render(spriteBatch);
    }

    public void fire() {
        setAnimation(Animation.FIRE.name, true);
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
