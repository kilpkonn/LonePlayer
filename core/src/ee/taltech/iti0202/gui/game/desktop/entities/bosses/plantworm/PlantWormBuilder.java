package ee.taltech.iti0202.gui.game.desktop.entities.bosses.plantworm;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

import ee.taltech.iti0202.gui.game.desktop.states.Play;

public class PlantWormBuilder {
    private Body body;
    private SpriteBatch sb;
    private String type;
    private Play play;
    private PlantWorm.Part part;
    private float size;
    private float x = 0;
    private float y = 0;

    public PlantWormBuilder setBody(Body body) {
        this.body = body;
        return this;
    }

    public PlantWormBuilder setSb(SpriteBatch sb) {
        this.sb = sb;
        return this;
    }

    public PlantWormBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public PlantWormBuilder setPlay(Play play) {
        this.play = play;
        return this;
    }

    public PlantWormBuilder setPart(PlantWorm.Part part) {
        this.part = part;
        return this;
    }

    public PlantWormBuilder setSize(float size) {
        this.size = size;
        return this;
    }

    public PlantWormBuilder setX(float x) {
        this.x = x;
        return this;
    }

    public PlantWormBuilder setY(float y) {
        this.y = y;
        return this;
    }

    public PlantWorm createPlantWorm() {
        return new PlantWorm(body, sb, type, play, part, size, x, y);
    }
}