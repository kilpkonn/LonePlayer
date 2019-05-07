package ee.taltech.iti0202.gui.game.desktop.entities.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.brashmonkey.spriter.Timeline;

import java.util.ArrayList;
import java.util.List;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.entities.animations.SpriteAnimation;
import ee.taltech.iti0202.gui.game.desktop.entities.checkpoints.Checkpoint;
import ee.taltech.iti0202.gui.game.desktop.entities.weapons.Weapon;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.input.MyInput;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.DMG_MULTIPLIER;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.DMG_ON_LANDING;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.PPM;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.ROLL_ON_LANDING_SPEED;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.V_WIDTH;

@EqualsAndHashCode(callSuper = true)
@Data
public class Player extends SpriteAnimation {

    private int health;
    private List<Vector2> doneDmg = new ArrayList<>();
    private Weapon weapon;
    private Checkpoint checkpoint;

    public Player(Body body, SpriteBatch sb) {
        super(body, sb, "images/player/rogue.scml");
        System.out.println("new Player");
        setScale(0.08f);
        setAnimationSpeed(100);
        setHeightOffset(10);
        health = 100;
    }

    public enum PlayerAnimation {
        RUN("run"),
        JUMP("jump"),
        IDLE("idle"),
        ROLL("roll"),
        ROLL2("roll2"),
        FACEPLANT("faceplant"),
        DASH("dash"),
        WAVE("wave");

        private final String name;

        PlayerAnimation(String s) {
            name = s;
        }

        public String toString() {
            return this.name;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        if (MyInput.isMouseDown(Game.settings.SHOOT)) {
            float angle = (float) Math.atan2(MyInput.getMouseLocation().y - (V_HEIGHT >> 2), MyInput.getMouseLocation().x - (V_WIDTH >> 2));
            aim(angle);
        }

        if (weapon != null) {
            Timeline.Key.Bone hand = getBone("right_hand");
            weapon.getBody().setTransform(new Vector2(hand.position.x / PPM, hand.position.y / PPM), (float) Math.toRadians(hand.angle));
            weapon.update(dt);
        }
    }

    public void aim(float angle) {
        rotateBone("right_shoulder", (360 - (float) Math.toDegrees(angle)) + (isFlippedX() ? 180 : 0)); // TODO: Fix offset
    }

    @Override
    public void setFlipX(boolean flipX) {
        super.setFlipX(flipX);
        if (weapon != null) {
            weapon.setFlipX(flipX);
        }
    }

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
                health -= Math.abs(velocity.y / 2);
                health = Math.max(0, health);
            }
            if (Math.abs(velocity.x) > ROLL_ON_LANDING_SPEED) {
                //body.applyForceToCenter(new Vector2(PLAYER_SPEED * velocity.x / Math.abs(velocity.x), 0), true); // Change to impulse?
                setAnimation(PlayerAnimation.ROLL2, true);
                if (Math.abs(velocity.y) < ROLL_ON_LANDING_SPEED) {
                    health -= Math.abs(velocity.y / 2);
                    health = Math.max(health, 0);
                    setAnimation(PlayerAnimation.FACEPLANT, true);
                }
            }
        }
    }


    public void setAnimation(PlayerAnimation animation) {
        setAnimation(animation.name, false);
    }

    public void setAnimation(PlayerAnimation animation, boolean playOnce) {
        setAnimation(animation.name, playOnce);
    }
}
