package ee.taltech.iti0202.gui.game.desktop.entities.weapons2;

import com.badlogic.gdx.math.Vector2;

import java.util.HashSet;
import java.util.Set;

import ee.taltech.iti0202.gui.game.desktop.entities.projectile2.bullet.Bullet;

public enum  WeaponData {
    DEAGLE (1f, 1),
    SHOTGUN (1.5f, 5),
    M4 (0.2f , 1);

    private final float coolDown;
    private final int bulletsPerShot;

    WeaponData(float c, int b) {
        coolDown = c;
        bulletsPerShot = b;
    }

    public float getCoolDown() {
        return coolDown;
    }

    public int getBulletsPerShot() {
        return bulletsPerShot;
    }

    public Set<Weapon.BulletInitData> generateBulletsShot(Vector2 pos, float angle, Bullet.Type type) {
        Set<Weapon.BulletInitData> bullets = new HashSet<>();
        for (int i = 0; i < bulletsPerShot; i++) {
            float x = (float) (Math.cos(angle) + Math.random() * 0.1 * 1000);
            float y = (float) (Math.sin(angle) + Math.random() * 0.1 * 1000);
            bullets.add(new Weapon.BulletInitData(pos, new Vector2(x, y), type));
        }
        return bullets;
    }
}
