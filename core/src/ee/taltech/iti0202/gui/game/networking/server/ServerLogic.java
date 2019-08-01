package ee.taltech.iti0202.gui.game.networking.server;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.controllers.BulletController;
import ee.taltech.iti0202.gui.game.desktop.controllers.WeaponController;
import ee.taltech.iti0202.gui.game.desktop.entities.animations.MultiplayerPlayerTweener;
import ee.taltech.iti0202.gui.game.desktop.physics.BulletBody;
import ee.taltech.iti0202.gui.game.desktop.physics.GameWorld;
import ee.taltech.iti0202.gui.game.desktop.physics.PlayerBody;
import ee.taltech.iti0202.gui.game.desktop.controllers.PlayerController;
import ee.taltech.iti0202.gui.game.desktop.physics.WeaponBody;
import ee.taltech.iti0202.gui.game.networking.serializable.Play;
import ee.taltech.iti0202.gui.game.networking.server.entity.Bullet;
import ee.taltech.iti0202.gui.game.networking.server.entity.Player;
import ee.taltech.iti0202.gui.game.networking.server.entity.PlayerControls;
import ee.taltech.iti0202.gui.game.networking.server.entity.Weapon;

public class ServerLogic implements Disposable {
    private GameWorld gameWorld;
    private ServerLogicThread logicThread;
    private PlayerController playerController;
    private WeaponController weaponController;
    private BulletController bulletController;

    private Map<Integer, Weapon> weapons = new ConcurrentHashMap<>();
    private Map<Integer, Bullet> bullets = new ConcurrentHashMap<>();

    public void loadWorld(String act, String map) {
        if (gameWorld != null) gameWorld.dispose();
        gameWorld = new GameWorld(act, map);
        bulletController = new BulletController(gameWorld.getBulletBodies(), gameWorld.getBullets());
        weaponController = new WeaponController(gameWorld.getWeaponBodies(), gameWorld.getWeapons(), this);
        playerController = new PlayerController(gameWorld.getPlayerBodies(), gameWorld.getPlayers(), weaponController);
    }

    public void run(Set<Player> players) {
        if (logicThread != null) logicThread.close();
        logicThread = new ServerLogicThread(players, weapons, bullets);
        logicThread.start();
    }

    public void addPlayer(Player player) {
        player.bodyId = gameWorld.addPlayer();
        playerController.addAnimation(player.bodyId);
    }

    public void addWeapon(ee.taltech.iti0202.gui.game.desktop.entities.weapons2.Weapon.Type type) {
        Weapon weapon = new Weapon();
        weapon.bodyId = gameWorld.addWeapon(type);
        weapon.type = type;
        weaponController.addAnimation(weapon.bodyId, weapon.type);
        weapons.put(weapon.bodyId, weapon);
    }

    public void addBullet(Bullet bullet) {
        bullet.bodyId = gameWorld.addBullet(bullet.position, bullet.velocity, bullet.angle, bullet.type);
        bulletController.addAnimation(bullet.bodyId, bullet.type);
        bullets.put(bullet.bodyId, bullet);
    }

    public void updatePlayerControls(PlayerControls controls) {
        if (controls.jump) playerController.tryJump(controls.id);
        if (controls.moveLeft) playerController.tryMoveLeft(controls.id);
        if (controls.moveRight) playerController.tryMoveRight(controls.id);
        if (controls.dashLeft) playerController.tryDashLeft(controls.id);
        if (controls.dashRight) playerController.tryDashRight(controls.id);

        if (controls.idle) playerController.trySetIdle(controls.id);

        playerController.trySetCurrentWeapon(controls.id, controls.currentWeapon);
        playerController.trySetDimension(controls.id, controls.dimension);
        playerController.trySetAim(controls.id, controls.isAiming, controls.aimingAngle);
    }

    private void updatePlayers(Set<Player> players, Play.EntitiesToBeRemoved entitiesRemoved) {
        for (Player player : players) {
            Body body = gameWorld.getPlayerBodies().get(player.bodyId);
            PlayerBody.PlayerBodyData bodyData = gameWorld.getPlayers().get(player.bodyId);
            MultiplayerPlayerTweener animation = playerController.getAnimations().get(player.bodyId);

            if (bodyData.health <= 0) {
                gameWorld.removePlayer(player.bodyId);  //Player dead
                playerController.getAnimations().remove(player.bodyId);
                entitiesRemoved.players.add(player.bodyId);
                addPlayer(player);
                return;
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
                if (bodyData.weapons[i] != null)
                    player.weapons[i] = bodyData.weapons[i].id;
            }
            player.currentWeaponIndex = (short) bodyData.currentWeaponIndex;
            player.isAiming = bodyData.isAiming;
            player.aimAngle = bodyData.aimAngle;
        }
    }

    private void updateWeapons(Map<Integer, Weapon> weapons, Play.EntitiesToBeRemoved entitiesRemoved) {
        for (int id : gameWorld.getWeaponsRemoved()) {
            weapons.remove(id);
            entitiesRemoved.weapons.add(id);
        }
        for (Weapon weapon : weapons.values()) {
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

    private void updateBullets(Map<Integer, Bullet> bullets, Play.EntitiesToBeRemoved entitiesRemoved) {
        for (int id : gameWorld.getBulletsRemoved()) {
            bullets.remove(id);
            entitiesRemoved.bullets.add(id);
        }
        for (Bullet bullet : bullets.values()) {
            Body body = gameWorld.getBulletBodies().get(bullet.bodyId);
            BulletBody.BulletBodyData bodyData = gameWorld.getBullets().get(bullet.bodyId);
            MultiplayerPlayerTweener animation = bulletController.getAnimations().get(bullet.bodyId);

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

    public Map<Integer, Weapon> getWeapons() {
        return weapons;
    }

    public Map<Integer, Bullet> getBullets() {
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
        private Set<Player> players;
        private Map<Integer, Weapon> weapons;
        private Map<Integer, Bullet> bullets;

        public ServerLogicThread(Set<Player> players, Map<Integer, Weapon> weapons, Map<Integer, Bullet> bullets) {
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
