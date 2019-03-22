package ee.taltech.iti0202.gui.game.desktop.handlers.gdx;

import java.util.Stack;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.states.GameState;
import ee.taltech.iti0202.gui.game.desktop.states.Menu;
import ee.taltech.iti0202.gui.game.desktop.states.Play;

public class GameStateManager {

    private Game game;

    private Stack<GameState> gameStates;

    public enum State {
        PLAY, MENU
    }

    public GameStateManager(Game game) {
        this.game = game;
        gameStates = new Stack<GameState>();
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

    }

    private GameState getState(State state) {
        if (state == State.MENU) {
            return new Menu(this);
        }
        System.out.println("desired state was no found!");
        return null;
    }

    private GameState getState(State state, int act, String level) {
        if (state == State.PLAY) {
            return new Play(this, act, level);
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

    public void pushState(State state, int act, String level) {
        gameStates.push(getState(state, act, level));
    }

    public void popState() {
        GameState g = gameStates.pop();
        g.dispose();
    }
}
