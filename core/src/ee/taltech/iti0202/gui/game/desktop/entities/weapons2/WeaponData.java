package ee.taltech.iti0202.gui.game.desktop.entities.weapons2;

public enum  WeaponData {
    DEAGLE (1f, 100),
    SHOTGUN (1.5f, 100),
    M4 (0.2f , 400);

    private final float coolDown;
    private final int bulletDmg;

    WeaponData(float c, int dmg) {
        coolDown = c;
        bulletDmg = dmg;
    }

    public float getCoolDown() {
        return coolDown;
    }

    public int getBulletDmg() {
        return bulletDmg;
    }
}
