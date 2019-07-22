package ee.taltech.iti0202.gui.game.desktop.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import ee.taltech.iti0202.gui.game.desktop.entities.projectile2.WeaponProjectile;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BACKGROUND;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BIT_BOSSES;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BIT_BULLET;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BIT_WORM;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.DIMENSION_1;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.DIMENSION_2;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.PPM;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.TERRA_DIMENSION_1;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.TERRA_DIMENSION_2;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.TERRA_SQUARES;

public class BulletBody {

    public static Body createBullet(World world, Vector2 pos, Vector2 velocity, int id, WeaponProjectile.Type type) {
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circle = new CircleShape();
        circle.setRadius(9 / PPM);

        fixtureDef.shape = circle;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0f;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = BIT_BULLET;
        fixtureDef.filter.maskBits =
                BIT_BOSSES
                | BIT_WORM
                | BACKGROUND
                | TERRA_SQUARES
                | DIMENSION_1
                | DIMENSION_2
                | TERRA_DIMENSION_1
                | TERRA_DIMENSION_2;  // TODO: Shoot only in one dimension?

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.allowSleep = true;
        bodyDef.bullet = true;
        bodyDef.fixedRotation = true;
        bodyDef.position.set(pos);
        bodyDef.linearVelocity.set(velocity);

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef).setUserData(new BulletBodyData(id));

        return body;
    }

    public static class BulletBodyData extends BodyData {
        public BulletBodyData(int id) {
            this.id = id;
        }
    }
}
