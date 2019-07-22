package ee.taltech.iti0202.gui.game.desktop.controllers;

import com.badlogic.gdx.physics.box2d.Body;
import com.brashmonkey.spriter.Entity;

import java.util.HashMap;
import java.util.Map;

import ee.taltech.iti0202.gui.game.desktop.entities.animations.MultiplayerPlayerTweener;
import ee.taltech.iti0202.gui.game.desktop.entities.animations.loader.AnimationLoader;
import ee.taltech.iti0202.gui.game.desktop.entities.projectile.bullet.Bullet;
import ee.taltech.iti0202.gui.game.desktop.entities.projectile2.WeaponProjectile;
import ee.taltech.iti0202.gui.game.desktop.physics.BulletBody;

public class BulletController {
    private Map<Integer, Body> bulletBodies;
    private Map<Integer, BulletBody.BulletBodyData> bullets;
    private Map<Integer, MultiplayerPlayerTweener> animations;

    public BulletController(Map<Integer, Body> bulletBodies, Map<Integer, BulletBody.BulletBodyData> bullets) {
        this.bulletBodies = bulletBodies;
        this.bullets = bullets;
        animations = new HashMap<>();
    }

    public void addAnimation(int id, WeaponProjectile.Type type) {
        Entity entity = AnimationLoader.getData(type.getAnimationFile()).getEntity(0);
        MultiplayerPlayerTweener tweener = new MultiplayerPlayerTweener(entity);
        tweener.speed = 100;
        tweener.setAnimation(Bullet.Animation.FLY);
        animations.put(id, tweener);
    }

    public void updateAnimations(float dt) {
        for (MultiplayerPlayerTweener weaponTweener : animations.values()) {
            weaponTweener.update(dt);
        }
    }

    public Map<Integer, MultiplayerPlayerTweener> getAnimations() {
        return animations;
    }
}
