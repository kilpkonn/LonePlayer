package ee.taltech.iti0202.gui.game.networking.server;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashSet;
import java.util.Set;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.controllers.WeaponController;
import ee.taltech.iti0202.gui.game.desktop.entities.animations.MultiplayerPlayerTweener;
import ee.taltech.iti0202.gui.game.desktop.physics.GameWorld;
import ee.taltech.iti0202.gui.game.desktop.physics.PlayerBody;
import ee.taltech.iti0202.gui.game.desktop.controllers.PlayerController;
import ee.taltech.iti0202.gui.game.desktop.physics.WeaponBody;
import ee.taltech.iti0202.gui.game.networking.server.entity.Player;
import ee.taltech.iti0202.gui.game.networking.server.entity.PlayerControls;
import ee.taltech.iti0202.gui.game.networking.server.entity.Weapon;

public class ServerLogic implements Disposable {
    private GameWorld gameWorld;
    private ServerLogicThread logicThread;
    private PlayerController playerController;
    private WeaponController weaponController;

    private Set<Weapon> weapons = new HashSet<>();

    public void loadWorld(String act, String map) {
        if (gameWorld != null) gameWorld.dispose();
        gameWorld = new GameWorld(act, map);
        playerController = new PlayerController(gameWorld.getPlayerBodies(), gameWorld.getPlayers());
        weaponController = new WeaponController(gameWorld.getWeaponBodies(), gameWorld.getWeapons());
    }

    public void run(Set<Player> players) {
        if (logicThread != null) logicThread.close();
        logicThread = new ServerLogicThread(players, weapons);
        logicThread.start();
    }

    public void setPlayer(Player player) {
        gameWorld.updatePlayer(player);
    }

    public void setWeapon(Weapon weapon) {
        gameWorld.updateWeapon(weapon);
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

    public void updatePlayerControls(PlayerControls controls) {
        if (controls.jump) playerController.tryJump(controls.id);
        if (controls.moveLeft) playerController.tryMoveLeft(controls.id);
        if (controls.moveRight) playerController.tryMoveRight(controls.id);
        if (controls.dashLeft) playerController.tryDashLeft(controls.id);
        if (controls.dashRight) playerController.tryDashRight(controls.id);

        if (controls.idle) playerController.trySetIdle(controls.id);

        playerController.trySetDimension(controls.id, controls.dimension);
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

            weapon.animation = animation.getCurrentAnimation();
            weapon.flippedAnimation = bodyData.flippedAnimation;
        }
    }

    public boolean isLoaded() {
        return gameWorld != null && playerController != null;
    }

    public Set<Weapon> getWeapons() {
        return weapons;
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

        public ServerLogicThread(Set<Player> players, Set<Weapon> weapons) {
            this.players = players;
            this.weapons = weapons;
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
                updatePlayers(players);
                updateWeapons(weapons);
                playerController.updateAnimations(dt);
                weaponController.updateAnimations(dt);
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
