package ee.taltech.iti0202.gui.game.desktop.entities.bosses;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public abstract class Properties {
    private BodyDef bdef;
    private FixtureDef fdef;

    protected Properties(BodyDef bdef, FixtureDef fdef, Vector2 position) { //here is abstract class to construct boss base and fixture upon it
        this.bdef = bdef;
        this.fdef = fdef;
    }

    public BodyDef getBdef() {
        return bdef;
    }

    public FixtureDef getFdef() {
        return fdef;
    }

}
