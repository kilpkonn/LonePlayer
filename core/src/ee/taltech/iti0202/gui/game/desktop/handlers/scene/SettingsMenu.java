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
    private GameButton loadButton;
    private GameButton restoreButtonActive;


    public SettingsMenu(OrthographicCamera cam) {
        super(0, 0, cam);

        pauseState = B2DVars.pauseState.STOPPED;

        hudCam.update();

        //Texture backLayer = Game.res.getTexture("backLayer");

        restoreButtonActive = new GameButton("Restore", V_WIDTH / 6f, V_HEIGHT / 2f - 40);
        saveButtonActive = new GameButton("Save", V_WIDTH / 6f, V_HEIGHT / 2f - 80);
        loadButton = new GameButton("Load", V_WIDTH / 6f, V_HEIGHT / 2f - 120);
        exitButtonActive = new GameButton("Exit", V_WIDTH / 6f, V_HEIGHT / 2f - 160);

        buttons = new HashSet<>(Arrays.asList(restoreButtonActive, saveButtonActive, exitButtonActive, loadButton));

        buttonType = new HashMap<GameButton, block>() {{
            put(restoreButtonActive, block.NEXT);
            put(saveButtonActive, block.SAVE);
            put(exitButtonActive, block.EXIT);
            put(loadButton, block.LOAD);
        }};
        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
    }
}
