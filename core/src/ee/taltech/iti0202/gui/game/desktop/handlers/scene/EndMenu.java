package ee.taltech.iti0202.gui.game.desktop.handlers.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_WIDTH;


public class EndMenu extends Scene{

    // Active
    private GameButton exitButtonActive;
    private GameButton settingsButtonActive;
    private GameButton nextButtonActive;

    public EndMenu(int act, String map, OrthographicCamera cam) {
        super(act, map, cam);

        hudCam.update();

        nextButtonActive = new GameButton("Next level", V_WIDTH / 6f, V_HEIGHT / 1.5f - 40);
        settingsButtonActive = new GameButton("Settings", V_WIDTH / 6f, V_HEIGHT / 1.5f - 80);
        exitButtonActive = new GameButton("Exit", V_WIDTH / 6f, V_HEIGHT / 1.5f - 120);

        buttons = new HashSet<>(Arrays.asList(nextButtonActive, settingsButtonActive, exitButtonActive));

        buttonType = new HashMap<GameButton, block>() {{
            put(nextButtonActive, block.NEXT);
            put(settingsButtonActive, block.SETTINGS);
            put(exitButtonActive, block.EXIT);
        }};
        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
    }
}
