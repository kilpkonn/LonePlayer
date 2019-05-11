package ee.taltech.iti0202.gui.game.desktop.entities.projectile.bullet.loader;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

import ee.taltech.iti0202.gui.game.desktop.entities.projectile.bullet.Bullet;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BACKGROUND;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BIT_BOSSES;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BIT_BULLET;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BIT_WORM;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.PPM;

public class BulletLoader {

    public static Bullet bulletLoader(SpriteBatch spriteBatch, World world, Vector2 positionRelativeToScreen, Vector2 destination, Vector2 positionRelativeToGame, double delta) {

        CircleShape circle = new CircleShape();
        circle.setRadius(9 / PPM);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = circle;
        fdef.friction = 1f;
        fdef.restitution = 0f;
        fdef.density = 1f;
        fdef.filter.categoryBits = BIT_BULLET;
        fdef.filter.maskBits = BIT_BOSSES | BIT_WORM | BACKGROUND;

        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.allowSleep = true;
        bdef.bullet = true;
        bdef.fixedRotation = true;
        Body body = world.createBody(bdef);
        body.createFixture(fdef).setUserData("bullet");
        Random r = new Random();
        double randomValue1 = -delta + (delta - -delta) * r.nextDouble();
        double randomValue2 = -delta + (delta - -delta) * r.nextDouble();
        body.applyLinearImpulse(new Vector2((float) (destination.x - positionRelativeToScreen.x + randomValue1), (float) (destination.y - positionRelativeToScreen.y + randomValue2)), positionRelativeToScreen, true);
        float angle = (float) Math.atan2((double) body.getLinearVelocity().y, (double) body.getLinearVelocity().x);
        body.setTransform(new Vector2(positionRelativeToGame.x, positionRelativeToGame.y), (angle + (float) Math.PI + ((float) Math.PI) / 1.0f));

        return new Bullet(world, spriteBatch, body);
    }
}
