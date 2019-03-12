package ee.taltech.iti0202.gui.game.desktop.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import ee.taltech.iti0202.gui.game.Game;

public class MagmaWorm extends Boss {
    public MagmaWorm(Body body, String type) {
        super(body, type);

        Texture tex = Game.res.getTexture("Player");
        TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[0];
        setAnimation(sprites, 1 / 12f);
    }

}
