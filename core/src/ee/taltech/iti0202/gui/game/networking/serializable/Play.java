package ee.taltech.iti0202.gui.game.networking.serializable;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import ee.taltech.iti0202.gui.game.networking.server.entity.Bullet;
import ee.taltech.iti0202.gui.game.networking.server.entity.Player;
import ee.taltech.iti0202.gui.game.networking.server.entity.Weapon;

public class Play implements Serializable {

    public static class Players implements Serializable {
        public Set<Player> players = new HashSet<>();
    }

    public static class Weapons implements Serializable {
        public Set<Weapon> weapons = new HashSet<>();
    }

    public static class Bullets implements Serializable {
        public Set<Bullet> bullets = new HashSet<>();
    }
}
