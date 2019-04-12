package ee.taltech.iti0202.gui.game.desktop.handlers.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.handlers.gdx.GameStateManager;
import ee.taltech.iti0202.gui.game.desktop.handlers.gdx.input.MyInput;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.components.GameButton;
import ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_WIDTH;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.pauseState.SETTINGS;


public class EndMenu extends Scene{

    private enum block {
        NEWGAME, NEXT, EXIT, SETTINGS
    }

    private block currBlock;
    private HashMap<GameButton, block> buttonType;
    private Runnable openSettingsFunc;

    private GameButton exitButton;
    private GameButton settingsButton;
    private GameButton nextButton;
    private GameButton playAgainButton;

    public EndMenu(String act, String map, OrthographicCamera cam, Runnable openSettingsFunc) {
        super(act, map, cam);
        this.openSettingsFunc = openSettingsFunc;

        hudCam.update();

        nextButton = new GameButton("Next level", V_WIDTH / 6f, V_HEIGHT / 1.5f - 40);
        playAgainButton = new GameButton("Play again", V_WIDTH / 6f, V_HEIGHT / 1.5f - 40);
        settingsButton = new GameButton("Settings", V_WIDTH / 6f, V_HEIGHT / 1.5f - 120);
        exitButton = new GameButton("Exit", V_WIDTH / 6f, V_HEIGHT / 1.5f - 160);

        buttons = new HashSet<>(Arrays.asList(nextButton, playAgainButton, settingsButton, exitButton));

        buttonType = new HashMap<GameButton, block>() {{
            put(nextButton, block.NEXT);
            put(playAgainButton, block.NEWGAME);
            put(settingsButton, block.SETTINGS);
            put(exitButton, block.EXIT);
        }};
        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
    }

    @Override
    public void handleInput() {
        if (MyInput.isMouseClicked(Game.settings.SHOOT)) {
            switch (currBlock) {
                case NEXT:
                    //TODO: Select next map
                    break;
                case NEWGAME:
                    GameStateManager.pushState(GameStateManager.State.PLAY, act, map);
                    break;
                case SETTINGS:
                    openSettingsFunc.run();
                    break;
                case EXIT:
                    GameStateManager.pushState(GameStateManager.State.MENU);
                    break;
            }
        }
    }

    @Override
    protected void updateCurrentBlock(GameButton button) {
        currBlock = buttonType.get(button);
    }
}
