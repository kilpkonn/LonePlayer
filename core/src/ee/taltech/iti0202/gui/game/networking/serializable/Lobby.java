package ee.taltech.iti0202.gui.game.networking.serializable;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.networking.server.player.Player;

public class Lobby implements Serializable {

    public static class NameChange implements Serializable {
        public NameChange() {}
        public NameChange(String newName) {
            this.newName = newName;
        }
        public String newName;
    }

    public static class Kick implements Serializable {
        public Kick() {}
        public Kick(Player playerToBeKicked) {
            this.playerToBeKicked = playerToBeKicked;
        }
        public Player playerToBeKicked;
    }

    public static class ActMapDifficulty implements Serializable {
        public ActMapDifficulty() {}
        public ActMapDifficulty(String act, String map, B2DVars.GameDifficulty difficulty) {
            this.act = act;
            this.map = map;
            this.difficulty = difficulty;
        }
        public String act;
        public String map;
        public B2DVars.GameDifficulty difficulty;
    }

    public static class Details implements Serializable {
        public Set<Player> players = new HashSet<>();
        public String act = "";
        public String map = "";
        public B2DVars.GameDifficulty difficulty;
    }

    public static class StartGame implements Serializable {
        public Details details = new Details();
    }
}
