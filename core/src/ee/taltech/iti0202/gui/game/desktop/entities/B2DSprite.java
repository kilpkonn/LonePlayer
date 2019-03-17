package ee.taltech.iti0202.gui.game.desktop.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import ee.taltech.iti0202.gui.game.desktop.handlers.scene.animations.Animation;
import ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars;

public class B2DSprite {

    private Body body;
    private Animation animation;

    B2DSprite(Body body) {
        System.out.println("New body: " + body.toString());
        this.body = body;
        animation = new Animation();
    }

    void setAnimation(TextureRegion[] reg, float delay) {
        animation.setFrames(reg, delay);
        float width = reg[0].getRegionWidth();
        float height = reg[0].getRegionHeight();
    }

    public void update(float dt) {
        animation.update(dt);
    }

    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(
                animation.getFrame(),
                body.getPosition().x * B2DVars.PPM,
                body.getPosition().y * B2DVars.PPM);

        sb.end();
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

}
