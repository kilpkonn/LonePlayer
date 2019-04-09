package ee.taltech.iti0202.gui.game.desktop.handlers.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import ee.taltech.iti0202.gui.game.desktop.handlers.scene.components.GameButton;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_WIDTH;

public class MainMenu extends Scene {
    private GameButton newGameButton;
    private GameButton loadGameButton;
    private GameButton settingsButton;
    private GameButton exitButton;

    public MainMenu(OrthographicCamera cam) {
        super(cam);

        newGameButton = new GameButton("New Game", V_WIDTH / 5f, V_HEIGHT / 1.5f);
        loadGameButton = new GameButton("Resume", V_WIDTH / 5f, V_HEIGHT / 1.5f - 40);
        settingsButton = new GameButton("Settings", V_WIDTH / 5f, V_HEIGHT / 1.5f - 80);
        exitButton = new GameButton("Exit", V_WIDTH / 5f, V_HEIGHT / 1.5f - 120);

        buttons = new HashSet<>(Arrays.asList(newGameButton, loadGameButton, settingsButton, exitButton));

        buttonType = new HashMap<GameButton, block>() {{
            put(newGameButton, block.NEWGAME);
            put(loadGameButton, block.RESUME);
            put(settingsButton, block.SETTINGS);
            put(exitButton, block.EXIT);
        }};

        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
    }
}
