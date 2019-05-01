package ee.taltech.iti0202.gui.game.desktop.entities.bosses.worm;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import ee.taltech.iti0202.gui.game.desktop.entities.bosses.Properties;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BIT_BULLET;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BIT_WORM;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.DIMENTSION_1;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.DIMENTSION_2;

public class WormProperties extends Properties {
    public WormProperties(BodyDef bdef, FixtureDef fdef, Vector2 position) {
        super(bdef, fdef, position);

        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(position);
        bdef.allowSleep = false;
        bdef.gravityScale = 0f;
        fdef.friction = 1f;
        fdef.restitution = 0f;
        fdef.density = 100f;
        fdef.filter.categoryBits = BIT_WORM;
        fdef.filter.maskBits = DIMENTSION_1 | DIMENTSION_2 | BIT_BULLET;
    }
}
