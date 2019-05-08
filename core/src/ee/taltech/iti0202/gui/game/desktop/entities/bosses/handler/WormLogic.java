package ee.taltech.iti0202.gui.game.desktop.entities.bosses.handler;

import com.badlogic.gdx.utils.Array;

import ee.taltech.iti0202.gui.game.desktop.entities.bosses.Boss;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WormLogic extends BossLogic {

    public WormLogic(Array<Boss> bossArray) {
        this.bossArray = bossArray;
        logic = "worm";
    }

    @Override
    public void update(float dt) {
        for (int i = 0; i < bossArray.size; i++) {
            if (bossArray.size >= 100) {
                if (i == bossArray.size - 1) {
                    bossArray.get(i).updateHeadBig(dt);
                } else {
                    bossArray.get(i).update(dt);
                }
            } else {
                if (i == bossArray.size - 1) {
                    bossArray.get(i).updateHeadSmall(dt);
                } else {
                    bossArray.get(i).update(dt);
                }
            }

        }
    }
}

