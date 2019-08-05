package ee.taltech.iti0202.gui.game.desktop.game_handlers.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.Arrays;
import java.util.HashSet;

import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.GameStateManager;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.components.GameButton;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.V_WIDTH;

public class MultiplayerPauseMenu extends Scene {

    private Runnable resumeFunc;
    private Runnable openSettingsFunc;
    private Runnable exitFunc;
    private GameButton exitButton;
    private GameButton settingsButton;
    private GameButton resumeButton;

    public MultiplayerPauseMenu(
            String act,
            String map,
            OrthographicCamera cam,
            Runnable resumeFunc,
            Runnable openSettingsFunc,
            Runnable exitFunc) {
        super(act, map, cam);
        this.resumeFunc = resumeFunc;
        this.openSettingsFunc = openSettingsFunc;
        this.exitFunc = exitFunc;

        hudCam.update();

        resumeButton = new GameButton("Resume", V_WIDTH / 5f, V_HEIGHT / 1.5f);
        settingsButton = new GameButton("Settings", V_WIDTH / 5f, V_HEIGHT / 1.5f - 40);
        exitButton = new GameButton("Exit", V_WIDTH / 5f, V_HEIGHT / 1.5f - 80);

        resumeButton.setOnAction(resumeFunc);

        settingsButton.setOnAction(openSettingsFunc);
        exitButton.setOnAction(exitFunc::run);


        buttons =
                new HashSet<>(
                        Arrays.asList(
                                resumeButton,
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
