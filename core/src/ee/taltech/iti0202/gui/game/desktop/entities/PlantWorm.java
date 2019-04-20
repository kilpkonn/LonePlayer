package ee.taltech.iti0202.gui.game.desktop.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

import ee.taltech.iti0202.gui.game.desktop.states.Play;

public class PlantWorm extends Boss {

    public enum PlantWormAnimation {
        IDLE ("idle"),
        ATTACK ("attack");

        private final String name;

        PlantWormAnimation(String s) {
            name = s;
        }

        public String toString() {
            return this.name;
        }
    }

    public enum Part {
        FLOWER_HEAD ("plantwormflowerhead"),
        CLAW_HEAD ("plantwormclawhead"),
        BODY ("plantwormbody"),
        TAIL ("plantwormbody"); //TODO: make tail / root

        private final String name;

        Part(String s) {
            name = s;
        }

        public String toString() {
            return this.name;
        }
    }

    public PlantWorm(Body body, SpriteBatch sb, String type, Play play, Part part, float size) {
        super(body, sb, play, "images/bosses/plantworm/plantworm.scml", part.name);
        body.setUserData(type); //TODO: Something more intelligent here
        setAnimation(PlantWormAnimation.IDLE.toString(), false);
        setAnimationSpeed(50);
        setScale(size);
    }
}
