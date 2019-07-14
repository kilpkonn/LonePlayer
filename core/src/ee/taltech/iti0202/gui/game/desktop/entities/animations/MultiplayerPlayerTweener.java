package ee.taltech.iti0202.gui.game.desktop.entities.animations;

import com.brashmonkey.spriter.Entity;
import com.brashmonkey.spriter.PlayerTweener;

import ee.taltech.iti0202.gui.game.desktop.entities.player.Player;

public class MultiplayerPlayerTweener extends PlayerTweener {


    public MultiplayerPlayerTweener(Entity entity) {
        super(entity);
    }

    public void setAnimation(Player.PlayerAnimation animation) {

    }

    public Player.PlayerAnimation getCurrentAnimation() {
        return Player.PlayerAnimation.IDLE;
    }
}
