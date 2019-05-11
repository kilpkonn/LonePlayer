package ee.taltech.iti0202.gui.game.desktop.entities.bosses.worm;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import ee.taltech.iti0202.gui.game.desktop.entities.player.handler.PlayerHandler;
import lombok.Builder;

@Builder
public class MagmaWorm extends Worm {

    private Body body;
    private SpriteBatch spriteBatch;
    private String type;
    private PlayerHandler playerHandler;
    private Part part;
    private float size;
    private float xOffset;
    private float yOffset;

	public MagmaWorm(
			Body body,
			SpriteBatch sb,
			String type,
			PlayerHandler playerHandler,
			Part part,
			float size) {
        this(body, sb, type, playerHandler, part, size, 0, 0);
    }

	public MagmaWorm(
			Body body,
			SpriteBatch sb,
			String type,
			PlayerHandler playerHandler,
			Part part,
			float size,
			float x,
			float y) {
		super(
				body,
				sb,
				type,
				playerHandler,
				part,
				size,
				x,
				y,
				"images/bosses/magmaworm/magmaworm.scml");
    }
}
