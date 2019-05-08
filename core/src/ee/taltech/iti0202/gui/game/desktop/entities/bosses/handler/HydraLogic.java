package ee.taltech.iti0202.gui.game.desktop.entities.bosses.handler;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import ee.taltech.iti0202.gui.game.desktop.entities.bosses.Boss;

public class HydraLogic extends BossLogic {
    protected Array<Boss> bossArray;

    public HydraLogic(Array<Boss> bossArray) {
        this.bossArray = bossArray;
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
    }

    @Override
    public void updare(float dt) {
        super.updare(dt);
    }
}
