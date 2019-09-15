package ee.taltech.iti0202.gui.game.desktop.entities.projectile.bullet.handler;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import ee.taltech.iti0202.gui.game.desktop.entities.Handler;
import ee.taltech.iti0202.gui.game.desktop.entities.projectile.bullet.Bullet;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.MyContactListener;

public class BulletHandler implements Handler {
    private Array<Bullet> bulletArray = new Array<>();
    private MyContactListener cl;

    public BulletHandler(MyContactListener cl) {
        this.cl = cl;
    }

    @Override
    public void update(float dt) {
        Array<Bullet> toBeRemoved = new Array<>();
        for (Bullet bullet : bulletArray) {
            if (cl.getCollidedBullets().containsKey(bullet.getBody())) {
                bullet.onHit();
                cl.getCollidedBullets().remove(bullet.getBody());
            }
            if (bullet.toBeRemoved()) {
                toBeRemoved.add(bullet);
            }
            bullet.update(dt);
        }

        bulletArray.removeAll(toBeRemoved, true); // What is identity?
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        for (Bullet bullet : bulletArray) {
            bullet.render(spriteBatch);
        }
    }

    public Array<Bullet> getBulletArray() {
        return bulletArray;
    }
}
