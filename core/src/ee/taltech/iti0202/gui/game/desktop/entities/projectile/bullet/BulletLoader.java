package ee.taltech.iti0202.gui.game.desktop.entities.projectile.bullet;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import ee.taltech.iti0202.gui.game.desktop.states.Play;
import lombok.Data;

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

        bdef = new BodyDef();
        bdef.bullet = true;
        bdef.allowSleep = false;
        bdef.fixedRotation = true;
        Body body = world.createBody(bdef);
        body.setTransform(position, body.getAngle());
        this.body = body;

        return new Bullet(world, position, spriteBatch, "images/bosses/plantworm/plantworm.scml", body); //todo adda nimation for bullet, cannon blast and red line ?
    }
}
