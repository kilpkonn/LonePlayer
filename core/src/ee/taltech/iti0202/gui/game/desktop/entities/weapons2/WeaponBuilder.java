package ee.taltech.iti0202.gui.game.desktop.entities.weapons2;

import com.badlogic.gdx.physics.box2d.Body;

public class WeaponBuilder {
    private Weapon.Type type;
    private Body body;

    public WeaponBuilder setType(Weapon.Type type) {
        this.type = type;
        return this;
    }

    public WeaponBuilder setBody(Body body) {
        this.body = body;
        return this;
    }

    public Weapon create() {
        return new Weapon(body, type.getAnimationFile());
    }
}
