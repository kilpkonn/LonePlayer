package ee.taltech.iti0202.gui.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import ee.taltech.iti0202.gui.game.desktop.handlers.Animation;
import ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars;

public class B2DSprite {

    private Body body;
    private Animation animation;
    private float width;
    private float height;

    public B2DSprite(Body body) {
        System.out.println("New body: " + body.toString());
        this.body = body;
        animation = new Animation();
    }

    public void setAnimation(TextureRegion[] reg, float delay) {
        animation.setFrames(reg, delay);
        width = reg[0].getRegionWidth();
        height = reg[0].getRegionHeight();
    }

    public void update(float dt) {
        animation.update(dt);
    }

    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(
                animation.getFrame(),
                body.getPosition().x * B2DVars.PPM - width / 2,
                body.getPosition().y * B2DVars.PPM - height / 2);

        // may the distance between 2 stages be 20 blocks (32 x 32)

        //System.out.println(body.getPosition().x);
        //System.out.println(body.getPosition().y);
        //System.out.println();
        sb.end();
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

}
