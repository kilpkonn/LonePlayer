package ee.taltech.iti0202.gui.game.desktop.entities.checkpoints;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import ee.taltech.iti0202.gui.game.desktop.entities.animations.SpriteAnimation;

public class Checkpoint extends SpriteAnimation {

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

    public void dispose() {
        setAnimation(CheckpointAnimation.DEFAULT);
    }

    public enum CheckpointAnimation {
        DEFAULT("default"),
        COLLECTED("collected");

        private final String name;

        CheckpointAnimation(String s) {
            name = s;
        }

        public String toString() {
            return this.name;
        }
    }
}
