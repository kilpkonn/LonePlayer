package ee.taltech.iti0202.gui.game.desktop.entities.projectile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

import ee.taltech.iti0202.gui.game.desktop.entities.animations.SpriteAnimation;

public abstract class WeaponProjectile extends SpriteAnimation {

    public WeaponProjectile(Body body, SpriteBatch sb, String path) {
        super(body, sb, path);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        super.render(spriteBatch);
    }
}
