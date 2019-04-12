package ee.taltech.iti0202.gui.game.desktop.handlers.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.handlers.gdx.GameStateManager;
import ee.taltech.iti0202.gui.game.desktop.handlers.gdx.input.MyInput;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.components.GameButton;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_WIDTH;


public class PauseMenu extends Scene{

    private enum block {
        SAVE, RESUME, EXIT, SETTINGS, SAVEANDEXIT
    }

    private PauseMenu.block currBlock;

    private HashMap<GameButton, PauseMenu.block> buttonType;

    private Runnable resumeFunc;
    private Runnable saveFunc;
    private Runnable openSettingsFunc;

    private GameButton exitButton;
    private GameButton settingsButton;
    private GameButton resumeButton;
    private GameButton saveButton;
    private GameButton saveAndExitButton;

    public PauseMenu(String act, String map, OrthographicCamera cam, Runnable resumeFunc, Runnable saveFunc, Runnable openSettingsFunc) {
        super(act, map, cam);
        //this.pauseState = B2DVars.pauseState.RUN;
        this.resumeFunc = resumeFunc;
        this.saveFunc = saveFunc;
        this.openSettingsFunc = openSettingsFunc;

        hudCam.update();

        //Texture backLayer = Game.res.getTexture("backLayer");

        resumeButton = new GameButton("Resume", V_WIDTH / 5f, V_HEIGHT / 1.5f + 40);
        saveButton = new GameButton("Save", V_WIDTH / 5f, V_HEIGHT / 1.5f);
        saveAndExitButton = new GameButton("Save n' Exit", V_WIDTH / 5f, V_HEIGHT / 1.5f - 40);
        settingsButton = new GameButton("Settings", V_WIDTH / 5f, V_HEIGHT / 1.5f - 80);
        exitButton = new GameButton("Exit", V_WIDTH / 5f, V_HEIGHT / 1.5f - 120);

        buttons = new HashSet<>(Arrays.asList(resumeButton, saveButton, saveAndExitButton, settingsButton, exitButton));

        buttonType = new HashMap<GameButton, block>() {{
            put(resumeButton, block.RESUME);
            put(saveButton, block.SAVE);
            put(saveAndExitButton, block.SAVEANDEXIT);
            put(settingsButton, block.SETTINGS);
            put(exitButton, block.EXIT);
        }};
        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
    }

    @Override
    public void handleInput() {
        if (MyInput.isMouseClicked(Game.settings.SHOOT)) {
            switch (currBlock) {
                case RESUME:
                    resumeFunc.run();
                    break;
                case SAVE:
                    saveFunc.run();
                    break;
                case SAVEANDEXIT:
                    saveFunc.run();
                    GameStateManager.pushState(GameStateManager.State.MENU);
                    break;
                case EXIT:
                    GameStateManager.pushState(GameStateManager.State.MENU);
                    break;
                case SETTINGS:
                    openSettingsFunc.run();
                    break;
            }
        }
    }

    @Override
    protected void updateCurrentBlock(GameButton button) {
        currBlock = buttonType.get(button);
    }
}
