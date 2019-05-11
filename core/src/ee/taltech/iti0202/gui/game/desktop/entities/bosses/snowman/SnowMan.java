package ee.taltech.iti0202.gui.game.desktop.entities.bosses.snowman;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.Boss;
import ee.taltech.iti0202.gui.game.desktop.entities.player.handler.PlayerHandler;
import lombok.Builder;

@Builder
public class SnowMan extends Boss {

    private Body body;
    private SpriteBatch spriteBatch;
    private String type;
    private PlayerHandler playerHandler;
    private float size;
    private float xOffset;
    private float yOffset;

    public SnowMan(
            Body body,
            SpriteBatch sb,
            String type,
            PlayerHandler playerHandler,
            float size,
            float x,
            float y) {
        super(playerHandler, body, sb, "images/bosses/snowman/snowman.scml", "snowman", size, x, y);
        this.body = body;
        this.spriteBatch = sb;
        this.type = type;
        this.playerHandler = playerHandler;
        this.xOffset = x;
        this.yOffset = y;
        body.setUserData(type);
        setAnimation(SnowManAnimation.IDLE.toString(), false);
        setAnimationSpeed(50);
        setScale(0.2f);
    }

    public enum SnowManAnimation {
        IDLE("idle"),
        ATTACK("attack");

        private final String name;

        SnowManAnimation(String s) {
            name = s;
        }

        public String toString() {
            return this.name;
        }
    }
}
