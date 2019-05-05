package ee.taltech.iti0202.gui.game.desktop.entities.checkpoints.handler;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import ee.taltech.iti0202.gui.game.desktop.entities.Handler;
import ee.taltech.iti0202.gui.game.desktop.entities.checkpoints.Checkpoint;
import ee.taltech.iti0202.gui.game.desktop.entities.player.handler.PlayerHandler;
import ee.taltech.iti0202.gui.game.desktop.handlers.gdx.MyContactListener;
import ee.taltech.iti0202.gui.game.desktop.handlers.sound.Sound;
import lombok.Data;

@Data
public class CheckpointHandler implements Handler {
    private Array<Checkpoint> checkpointList = new Array<>();

    public CheckpointHandler() {

    }

    @Override
    public void update(float dt) {
        for (Checkpoint checkpoint : checkpointList) {
            checkpoint.update(dt);
        }
    }

    public void setPlayerNewCheckpoint(MyContactListener cl, PlayerHandler playerHandler) {
        if (cl.isNewCheckpoint()) {
            Checkpoint curTemp = checkpointList.get(0); // just in case
            for (Checkpoint checkpoint : checkpointList) {
                if (Math.pow(checkpoint.getPosition().x - playerHandler.getPlayer().getBody().getPosition().x, 2) + Math.pow(checkpoint.getPosition().y - playerHandler.getPlayer().getBody().getPosition().y, 2) <=
                        Math.pow(curTemp.getPosition().x - playerHandler.getPlayer().getBody().getPosition().x, 2) + Math.pow(curTemp.getPosition().y - playerHandler.getPlayer().getBody().getPosition().y, 2)) {
                    curTemp = checkpoint;
                }
            }
            cl.setCurCheckpoint(curTemp.getBody());
            curTemp.onReached();
            if (playerHandler.getPlayer().getCheckpoint() != null)
                playerHandler.getPlayer().getCheckpoint().dispose();
            playerHandler.getPlayer().setCheckpoint(curTemp);

            Sound.playSoundOnce("sounds/checkpoint.ogg");
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        // draw checkpoint
        if (checkpointList.size != 0)
            for (Checkpoint checkpoint : checkpointList) {
                checkpoint.render(spriteBatch);
            }
    }
}
