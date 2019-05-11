package ee.taltech.iti0202.gui.game.desktop.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import ee.taltech.iti0202.gui.game.Game;

public class EndPoint extends B2DSprite {

    public EndPoint(Body body) {

        super(body);

        Texture tex = Game.res.getTexture("Checkpoint");
        TextureRegion[] sprites = TextureRegion.split(tex, 32, 64)[0];

        setAnimation(sprites, 1 / 12f);
    }
}
