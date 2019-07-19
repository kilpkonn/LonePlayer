package ee.taltech.iti0202.gui.game.desktop.entities.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.brashmonkey.spriter.Timeline;

import ee.taltech.iti0202.gui.game.desktop.entities.animations.SpriteAnimation;
import ee.taltech.iti0202.gui.game.desktop.entities.animations.loader.MultiplayerAnimation;
import ee.taltech.iti0202.gui.game.desktop.entities.weapons2.Weapon;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars;

public class Player2 extends SpriteAnimation {

    private Weapon[] weapons = new Weapon[3];
    private int currentWeapon;
    private float aimAngle;
    private boolean isAiming = false;

    public Player2(Body body, SpriteBatch sb) {
        super(body, sb, "images/player/rogue.scml");
        setScale(0.08f);
        setAnimationSpeed(100);
        setHeightOffset(10);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if (isAiming) {
            rotateBone("right_shoulder", (((float) -Math.toDegrees(aimAngle))));
        }

        Timeline.Key.Bone hand = getBone("right_hand");
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
            weapons[currentWeapon].render(sb);
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
        setAnimation(animation.getName(), animation.isToPlayOnce());
    }

    public void setWeapons(Weapon[] weapons) {
        this.weapons = weapons;
    }

    public void setCurrentWeapon(int currentWeapon) {
        this.currentWeapon = currentWeapon;
    }
}
