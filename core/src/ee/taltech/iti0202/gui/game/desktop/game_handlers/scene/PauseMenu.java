package ee.taltech.iti0202.gui.game.desktop.game_handlers.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;
import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.GameStateManager;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.input.MyInput;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.components.GameButton;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.V_WIDTH;

public class PauseMenu extends Scene {

    private Runnable resumeFunc;
    private Runnable saveFunc;
    private Runnable openSettingsFunc;
    private GameButton exitButton;
    private GameButton settingsButton;
    private GameButton resumeButton;
    private GameButton saveButton;
    private GameButton saveAndExitButton;
    public boolean done = false;

    public PauseMenu(
            String act,
            String map,
            OrthographicCamera cam,
            Runnable resumeFunc,
            Runnable saveFunc,
            Runnable openSettingsFunc) {
        super(act, map, cam);
        this.resumeFunc = resumeFunc;
        this.saveFunc = saveFunc;
        this.openSettingsFunc = openSettingsFunc;

        hudCam.update();

        resumeButton = new GameButton("Resume", V_WIDTH / 5f, V_HEIGHT / 1.5f + 40);
        saveButton = new GameButton("Save", V_WIDTH / 5f, V_HEIGHT / 1.5f);
        saveAndExitButton = new GameButton("Save n' Exit", V_WIDTH / 5f, V_HEIGHT / 1.5f - 40);
        settingsButton = new GameButton("Settings", V_WIDTH / 5f, V_HEIGHT / 1.5f - 80);
        exitButton = new GameButton("Exit", V_WIDTH / 5f, V_HEIGHT / 1.5f - 120);

        resumeButton.setOnAction(resumeFunc);
        saveButton.setOnAction(saveFunc);
        saveAndExitButton.setOnAction(() -> {
            saveFunc.run();
            GameStateManager.pushState(GameStateManager.State.MENU);
        });

        settingsButton.setOnAction(openSettingsFunc);
        exitButton.setOnAction(() -> done = true);


        buttons =
                new HashSet<>(
                        Arrays.asList(
                                resumeButton,
                                saveButton,
                                saveAndExitButton,
                                settingsButton,
                                exitButton));

        for (GameButton button : buttons) played.put(button, false);

        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
    }

    @Override
    public void handleInput() { }

    @Override
    protected void updateCurrentBlock(GameButton button) { }
}
