package ee.taltech.iti0202.gui.game.desktop.controllers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.brashmonkey.spriter.Entity;

import java.util.HashMap;
import java.util.Map;

import ee.taltech.iti0202.gui.game.desktop.entities.animations.MultiplayerPlayerTweener;
import ee.taltech.iti0202.gui.game.desktop.entities.animations.loader.AnimationLoader;
import ee.taltech.iti0202.gui.game.desktop.entities.projectile2.bullet.Bullet;
import ee.taltech.iti0202.gui.game.desktop.entities.weapons2.Weapon;
import ee.taltech.iti0202.gui.game.desktop.physics.PlayerBody;
import ee.taltech.iti0202.gui.game.desktop.physics.WeaponBody;
import ee.taltech.iti0202.gui.game.networking.server.ServerLogic;

public class WeaponController {

    private ServerLogic serverLogic;
    private Map<Integer, Body> weaponBodies;
    private Map<Integer, WeaponBody.WeaponBodyData> weapons;
    private Map<Integer, MultiplayerPlayerTweener> animations;

    public WeaponController(Map<Integer, Body> weaponBodies, Map<Integer, WeaponBody.WeaponBodyData> weapons, ServerLogic serverLogic) {
        this.weaponBodies = weaponBodies;
        this.weapons = weapons;
        this.serverLogic = serverLogic;
        animations = new HashMap<>();
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

    public void updateFiring(PlayerBody.PlayerBodyData playerBodyData) {
        if (playerBodyData.weapons[playerBodyData.currentWeaponIndex] == null) return;
        Body body = weaponBodies.get(playerBodyData.weapons[playerBodyData.currentWeaponIndex].id);
        WeaponBody.WeaponBodyData data = weapons.get(playerBodyData.weapons[playerBodyData.currentWeaponIndex].id);
        MultiplayerPlayerTweener weaponTweener = animations.get(playerBodyData.weapons[playerBodyData.currentWeaponIndex].id);

        if (playerBodyData.isAiming && data.bulletHeat <= 0) {  // TODO: Fix no shooting animation when aim moving
            weaponTweener.setAnimation(ee.taltech.iti0202.gui.game.desktop.entities.weapons.Weapon.Animation.FIRE);
            data.bulletHeat = data.type.getData().getCoolDown();

            if (serverLogic != null) {
                for (Weapon.BulletInitData b : data.type.getData().generateBulletsShot(body.getPosition(), playerBodyData.aimAngle, Bullet.Type.BULLET)) {  //TODO: Bullet types..
                    ee.taltech.iti0202.gui.game.networking.server.entity.Bullet bullet = new ee.taltech.iti0202.gui.game.networking.server.entity.Bullet();
                    bullet.position = b.pos;
                    bullet.velocity = b.velocity;
                    bullet.angle = b.angle;
                    bullet.type = b.type;
                    serverLogic.addBullet(bullet);
                }
            }
        }
    }

    public boolean trySetWeaponTransform(int id, Vector2 pos, float angle) {
        if (weaponBodies.containsKey(id)) {
            weaponBodies.get(id).setTransform(pos, angle);
            return true;
        }
        return false;
    }

    public Map<Integer, MultiplayerPlayerTweener> getAnimations() {
        return animations;
    }

    public void removeWeapon(int id) {
        animations.remove(id);
    }
}
