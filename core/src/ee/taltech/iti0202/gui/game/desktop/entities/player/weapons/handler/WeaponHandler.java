package ee.taltech.iti0202.gui.game.desktop.entities.player.weapons.handler;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

import java.util.List;

import ee.taltech.iti0202.gui.game.desktop.entities.player.Player;
import ee.taltech.iti0202.gui.game.desktop.entities.player.weapons.Weapon;
import lombok.Data;

@Data
public class WeaponHandler {

    private final World world;
    private Weapon weapon;
    private List<Weapon> weaponList; //todo

    public WeaponHandler(World world) {
        this.world = world;
    }

    public void update(float dt) {
        //weapon.update(dt);
    }

    public void render(SpriteBatch spriteBatch) {
        //weapon.render(spriteBatch);
    }
}
