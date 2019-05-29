package ee.taltech.iti0202.gui.game.networking.serializable;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import ee.taltech.iti0202.gui.game.networking.server.player.Player;

public class Lobby implements Serializable {

    public static class NameChange implements Serializable {
        public String newName = "";
    }

    public static class Details implements Serializable {
        public Set<Player> players = new HashSet<>();
    }
}
