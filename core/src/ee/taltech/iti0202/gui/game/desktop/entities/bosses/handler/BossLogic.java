package ee.taltech.iti0202.gui.game.desktop.entities.bosses.handler;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import ee.taltech.iti0202.gui.game.desktop.entities.bosses.Boss;
import lombok.Data;

@Data
public abstract class BossLogic {
    protected Array<Array<Boss>> BossBossArray = new Array<>();
    protected Array<Boss> bossArray = new Array<>();
    protected String logic;

    public void update(float dt) {
        for (Boss boss : bossArray) {
            boss.update(dt);
        }
    }

    public void render(SpriteBatch sb) {
        for (Boss boss : bossArray) {
            boss.render(sb);
        }
    }
}
