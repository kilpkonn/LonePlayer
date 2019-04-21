package ee.taltech.iti0202.gui.game.desktop.entities.animated;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.brashmonkey.spriter.Data;
import com.brashmonkey.spriter.LibGdx.LibGdxDrawer;
import com.brashmonkey.spriter.LibGdx.LibGdxLoader;

import java.util.HashSet;

import ee.taltech.iti0202.gui.game.desktop.entities.animated.loader.AnimationLoader;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PPM;

public class SpriteAnimation {

    private MyPlayerTweener playerTweener;
    private Data data;
    private LibGdxLoader loader;
    private LibGdxDrawer drawer;
    private float heightOffset;

    protected Body body;
    private float opacity = 1;

    public SpriteAnimation(Body body, SpriteBatch sb, String path) {
        this(body, sb, path, null, 0, 0);
    }

    public SpriteAnimation(Body body, SpriteBatch sb, String path, String entity) {
        this(body, sb, path, entity, 0, 0);
    }

    public SpriteAnimation(Body body, SpriteBatch sb, String path, String entity, float x, float y) {
        System.out.println("New body: " + body.toString());
        this.body = body;
        //animation = new Animation();
        data = AnimationLoader.getData(path);
        loader = AnimationLoader.getLoader(path);


        drawer = new LibGdxDrawer(loader, sb, null); // no shape rendering
        if (entity != null) {
            playerTweener = new MyPlayerTweener(data.getEntity(entity));
        } else {
            playerTweener = new MyPlayerTweener(data.getEntity(0));
        }
        playerTweener.setPivot(x, y);
        HashSet<String> toPlayOnce = new HashSet<>();
        toPlayOnce.add("roll");
        toPlayOnce.add("attack");
        playerTweener.setAnimToPlayOnce(toPlayOnce);
    }

    public void update(float dt) {
        //animation.update(dt);
        playerTweener.update(dt);
        playerTweener.setPosition(body.getPosition().x * PPM, body.getPosition().y * PPM + heightOffset);
        playerTweener.setAngle((float) Math.toDegrees(body.getAngle()));
    }

    public void render(SpriteBatch sb) {
        sb.setColor(1, 1, 1, opacity);
        sb.begin();

        drawer.updateSpriteBatch(sb);
        drawer.draw(playerTweener);

        sb.setColor(1, 1, 1, 1);
        sb.end();
    }

    protected void setAnimation(String animation, boolean playOnce) {
        playerTweener.setAnimation(animation, playOnce);
    }

    public void setFlipX(boolean flipX) {
        if ((playerTweener.flippedX() == -1) != flipX)
            playerTweener.flipX();
    }

    public void setScale(float scale) {
        playerTweener.setScale(scale);
    }

    public void setAnimationSpeed(int speed) {
        playerTweener.speed = speed;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public void setHeightOffset(float offset) {
        this.heightOffset = offset;
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

}
