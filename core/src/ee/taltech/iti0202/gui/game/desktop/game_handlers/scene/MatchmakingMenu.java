package ee.taltech.iti0202.gui.game.desktop.game_handlers.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.input.MyInput;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.components.GameButton;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.V_WIDTH;

public class MatchmakingMenu extends Scene {

    private block currBlock;
    private Runnable backFunc;

    private GameButton backButton;
    private GameButton startButton;
    private HashMap<GameButton, block> buttonType;

    public MatchmakingMenu(OrthographicCamera cam, Runnable backFunc) {
        super(cam);

        this.backFunc = backFunc;

        backButton = new GameButton("Back", V_WIDTH / 6f, V_HEIGHT / 1.2f - 40);
        startButton = new GameButton("Start", V_WIDTH * 5 / 6f, V_HEIGHT / 1.2f - 40);

        buttons = new HashSet<>(Arrays.asList(backButton, startButton));

        buttonType =
                new HashMap<GameButton, block>() {
                    {
                        put(backButton, block.BACK);
                        put(startButton, block.START);
                    }
                };

        for (GameButton button : buttons) played.put(button, false);

        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
    }

    @Override
    public void handleInput() {
        if (MyInput.isMouseClicked(Game.settings.SHOOT) && currBlock != null) {
            switch (currBlock) {
                case START:
                    playSoundOnce("sounds/menu_click.wav", 0.5f);
                    // TODO: Start game
                    break;
                case BACK:
                    playSoundOnce("sounds/negative_2.wav", 0.5f);
                    backFunc.run();
                    break;
            }
        }
    }

    @Override
    protected void updateCurrentBlock(GameButton btn) {
        currBlock = buttonType.get(btn);
    }

    private enum block {
        START,
        BACK
    }
}
