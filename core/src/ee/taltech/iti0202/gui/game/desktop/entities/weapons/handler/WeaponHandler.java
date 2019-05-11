package ee.taltech.iti0202.gui.game.desktop.entities.weapons.handler;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

import ee.taltech.iti0202.gui.game.desktop.entities.Handler;
import ee.taltech.iti0202.gui.game.desktop.entities.player.Player;
import ee.taltech.iti0202.gui.game.desktop.entities.weapons.Weapon;
import ee.taltech.iti0202.gui.game.desktop.entities.weapons.loader.WeaponLoader;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.canvas.Draw;
import lombok.Data;

@Data
public class WeaponHandler implements Handler {

    private final World world;
    private final Draw draw;
    private List<Weapon> playersWeapons = new ArrayList<>();
    private List<Weapon> weaponsOnMap = new ArrayList<>();

    public WeaponHandler(World world, Draw draw) {
        this.world = world;
        this.draw = draw;
    }

    public void deadPlayerWeaponTransfer(Player player) {
        for (Weapon weapon : player.getWeapons()) {
            playersWeapons.remove(weapon);
            weaponsOnMap.add(weapon);
        }
    }

    @Override
    public void update(float dt) {
        for (Weapon weapon : weaponsOnMap) {
            weapon.update(dt);  //TODO: Make it possible to pick up weapons
        }

        for (Weapon weapon : playersWeapons) {
            weapon.update(dt);
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        for (Weapon weapon : weaponsOnMap) {
            weapon.render(spriteBatch);
        }
        draw.getPlayerHandler().getPlayer().getWeapon().render(spriteBatch);

    }

    public void addWeapon(Weapon weapon) {
        if (!playersWeapons.contains(weapon)) {
            playersWeapons.add(weapon);
        }
    }

    public Weapon initWeapon(String type, SpriteBatch spriteBatch, Player player) {
        Weapon weapon = WeaponLoader.buildWeapon(type, spriteBatch, this);
        addWeapon(weapon);
        player.setWeapon(weapon);
        return weapon;
    }
}
