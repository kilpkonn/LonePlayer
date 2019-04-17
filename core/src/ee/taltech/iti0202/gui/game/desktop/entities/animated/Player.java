package ee.taltech.iti0202.gui.game.desktop.entities.animated;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars;


public class Player extends SpriteAnimation {

    public enum PlayerAnimation {
        RUN ("run"),
        JUMP ("jump"),
        IDLE ("idle"),
        ROLL ("roll"),
        DASH ("dash");

        private final String name;

        PlayerAnimation(String s) {
            name = s;
        }

        public String toString() {
            return this.name;
        }
    }

    private int health;

    public Player(Body body, SpriteBatch sb) {
        super(body, sb, "images/player/rogue.scml");
        setScale(0.08f);
        setAnimationSpeed(100);
        setHeightOffset(10);
        health = 100;
    }

    public void setAnimation(PlayerAnimation animation) {
        setAnimation(animation.name, animation.name.equals("roll"));
    }

    public void onCheckpointReached(Checkpoint checkpoint) {
        checkpoint.onReached();
        //TODO: Celebrate?
    }

    public void onLanded(Vector2 velocity) {
        if (velocity.y > 3) {
            health -= velocity.y; //TODO: Some fancy function here
        }
        if (velocity.x > B2DVars.ROLL_ON_LANDING_SPEED) {
            setAnimation(PlayerAnimation.ROLL);
        }
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }


}
