package ee.taltech.iti0202.gui.game.desktop.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.animations.Animation;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.PPM;

public class B2DSprite {

    protected Body body;
    private Animation animation;
    private float opacity = 1;

    B2DSprite(Body body) {
        System.out.println("New body: " + body.toString());
        this.body = body;
        animation = new Animation();
    }

    void setAnimation(TextureRegion[] reg, float delay) {
        animation.setFrames(reg, delay);
    }

    public void update(float dt) {
        animation.update(dt);
    }

    public void render(SpriteBatch sb, boolean rotate) {
        sb.setColor(1, 1, 1, opacity);
        sb.begin();

        float posX = body.getPosition().x * PPM;
        float posY = body.getPosition().y * PPM;
        double rotation = Math.toDegrees(body.getAngle());
        sb.draw(
                animation.getFrame(),
                posX,
                posY,
                1,
                1,
                animation.getFrame().getRegionWidth(),
                animation.getFrame().getRegionHeight(),
                1,
                1,
                (float) rotation);

        sb.setColor(1, 1, 1, 1);
        sb.end();
    }

    public void render(SpriteBatch sb) {
        sb.setColor(1, 1, 1, opacity);
        sb.begin();

        sb.draw(animation.getFrame(), body.getPosition().x * PPM, body.getPosition().y * PPM);

        sb.end();
        sb.setColor(1, 1, 1, 1);
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }
}
