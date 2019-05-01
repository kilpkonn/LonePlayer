package ee.taltech.iti0202.gui.game.desktop.entities.bosses.worm;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

import ee.taltech.iti0202.gui.game.desktop.states.Play;
import lombok.Builder;

@Builder
public class SnowWorm extends Worm {

    private Body body;
    private SpriteBatch spriteBatch;
    private String type;
    private Play play;
    private Part part;
    private float size;
    private float xOffset;
    private float yOffset;

    public SnowWorm(Body body, SpriteBatch sb, String type, Play play, Part part, float size) {
        this(body, sb, type, play, part, size, 0, 0);
    }

    public SnowWorm(Body body, SpriteBatch sb, String type, Play play, Part part, float size, float x, float y) {
        super(body, sb, type, play, part, size, x, y, "images/bosses/snowworm/magmaworm.scml");
    }
}
