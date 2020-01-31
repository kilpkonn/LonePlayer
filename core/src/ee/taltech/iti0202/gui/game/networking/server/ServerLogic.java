package ee.taltech.iti0202.gui.game.networking.server;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.controllers.BulletController;
import ee.taltech.iti0202.gui.game.desktop.controllers.PlayerController;
import ee.taltech.iti0202.gui.game.desktop.controllers.WeaponController;
import ee.taltech.iti0202.gui.game.desktop.entities.animations.MultiplayerPlayerTweener;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.desktop.physics.BulletBody;
import ee.taltech.iti0202.gui.game.desktop.physics.GameWorld;
import ee.taltech.iti0202.gui.game.desktop.physics.PlayerBody;
import ee.taltech.iti0202.gui.game.desktop.physics.WeaponBody;
import ee.taltech.iti0202.gui.game.networking.serializable.Play;
import ee.taltech.iti0202.gui.game.networking.server.entity.BulletEntity;
import ee.taltech.iti0202.gui.game.networking.server.entity.PlayerControls;
import ee.taltech.iti0202.gui.game.networking.server.entity.PlayerEntity;
import ee.taltech.iti0202.gui.game.networking.server.entity.WeaponEntity;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BOSSES;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BOSS_BASE_HP;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.CHECKPOINTS;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.DMG_MULTIPLIER;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.DMG_ON_LANDING_SPEED;

public class ServerLogic implements Disposable {
    private GameWorld gameWorld;
    private ServerLogicThread logicThread;
    private PlayerController playerController;
    private WeaponController weaponController;
    private BulletController bulletController;

    private Map<Integer, WeaponEntity> weapons = new ConcurrentHashMap<>();
    private Map<Integer, BulletEntity> bullets = new ConcurrentHashMap<>();

    public void loadWorld(String act, String map) {
        if (gameWorld != null) gameWorld.dispose();
        gameWorld = new GameWorld(act, map);
        bulletController = new BulletController(gameWorld.getBulletBodies(), gameWorld.getBullets());
        weaponController = new WeaponController(gameWorld.getWeaponBodies(), gameWorld.getWeapons(), this);
        playerController = new PlayerController(gameWorld.getPlayerBodies(), gameWorld.getPlayers(), weaponController);
    }

    public void setDifficulty(B2DVars.GameDifficulty difficulty) {
        switch (difficulty) {
            case EASY:
                DMG_MULTIPLIER = 1;
                DMG_ON_LANDING_SPEED = 6;
                CHECKPOINTS = true;
                BOSSES = false;
                break;

            case HARD:
                DMG_MULTIPLIER = 1.5f;
                DMG_ON_LANDING_SPEED = 5;
                CHECKPOINTS = true;
                BOSSES = true;
                break;

            case BRUTAL:
                DMG_MULTIPLIER = 2;
                DMG_ON_LANDING_SPEED = 4;
                CHECKPOINTS = true;
                BOSS_BASE_HP *= 2;
                BOSSES = true;
                break;
        }
    }

    public void run(Set<PlayerEntity> players) {
        if (logicThread != null) logicThread.close();
        logicThread = new ServerLogicThread(players, weapons, bullets);
        logicThread.start();
    }

    public void addPlayer(PlayerEntity player) {
        player.bodyId = gameWorld.addPlayer();
        PlayerBody.PlayerBodyData bodyData = gameWorld.getPlayers().get(player.bodyId);
        bodyData.kills = player.kills;  //Keep kills
        bodyData.damage = player.damage; //Keep dmg
        playerController.addAnimation(player.bodyId);
    }

    public void addWeapon(ee.taltech.iti0202.gui.game.desktop.entities.weapons2.Weapon.Type type) {
        WeaponEntity weapon = new WeaponEntity();
        weapon.bodyId = gameWorld.addWeapon(type);
        weapon.type = type;
        weaponController.addAnimation(weapon.bodyId, weapon.type);
        weapons.put(weapon.bodyId, weapon);
    }

    public void addBullet(BulletEntity bullet) {
        bullet.bodyId = gameWorld.addBullet(bullet.position, bullet.velocity, bullet.angle, bullet.type, bullet.shooterId);
        bulletController.addAnimation(bullet.bodyId, bullet.type);
        bullets.put(bullet.bodyId, bullet);
    }

    public void updatePlayerControls(PlayerControls controls) {
        if (controls.jump) playerController.tryJump(controls.bodyId);
        if (controls.moveLeft) playerController.tryMoveLeft(controls.bodyId);
        if (controls.moveRight) playerController.tryMoveRight(controls.bodyId);
        if (controls.dashLeft) playerController.tryDashLeft(controls.bodyId);
        if (controls.dashRight) playerController.tryDashRight(controls.bodyId);

        if (controls.idle) playerController.trySetIdle(controls.bodyId);

        playerController.trySetCurrentWeapon(controls.bodyId, controls.currentWeapon);
        playerController.trySetDimension(controls.bodyId, controls.dimension);
        playerController.trySetAim(controls.bodyId, controls.isAiming, controls.aimingAngle);
    }

    private void updatePlayers(Set<PlayerEntity> players, Play.EntitiesToBeRemoved entitiesRemoved) {
        for (PlayerEntity player : players) {
            Body body = gameWorld.getPlayerBodies().get(player.bodyId);
            PlayerBody.PlayerBodyData bodyData = gameWorld.getPlayers().get(player.bodyId);
            MultiplayerPlayerTweener animation = playerController.getAnimations().get(player.bodyId);

            if (bodyData.health <= 0) {
                gameWorld.removePlayer(player.bodyId);  //PlayerEntity dead
                playerController.getAnimations().remove(player.bodyId);
                entitiesRemoved.players.add(player.bodyId);
                addPlayer(player);
                player.deaths++;
                continue;
            }

            player.wallJump = (short) bodyData.wallJump;
            player.health = (short) bodyData.health;
            player.dash = bodyData.dash;
            player.onGround = bodyData.onGround;
            player.doubleJump = bodyData.doubleJump;
            player.dimension = bodyData.dimension;

            player.position = body.getPosition();
            player.velocity = body.getLinearVelocity();

            player.animation = animation.getCurrentAnimation();
            player.flippedAnimation = bodyData.flippedAnimation;

            for (int i = 0; i < player.weapons.length; i++) {
                player.weapons[i] = bodyData.weapons[i] != null ? bodyData.weapons[i].id : -1;
            }
            player.currentWeaponIndex = (short) bodyData.currentWeaponIndex;
            player.isAiming = bodyData.isAiming;
            player.aimAngle = bodyData.aimAngle;

            player.kills = (short) bodyData.kills;
            player.damage = (short) bodyData.damage;
        }
    }

    private void updateWeapons(Map<Integer, WeaponEntity> weapons, Play.EntitiesToBeRemoved entitiesRemoved) {
        for (int id : gameWorld.getWeaponsRemoved()) {
            weapons.remove(id);
            weaponController.removeWeapon(id);
            entitiesRemoved.weapons.add(id);
        }
        for (WeaponEntity weapon : weapons.values()) {
            Body body = gameWorld.getWeaponBodies().get(weapon.bodyId);
            WeaponBody.WeaponBodyData bodyData = gameWorld.getWeapons().get(weapon.bodyId);
            MultiplayerPlayerTweener animation = weaponController.getAnimations().get(weapon.bodyId);

            weapon.position = body.getPosition();
            weapon.angle = body.getAngle();
            weapon.velocity = body.getLinearVelocity();

            weapon.dropped = bodyData.dropped;

            weapon.animation = animation.getCurrentAnimation();
            weapon.flippedAnimation = bodyData.flippedAnimation;
        }
    }

    private void updateBullets(Map<Integer, BulletEntity> bullets, Play.EntitiesToBeRemoved entitiesRemoved) {
        for (int id : gameWorld.getBulletsRemoved()) {
            bullets.remove(id);
            bulletController.removeBullet(id);
            entitiesRemoved.bullets.add(id);
        }
        for (BulletEntity bullet : bullets.values()) {
            Body body = gameWorld.getBulletBodies().get(bullet.bodyId);
            BulletBody.BulletBodyData bodyData = gameWorld.getBullets().get(bullet.bodyId);
            MultiplayerPlayerTweener animation = bulletController.getAnimations().get(bullet.bodyId);

            if (bodyData.isToBeRemoved) {
                gameWorld.removeBullet(bullet.bodyId);
                bullets.remove(bullet.bodyId);
                bulletController.removeBullet(bullet.bodyId);
                entitiesRemoved.bullets.add(bullet.bodyId);
                continue;
            }

            bullet.position = body.getPosition();
            bullet.angle = body.getAngle();
            bullet.velocity = body.getLinearVelocity();

            bullet.isHit = bodyData.isHit;
            bullet.type = bodyData.type;
            bullet.animation = animation.getCurrentAnimation();
        }
    }

    public boolean isLoaded() {
        return gameWorld != null && playerController != null;
    }

    public Map<Integer, WeaponEntity> getWeapons() {
        return weapons;
    }

    public Map<Integer, BulletEntity> getBullets() {
        return bullets;
    }

    @Override
    public void dispose() {
        if (gameWorld != null)gameWorld.dispose();
        if (logicThread != null) logicThread.close();
        System.gc();
    }

    public class ServerLogicThread extends Thread {

        private boolean running = true;
        private Set<PlayerEntity> players;
        private Map<Integer, WeaponEntity> weapons;
        private Map<Integer, BulletEntity> bullets;

        public ServerLogicThread(Set<PlayerEntity> players, Map<Integer, WeaponEntity> weapons, Map<Integer, BulletEntity> bullets) {
            this.players = players;
            this.weapons = weapons;
            this.bullets = bullets;
        }

        public void close() {
            running = false;
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            while (running) {
                float dt = (System.currentTimeMillis() - start) / 1000f;
                start = System.currentTimeMillis();

                gameWorld.update(dt);

                playerController.updateAnimations(dt);
                weaponController.updateAnimations(dt);
                bulletController.updateAnimations(dt);

                Play.EntitiesToBeRemoved entitiesRemoved = new Play.EntitiesToBeRemoved();

                updatePlayers(players, entitiesRemoved);
                updateWeapons(weapons, entitiesRemoved);
                updateBullets(bullets, entitiesRemoved);

                Game.server.updateWorld(entitiesRemoved);

                try {
                    Thread.sleep(33);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
