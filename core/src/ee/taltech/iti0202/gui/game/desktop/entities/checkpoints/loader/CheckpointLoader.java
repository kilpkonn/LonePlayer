package ee.taltech.iti0202.gui.game.desktop.entities.checkpoints.loader;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import ee.taltech.iti0202.gui.game.desktop.entities.checkpoints.Checkpoint;
import ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.DIMENTSION_1;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.DIMENTSION_2;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PPM;

public class CheckpointLoader {

    public static Checkpoint createCheckpoints(Vector2 pos, World world, SpriteBatch spriteBatch) {
        System.out.println("new checkpoint");
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        bdef.position.set(pos);
        bdef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bdef);
        PolygonShape polyShape = new PolygonShape();
        polyShape.setAsBox(4 / PPM, 32 / PPM, new Vector2(0, 4 / PPM), 0);
        fdef.shape = polyShape;
        fdef.filter.categoryBits = DIMENTSION_1 | DIMENTSION_2;
        fdef.filter.maskBits = B2DVars.BIT_ALL;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("checkpoint");
        return new Checkpoint(body, spriteBatch);
    }

    public static Checkpoint createEndPoint(Vector2 pos, World world, SpriteBatch spriteBatch) {
        System.out.println("new endpoint");
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        bdef.position.set(pos);
        bdef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bdef);
        PolygonShape polyShape = new PolygonShape();
        polyShape.setAsBox(64 / PPM, 32 / PPM, new Vector2(0, 4 / PPM), 0);
        fdef.shape = polyShape;
        fdef.filter.categoryBits = DIMENTSION_1 | DIMENTSION_2;
        fdef.filter.maskBits = B2DVars.BIT_ALL;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("end");
        return new Checkpoint(body, spriteBatch);
    }
}
