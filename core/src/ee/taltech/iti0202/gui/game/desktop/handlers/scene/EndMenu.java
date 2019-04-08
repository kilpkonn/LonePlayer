package ee.taltech.iti0202.gui.game.desktop.handlers.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_WIDTH;


public class EndMenu extends Scene{

    // Active
    private GameButton exitButton;
    private GameButton settingsButton;
    private GameButton nextButton;
    private GameButton playAgainButton;

    public EndMenu(String act, String map, OrthographicCamera cam) {
        super(act, map, cam);

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
}
