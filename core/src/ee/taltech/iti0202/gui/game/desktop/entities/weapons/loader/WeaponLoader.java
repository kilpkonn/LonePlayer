package ee.taltech.iti0202.gui.game.desktop.entities.weapons.loader;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import ee.taltech.iti0202.gui.game.desktop.entities.weapons.Deagle;
import ee.taltech.iti0202.gui.game.desktop.entities.weapons.M4;
import ee.taltech.iti0202.gui.game.desktop.entities.weapons.Weapon;
import ee.taltech.iti0202.gui.game.desktop.entities.weapons.handler.WeaponHandler;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.*;

public class WeaponLoader {

    public static Weapon buildWeapon(
            String type, SpriteBatch spriteBatch, WeaponHandler weaponHandler) {
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        CircleShape circle = new CircleShape();
        bdef.type = BodyDef.BodyType.DynamicBody;
        Body body = weaponHandler.getWorld().createBody(bdef);
        circle.setRadius(9 / PPM);
        fdef.shape = circle;
        fdef.filter.categoryBits = BIT_WEAPON;
        fdef.filter.maskBits = TERRA_SQUARES | BACKGROUND | TERRA_DIMENTSION_1 | TERRA_DIMENTSION_2;
        body.createFixture(fdef).setUserData("weapon");

        Weapon weapon;

        switch (type) {
            case "Deagle":
                weapon = new Deagle(weaponHandler.getWorld(), spriteBatch, body);
                break;
            case "M4":
                weapon = new M4(weaponHandler.getWorld(), spriteBatch, body);
                break;
            default:
                weapon = new Deagle(weaponHandler.getWorld(), spriteBatch, body);
                break;
        }

        weaponHandler.addWeapon(weapon);

        return weapon;
    }
}
