package ee.taltech.iti0202.gui.game.desktop.entities.bosses.handler;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ee.taltech.iti0202.gui.game.desktop.entities.bosses.Boss;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SnowManLogic extends BossLogic {
    public SnowManLogic(Boss boss) {
        bossArray.add(boss);
        logic = "snowman";
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
    }
}
