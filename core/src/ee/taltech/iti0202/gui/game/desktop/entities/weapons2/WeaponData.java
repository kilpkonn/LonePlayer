package ee.taltech.iti0202.gui.game.desktop.entities.weapons2;

import com.badlogic.gdx.math.Vector2;

import java.util.HashSet;
import java.util.Set;

import ee.taltech.iti0202.gui.game.desktop.entities.projectile2.bullet.Bullet;

public enum  WeaponData {
    DEAGLE (0.8f, 1),
    SHOTGUN (1f, 5),
    M4 (0.15f , 1);

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
            float x = (float) (Math.cos(angle) + Math.random() * 0.1) * 100f;
            float y = (float) (Math.sin(angle) + Math.random() * 0.1) * 100f;
            //pos = new Vector2(pos.x + x * 0.1f, pos.y + y * 0.1f); // Spawn 0.1 s ahead to avoid collision with shooter
            bullets.add(new Weapon.BulletInitData(pos, new Vector2(x, y), angle, type));
        }
        return bullets;
    }
}
