package ee.taltech.iti0202.gui.game.desktop.entities.projectile.bullet;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import ee.taltech.iti0202.gui.game.desktop.entities.projectile.WeaponProjectile;
import lombok.Data;

@Data
public class Bullet extends WeaponProjectile {

    private World world;
    private Vector2 position;
    private Body body;
    private SpriteBatch spriteBatch;

    public Bullet(World world, SpriteBatch sb, String path, Body body) {
        super(body, sb, path);
        this.world = world;
        this.body = body;
        this.spriteBatch = sb;

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
}
