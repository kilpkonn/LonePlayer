package ee.taltech.iti0202.gui.game.networking.server.entity;

import java.io.Serializable;

import ee.taltech.iti0202.gui.game.desktop.entities.animations.loader.MultiplayerAnimation;

public class WeaponEntity extends Entity implements Serializable {
    public float angle;
    public boolean flippedAnimation;
    public MultiplayerAnimation animation;
    public ee.taltech.iti0202.gui.game.desktop.entities.weapons2.Weapon.Type type;

    public boolean dropped;
}
