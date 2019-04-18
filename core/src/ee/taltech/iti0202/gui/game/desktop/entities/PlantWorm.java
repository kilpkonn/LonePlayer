package ee.taltech.iti0202.gui.game.desktop.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

import ee.taltech.iti0202.gui.game.desktop.states.Play;

public class PlantWorm extends Boss {

    public enum Part {
        HEAD ("plantwormhead"),
        BODY ("plantwormbody"),
        TAIL ("plantwormtail");

        private final String name;

        Part(String s) {
            name = s;
        }

        public String toString() {
            return this.name;
        }
    }

    public PlantWorm(Body body, SpriteBatch sb, String type, Play play, Part part, float size) {
        super(body, sb, play, "images/bosses/magmaworm/magmaworm.scml", (part == Part.HEAD) ? "head" : "body");
        body.setUserData(type); //TODO: Something more intelligent here
        setAnimation(MagmaWorm.MagmaWormAnimation.DEFAULT.toString(), false); //TODO: Plantworm animations
        setAnimationSpeed(50);
        setScale(size);
    }
}
