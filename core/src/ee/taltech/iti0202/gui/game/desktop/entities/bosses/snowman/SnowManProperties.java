package ee.taltech.iti0202.gui.game.desktop.entities.bosses.snowman;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.Properties;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.*;

public class SnowManProperties extends Properties {
    public SnowManProperties(BodyDef bdef, FixtureDef fdef, Vector2 position) {
        super(bdef, fdef, position);

        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(position);
        bdef.allowSleep = false;
        bdef.gravityScale = 0f;
        fdef.friction = 1f;
        fdef.restitution = 0f;
        fdef.density = 10f;
        fdef.filter.categoryBits = BIT_WORM;
        fdef.filter.maskBits = DIMENSION_1 | DIMENSION_2 | BIT_BULLET;
    }
}
