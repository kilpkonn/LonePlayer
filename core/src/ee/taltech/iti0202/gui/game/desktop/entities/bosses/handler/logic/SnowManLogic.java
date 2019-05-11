package ee.taltech.iti0202.gui.game.desktop.entities.bosses.handler.logic;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.Boss;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.MyContactListener;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SnowManLogic extends BossLogic {
    public SnowManLogic(Boss boss, MyContactListener cl) {
        bossArray.add(boss);
        this.cl = cl;
        logic = "snowman";
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        super.updateHP(dt);
    }
}
