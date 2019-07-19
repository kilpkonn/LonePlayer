package ee.taltech.iti0202.gui.game.desktop.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import ee.taltech.iti0202.gui.game.networking.server.entity.Weapon;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BACKGROUND;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BIT_BOSSES;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BIT_BULLET;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BIT_WEAPON;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BIT_WORM;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.DIMENSION_1;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.DIMENSION_2;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.FRICTION;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.PPM;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.TERRA_DIMENSION_1;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.TERRA_SQUARES;

public class PlayerBody {

    public static Body createPlayer(World world, Vector2 pos, int id) {
        BodyDef bodyDef = new BodyDef();
        short mask = BIT_BOSSES
                | BIT_WORM
                | DIMENSION_1
                | DIMENSION_2
                | TERRA_SQUARES
                | BACKGROUND
                | TERRA_DIMENSION_1
                | BIT_BULLET
                | BIT_WEAPON;
        bodyDef.position.set(pos);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bodyDef);

        PolygonShape polyShape = new PolygonShape();
        polyShape.setAsBox(2 / PPM, 8 / PPM, new Vector2(-20 / PPM, 20 / PPM), 0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polyShape;
        fixtureDef.filter.categoryBits = DIMENSION_1 | DIMENSION_2;
        fixtureDef.filter.maskBits = mask;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData(new PlayerLeftSide(id));

        polyShape.setAsBox(2 / PPM, 8 / PPM, new Vector2(20 / PPM, 20 / PPM), 0);
        fixtureDef.shape = polyShape;
        fixtureDef.filter.categoryBits = DIMENSION_1 | DIMENSION_2;
        fixtureDef.filter.maskBits = mask;
        body.createFixture(fixtureDef).setUserData(new PlayerRightSide(id));

        CircleShape circle = new CircleShape();

        fixtureDef.isSensor = false;
        circle.setRadius(9 / PPM);
        fixtureDef.shape = circle;
        fixtureDef.filter.categoryBits = DIMENSION_1 | DIMENSION_2;
        fixtureDef.filter.maskBits = mask;
        body.createFixture(fixtureDef).setFriction(FRICTION);
        body.setUserData(new PlayerBodyData(id));

        polyShape.setAsBox(8 / PPM, 18 / PPM, new Vector2(0, 12 / PPM), 0);
        fixtureDef.shape = polyShape;
        fixtureDef.filter.categoryBits = DIMENSION_1 | DIMENSION_2;
        fixtureDef.filter.maskBits = mask;
        body.createFixture(fixtureDef).setUserData(body.getUserData());

        polyShape.setAsBox(4 / PPM, 1 / PPM, new Vector2(0, -15 / PPM), 0);
        fixtureDef.shape = polyShape;
        fixtureDef.filter.categoryBits = DIMENSION_1 | DIMENSION_2;
        fixtureDef.filter.maskBits = mask;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData(new PlayerFoot(id));

        return body;
    }

    public static class PlayerRightSide extends BodyData{
        public PlayerRightSide(int id) {
            this.id = id;
        }
    }

    public static class PlayerLeftSide extends BodyData{
        public PlayerLeftSide(int id) {
            this.id = id;
        }
    }

    public static class PlayerFoot extends BodyData{
        public PlayerFoot(int id) {
            this.id = id;
        }
    }

    public static class PlayerBodyData extends BodyData{
        public int wallJump = 0;
        public int health = 100;
        public boolean dash = false;
        public boolean onGround = false;
        public boolean doubleJump = false;
        public boolean dimension = true;

        public boolean flippedAnimation = false;
        public WeaponBody.WeaponBodyData[] weapons = new WeaponBody.WeaponBodyData[3];

        public PlayerBodyData(int id) {
            this.id = id;
        }
    }
}
