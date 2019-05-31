package ee.taltech.iti0202.gui.game.desktop.game_handlers.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.input.MyInput;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.components.GameButton;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.V_WIDTH;

public class MainMenu extends Scene {

    private Runnable newGameFunc;
    private Runnable matchmakingFunc;
    private Runnable resumeGameFunc;
    private Runnable settingsFunc;
    private GameButton newGameButton;
    private GameButton matchmakingButton;
    private GameButton loadGameButton;
    private GameButton settingsButton;
    private GameButton exitButton;

    public MainMenu(
            OrthographicCamera cam,
            Runnable newGameFunc,
            Runnable matchmakingFunc,
            Runnable resumeGameFunc,
            Runnable settingsFunc) {
        super(cam);
        this.newGameFunc = newGameFunc;
        this.matchmakingFunc = matchmakingFunc;
        this.resumeGameFunc = resumeGameFunc;
        this.settingsFunc = settingsFunc;

        newGameButton = new GameButton("New Game", V_WIDTH / 5f, V_HEIGHT / 1.5f + 40);
        matchmakingButton = new GameButton("Matchmaking", V_WIDTH / 5f, V_HEIGHT / 1.5f);
        loadGameButton = new GameButton("Resume", V_WIDTH / 5f, V_HEIGHT / 1.5f - 40);
        settingsButton = new GameButton("Settings", V_WIDTH / 5f, V_HEIGHT / 1.5f - 80);
        exitButton = new GameButton("Exit", V_WIDTH / 5f, V_HEIGHT / 1.5f - 120);
        newGameButton.setOnAction(newGameFunc);
        matchmakingButton.setOnAction(matchmakingFunc);
        loadGameButton.setOnAction(resumeGameFunc);
        settingsButton.setOnAction(settingsFunc);
        exitButton.setOnAction(() -> {
            playSoundOnce("sounds/negative_2.wav", 0.5f);
            Gdx.app.exit();
        });

        buttons =
                new HashSet<>(
                        Arrays.asList(newGameButton,
                                loadGameButton,
                                settingsButton,
                                exitButton,
                                matchmakingButton));


        for (GameButton button : buttons) played.put(button, false);

        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
    }

    @Override
    public void handleInput() { }

    @Override
    protected void updateCurrentBlock(GameButton button) { }
}
