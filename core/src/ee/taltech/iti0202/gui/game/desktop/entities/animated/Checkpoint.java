package ee.taltech.iti0202.gui.game.desktop.entities.animated;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

public class Checkpoint extends SpriteAnimation {

    public enum CheckpointAnimation {
        DEFAULT ("default"),
        COLLECTED ("collected");

        private final String name;

        CheckpointAnimation(String s) {
            name = s;
        }

        public String toString() {
            return this.name;
        }
    }

    public Checkpoint(Body body, SpriteBatch sb) {
        super(body, sb, "images/checkpoint/checkpoint.scml");

        setScale(0.1f);
        setAnimationSpeed(50);
        setHeightOffset(50);
        setAnimation(CheckpointAnimation.DEFAULT);
    }

    public void setAnimation(Checkpoint.CheckpointAnimation animation) {
        setAnimation(animation.name, animation.name.equals("roll"));
    }

    public void onReached() {
        setAnimation(CheckpointAnimation.COLLECTED);
    }
}

