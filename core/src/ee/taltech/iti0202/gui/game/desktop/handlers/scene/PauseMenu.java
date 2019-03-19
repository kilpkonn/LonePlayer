package ee.taltech.iti0202.gui.game.desktop.handlers.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_WIDTH;


public class PauseMenu extends Scene{
    // Active
    private GameButton exitButtonActive;
    private GameButton settingsButtonActive;
    private GameButton resumeButtonActive;
    private GameButton saveButtonActive;
    private GameButton saveAndExitButtonActive;

    public PauseMenu(int act, int map, OrthographicCamera cam) {

        super(act, map, cam);
        this.pauseState = B2DVars.pauseState.RUN;

        hudCam.update();

        //Texture backLayer = Game.res.getTexture("backLayer");

        resumeButtonActive = new GameButton("Resume", V_WIDTH / 5f, V_HEIGHT / 1.5f + 40);
        saveButtonActive = new GameButton("Save", V_WIDTH / 5f, V_HEIGHT / 1.5f);
        saveAndExitButtonActive = new GameButton("Save n' Exit", V_WIDTH / 5f, V_HEIGHT / 1.5f - 40);
        settingsButtonActive = new GameButton("Settings", V_WIDTH / 5f, V_HEIGHT / 1.5f - 80);
        exitButtonActive = new GameButton("Exit", V_WIDTH / 5f, V_HEIGHT / 1.5f - 120);

        buttons = new HashSet<>(Arrays.asList(resumeButtonActive, saveButtonActive, saveAndExitButtonActive, settingsButtonActive, exitButtonActive));

        buttonType = new HashMap<GameButton, block>() {{
            put(resumeButtonActive, block.RESUME);
            put(saveButtonActive, block.SAVE);
            put(saveAndExitButtonActive, block.SAVEANDEXIT);
            put(settingsButtonActive, block.SETTINGS);
            put(exitButtonActive, block.EXIT);
        }};
        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
    }
}
