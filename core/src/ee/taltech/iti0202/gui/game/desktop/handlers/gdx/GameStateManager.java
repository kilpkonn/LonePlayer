package ee.taltech.iti0202.gui.game.desktop.handlers.gdx;

import java.util.Stack;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.desktop.states.GameState;
import ee.taltech.iti0202.gui.game.desktop.states.Menu;
import ee.taltech.iti0202.gui.game.desktop.states.Play;
import ee.taltech.iti0202.gui.game.desktop.states.gameprogress.GameProgress;

public class GameStateManager {

    private static Game game;
    private static Stack<GameState> gameStates;

    public enum State {
        PLAY, MENU
    }

    public GameStateManager(Game newGame) {
        game = newGame;
        gameStates = new Stack<>();
        pushState(State.MENU);
    }

    public static Game game() {
        return game;
    }

    public static void update(float dt) {
        gameStates.peek().update(dt);
    }

    public static void render() {
        gameStates.peek().render();
    }

    private static GameState getState(State state) {
        if (state == State.MENU) {
            return new Menu();
        }
        System.out.println("desired state was no found!");
        return null;
    }

    private static GameState getState(State state, GameProgress progress) {
        if (state == State.PLAY) {
            return new Play(progress);
        }
        System.out.println("desired state was no found!");
        return null;
    }

    private static GameState getState(State state, String act, String map, B2DVars.gameDifficulty difficulty) {
        if (state == State.PLAY) {
            return new Play(act, map, difficulty);
        }
        System.out.println("desired state was no found!");
        return null;
    }

    public static void setState(State state) {
        popState();
        pushState(state);
    }

    public static void pushState(State state) {
        gameStates.push(getState(state));
        System.out.println(state);
    }

    public static void pushState(State state, GameProgress progress) {
        gameStates.push(getState(state, progress));
    }

    public static void pushState(State state, String act, String map, B2DVars.gameDifficulty difficulty) {
        gameStates.push(getState(state, act, map, difficulty));
    }

    public static void popState() {
        GameState g = gameStates.pop();
        g.dispose();
    }
}
