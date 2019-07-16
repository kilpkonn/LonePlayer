package ee.taltech.iti0202.gui.game.networking.server.entity;

import java.io.Serializable;

public class Weapon extends Entity implements Serializable {
    public float angle;
    public boolean flippedAnimation;
    public ee.taltech.iti0202.gui.game.desktop.entities.weapons.Weapon.Animation animation;
    public ee.taltech.iti0202.gui.game.desktop.entities.weapons.Weapon.Type type;
}
