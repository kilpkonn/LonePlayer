package ee.taltech.iti0202.gui.game.desktop.entities.projectile.bullet.handler;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import ee.taltech.iti0202.gui.game.desktop.entities.Handler;
import ee.taltech.iti0202.gui.game.desktop.entities.projectile.bullet.Bullet;
import lombok.Data;

@Data
public class BulletHandler implements Handler {
    private Array<Bullet> bulletArray = new Array<>();

    public BulletHandler() {
    }

    @Override
    public void update(float dt) {
        for (Bullet bullet : bulletArray) {
            bullet.update(dt);
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        for (Bullet bullet : bulletArray) {
            bullet.render(spriteBatch);
        }
    }
}
