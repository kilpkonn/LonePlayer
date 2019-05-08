package ee.taltech.iti0202.gui.game.desktop.entities.bosses.handler;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import ee.taltech.iti0202.gui.game.desktop.entities.Handler;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.Boss;
import lombok.Data;

@Data
public class BossHander implements Handler {

    private Array<BossLogic> LogicHandlers = new Array<>();

    public void addWorm(Array<Boss> bosses) {
        LogicHandlers.add(new WormLogic(bosses));
    }

    public void addHydra(Array<Array<Boss>> bosses) {
        LogicHandlers.add(new HydraLogic(bosses));
    }

    public void addSnowMan(Boss bosses) {
        LogicHandlers.add(new SnowManLogic(bosses));
    }

    @Override
    public void update(float dt) {
        for (BossLogic logic : LogicHandlers) {
            logic.update(dt);
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        for (BossLogic logic : LogicHandlers) {
            logic.render(spriteBatch);
        }
    }
}
