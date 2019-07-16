package ee.taltech.iti0202.gui.game.desktop.controllers;

import com.badlogic.gdx.physics.box2d.Body;

import java.util.HashMap;
import java.util.Map;

import ee.taltech.iti0202.gui.game.desktop.entities.animations.MultiplayerPlayerTweener;
import ee.taltech.iti0202.gui.game.desktop.physics.WeaponBody;

public class WeaponController {

    private Map<Integer, Body> weaponBodies;
    private Map<Integer, WeaponBody.WeaponBodyData> weapons;
    private Map<Integer, MultiplayerPlayerTweener> animations;

    public WeaponController(Map<Integer, Body> weaponBodies, Map<Integer, WeaponBody.WeaponBodyData> weapons) {
        this.weaponBodies = weaponBodies;
        this.weapons = weapons;
        this.animations = new HashMap<>();
    }

    public void addAnimation(int id) {

    }
}
