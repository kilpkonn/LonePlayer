package ee.taltech.iti0202.gui.game.networking.serializable;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import ee.taltech.iti0202.gui.game.networking.server.entity.BulletEntity;
import ee.taltech.iti0202.gui.game.networking.server.entity.PlayerEntity;
import ee.taltech.iti0202.gui.game.networking.server.entity.WeaponEntity;

public class Play implements Serializable {

    public static class Players implements Serializable {
        public Set<PlayerEntity> players = new HashSet<>();
    }

    public static class Weapons implements Serializable {
        public Set<WeaponEntity> weapons = new HashSet<>();
    }

    public static class Bullets implements Serializable {
        public Set<BulletEntity> bullets = new HashSet<>();
    }

    public static class EntitiesToBeRemoved implements Serializable {
        public Set<Integer> players = new HashSet<>();
        public Set<Integer> weapons = new HashSet<>();
        public Set<Integer> bullets = new HashSet<>();
    }
}
