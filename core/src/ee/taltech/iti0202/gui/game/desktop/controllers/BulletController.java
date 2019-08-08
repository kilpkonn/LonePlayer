package ee.taltech.iti0202.gui.game.desktop.controllers;

import com.badlogic.gdx.physics.box2d.Body;
import com.brashmonkey.spriter.Entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
        animations = new ConcurrentHashMap<>();
    }

    public void addAnimation(int id, WeaponProjectile.Type type) {
        Entity entity = AnimationLoader.getData(type.getAnimationFile()).getEntity(0);
        MultiplayerPlayerTweener tweener = new MultiplayerPlayerTweener(entity);
        tweener.speed = 100;
        tweener.setAnimation(Bullet.Animation.FLY);
        animations.put(id, tweener);
    }

    public void updateAnimations(float dt) {
        for (Map.Entry<Integer, MultiplayerPlayerTweener> entry : animations.entrySet()) {
            BulletBody.BulletBodyData data = bullets.get(entry.getKey());
            //TODO: ATM get removed from word first upon hitting border
            if (data != null && data.isHit && entry.getValue().getCurrentAnimation() != Bullet.Animation.HIT) {  //TODO: Some other way not to double push
                entry.getValue().setAnimation(Bullet.Animation.HIT);
                entry.getValue().setOnAnimationEndFunc(() -> data.isToBeRemoved = true);
            }
            entry.getValue().update(dt);
        }
    }

    public Map<Integer, MultiplayerPlayerTweener> getAnimations() {
        return animations;
    }

    public void removeBullet(int id) {
        animations.remove(id);
    }
}
