package ee.taltech.iti0202.gui.game.desktop.handlers.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.awt.Button;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.desktop.settings.Settings;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_WIDTH;

public class SettingsMenu extends Scene {

    public enum SettingBlock {
        MOVE_LEFT, MOVE_RIGHT, JUMP, ESC, DEFAULT
    }

    // Active
    private GameButton exitButtonActive;
    private GameButton saveButtonActive;
    private GameButton loadButton;
    private GameButton restoreButtonActive;

    private HashMap<GameButton, SettingBlock> settingsButtons = new HashMap<>();

    private GameButton btnWaitingInput;


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

        GameButton moveLeftButton = new GameButton("Move left", V_WIDTH / 2f, V_HEIGHT / 2f - 200);
        GameButton moveRightButton = new GameButton("Move right", V_WIDTH / 2f, V_HEIGHT / 2f - 160);

        settingsButtons.put(moveLeftButton, SettingBlock.MOVE_LEFT);
        settingsButtons.put(moveRightButton, SettingBlock.MOVE_RIGHT);


        for (GameButton btn : settingsButtons.keySet()) buttonType.put(btn, block.SETTINGS);
        buttons.addAll(settingsButtons.keySet());

        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
    }

    public void handleSettingsButtonClick() {
        for (GameButton btn: settingsButtons.keySet()) {
            if (btn.hoverOver()) btnWaitingInput = btn;
        }
    }

    public void handleKey(int key) {
        if (btnWaitingInput == null || key <= -1) return;
        switch (settingsButtons.get(btnWaitingInput)) {
            case MOVE_LEFT:
                Game.settings.MOVE_LEFT = key;
            case MOVE_RIGHT:
                Game.settings.MOVE_RIGHT = key;
        }
    }
}
