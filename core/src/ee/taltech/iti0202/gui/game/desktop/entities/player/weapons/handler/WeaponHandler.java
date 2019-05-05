package ee.taltech.iti0202.gui.game.desktop.entities.player.weapons.handler;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

import ee.taltech.iti0202.gui.game.desktop.entities.player.weapons.Weapon;
import lombok.Data;

@Data
public class WeaponHandler {

    private final World world;
    private Weapon weapon;

    public WeaponHandler(World world) {
        this.world = world;
    }

    public void update(float dt) {

    }

    public void render(SpriteBatch spriteBatch) {

    }
}
