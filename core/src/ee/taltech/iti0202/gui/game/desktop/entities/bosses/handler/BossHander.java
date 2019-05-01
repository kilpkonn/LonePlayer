package ee.taltech.iti0202.gui.game.desktop.entities.bosses.handler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import ee.taltech.iti0202.gui.game.desktop.entities.bosses.Boss;
import lombok.Data;

@Data
public class BossHander {

    private Array<Boss> SnowManArray;
    private Array<Array<Boss>> MagmaBossArray;
    private Array<Array<Boss>> PlantBossArray;

    private int takingTurnsBase = 10; // how long one boss attacks
    private int currentlyActiveBoss = 0;
    private int timeElapsed = 0;
    private int PlantBossSize = 1;


    public BossHander() {
        SnowManArray = new Array<>();
        MagmaBossArray = new Array<>();
        PlantBossArray = new Array<>();

    }

    public void updateBosses(float dt) {
        if (SnowManArray.size != 0) {
            for (Boss boss : SnowManArray) {
                boss.update(dt);
            }
        }

        if (MagmaBossArray.size != 0) {
            for (Array<Boss> bossList : MagmaBossArray)
                for (int i = 0; i < bossList.size; i++) {
                    if (bossList.size >= 100) {
                        if (i == bossList.size - 1) {
                            bossList.get(i).updateHeadBig(dt);
                        } else {
                            bossList.get(i).update(dt);
                        }
                    } else {
                        if (i == bossList.size - 1) {
                            bossList.get(i).updateHeadSmall(dt);
                        } else {
                            bossList.get(i).update(dt);
                        }
                    }
                }
        }


        if (PlantBossArray.size != 0) {
            int takingTurns = takingTurnsBase * Gdx.graphics.getFramesPerSecond();
            timeElapsed++;
            if (timeElapsed > takingTurns) {
                timeElapsed = 0;
                currentlyActiveBoss = (currentlyActiveBoss + 1) % PlantBossSize;
            }
            int j = 0;
            for (Array<Boss> bossList : PlantBossArray) {
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

    public void renderBosses(SpriteBatch sb) {
        if (SnowManArray != null)
            for (Boss boss : SnowManArray)
                boss.render(sb);

        if (MagmaBossArray != null) {
            for (Array<Boss> bossList : MagmaBossArray)
                for (Boss boss : bossList) boss.render(sb);
        }

        if (PlantBossArray != null) {
            for (Array<Boss> bossList : PlantBossArray) {
                for (Boss boss : bossList) boss.render(sb);
                bossList.get(0).render(sb);
            }
        }
    }
}
