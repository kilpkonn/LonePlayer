package ee.taltech.iti0202.gui.game.networking.server;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;

import java.util.Set;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.entities.animations.MultiplayerPlayerTweener;
import ee.taltech.iti0202.gui.game.desktop.physics.GameWorld;
import ee.taltech.iti0202.gui.game.desktop.physics.PlayerBody;
import ee.taltech.iti0202.gui.game.desktop.physics.PlayerController;
import ee.taltech.iti0202.gui.game.networking.server.entity.Player;
import ee.taltech.iti0202.gui.game.networking.server.entity.PlayerControls;

public class ServerLogic implements Disposable {
    private GameWorld gameWorld;
    private ServerLogicThread logicThread;
    private PlayerController playerController;

    public void loadWorld(String act, String map) {
        if (gameWorld != null) gameWorld.dispose();
        gameWorld = new GameWorld(act, map);
        playerController = new PlayerController(gameWorld.getPlayerBodies(), gameWorld.getPlayers());
    }

    public void run(Set<Player> players) {
        if (logicThread != null) logicThread.close();
        logicThread = new ServerLogicThread(players);
        logicThread.start();
    }

    public void setPlayer(Player player) {
        gameWorld.updatePlayer(player);
    }

    public void addPlayer(Player player) {
        player.bodyId = gameWorld.addPlayer();
        playerController.addAnimation(player.id);
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

    public boolean isLoaded() {
        return gameWorld != null && playerController != null;
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

        public ServerLogicThread(Set<Player> players) {
            this.players = players;
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
                //TODO: Fixed rate?

                gameWorld.update(dt);
                updatePlayers(players);
                playerController.updateAnimations(dt);
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
