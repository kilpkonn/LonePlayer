package ee.taltech.iti0202.gui.game.desktop.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

import ee.taltech.iti0202.gui.game.desktop.states.Play;

public class PlantWorm extends Boss {

    private float max_speed = 3;
    private float time = 0;

    public PlantWorm(Body body, SpriteBatch sb, String type, Play play, PlantWorm.Part part, float size) {
        this(body, sb, type, play, part, size, 0, 0);
    }

    public PlantWorm(Body body, SpriteBatch sb, String type, Play play, Part part, float size, float x, float y) {
        super(body, sb, play, "images/bosses/plantworm/plantworm.scml", part.name, x, y);
        body.setUserData(type); //TODO: Something more intelligent here
        setAnimation(PlantWormAnimation.IDLE.toString(), false);
        setAnimationSpeed(50);
        setScale(size);
    }

    public enum PlantWormAnimation {
        IDLE("idle"),
        ATTACK("attack");

        private final String name;

        PlantWormAnimation(String s) {
            name = s;
        }

        public String toString() {
            return this.name;
        }
    }

    public enum Part {
        HEAD("plantwormhead"),
        BODY("plantwormbody"),
        TAIL("plantwormbody"); //TODO: make tail / root

        private final String name;

        Part(String s) {
            name = s;
        }

        public String toString() {
            return this.name;
        }
    }
}
