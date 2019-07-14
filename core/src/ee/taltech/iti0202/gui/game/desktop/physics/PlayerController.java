package ee.taltech.iti0202.gui.game.desktop.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.brashmonkey.spriter.Entity;

import java.util.HashMap;
import java.util.Map;

import ee.taltech.iti0202.gui.game.desktop.entities.animations.MultiplayerPlayerTweener;
import ee.taltech.iti0202.gui.game.desktop.entities.animations.loader.AnimationLoader;
import ee.taltech.iti0202.gui.game.desktop.entities.player.Player;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.MAX_SPEED;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.PLAYER_DASH_FORCE_SIDE;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.PLAYER_DASH_FORCE_UP;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.PLAYER_SPEED;

public class PlayerController {
    private Map<Integer, Body> playerBodies;
    private Map<Integer, PlayerBody.PlayerBodyData> players;
    private Map<Integer, MultiplayerPlayerTweener> animations;

    private Entity playerEntity;

    public PlayerController(Map<Integer, Body> playerBodies, Map<Integer, PlayerBody.PlayerBodyData> players) {
        this.playerBodies = playerBodies;
        this.players = players;
        playerEntity = AnimationLoader.getData("images/player/rogue.scml").getEntity(0);
        this.animations = new HashMap<>();
        for (int i : players.keySet()) {
            addAnimation(i);
        }
    }

    public void addAnimation(int id) {
        MultiplayerPlayerTweener tweener = new MultiplayerPlayerTweener(playerEntity);
        tweener.speed = 100;
        animations.put(id, tweener);
    }

    public void updateAnimations(float dt) {
        for (MultiplayerPlayerTweener playerTweener : animations.values()) {
            playerTweener.update(dt);
        }
    }

    public boolean tryJump(int id) {
        PlayerBody.PlayerBodyData data = players.get(id);
        Body body = playerBodies.get(id);
        MultiplayerPlayerTweener animation = animations.get(id);

        if (data.onGround) {
            body.applyLinearImpulse(new Vector2(0, PLAYER_DASH_FORCE_UP), body.getPosition(), true);
            animation.setAnimation(Player.PlayerAnimation.JUMP);
            return true;
        } else if (data.wallJump != 0) {
            body.applyLinearImpulse(new Vector2(data.wallJump * PLAYER_DASH_FORCE_UP, PLAYER_DASH_FORCE_UP),
                    body.getPosition(), true);
            data.wallJump = 0;
            return true;
        } else if (data.doubleJump) {
            body.applyLinearImpulse(new Vector2(0, PLAYER_DASH_FORCE_UP), body.getPosition(), true);
            animation.setAnimation(Player.PlayerAnimation.ROLL);
            data.doubleJump = false;
            return true;
        }

        return false;
    }

    public boolean tryMoveLeft(int id) {
        PlayerBody.PlayerBodyData data = players.get(id);
        Body body = playerBodies.get(id);
        MultiplayerPlayerTweener animation = animations.get(id);
        if (body.getLinearVelocity().x > -MAX_SPEED) {
            if (data.onGround) {
                body.applyForceToCenter(-PLAYER_SPEED, 0, true);
            } else {
               body.applyForceToCenter(-PLAYER_SPEED * 1.25f, 0, true);
            }
            animation.setAnimation(Player.PlayerAnimation.RUN);
            data.flippedAnimation = true;
            return true;
        }
        return false;
    }

    public boolean tryMoveRight(int id) {
        PlayerBody.PlayerBodyData data = players.get(id);
        Body body = playerBodies.get(id);
        MultiplayerPlayerTweener animation = animations.get(id);
        if (body.getLinearVelocity().x < MAX_SPEED) {
            if (data.onGround) {
                body.applyForceToCenter(PLAYER_SPEED, 0, true);
            } else {
                body.applyForceToCenter(PLAYER_SPEED * 1.25f, 0, true);
            }
            animation.setAnimation(Player.PlayerAnimation.RUN);
            data.flippedAnimation = false;
            return true;
        }
        return false;
    }

    public boolean tryDashLeft(int id) {
        PlayerBody.PlayerBodyData data = players.get(id);
        Body body = playerBodies.get(id);
        MultiplayerPlayerTweener animation = animations.get(id);

        if (!data.onGround && data.dash) {
            if (body.getLinearVelocity().x > 0) {
                body.applyLinearImpulse(new Vector2(-body.getLinearVelocity().x, 0), body.getPosition(), true);
            } else {
                body.applyLinearImpulse(new Vector2(-PLAYER_DASH_FORCE_SIDE, 0), body.getPosition(), true);
            }
            data.dash = false;
            animation.setAnimation(Player.PlayerAnimation.DASH);
            data.flippedAnimation = true;
            return true;
        }
        return false;
    }

    public boolean tryDashRight(int id) {
        PlayerBody.PlayerBodyData data = players.get(id);
        Body body = playerBodies.get(id);
        MultiplayerPlayerTweener animation = animations.get(id);

        if (!data.onGround && data.dash) {
            if (body.getLinearVelocity().x < 0) {
                body.applyLinearImpulse(new Vector2(-body.getLinearVelocity().x, 0), body.getPosition(), true);
            } else {
                body.applyLinearImpulse(new Vector2(PLAYER_DASH_FORCE_SIDE, 0), body.getPosition(), true);
            }
            data.dash = false;

            data.flippedAnimation = false;
            animation.setAnimation(Player.PlayerAnimation.DASH);
            return true;
        }
        return false;
    }

    public Map<Integer, MultiplayerPlayerTweener> getAnimations() {
        return animations;
    }
}
