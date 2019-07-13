package ee.taltech.iti0202.gui.game.networking.server;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;

import java.util.Set;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.physics.GameWorld;
import ee.taltech.iti0202.gui.game.desktop.physics.PlayerBody;
import ee.taltech.iti0202.gui.game.desktop.physics.PlayerController;
import ee.taltech.iti0202.gui.game.networking.server.player.Player;
import ee.taltech.iti0202.gui.game.networking.server.player.PlayerControls;

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
        logicThread.run();
    }

    /*public void update(float dt) {
        gameWorld.update(dt);
    }*/

    public void setPlayer(Player player) {
        gameWorld.updatePlayer(player);
    }

    public void addPlayer(Player player) {
        player.bodyId = gameWorld.addPlayer();
    }

    public void updatePlayerControls(PlayerControls controls) {
        if (controls.jump) playerController.tryJump(controls.id);
    }

    private void updatePlayers(Set<Player> players) {
        for (Player player : players) {
            Body body = gameWorld.getPlayerBodies().get(player.id);
            PlayerBody.PlayerBodyData bodyData = gameWorld.getPlayers().get(player.id);

            player.wallJump = (short) bodyData.wallJump;
            player.health = (short) bodyData.health;
            player.dash = bodyData.dash;
            player.onGround = bodyData.onGround;
            player.doubleJump = bodyData.doubleJump;

            player.position = body.getPosition();
            player.velocity = body.getLinearVelocity();
        }
    }

    public boolean isLoaded() {
        return gameWorld != null;
    }

    @Override
    public void dispose() {
        gameWorld.dispose();
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
                float dt = (System.currentTimeMillis() - start) / 1000000f;
                start = System.currentTimeMillis();
                //TODO: Fixed rate?

                gameWorld.update(dt);
                updatePlayers(players);
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
