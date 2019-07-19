package ee.taltech.iti0202.gui.game.desktop.controllers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.brashmonkey.spriter.Entity;
import com.brashmonkey.spriter.Timeline;

import java.util.HashMap;
import java.util.Map;

import ee.taltech.iti0202.gui.game.desktop.entities.animations.MultiplayerPlayerTweener;
import ee.taltech.iti0202.gui.game.desktop.entities.animations.loader.AnimationLoader;
import ee.taltech.iti0202.gui.game.desktop.entities.weapons2.Weapon;
import ee.taltech.iti0202.gui.game.desktop.physics.PlayerBody;
import ee.taltech.iti0202.gui.game.desktop.physics.WeaponBody;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.PPM;

public class WeaponController {

    private Map<Integer, Body> weaponBodies;
    private Map<Integer, WeaponBody.WeaponBodyData> weapons;
    private Map<Integer, MultiplayerPlayerTweener> animations;
    private PlayerController playerController;

    public WeaponController(Map<Integer, Body> weaponBodies, Map<Integer, WeaponBody.WeaponBodyData> weapons, PlayerController playerController) {
        this.weaponBodies = weaponBodies;
        this.weapons = weapons;
        this.playerController = playerController;
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
        /*for (PlayerBody.PlayerBodyData player : playerController.getPlayers().values()) {
            Timeline.Key.Bone hand = playerController.getAnimations().get(player.id).getBone("right_hand");
            for (WeaponBody.WeaponBodyData weapon : player.weapons) {
                if (weapon != null) {
                    weaponBodies.get(weapon.id).setTransform(
                            new Vector2(hand.position.x / PPM, hand.position.y / PPM),
                            (float) Math.toRadians(hand.angle));
                }
            }
        }*/
        for (MultiplayerPlayerTweener playerTweener : animations.values()) {
            playerTweener.update(dt);
        }
    }

    public Map<Integer, MultiplayerPlayerTweener> getAnimations() {
        return animations;
    }
}
