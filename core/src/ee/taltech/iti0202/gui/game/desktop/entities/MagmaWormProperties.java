package ee.taltech.iti0202.gui.game.desktop.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BIT_WORM;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.DIMENTSION_1;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.DIMENTSION_2;

public class MagmaWormProperties {
    private BodyDef bdef;
    private FixtureDef fdef;

    public MagmaWormProperties(BodyDef bdef, FixtureDef fdef, Vector2 position) {
        this.bdef = bdef;
        this.fdef = fdef;

        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(position);
        bdef.allowSleep = false;
        bdef.gravityScale = 0;
        fdef.friction = 0f;
        fdef.restitution = 0f;
        fdef.density = 1f;
        fdef.filter.categoryBits = BIT_WORM;
        fdef.filter.maskBits = DIMENTSION_1 | DIMENTSION_2;
    }


    public BodyDef getBdef() {
        return bdef;
    }

    public FixtureDef getFdef() {
        return fdef;
    }
}
