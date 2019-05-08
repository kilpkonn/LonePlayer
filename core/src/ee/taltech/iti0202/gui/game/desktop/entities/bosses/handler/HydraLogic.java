package ee.taltech.iti0202.gui.game.desktop.entities.bosses.handler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import ee.taltech.iti0202.gui.game.desktop.entities.bosses.Boss;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class HydraLogic extends BossLogic {
    private int takingTurnsBase = 10; // how long one boss attacks
    private int currentlyActiveBoss = 0;
    private int timeElapsed = 0;
    private int PlantBossSize = 1;

    public HydraLogic(Array<Array<Boss>> bossArray) {
        this.BossBossArray = bossArray;
        logic = "hydra";
        setPlantBossSize(bossArray.size);
    }

    @Override
    public void render(SpriteBatch sb) {
        for (Array<Boss> bossList : BossBossArray) {
            for (Boss boss : bossList) boss.render(sb);
            bossList.get(0).render(sb);
        }
    }

    @Override
    public void update(float dt) {
        if (BossBossArray.size != 0) {
            int takingTurns = takingTurnsBase * Gdx.graphics.getFramesPerSecond();
            timeElapsed++;
            if (timeElapsed > takingTurns) {
                timeElapsed = 0;
                currentlyActiveBoss = (currentlyActiveBoss + 1) % PlantBossSize;
            }
            int j = 0;
            for (Array<Boss> bossList : BossBossArray) {
                for (int i = 0; i < bossList.size; i++) {
                    if (i == 0) {
                        if (j == currentlyActiveBoss) {
                            bossList.get(i).updateHeadBig(dt);
                        } else {
                            bossList.get(i).updateCircularMotion(dt);
                        }
                        bossList.get(i).updateRotation(dt);
                    } else if (i == bossList.size - 1) {
                        bossList.get(i).updateRotation(dt);
                    } else {
                        bossList.get(i).update(dt);
                    }

                }
                j++;
            }
        }
    }
}
