package ee.taltech.iti0202.gui.game.desktop.entities.bosses.handler.logic;

import com.badlogic.gdx.utils.Array;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.Boss;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.MyContactListener;

public class WormLogic extends BossLogic {

    public WormLogic(Array<Boss> bossArray, MyContactListener cl) {
        this.bossArray = bossArray;
        this.cl = cl;
        health *= bossArray.size;
        health *= bossArray.size > 100 ? 2 : 0.5;
        totalHealth = health;
        speed = 3;
        System.out.println(totalHealth);
        logic = "worm";
    }

    @Override
    public void update(float dt) {
        super.updateHP(dt);
        if (health / totalHealth <= 0.50) {
            if (health / totalHealth > 0.10) {
                speed = 4;
            } else if (health / totalHealth > 0) {
                speed = 5;
            } else {
                updateDeath(dt);
            }
        }
        if (update) {
            for (int i = 0; i < bossArray.size; i++) {
                if (bossArray.size >= 50) {
                    if (i == bossArray.size - 1) {
                        bossArray.get(i).updateHeadBig(dt, speed);
                    } else {
                        bossArray.get(i).update(dt);
                    }
                } else {
                    if (i == bossArray.size - 1) {
                        bossArray.get(i).updateHeadSmall(dt, speed);
                    } else {
                        bossArray.get(i).update(dt);
                    }
                }
            }
        }
    }
}
