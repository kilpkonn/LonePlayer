package ee.taltech.iti0202.gui.game.desktop.handlers.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_WIDTH;

public class MainMenu extends Scene {
    private GameButton newGameButtonActive;
    private GameButton loadGameButtonActive;
    private GameButton settingsButtonActive;
    private GameButton exitButtonActive;

    public MainMenu(OrthographicCamera cam) {
        super(cam);

        newGameButtonActive = new GameButton("New Game", V_WIDTH / 5f, V_HEIGHT / 1.5f);
        loadGameButtonActive = new GameButton("Resume", V_WIDTH / 5f, V_HEIGHT / 1.5f - 40);
        settingsButtonActive = new GameButton("Settings", V_WIDTH / 5f, V_HEIGHT / 1.5f - 80);
        exitButtonActive = new GameButton("Exit", V_WIDTH / 5f, V_HEIGHT / 1.5f - 120);

        buttons = new HashSet<>(Arrays.asList(newGameButtonActive, loadGameButtonActive, settingsButtonActive, exitButtonActive));

        buttonType = new HashMap<GameButton, block>() {{
            put(newGameButtonActive, block.NEWGAME);
            put(loadGameButtonActive, block.RESUME);
            put(settingsButtonActive, block.SETTINGS);
            put(exitButtonActive, block.EXIT);
        }};

        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
    }
}
