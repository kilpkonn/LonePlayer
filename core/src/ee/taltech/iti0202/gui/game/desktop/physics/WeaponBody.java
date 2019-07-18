package ee.taltech.iti0202.gui.game.desktop.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import ee.taltech.iti0202.gui.game.desktop.entities.weapons2.Weapon;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BACKGROUND;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BIT_BULLET;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BIT_WEAPON;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.DIMENSION_1;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.DIMENSION_2;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.PPM;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.TERRA_DIMENSION_1;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.TERRA_DIMENSION_2;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.TERRA_SQUARES;

public class WeaponBody {

    public static Body createWeapon(World world, Vector2 pos, int id, Weapon.Type type) {
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circle = new CircleShape();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(pos);
        circle.setRadius(9 / PPM);
        fixtureDef.shape = circle;
        fixtureDef.filter.categoryBits = BIT_WEAPON;
        fixtureDef.filter.maskBits = DIMENSION_1 | DIMENSION_2 | TERRA_SQUARES | BACKGROUND | TERRA_DIMENSION_1 | TERRA_DIMENSION_2 | BIT_BULLET;

        Body body = world.createBody(bodyDef);
        WeaponBodyData data = new WeaponBodyData(id, type);
        body.createFixture(fixtureDef).setUserData(data);
        body.setUserData(data);
        return body;
    }

    public static class WeaponBodyData {
        public int id;
        public Weapon.Type type;
        public boolean flippedAnimation;
        public boolean dropped = true;

        public WeaponBodyData(int id, Weapon.Type type) {
            this.id = id;
            this.type = type;
        }
    }
}
