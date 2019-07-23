package ee.taltech.iti0202.gui.game.networking.server;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashSet;
import java.util.Set;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.controllers.BulletController;
import ee.taltech.iti0202.gui.game.desktop.controllers.WeaponController;
import ee.taltech.iti0202.gui.game.desktop.entities.animations.MultiplayerPlayerTweener;
import ee.taltech.iti0202.gui.game.desktop.physics.BulletBody;
import ee.taltech.iti0202.gui.game.desktop.physics.GameWorld;
import ee.taltech.iti0202.gui.game.desktop.physics.PlayerBody;
import ee.taltech.iti0202.gui.game.desktop.controllers.PlayerController;
import ee.taltech.iti0202.gui.game.desktop.physics.WeaponBody;
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

    private Set<Weapon> weapons = new HashSet<>();
    private Set<Bullet> bullets = new HashSet<>();

    public void loadWorld(String act, String map) {
        if (gameWorld != null) gameWorld.dispose();
        gameWorld = new GameWorld(act, map);
        playerController = new PlayerController(gameWorld.getPlayerBodies(), gameWorld.getPlayers());
        weaponController = new WeaponController(gameWorld.getWeaponBodies(), gameWorld.getWeapons(), playerController);
        bulletController = new BulletController(gameWorld.getBulletBodies(), gameWorld.getBullets());
    }

    public void run(Set<Player> players) {
        if (logicThread != null) logicThread.close();
        logicThread = new ServerLogicThread(players, weapons, bullets);
        logicThread.start();
    }

    public void addPlayer(Player player) {
        player.bodyId = gameWorld.addPlayer();
        playerController.addAnimation(player.id);
    }

    public void addWeapon(ee.taltech.iti0202.gui.game.desktop.entities.weapons2.Weapon.Type type) {
        Weapon weapon = new Weapon();
        weapon.bodyId = gameWorld.addWeapon(type);
        weapon.type = type;
        weaponController.addAnimation(weapon.bodyId, weapon.type);
        weapons.add(weapon);
    }

    private void addBullet(Bullet bullet) {
        bullet.bodyId = gameWorld.addBullet(bullet.position, bullet.velocity, bullet.type);
        bulletController.addAnimation(bullet.bodyId, bullet.type);
        bullets.add(bullet);
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

    private void updatePlayers(Set<Player> players) {
        for (Player player : players) {
            Body body = gameWorld.getPlayerBodies().get(player.id);
            PlayerBody.PlayerBodyData bodyData = gameWorld.getPlayers().get(player.id);
            MultiplayerPlayerTweener animation = playerController.getAnimations().get(player.id);

            player.wallJump = (short) bodyData.wallJump;
            player.health = (short) bodyData.health;
            player.dash = bodyData.dash;
            player.onGround = bodyData.onGround;
            player.doubleJump = bodyData.doubleJump;
            player.dimension = bodyData.dimension;

            player.position = body.getTransform().getPosition();
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

    private void updateWeapons(Set<Weapon> weapons) {
        for (Weapon weapon : weapons) {
            Body body = gameWorld.getWeaponBodies().get(weapon.bodyId);
            WeaponBody.WeaponBodyData bodyData = gameWorld.getWeapons().get(weapon.bodyId);
            MultiplayerPlayerTweener animation = weaponController.getAnimations().get(weapon.bodyId);

            weapon.position = body.getTransform().getPosition();
            weapon.angle = body.getAngle();
            weapon.velocity = body.getLinearVelocity();

            weapon.dropped = bodyData.dropped;

            weapon.animation = animation.getCurrentAnimation();
            weapon.flippedAnimation = bodyData.flippedAnimation;
        }
    }

    private void updateBullets(Set<Bullet> bullets) {
        for (Bullet bullet : bullets) {
            Body body = gameWorld.getBulletBodies().get(bullet.bodyId);
            BulletBody.BulletBodyData bodyData = gameWorld.getBullets().get(bullet.bodyId);
            MultiplayerPlayerTweener animation = bulletController.getAnimations().get(bullet.bodyId);

            bullet.position = body.getTransform().getPosition();
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

    public Set<Weapon> getWeapons() {
        return weapons;
    }

    public Set<Bullet> getBullets() {
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
        private Set<Weapon> weapons;
        private Set<Bullet> bullets;

        public ServerLogicThread(Set<Player> players, Set<Weapon> weapons, Set<Bullet> bullets) {
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

                updatePlayers(players);
                updateWeapons(weapons);
                updateBullets(bullets);

                Game.server.updateWorld();

                try {
                    Thread.sleep(33);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
