package ee.taltech.iti0202.gui.game.desktop.handlers.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_WIDTH;

public class SettingsMenu extends Scene {
    // Active
    private GameButton exitButtonActive;
    private GameButton saveButtonActive;
    private GameButton restoreButtonActive;


    public SettingsMenu(int act, int map, OrthographicCamera cam) {
        super(act, map, cam);

        pauseState = B2DVars.pauseState.STOPPED;

        hudCam.update();

        Texture backLayer = Game.res.getTexture("backLayer");

        restoreButtonActive = new GameButton("Restore", V_WIDTH / 2f, V_HEIGHT / 1.5f - 40);
        saveButtonActive = new GameButton("Save", V_WIDTH / 2f, V_HEIGHT / 1.5f - 80);
        exitButtonActive = new GameButton("Exit", V_WIDTH / 2f, V_HEIGHT / 1.5f - 120);

        buttons = new HashSet<>(Arrays.asList(restoreButtonActive, saveButtonActive, exitButtonActive));

        buttonType = new HashMap<GameButton, block>() {{
            put(restoreButtonActive, block.NEXT);
            put(saveButtonActive, block.SETTINGS);
            put(exitButtonActive, block.EXIT);
        }};
        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
    }
}
