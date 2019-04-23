package ee.taltech.iti0202.gui.game.desktop.entities.bosses.snowman;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

import ee.taltech.iti0202.gui.game.desktop.entities.bosses.Boss;
import ee.taltech.iti0202.gui.game.desktop.states.Play;

public class SnowMan extends Boss {

    public SnowMan(Body body, SpriteBatch sb, String type, Play play, float x, float y) {
        super(body, sb, play, "images/bosses/snowman/snowman.scml", "snowman", x, y);
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
