package ee.taltech.iti0202.gui.game.desktop.states;

import ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.desktop.states.gameprogress.GameProgress;

public class PlayBuilder {
    private String act;
    private String map;
    private B2DVars.gameDifficulty difficulty;
    private GameProgress progress;

    public PlayBuilder setAct(String act) {
        this.act = act;
        return this;
    }

    public PlayBuilder setMap(String map) {
        this.map = map;
        return this;
    }

    public PlayBuilder setDifficulty(B2DVars.gameDifficulty difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public PlayBuilder setProgress(GameProgress progress) {
        this.progress = progress;
        return this;
    }

    public Play createPlay() {
        return new Play(act, map, difficulty, progress);
    }
}