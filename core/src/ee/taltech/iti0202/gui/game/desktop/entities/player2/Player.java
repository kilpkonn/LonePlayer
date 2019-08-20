package ee.taltech.iti0202.gui.game.desktop.entities.player2;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.brashmonkey.spriter.Timeline;

import java.io.Serializable;

import ee.taltech.iti0202.gui.game.desktop.entities.animations.SpriteAnimation2;
import ee.taltech.iti0202.gui.game.desktop.entities.animations.loader.MultiplayerAnimation;
import ee.taltech.iti0202.gui.game.desktop.entities.weapons2.Weapon;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars;

public class Player extends SpriteAnimation2 {

    private Weapon[] weapons = new Weapon[3];
    private int currentWeapon;
    private float aimAngle;
    private boolean isAiming = false;

    public Player(Body body) {
        super(body, "images/player/rogue.scml");
        setAnimation(PlayerAnimation.IDLE);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if (isAiming) {
            float offset = playerTweener.getCurrentAnimation().getName().equals("run")
                            ? (isFlippedX() ? (float) 0 : (float) (Math.PI / 4))
                            : (playerTweener.getCurrentAnimation().getName().equals("dash")
                            ? (isFlippedX() ? (float) 0 : (float) (Math.PI / 4))
                            : (float) (Math.PI / 8));
            float flipped = isFlippedX() ? -(float) Math.PI / 4 + (float) Math.PI : 0;
            playerTweener.setBone("right_shoulder", (((float) Math.toDegrees(aimAngle + offset + flipped))));
        }

        Timeline.Key.Bone hand = playerTweener.getBone("right_hand");
        for (Weapon weapon: weapons) {
            if (weapon != null) {
                weapon.getBody().setTransform(
                        new Vector2(hand.position.x / B2DVars.PPM, hand.position.y / B2DVars.PPM),
                        (float) Math.toRadians(hand.angle));
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);

        if (weapons[currentWeapon] != null) {
            weapons[currentWeapon].forceRender(sb);
        }
    }

    @Override
    public void setFlipX(boolean flipX) {
        super.setFlipX(flipX);
        for (Weapon weapon : weapons) {
            if (weapon != null) {
                weapon.setFlipX(flipX);
            }
        }
    }

    public void setAnimation(MultiplayerAnimation animation) {
        playerTweener.setAnimation(animation);
    }

    public void setAiming(boolean isAiming, float aimAngle) {
        this.isAiming = isAiming;
        this.aimAngle = aimAngle;
    }

    public void setWeapons(Weapon[] weapons) {
        this.weapons = weapons;
    }

    public void setCurrentWeapon(int currentWeapon) {
        this.currentWeapon = currentWeapon;
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
        public float getScale() {
            return 0.08f;
        }

        @Override
        public int getSpeed() {
            return 100;
        }

        @Override
        public boolean isToPlayOnce() {
            return toPlayOnce;
        }

        @Override
        public boolean isHardSet() {
            return false;
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
