package ee.taltech.iti0202.gui.game.desktop.handlers.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.handlers.gdx.input.MyInput;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.components.GameButton;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_WIDTH;

public class MainMenu extends Scene {

    private enum block {
        NEWGAME, RESUME, EXIT, SETTINGS, DEFAULT
    }

    private block currBlock = block.DEFAULT;
    private Runnable newGameFunc;
    private Runnable resumeGameFunc;
    private Runnable settingsFunc;

    private GameButton newGameButton;
    private GameButton loadGameButton;
    private GameButton settingsButton;
    private GameButton exitButton;

    private HashMap<GameButton, MainMenu.block> buttonType;

    public MainMenu(OrthographicCamera cam, Runnable newGameFunc, Runnable resumeGameFunc, Runnable settingsFunc) {
        super(cam);
        this.newGameFunc = newGameFunc;
        this.resumeGameFunc = resumeGameFunc;
        this.settingsFunc = settingsFunc;

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

        for (GameButton button : buttons) played.put(button, false);

        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
    }

    @Override
    public void handleInput() {
        if (MyInput.isMouseClicked(Game.settings.SHOOT)) {
            switch (currBlock) {
                case NEWGAME:
                    playSoundOnce("sounds/menu_click.wav", 0.5f);
                    newGameFunc.run();
                    break;
                case RESUME:
                    playSoundOnce("sounds/menu_click.wav", 0.5f);
                    resumeGameFunc.run();
                    break;
                case SETTINGS:
                    playSoundOnce("sounds/menu_click.wav", 0.5f);
                    settingsFunc.run();
                    break;
                case EXIT:
                    playSoundOnce("sounds/menu_click.wav", 0.5f);
                    Gdx.app.exit();
                    break;
            }
        }
    }

    @Override
    protected void updateCurrentBlock(GameButton button) {
        currBlock = buttonType.get(button);
    }
}
