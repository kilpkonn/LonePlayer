package ee.taltech.iti0202.gui.game.desktop.entities.animations;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.brashmonkey.spriter.Data;
import com.brashmonkey.spriter.LibGdx.LibGdxDrawer;
import com.brashmonkey.spriter.LibGdx.LibGdxLoader;

import ee.taltech.iti0202.gui.game.desktop.entities.animations.loader.AnimationLoader;
import ee.taltech.iti0202.gui.game.desktop.entities.animations.loader.MultiplayerAnimation;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.PPM;

public class SpriteAnimation2 {

    protected Body body;
    protected MultiplayerPlayerTweener playerTweener;

    private Data data;
    private LibGdxLoader loader;
    private LibGdxDrawer drawer;

    private float opacity = 1;

    public SpriteAnimation2(Body body, String path) {
        this(body, path, null);
    }

    public SpriteAnimation2(Body body, String path, String entity) {
        this.body = body;

        data = AnimationLoader.getData(path);
        loader = AnimationLoader.getLoader(path);

        drawer = new LibGdxDrawer(loader, new SpriteBatch(), null); // no shape rendering
        if (entity != null) {
            playerTweener = new MultiplayerPlayerTweener(data.getEntity(entity));
        } else {
            playerTweener = new MultiplayerPlayerTweener(data.getEntity(0));
        }
    }

    public void update(float dt) {
        playerTweener.update(dt);
        playerTweener.setPosition(body.getPosition().x * PPM, body.getPosition().y * PPM);
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

    public void setAnimation(MultiplayerAnimation animation) {
        playerTweener.setAnimation(animation);
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public void setFlipX(boolean flipX) {
        if ((playerTweener.flippedX() == -1) != flipX) playerTweener.flipX();
    }

    public boolean isFlippedX() {
        return playerTweener.flippedX() == -1;
    }

    public Body getBody() {
        return body;
    }
}
