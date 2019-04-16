package ee.taltech.iti0202.gui.game.desktop.entities.animated;

import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Entity;
import com.brashmonkey.spriter.PlayerTweener;

import java.util.HashSet;

import ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars;

public class MyPlayerTweener extends PlayerTweener {
    private float influence;
    private boolean playingOnce;
    private Animation prevAnimation;
    private HashSet<String> animToPlayOnce = new HashSet<>();

    public MyPlayerTweener(Entity entity) {
        super(entity);
        getSecondPlayer().addListener(new MyPlayerListener(new Runnable() {
            @Override
            public void run() {
                if (playingOnce) {
                    setAnimation(prevAnimation.name, false); //TODO: Safer launch -> handle return on first anim
                    influence = 1;
                    playingOnce = false;
                }
            }
        }));
    }

    public void update(float dt) {
        if (influence < 1) {
            influence += dt / B2DVars.PLAYER_ANIMATION_CHANGE_SPEED;
            if (influence > 1) influence = 1;
            setWeight(influence);
        }
        super.update(dt);
    }

    /*public void setAnimation(String anim) {
        if (!anim.equals(getSecondPlayer().getAnimation().name)) {
            influence = 0;
            getFirstPlayer().setAnimation(getSecondPlayer().getAnimation());
            getSecondPlayer().setAnimation(anim);
        }
    }*/

    public void setAnimation(String anim, boolean playOnce) {
        if (!anim.equals(getSecondPlayer().getAnimation().name)) {
            if (animToPlayOnce.contains(anim)) {
                influence = 0.5f;
            } else if (animToPlayOnce.contains(getSecondPlayer().getAnimation().name)) {
                influence = 1;
            } else {
                influence = 0;
            }
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
}
