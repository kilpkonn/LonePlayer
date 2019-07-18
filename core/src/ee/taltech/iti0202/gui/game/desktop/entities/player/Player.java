package ee.taltech.iti0202.gui.game.desktop.entities.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.brashmonkey.spriter.Timeline;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.entities.animations.SpriteAnimation;
import ee.taltech.iti0202.gui.game.desktop.entities.animations.loader.MultiplayerAnimation;
import ee.taltech.iti0202.gui.game.desktop.entities.weapons.Weapon;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.input.MyInput;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.sound.Sound;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class Player extends SpriteAnimation {

    private int health;
    private List<Vector2> doneDmg = new ArrayList<>();
    private List<Weapon> weapons = new ArrayList<>();
    private Weapon currentWeapon;

    public Player(Body body, SpriteBatch sb) {
        super(body, sb, "images/player/rogue.scml");
        System.out.println("new Player");
        setScale(0.08f);
        setAnimationSpeed(100);
        setHeightOffset(10);
        health = 100;
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

        if (currentWeapon != null) {
            Timeline.Key.Bone hand = getBone("right_hand");
            currentWeapon.getBody()
                    .setTransform(
                            new Vector2(hand.position.x / PPM, hand.position.y / PPM),
                            (float) Math.toRadians(hand.angle));
            currentWeapon.update(dt);
        }
    }

    public void aim() {
        if (currentWeapon == null) return;
        float offset =
                getCurrentAnimation().name.equals("run")
                        ? (isFlippedX() ? (float) 0 : (float) (Math.PI / 4))
                        : (getCurrentAnimation().name.equals("dash")
                        ? (isFlippedX() ? (float) 0 : (float) (Math.PI / 4))
                        : (float) (Math.PI / 8));
        float flipped = isFlippedX() ? -(float) Math.PI / 4 + (float) Math.PI : 0;
        float angle = (float) Math.atan2(
                MyInput.getMouseLocation().y - (double) V_HEIGHT / 2,
                MyInput.getMouseLocation().x - (double) V_WIDTH / 2)
                + flipped
                + offset;

        rotateBone("right_shoulder", (((float) -Math.toDegrees(angle))));
    }

    @Override
    public void setFlipX(boolean flipX) {
        super.setFlipX(flipX);
        for (Weapon weapon : weapons) {
            weapon.setFlipX(flipX);
        }
    }

    public void onLanded(Vector2 velocity, Boolean grounded) {
        // System.out.println(Math.abs(velocity.x));

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
                    health -=
                            Math.abs(
                                    velocity.y
                                            * Math.abs(velocity.y)
                                            / DMG_ON_LANDING
                                            * DMG_MULTIPLIER);
                }
                Sound.playSoundOnce("sounds/jump/Jump " + new Random().nextInt(3) + ".ogg", 0.1f);
                health -= Math.abs(velocity.y / 2);
                health = Math.max(0, health);
            }
            if (Math.abs(velocity.x) > ROLL_ON_LANDING_SPEED) {
                // body.applyForceToCenter(new Vector2(PLAYER_SPEED * velocity.x /
                // Math.abs(velocity.x), 0),
                // true); // Change to impulse?
                Sound.playSoundOnce("sounds/jump/fall.ogg", 0.1f);
                setAnimation(PlayerAnimation.ROLL2, true);
                if (Math.abs(velocity.y) < ROLL_ON_LANDING_SPEED) {
                    health -= Math.abs(velocity.y / 2);
                    health = Math.max(health, 0);
                    setAnimation(PlayerAnimation.FACEPLANT, true);
                }
            }
        }
    }

    public void setAnimation(MultiplayerAnimation animation) {
        setAnimation(animation.getName(), animation.isToPlayOnce());
    }

    public void setAnimation(PlayerAnimation animation, boolean playOnce) {
        setAnimation(animation.name, playOnce);
    }

    public void addWeapon(Weapon weapon) {
        this.weapons.add(weapon);
    }

    public void setCurrentWeapon(Weapon currentWeapon) {
        this.currentWeapon = currentWeapon;
        if (!weapons.contains(currentWeapon)) {
            weapons.add(currentWeapon);
        }
    }

    public enum PlayerAnimation implements MultiplayerAnimation, Serializable {
        RUN("run", false),
        JUMP("jump", false),
        IDLE("idle", false),
        ROLL("roll", true),
        ROLL2("roll2", true),
        FACEPLANT("faceplant", false),
        DASH("dash", true),
        WAVE("wave", true);

        private final String name;
        private final boolean toPlayOnce;

        PlayerAnimation(String s, boolean playOnce) {
            name = s;
            toPlayOnce = playOnce;
        }

        @Override
        public boolean isToPlayOnce() {
            return toPlayOnce;
        }

        @Override
        public String getName() {
            return this.name;
        }


        public String toString() {
            return this.name;
        }
    }
}
