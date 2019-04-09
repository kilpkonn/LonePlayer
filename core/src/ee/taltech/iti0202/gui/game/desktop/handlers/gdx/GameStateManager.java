package ee.taltech.iti0202.gui.game.desktop.handlers.gdx;

import java.util.Stack;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.states.GameState;
import ee.taltech.iti0202.gui.game.desktop.states.Menu;
import ee.taltech.iti0202.gui.game.desktop.states.Play;
import ee.taltech.iti0202.gui.game.desktop.states.gameprogress.GameProgress;

public class GameStateManager {

    private Game game;
    private boolean booting = true;
    private Stack<GameState> gameStates;

    public enum State {
        PLAY, MENU
    }

    public GameStateManager(Game game) {
        this.game = game;
        gameStates = new Stack<>();
        pushState(State.MENU);
    }

    public Game game() {
        return game;
    }

    public void update(float dt) {
        gameStates.peek().update(dt);
    }

    public void render() {
        gameStates.peek().render();
        if (booting) {
            setState(State.MENU);
            booting = false;
        }
    }

    private GameState getState(State state) {
        if (state == State.MENU) {
            return new Menu(this);
        }
        System.out.println("desired state was no found!");
        return null;
    }

    private GameState getState(State state, String act, String level) {
        if (state == State.PLAY) {
            return new Play(this, act, level);
        }
        System.out.println("desired state was no found!");
        return null;
    }

    private GameState getState(State state, GameProgress progress) {
        if (state == State.PLAY) {
            return new Play(this, progress);
        }
        System.out.println("desired state was no found!");
        return null;
    }

    public void setState(State state) {
        popState();
        pushState(state);
    }

    public void pushState(State state) {
        gameStates.push(getState(state));
        System.out.println(state);
    }

    public void pushState(State state, String act, String level) {
        gameStates.push(getState(state, act, level));
    }

    public void pushState(State state, GameProgress progress) {
        gameStates.push(getState(state, progress));
    }

    public void popState() {
        GameState g = gameStates.pop();
        g.dispose();
    }
}
