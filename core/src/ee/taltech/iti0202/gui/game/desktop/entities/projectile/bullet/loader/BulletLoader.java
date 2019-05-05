package ee.taltech.iti0202.gui.game.desktop.entities.projectile.bullet.loader;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import ee.taltech.iti0202.gui.game.desktop.entities.projectile.bullet.Bullet;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BIT_BOSSES;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BIT_BULLET;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BIT_WORM;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PPM;

public class BulletLoader {

    public static Bullet bulletLoader(SpriteBatch spriteBatch, World world, Vector2 positionRelativeToScreen, Vector2 destination, Vector2 positionRelativeToGame) {

        CircleShape circle = new CircleShape();
        circle.setRadius(9 / PPM);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = circle;
        fdef.friction = 1f;
        fdef.restitution = 0f;
        fdef.density = 10f;
        fdef.filter.categoryBits = BIT_BULLET;
        fdef.filter.maskBits = BIT_BOSSES | BIT_WORM;

        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.allowSleep = true;
        bdef.bullet = true;
        bdef.fixedRotation = true;
        Body body = world.createBody(bdef);
        body.createFixture(fdef).setUserData("bullet");
        body.setTransform(new Vector2(positionRelativeToGame.x, positionRelativeToGame.y), body.getAngle());
        System.out.println(destination);
        System.out.println(positionRelativeToScreen);
        System.out.println(positionRelativeToGame);
        body.applyLinearImpulse(new Vector2((destination.x - positionRelativeToScreen.x), (destination.y - positionRelativeToScreen.y)), positionRelativeToScreen, true);

        return new Bullet(world, spriteBatch, body); //todo add animation for bullet, cannon blast and red line ?
    }
}
