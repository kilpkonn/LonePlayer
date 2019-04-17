package ee.taltech.iti0202.gui.game.desktop.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.states.Play;

public class PlantWorm extends Boss {
    public PlantWorm(Body body, String type, Play play, String part) {
        super(body, type, play);
        Texture tex = Game.res.getTexture(part);
        TextureRegion[] sprites;
        switch (part) {
            case "head1":
                sprites = TextureRegion.split(tex, tex.getWidth() / 4, tex.getHeight())[0];
                break;
            default:
                sprites = TextureRegion.split(tex, tex.getHeight(), tex.getHeight())[0];
                break;
        }
        setAnimation(sprites, 1 / 12f);
    }
}
