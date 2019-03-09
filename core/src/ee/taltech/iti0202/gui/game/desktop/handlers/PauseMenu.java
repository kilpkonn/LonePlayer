package ee.taltech.iti0202.gui.game.desktop.handlers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ee.taltech.iti0202.gui.game.desktop.states.Play;

public class PauseMenu {
    private B2DVars.pauseState pauseState;

    public PauseMenu(int act, int map) {

        this.pauseState = B2DVars.pauseState.RUN;
    }

    public void setGameState(B2DVars.pauseState s){
        this.pauseState = s;
    }

    public B2DVars.pauseState getPauseState() {
        return pauseState;
    }
}
