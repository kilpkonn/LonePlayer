package ee.taltech.iti0202.gui.game.desktop.entities.projectile.bullet;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import ee.taltech.iti0202.gui.game.desktop.states.Play;
import lombok.Data;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BIT_ALL;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PPM;

@Data
public class BulletLoader {
    private Play play;
    private SpriteBatch spriteBatch;
    private FixtureDef fdef;
    private BodyDef bdef;
    private World world;
    private Vector2 position;
    private Body body;

    public Bullet bulletLoader(Play play, SpriteBatch spriteBatch, World world, Vector2 position) {

        this.play = play;
        this.spriteBatch = spriteBatch;
        this.fdef = new FixtureDef();
        this.bdef = new BodyDef();
        this.world = world;
        this.position = position;

        CircleShape circle = new CircleShape();
        circle.setRadius(9 / PPM);

        fdef.shape = circle;
        fdef.friction = 1f;
        fdef.restitution = 0f;
        fdef.density = 0f;
        fdef.filter.categoryBits = BIT_ALL;
        fdef.filter.maskBits = BIT_ALL;

        bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.allowSleep = false;
        bdef.bullet = true;
        bdef.fixedRotation = true;
        Body body = world.createBody(bdef);
        body.createFixture(fdef).setUserData("bullet");
        body.setTransform(new Vector2(position.x, position.y + 1), body.getAngle());
        body.applyLinearImpulse(new Vector2(10, 10), position, true);
        this.body = body;

        return new Bullet(world, spriteBatch, "images/bosses/plantworm/plantworm.scml", body); //todo add animation for bullet, cannon blast and red line ?
    }
}
