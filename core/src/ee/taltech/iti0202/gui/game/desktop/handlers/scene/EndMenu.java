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


public class EndMenu extends Scene{

    private enum block {
        NEWGAME, NEXT, EXIT, SETTINGS
    }

    private OrthographicCamera cam;

    private block currBlock;
    private HashMap<GameButton, block> buttonType;
    private Runnable openSettingsFunc;

    private GameButton exitButton;
    private GameButton nextButton;
    private GameButton playAgainButton;

    public EndMenu(String act, String map, OrthographicCamera cam, Runnable openSettingsFunc, OrthographicCamera Ccam) {
        super(act, map, cam);
        this.cam = Ccam;
        this.openSettingsFunc = openSettingsFunc;

        hudCam.update();

        nextButton = new GameButton("Next level", V_WIDTH / 6f, V_HEIGHT / 1.5f - 40);
        playAgainButton = new GameButton("Play again", V_WIDTH / 6f, V_HEIGHT / 1.5f - 80);
        exitButton = new GameButton("Exit", V_WIDTH / 6f, V_HEIGHT / 1.5f - 160);

        buttons = new HashSet<>(Arrays.asList(nextButton, playAgainButton, exitButton));

        buttonType = new HashMap<GameButton, block>() {{
            put(nextButton, block.NEXT);
            put(playAgainButton, block.NEWGAME);
            put(exitButton, block.EXIT);
        }};
        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
    }

    @Override
    public void handleInput() {
        if (MyInput.isMouseClicked(Game.settings.SHOOT)) {
            cam.zoom = 1f;
            switch (currBlock) {
                case NEXT:
                    //TODO push next state
                    GameStateManager.pushState(GameStateManager.State.PLAY, act, map);
                    break;
                case NEWGAME:
                    GameStateManager.pushState(GameStateManager.State.PLAY, act, map);
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
