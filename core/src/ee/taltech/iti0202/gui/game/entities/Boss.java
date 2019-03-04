package ee.taltech.iti0202.gui.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import ee.taltech.iti0202.gui.game.Game;

public class Boss extends B2DSprite {

    public Boss(Body body) {

        super(body);

        Texture tex = Game.res.getTexture("Player");
        TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[0];

        setAnimation(sprites, 1 / 12f);

    }
}
