package ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class ParallaxBackground extends Actor {

    private float x, y, width, height, scaleX, scaleY;
    private int originX;
    private int originY;
    private int rotation;
    private int srcY;
    private boolean flipX, flipY;
    private float scroll;
    private Array<Texture> layers;
    private float speed;

    public ParallaxBackground(Array<Texture> textures) {
        layers = textures;
        for (int i = 0; i < textures.size; i++) {
			layers.get(i)
					.setWrap(
							Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }
        scroll = 0;
        speed = 0;

        x = y = originX = originY = rotation = srcY = 0;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        scaleY = height / textures.get(0).getHeight();
        scaleX = 1f;
        flipX = flipY = false;
    }

    public void setSpeed(float newSpeed) {
        this.speed = newSpeed;
    }

    @Override
    public void act(float dt) {
        scroll += speed * dt;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);
        for (int i = 0; i < layers.size; i++) {
            float LAYER_SPEED_DIFFERENCE = 1f;
            int srcX = (int) (scroll + i * LAYER_SPEED_DIFFERENCE * scroll);
			batch.draw(
					layers.get(i),
					x,
					y,
					originX,
					originY,
					width,
					height,
					scaleX,
					scaleY,
					rotation,
					srcX,
					srcY,
					layers.get(i).getWidth(),
					layers.get(i).getHeight(),
					flipX,
					flipY);
        }
    }
}
