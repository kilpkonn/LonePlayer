package ee.taltech.iti0202.gui.game.desktop.entities.bosses.handler.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import ee.taltech.iti0202.gui.game.desktop.entities.bosses.Boss;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.MyContactListener;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class HydraLogic extends BossLogic {
    private int takingTurnsBase = 10; // how long one boss attacks
    private int currentlyActiveBoss = 0;
    private int timeElapsed = 0;
    private int PlantBossSize = 1;

    public HydraLogic(Array<Array<Boss>> bossArray, MyContactListener cl) {
        this.BossBossArray = bossArray;
        this.cl = cl;
        logic = "hydra";
        health *= Math.pow(bossArray.size, 2);
        totalHealth = health;
        speed = 2;
        System.out.println(totalHealth);
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

        if (health / totalHealth <= 0.50) {
            if (health / totalHealth > 0.10) {
                setSpeed(3);
            } else if (health / totalHealth > 0) {
                setSpeed(4);
            } else {
                updateDeath(dt);
            }
        }

        dt *= speed / 2;

        if (BossBossArray.size != 0 && update) {
            super.updateHP(dt);
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
                            bossList.get(i).updateHeadBig(dt, speed);
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
