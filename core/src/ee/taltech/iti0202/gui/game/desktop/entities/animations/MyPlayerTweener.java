package ee.taltech.iti0202.gui.game.desktop.entities.animations;

import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Entity;
import com.brashmonkey.spriter.PlayerTweener;

import java.util.HashSet;

import ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars;

public class MyPlayerTweener extends PlayerTweener {
    private float influence;
    private float changeSpeed;
    private boolean playingOnce;
    private boolean stopOnAnimOver = false;
    private boolean animOver = false;
    private Animation prevAnimation;
    private HashSet<String> animToPlayOnce = new HashSet<>();

    public MyPlayerTweener(Entity entity) {
        super(entity);
        getSecondPlayer().addListener(new MyPlayerListener(new Runnable() {
            @Override
            public void run() {
                if (playingOnce) {
                    playingOnce = false;
                    if (prevAnimation != null && !stopOnAnimOver) {
                        setAnimation(prevAnimation.name, false);
                    }
                    influence = 1;
                }

                if (stopOnAnimOver) {
                    speed = 0;
                    animOver = true;
                }
            }
        }));
    }

    public void update(float dt) {
        if (influence < 1) {
            influence += dt * changeSpeed / B2DVars.PLAYER_ANIMATION_CHANGE_SPEED;
            if (influence > 1) influence = 1;
            setWeight(influence);
        }
        super.update(dt);
    }

    public void setAnimation(String anim, boolean playOnce) {
        animOver = false;

        if (playingOnce && !animToPlayOnce.contains(anim)) {
            prevAnimation = getEntity().getAnimation(anim); //Life hack
            return;
        }

        if (!anim.equals(getSecondPlayer().getAnimation().name)) {
            if (animToPlayOnce.contains(anim)) {
                changeSpeed = 2f;
            } else if (animToPlayOnce.contains(getSecondPlayer().getAnimation().name)) {
                changeSpeed = 2f;
            } else {
                changeSpeed = 1;
            }
            influence = 0;
            getFirstPlayer().setAnimation(getSecondPlayer().getAnimation());
            getSecondPlayer().setAnimation(anim);

            playingOnce = playOnce;

            if (!animToPlayOnce.contains(getFirstPlayer().getAnimation().name)) {
                prevAnimation = getFirstPlayer().getAnimation();
            }
        }
    }

    @Override
    public void setEntity(Entity entity) {
        super.setEntity(entity);
    }

    public void setAnimToPlayOnce(HashSet<String> animations) {
        this.animToPlayOnce = animations;
    }

    public void setStopOnAnimOver(boolean stop) {
        this.stopOnAnimOver = stop;
    }

    public boolean isAnimationOver() {
        return animOver;
    }
}
