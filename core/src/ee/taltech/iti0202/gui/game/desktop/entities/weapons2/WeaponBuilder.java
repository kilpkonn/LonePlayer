package ee.taltech.iti0202.gui.game.desktop.entities.weapons2;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

public class WeaponBuilder {
    private Weapon.Type type;
    private SpriteBatch spriteBatch;
    private Body body;

    public WeaponBuilder setType(Weapon.Type type) {
        this.type = type;
        return this;
    }

    public WeaponBuilder setSpriteBatch(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
        return this;
    }

    public WeaponBuilder setBody(Body body) {
        this.body = body;
        return this;
    }

    public Weapon create() {
        switch (type) {
            case M4:
                return new M4(body, spriteBatch);
            case DEAGLE:
                return new Deagle(body, spriteBatch);
            case SHOTGUN:
                return new Shotgun(body, spriteBatch);
        }
        return null; // Never get here
    }
}
