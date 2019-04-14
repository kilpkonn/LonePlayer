package ee.taltech.iti0202.gui.game.desktop.entities.animated;

import com.brashmonkey.spriter.Entity;
import com.brashmonkey.spriter.PlayerTweener;

import ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars;

public class MyPlayerTweener extends PlayerTweener {
    private float influence;

    public MyPlayerTweener(Entity entity) {
        super(entity);
    }

    public void update(float dt) {
        if (influence < 1) {
            influence += dt / B2DVars.PLAYER_ANIMATION_CHANGE_SPEED;
            if (influence > 1) influence = 1;
            setWeight(influence);
        }

        super.update(); //TODO: Add dt
    }

    public void setAnimation(String anim, boolean flipX) {
        if (!anim.equals(getSecondPlayer().getAnimation().name)) {
            influence = 0;
            getFirstPlayer().setAnimation(getSecondPlayer().getAnimation());
            getSecondPlayer().setAnimation(anim);
        }
    }

    @Override
    public void setEntity(Entity entity) {
        super.setEntity(entity);
    }
}
