package ee.taltech.iti0202.gui.game.desktop.handlers.scene;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.components.GameButton;
import ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_WIDTH;

public class SettingsMenu extends Scene {

    public enum SettingBlock {
        MOVE_LEFT, MOVE_RIGHT, JUMP, ESC, TOGGLE_DIMENSION, DEFAULT
    }

    // Active
    private GameButton exitButton;
    private GameButton saveButton;
    private GameButton loadButton;
    private GameButton restoreButton;

    private GameButton moveLeftDisplay;
    private GameButton moveRightDisplay;
    private GameButton jumpDisplay;
    private GameButton escDisplay;
    private GameButton changeDimensionDisplay;

    private HashMap<GameButton, SettingBlock> settingsButtons = new HashMap<>();
    private HashMap<GameButton, GameButton> keyBindButtons = new HashMap<>();

    private GameButton btnWaitingInput;


    public SettingsMenu(OrthographicCamera cam) {
        super("", "", cam);

        pauseState = B2DVars.pauseState.STOPPED;

        hudCam.update();

        //Texture backLayer = Game.res.getTexture("backLayer");

        restoreButton = new GameButton("Restore", V_WIDTH / 6f, V_HEIGHT / 2f - 40);
        saveButton = new GameButton("Save", V_WIDTH / 6f, V_HEIGHT / 2f - 80);
        loadButton = new GameButton("Load", V_WIDTH / 6f, V_HEIGHT / 2f - 120);
        exitButton = new GameButton("Back", V_WIDTH / 6f, V_HEIGHT / 2f - 160);

        buttons = new HashSet<>(Arrays.asList(restoreButton, saveButton, exitButton, loadButton));

        buttonType = new HashMap<GameButton, block>() {{
            put(restoreButton, block.NEXT);
            put(saveButton, block.SAVE);
            put(exitButton, block.EXIT);
            put(loadButton, block.LOAD);
        }};

        GameButton moveLeftButton = new GameButton("Move left", V_WIDTH / 3f, V_HEIGHT / 2f + 200);
        GameButton moveRightButton = new GameButton("Move right", V_WIDTH / 3f, V_HEIGHT / 2f + 160);
        GameButton jumpButton = new GameButton("Jump", V_WIDTH / 3f, V_HEIGHT / 2f + 120);
        GameButton escButton = new GameButton("Pause", V_WIDTH / 3f, V_HEIGHT / 2f + 80);
        GameButton changeDimensionButton = new GameButton("Toggle dimension", V_WIDTH / 3f, V_HEIGHT / 2f + 40);

        settingsButtons.put(moveLeftButton, SettingBlock.MOVE_LEFT);
        settingsButtons.put(moveRightButton, SettingBlock.MOVE_RIGHT);
        settingsButtons.put(jumpButton, SettingBlock.JUMP);
        settingsButtons.put(escButton, SettingBlock.ESC);
        settingsButtons.put(changeDimensionButton, SettingBlock.TOGGLE_DIMENSION);

        moveLeftDisplay = new GameButton(Input.Keys.toString(Game.settings.MOVE_LEFT), V_WIDTH / 1.5f, V_HEIGHT / 2f + 200);
        moveRightDisplay = new GameButton(Input.Keys.toString(Game.settings.MOVE_RIGHT), V_WIDTH / 1.5f, V_HEIGHT / 2f + 160);
        jumpDisplay = new GameButton(Input.Keys.toString(Game.settings.JUMP), V_WIDTH / 1.5f, V_HEIGHT / 2f + 120);
        escDisplay = new GameButton(Input.Keys.toString(Game.settings.ESC), V_WIDTH / 1.5f, V_HEIGHT / 2f + 80);
        changeDimensionDisplay = new GameButton(Input.Keys.toString(Game.settings.CHANGE_DIMENTION), V_WIDTH / 1.5f, V_HEIGHT / 2f + 40);

        keyBindButtons.put(moveLeftButton, moveLeftDisplay);
        keyBindButtons.put(moveRightButton, moveRightDisplay);
        keyBindButtons.put(jumpButton, jumpDisplay);
        keyBindButtons.put(escButton, escDisplay);
        keyBindButtons.put(changeDimensionButton, changeDimensionDisplay);


        for (GameButton btn : settingsButtons.keySet()) buttonType.put(btn, block.SETTINGS);
        buttons.addAll(settingsButtons.keySet());

        for (GameButton btn : keyBindButtons.values()) {
            buttonType.put(btn, block.DEFAULT);
            btn.setAcceptHover(false);
        }
        buttons.addAll(keyBindButtons.values());

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
                break;
            case MOVE_RIGHT:
                Game.settings.MOVE_RIGHT = key;
                break;
            case JUMP:
                Game.settings.JUMP = key;
                break;
            case TOGGLE_DIMENSION:
                Game.settings.CHANGE_DIMENTION = key;
                break;
            case ESC:
                Game.settings.ESC = key;
                break;
        }
        updateKeyBindsDisplayed(btnWaitingInput, key);
        btnWaitingInput = null;
    }

    public void updateAllBindsDisplayed() {
        moveLeftDisplay.setText(Input.Keys.toString(Game.settings.MOVE_LEFT));
        moveRightDisplay.setText(Input.Keys.toString(Game.settings.MOVE_RIGHT));
        jumpDisplay.setText(Input.Keys.toString(Game.settings.JUMP));
        escDisplay.setText(Input.Keys.toString(Game.settings.ESC));
        changeDimensionDisplay.setText(Input.Keys.toString(Game.settings.CHANGE_DIMENTION));
    }

    private void updateKeyBindsDisplayed(GameButton btn, int key) {
        keyBindButtons.get(btn).setText(Input.Keys.toString(key));
    }
}
