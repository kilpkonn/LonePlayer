package ee.taltech.iti0202.gui.game.desktop.entities.bosses.worm;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

import ee.taltech.iti0202.gui.game.desktop.entities.bosses.Boss;
import ee.taltech.iti0202.gui.game.desktop.states.Play;

abstract class Worm extends Boss {

    public Worm(Body body, SpriteBatch sb, String type, Play play, Part part, float size, String path) {
        this(body, sb, type, play, part, size, 0, 0, path);
    }

    public Worm(Body body, SpriteBatch sb, String type, Play play, Part part, float size, float x, float y, String path) {
        super(body, sb, play, path, part.toString(), size, x, y);
        body.setUserData(type + type);
        setAnimation(Worm.WormAnimation.DEFAULT.name, false);
        setAnimationSpeed(50);
        setScale(size); //TODO: Bend animation / smoother bodies
    }

    public enum WormAnimation {
        DEFAULT("default");

        private final String name;

        WormAnimation(String s) {
            name = s;
        }

        public String toString() {
            return this.name;
        }
    }
}
