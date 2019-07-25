package ee.taltech.iti0202.gui.game.desktop.controllers;

import com.badlogic.gdx.physics.box2d.Body;
import com.brashmonkey.spriter.Entity;

import java.util.HashMap;
import java.util.Map;

import ee.taltech.iti0202.gui.game.desktop.entities.animations.MultiplayerPlayerTweener;
import ee.taltech.iti0202.gui.game.desktop.entities.animations.loader.AnimationLoader;
import ee.taltech.iti0202.gui.game.desktop.entities.weapons2.Weapon;
import ee.taltech.iti0202.gui.game.desktop.physics.PlayerBody;
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

    public void addAnimation(int id, Weapon.Type type) {
        Entity entity = AnimationLoader.getData(type.getAnimationFile()).getEntity(0);
        MultiplayerPlayerTweener tweener = new MultiplayerPlayerTweener(entity);
        tweener.speed = 100;
        tweener.setAnimation(ee.taltech.iti0202.gui.game.desktop.entities.weapons.Weapon.Animation.DEFAULT);
        animations.put(id, tweener);
    }

    public void updateAnimations(float dt) {
        for (WeaponBody.WeaponBodyData data : weapons.values()) {
            if (data.bulletHeat > 0) data.bulletHeat -= dt;
        }
        for (MultiplayerPlayerTweener weaponTweener : animations.values()) {
            weaponTweener.update(dt);
        }
    }

    public void updateFireing(PlayerBody.PlayerBodyData playerBodyData) {
        if (playerBodyData.weapons[playerBodyData.currentWeaponIndex] == null) return;
        WeaponBody.WeaponBodyData data = weapons.get(playerBodyData.weapons[playerBodyData.currentWeaponIndex].id);
        MultiplayerPlayerTweener weaponTweener = animations.get(playerBodyData.weapons[playerBodyData.currentWeaponIndex].id);

        if (playerBodyData.isAiming && data.bulletHeat <= 0) {
            weaponTweener.setAnimation(ee.taltech.iti0202.gui.game.desktop.entities.weapons.Weapon.Animation.FIRE);
        } else {
            weaponTweener.setAnimation(ee.taltech.iti0202.gui.game.desktop.entities.weapons.Weapon.Animation.DEFAULT);
        }
    }

    public Map<Integer, MultiplayerPlayerTweener> getAnimations() {
        return animations;
    }
}
