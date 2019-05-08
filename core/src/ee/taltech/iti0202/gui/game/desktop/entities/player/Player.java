package ee.taltech.iti0202.gui.game.desktop.entities.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.brashmonkey.spriter.Timeline;

import java.util.ArrayList;
import java.util.List;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.entities.animations.SpriteAnimation;
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
            aim();
        }

        if (weapon != null) {
            Timeline.Key.Bone hand = getBone("right_hand");
            weapon.getBody().setTransform(new Vector2(hand.position.x / PPM, hand.position.y / PPM), (float) Math.toRadians(hand.angle));
            weapon.update(dt);
        }
    }

    public void aim() {
        if (weapon == null) return;
        //float offset = weapon.getOffset().containsKey(getCurrentAnimation().name) ? weapon.getOffset().get(getCurrentAnimation().name) : 0;
        //System.out.println(offset);
        //System.out.println(Math.toDegrees(body.getAngle()) + " - " + getBone("body").angle);
        /*float offset = (float) (body.getAngle()
                + Math.toRadians((getBone("right_hand").angle)
                - (getBone("right_arm").angle))
                //- Math.toRadians(getBone("body").angle)
                + Math.PI / 3);*/
        float offset = getCurrentAnimation().name.equals("run") ? (isFlippedX() ? (float) 0 : (float) (Math.PI / 4)) : (getCurrentAnimation().name.equals("dash") ? (isFlippedX() ? (float) 0 : (float) (Math.PI / 4)) : (float) (Math.PI / 8));
        float flipped = isFlippedX() ? -(float) Math.PI / 4 + (float) Math.PI : 0;
        float angle = (float) Math.atan2(MyInput.getMouseLocation().y - (double) V_HEIGHT / 2, MyInput.getMouseLocation().x - (double) V_WIDTH / 2) + flipped + offset;

        rotateBone("right_shoulder", (((float) -Math.toDegrees(angle))));
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
