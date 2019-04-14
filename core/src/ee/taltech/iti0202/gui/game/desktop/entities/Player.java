package ee.taltech.iti0202.gui.game.desktop.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import ee.taltech.iti0202.gui.game.Game;

public class Player extends B2DSprite {

    private int numCrystals;
    private int totalCrystals;
    private int health;

    public Player(Body body) {

        super(body);

        Texture tex = Game.res.getTexture("Llama");
        TextureRegion[] sprites = TextureRegion.split(tex, tex.getHeight(), tex.getHeight())[0];

        setAnimation(sprites, 1 / 6f);

        health = 100;
    }

    public void collectCrystal() {
        numCrystals++;
    }

    public int getNumCrystals() {
        return numCrystals;
    }

    public void setTotalCrystals(int i) {
        totalCrystals = i;
    }

    public int getTotalCrystals() {
        return totalCrystals;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

}
