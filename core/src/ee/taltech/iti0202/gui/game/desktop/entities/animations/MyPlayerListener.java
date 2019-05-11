package ee.taltech.iti0202.gui.game.desktop.entities.animations;

import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Mainline;
import com.brashmonkey.spriter.Player;

public class MyPlayerListener implements Player.PlayerListener {

    private Runnable onAnimationFinished;

    public MyPlayerListener(Runnable onAnimationFinished) {
        this.onAnimationFinished = onAnimationFinished;
    }

    @Override
    public void animationFinished(Animation animation) {
        onAnimationFinished.run();
    }

    @Override
    public void animationChanged(Animation oldAnim, Animation newAnim) {}

    @Override
    public void preProcess(Player player) {}

    @Override
    public void postProcess(Player player) {}

    @Override
    public void mainlineKeyChanged(Mainline.Key prevKey, Mainline.Key newKey) {}
}
