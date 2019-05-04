package ee.taltech.iti0202.gui.game.desktop.entities.animations;

import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.Timeline;

public class MyAttachment extends Player.Attachment {

    public MyAttachment(Timeline.Key.Bone parent) {
        super(parent);
    }

    @Override
    protected void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
    }

    @Override
    protected void setScale(float xscale, float yscale) {
        this.scale.x = xscale;
        this.scale.y = yscale;
    }

    @Override
    protected void setAngle(float angle) {
        this.angle = angle;
    }
}
