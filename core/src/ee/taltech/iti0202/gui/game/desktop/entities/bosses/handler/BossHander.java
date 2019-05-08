package ee.taltech.iti0202.gui.game.desktop.entities.bosses.handler;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import ee.taltech.iti0202.gui.game.desktop.entities.Handler;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.Boss;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.handler.logic.BossLogic;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.handler.logic.HydraLogic;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.handler.logic.SnowManLogic;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.handler.logic.WormLogic;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.MyContactListener;
import lombok.Data;

@Data
public class BossHander implements Handler {

    private MyContactListener cl;
    private Array<BossLogic> LogicHandlers = new Array<>();

    public BossHander(MyContactListener cl) {
        this.cl = cl;
    }

    public void addWorm(Array<Boss> bosses) {
        LogicHandlers.add(new WormLogic(bosses, cl));
    }

    public void addHydra(Array<Array<Boss>> bosses) {
        LogicHandlers.add(new HydraLogic(bosses, cl));
    }

    public void addSnowMan(Boss bosses) {
        LogicHandlers.add(new SnowManLogic(bosses, cl));
    }

    @Override
    public void update(float dt) {
        for (BossLogic logic : LogicHandlers) {
            System.out.println(logic.getHealth());
            if (logic.getHealth() > 50) {
                logic.update(dt);
            } else if (logic.getHealth() > 10) {
                logic.setSpeed(4);
                logic.update(dt);
            } else if (logic.getHealth() > 0) {
                logic.setSpeed(5);
                logic.update(dt);
            } else {
                logic.updateDeath(dt);
            }
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        for (BossLogic logic : LogicHandlers) {
            logic.render(spriteBatch);
        }
    }
}
