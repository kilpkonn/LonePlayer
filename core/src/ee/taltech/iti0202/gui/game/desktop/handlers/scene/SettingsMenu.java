package ee.taltech.iti0202.gui.game.desktop.handlers.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.components.GameButton;
import ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.desktop.settings.Settings;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_WIDTH;

public class SettingsMenu extends Scene {

    public enum SettingBlock {
        MOVE_LEFT,
        MOVE_RIGHT,
        JUMP,
        ESC,
        TOGGLE_DIMENSION,
        ENABLE_VSYNC,
        SHOW_FPS,
        ENABLE_DEV_MAPS,
        DEFAULT
    }

    private Game game;

    private GameButton exitButton;
    private GameButton saveButton;
    private GameButton loadButton;
    private GameButton restoreButton;

    private GameButton moveLeftDisplay;
    private GameButton moveRightDisplay;
    private GameButton jumpDisplay;
    private GameButton escDisplay;
    private GameButton changeDimensionDisplay;

    private GameButton enableVSyncDisplay;
    private GameButton showFpsDisplay;
    private GameButton enableDevMapsDisplay;

    private HashMap<GameButton, SettingBlock> settingsButtons = new HashMap<>();
    private HashMap<GameButton, GameButton> keyBindButtons = new HashMap<>();

    private GameButton btnWaitingInput;


    public SettingsMenu(OrthographicCamera cam, Game game) {
        super("", "", cam);
        this.game = game;
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

        GameButton enableVSyncButton = new GameButton("Enable VSync", V_WIDTH / 3f, V_HEIGHT / 3f + 400);
        GameButton showFpsButton = new GameButton("Show FPS", V_WIDTH / 3f, V_HEIGHT / 3f + 360);
        GameButton enableDevMapsButton = new GameButton("Enable DEV maps", V_WIDTH / 3f, V_HEIGHT / 3f + 320);

        GameButton moveLeftButton = new GameButton("Move left", V_WIDTH / 3f, V_HEIGHT / 3f + 200);
        GameButton moveRightButton = new GameButton("Move right", V_WIDTH / 3f, V_HEIGHT / 3f + 160);
        GameButton jumpButton = new GameButton("Jump", V_WIDTH / 3f, V_HEIGHT / 3f + 120);
        GameButton escButton = new GameButton("Pause", V_WIDTH / 3f, V_HEIGHT / 3f + 80);
        GameButton changeDimensionButton = new GameButton("Toggle dimension", V_WIDTH / 3f, V_HEIGHT / 3f + 40);

        settingsButtons.put(enableVSyncButton, SettingBlock.ENABLE_VSYNC);
        settingsButtons.put(showFpsButton, SettingBlock.SHOW_FPS);
        settingsButtons.put(enableDevMapsButton, SettingBlock.ENABLE_DEV_MAPS);

        settingsButtons.put(moveLeftButton, SettingBlock.MOVE_LEFT);
        settingsButtons.put(moveRightButton, SettingBlock.MOVE_RIGHT);
        settingsButtons.put(jumpButton, SettingBlock.JUMP);
        settingsButtons.put(escButton, SettingBlock.ESC);
        settingsButtons.put(changeDimensionButton, SettingBlock.TOGGLE_DIMENSION);

        enableVSyncDisplay = new GameButton(Game.settings.ENABLE_VSYNC ? "Yes" : "No", V_WIDTH / 1.5f, V_HEIGHT / 3f + 400);
        showFpsDisplay = new GameButton(Game.settings.SHOW_FPS ? "Yes" : "No", V_WIDTH / 1.5f, V_HEIGHT / 3f + 360);
        enableDevMapsDisplay = new GameButton(Game.settings.ENABLE_DEV_MAPS ? "Yes" : "No", V_WIDTH / 1.5f, V_HEIGHT / 3f + 320);

        moveLeftDisplay = new GameButton(Input.Keys.toString(Game.settings.MOVE_LEFT), V_WIDTH / 1.5f, V_HEIGHT / 3f + 200);
        moveRightDisplay = new GameButton(Input.Keys.toString(Game.settings.MOVE_RIGHT), V_WIDTH / 1.5f, V_HEIGHT / 3f + 160);
        jumpDisplay = new GameButton(Input.Keys.toString(Game.settings.JUMP), V_WIDTH / 1.5f, V_HEIGHT / 3f + 120);
        escDisplay = new GameButton(Input.Keys.toString(Game.settings.ESC), V_WIDTH / 1.5f, V_HEIGHT / 3f + 80);
        changeDimensionDisplay = new GameButton(Input.Keys.toString(Game.settings.CHANGE_DIMENTION), V_WIDTH / 1.5f, V_HEIGHT / 3f + 40);


        keyBindButtons.put(enableVSyncButton, enableVSyncDisplay);
        keyBindButtons.put(showFpsButton, showFpsDisplay);
        keyBindButtons.put(enableDevMapsButton, enableDevMapsDisplay);

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
            if (btn.hoverOver() && checkNonKeybindButtons(btn)) {
                btnWaitingInput = btn;
            }
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

    private boolean checkNonKeybindButtons(GameButton btn) {
        switch (settingsButtons.get(btn)) {
            case ENABLE_VSYNC:
                Game.settings.ENABLE_VSYNC = !Game.settings.ENABLE_VSYNC;
                keyBindButtons.get(btn).setText(Game.settings.ENABLE_VSYNC ? "Yes" : "No");
                Gdx.graphics.setVSync(Game.settings.ENABLE_VSYNC); //TODO: Produces error while in-game, works in menu
                break;
            case SHOW_FPS:
                Game.settings.SHOW_FPS = !Game.settings.SHOW_FPS;
                keyBindButtons.get(btn).setText(Game.settings.SHOW_FPS ? "Yes" : "No");
                break;
            case ENABLE_DEV_MAPS:
                Game.settings.ENABLE_DEV_MAPS = !Game.settings.ENABLE_DEV_MAPS;
                keyBindButtons.get(btn).setText(Game.settings.ENABLE_DEV_MAPS ? "Yes" : "No");
                break;
                default:
                    return true;
        }
        return false;
    }

    private void updateKeyBindsDisplayed(GameButton btn, int key) {
        keyBindButtons.get(btn).setText(Input.Keys.toString(key));
    }
}
