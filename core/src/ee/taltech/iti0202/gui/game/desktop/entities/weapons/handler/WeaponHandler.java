package ee.taltech.iti0202.gui.game.desktop.entities.weapons.handler;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

import ee.taltech.iti0202.gui.game.desktop.entities.Handler;
import ee.taltech.iti0202.gui.game.desktop.entities.player.Player;
import ee.taltech.iti0202.gui.game.desktop.entities.weapons.Weapon;
import ee.taltech.iti0202.gui.game.desktop.entities.weapons.loader.WeaponLoader;
import lombok.Data;

@Data
public class WeaponHandler implements Handler {

    private final World world;
    private List<Weapon> weaponList = new ArrayList<>();

    public WeaponHandler(World world) {
        this.world = world;
    }

    @Override
    public void update(float dt) {
        for (Weapon weapon : weaponList) {
            weapon.update(dt);
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        for (Weapon weapon : weaponList) {
            weapon.render(spriteBatch);
        }
    }

    public void addWeapon(Weapon weapon) {
        if (!weaponList.contains(weapon)) {
            weaponList.add(weapon);
        }
    }

    public Weapon initWeapon(String type, SpriteBatch spriteBatch, Player player) {
        Weapon weapon = WeaponLoader.buildWeapon(type, spriteBatch, this);
        addWeapon(weapon);
        player.setWeapon(weapon);
        return weapon;
    }
}
