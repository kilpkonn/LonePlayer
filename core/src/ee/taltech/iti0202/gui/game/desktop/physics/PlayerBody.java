package ee.taltech.iti0202.gui.game.desktop.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BACKGROUND;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BIT_BOSSES;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BIT_WORM;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.DIMENSION_1;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.DIMENSION_2;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.FRICTION;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.PPM;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.TERRA_DIMENSION_1;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.TERRA_DIMENSION_2;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.TERRA_SQUARES;

public class PlayerBody extends Body {

    public PlayerBody(World world, long addr) {
        super(world, addr);
    }

    public static Body createPlayer(World world, int id) {
        BodyDef bodyDef = new BodyDef();
        short mask;
        // TODO: Make masks work, make usable when re-spawning
        if (true) { //(draw.isDimension()) {
            mask =
                    BIT_BOSSES
                            | BIT_WORM
                            | DIMENSION_1
                            | DIMENSION_2
                            | TERRA_SQUARES
                            | BACKGROUND
                            | TERRA_DIMENSION_1;
        } else {
            mask =
                    BIT_BOSSES
                            | BIT_WORM
                            | DIMENSION_1
                            | DIMENSION_2
                            | TERRA_SQUARES
                            | BACKGROUND
                            | TERRA_DIMENSION_2;
        }
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bodyDef);

        PolygonShape polyShape = new PolygonShape();
        polyShape.setAsBox(2 / PPM, 8 / PPM, new Vector2(-20 / PPM, 20 / PPM), 0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polyShape;
        fixtureDef.filter.categoryBits = DIMENSION_1 | DIMENSION_2;
        fixtureDef.filter.maskBits = mask;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData("side_l");

        polyShape.setAsBox(2 / PPM, 8 / PPM, new Vector2(20 / PPM, 20 / PPM), 0);
        fixtureDef.shape = polyShape;
        fixtureDef.filter.categoryBits = DIMENSION_1 | DIMENSION_2;
        fixtureDef.filter.maskBits = mask;
        body.createFixture(fixtureDef).setUserData("side_r");

        CircleShape circle = new CircleShape();

        fixtureDef.isSensor = false;
        circle.setRadius(9 / PPM);
        fixtureDef.shape = circle;
        fixtureDef.filter.categoryBits = DIMENSION_1 | DIMENSION_2;
        fixtureDef.filter.maskBits = mask;
        body.createFixture(fixtureDef).setFriction(FRICTION);
        body.setUserData("playerBody");

        polyShape.setAsBox(8 / PPM, 18 / PPM, new Vector2(0, 12 / PPM), 0);
        fixtureDef.shape = polyShape;
        fixtureDef.filter.categoryBits = DIMENSION_1 | DIMENSION_2;
        fixtureDef.filter.maskBits = mask;
        body.createFixture(fixtureDef).setUserData(new PlayerBodyData(id));

        polyShape.setAsBox(4 / PPM, 1 / PPM, new Vector2(0, -15 / PPM), 0);
        fixtureDef.shape = polyShape;
        fixtureDef.filter.categoryBits = DIMENSION_1 | DIMENSION_2;
        fixtureDef.filter.maskBits = mask;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData("foot");
        return body;
    }

    public static class PlayerBodyData {
        public int id;

        public PlayerBodyData(int id) {
            this.id = id;
        }
    }
}
