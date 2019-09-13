package ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.desktop.states.GameState;
import ee.taltech.iti0202.gui.game.desktop.states.Menu;
import ee.taltech.iti0202.gui.game.desktop.states.Multiplayer;
import ee.taltech.iti0202.gui.game.desktop.states.Play;
import ee.taltech.iti0202.gui.game.desktop.states.gameprogress.GameProgress;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

public class GameStateManager {

    private static Game game;
    private static Deque<GameState> gameStates;

    public GameStateManager(Game newGame) {
        game = newGame;
        gameStates = new ArrayDeque<>();
        pushState(State.MENU);
    }

    public static GameState currentState() {
        return gameStates.peek();
    }

    public static Game game() {
        return game;
    }

    public static void update(float dt) {
        gameStates.getLast().update(dt);
    }

    public static void render() {
        gameStates.getLast().render();
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

    private static GameState getState(
            State state, String act, String map, B2DVars.GameDifficulty difficulty) {
        if (state == State.PLAY) {
            return new Play(act, map, difficulty);
        } else if (state == State.MULTIPLAYER) {
            return new Multiplayer(act, map, difficulty);
        }
        System.out.println("desired state was no found!");
        return null;
    }

    public static void setState(State state) {
        game.getSound().stop();
        popState();
        pushState(state);
    }

    public static void pushState(State state) {
        popState();
        gameStates.push(getState(state));
        System.out.println(state);
    }

    public static void pushState(State state, GameProgress progress) {
        popState();
        gameStates.push(getState(state, progress));
    }

    public static void pushState(
            State state, String act, String map, B2DVars.GameDifficulty difficulty) {
        popState();
        gameStates.push(getState(state, act, map, difficulty));
    }

    public static void popState() {
        if (gameStates.isEmpty()) {
            return;
        }
        GameState g = gameStates.pop();
        g.dispose();
    }

    public enum State {
        PLAY,
        MENU,
        MULTIPLAYER
    }
}
