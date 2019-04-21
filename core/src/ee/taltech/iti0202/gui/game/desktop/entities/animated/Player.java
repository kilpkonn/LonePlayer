package ee.taltech.iti0202.gui.game.desktop.entities.animated;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.ArrayList;
import java.util.List;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.DMG_MULTIPLIER;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.DMG_ON_LANDING;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.ROLL_ON_LANDING_SPEED;


public class Player extends SpriteAnimation {

    private List<Vector2> doneDmg = new ArrayList<>();

    public void onLanded(Vector2 velocity, Boolean grounded) {
        //System.out.println(Math.abs(velocity.x));
        if (doneDmg.size() < 10) {
            doneDmg.add(new Vector2(velocity.x, velocity.y));
        } else {
            doneDmg.remove(0);
            doneDmg.add(new Vector2(velocity.x, velocity.y));
        }

        for (Vector2 dmg : doneDmg) {
            if (Math.abs(dmg.y) > Math.abs(velocity.y)) {
                velocity = dmg;
            }
        }

        if (grounded && doneDmg.size() > 4) {
            doneDmg = new ArrayList<>();
            if (Math.abs(velocity.y) > DMG_ON_LANDING) {
                if (Math.abs(velocity.x) < ROLL_ON_LANDING_SPEED) {
                    health -= Math.abs(velocity.y * Math.abs(velocity.y) / DMG_ON_LANDING * DMG_MULTIPLIER);
                }
                health -= Math.abs(velocity.y * 2);
                health = Math.max(0, health);
            }
            if (Math.abs(velocity.x) > ROLL_ON_LANDING_SPEED) {
                if (Math.abs(velocity.x) > ROLL_ON_LANDING_SPEED * 2) {
                    health -= Math.abs(velocity.x);
                    health = Math.max(health, 0);
                }
                setAnimation(PlayerAnimation.ROLL);
            }
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
        System.out.println("HERE");
        //TODO: Celebrate?
    }

    public enum PlayerAnimation {
        RUN("run"),
        JUMP("jump"),
        IDLE("idle"),
        ROLL("roll"),
        DASH("dash");

        private final String name;

        PlayerAnimation(String s) {
            name = s;
        }

        public String toString() {
            return this.name;
        }
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }


}
