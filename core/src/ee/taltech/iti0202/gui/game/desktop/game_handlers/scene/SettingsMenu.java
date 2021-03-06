package ee.taltech.iti0202.gui.game.desktop.game_handlers.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.input.MyInput;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.components.GameButton;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.V_WIDTH;

public class SettingsMenu extends Scene {

    private float settingsXLocation = V_WIDTH / 2f;
    private float displayXLocation = V_WIDTH / 1.3f;
    private Runnable backFunc;
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

    public SettingsMenu(OrthographicCamera cam, Runnable backFunc) {
        super("", "", cam);
        this.backFunc = backFunc;

        hudCam.update();

        // Texture backLayer = Game.res.getTexture("backLayer");

        restoreButton = new GameButton("Restore", V_WIDTH / 6f, V_HEIGHT / 2f - 40);
        saveButton = new GameButton("Save", V_WIDTH / 6f, V_HEIGHT / 2f - 80);
        loadButton = new GameButton("Load", V_WIDTH / 6f, V_HEIGHT / 2f - 120);
        exitButton = new GameButton("Back", V_WIDTH / 6f, V_HEIGHT / 2f - 160);

        exitButton.setOnAction(backFunc);
        saveButton.setOnAction(() -> Game.settings.save(B2DVars.PATH + "settings/settings.json"));
        restoreButton.setOnAction(() -> {
            Game.settings = Game.settings.loadDefault();
            updateAllBindsDisplayed();
        });
        loadButton.setOnAction(() -> {
            Game.settings = Game.settings.load(B2DVars.PATH + "settings/settings.json");
            updateAllBindsDisplayed();
        });

        buttons = new HashSet<>(Arrays.asList(restoreButton, saveButton, exitButton, loadButton));

        GameButton enableVSyncButton =
                new GameButton("Enable VSync", settingsXLocation, V_HEIGHT / 3f + 400);
        GameButton showFpsButton =
                new GameButton("Show FPS", settingsXLocation, V_HEIGHT / 3f + 360);
        GameButton enableDevMapsButton =
                new GameButton("Enable DEV maps", settingsXLocation, V_HEIGHT / 3f + 320);

        GameButton moveLeftButton =
                new GameButton("Move left", settingsXLocation, V_HEIGHT / 3f + 200);
        GameButton moveRightButton =
                new GameButton("Move right", settingsXLocation, V_HEIGHT / 3f + 160);
        GameButton jumpButton = new GameButton("Jump", settingsXLocation, V_HEIGHT / 3f + 120);
        GameButton escButton = new GameButton("Pause", settingsXLocation, V_HEIGHT / 3f + 80);
        GameButton changeDimensionButton =
                new GameButton("Toggle dimension", settingsXLocation, V_HEIGHT / 3f + 40);

        settingsButtons.put(enableVSyncButton, SettingBlock.ENABLE_VSYNC);
        settingsButtons.put(showFpsButton, SettingBlock.SHOW_FPS);
        settingsButtons.put(enableDevMapsButton, SettingBlock.ENABLE_DEV_MAPS);

        settingsButtons.put(moveLeftButton, SettingBlock.MOVE_LEFT);
        settingsButtons.put(moveRightButton, SettingBlock.MOVE_RIGHT);
        settingsButtons.put(jumpButton, SettingBlock.JUMP);
        settingsButtons.put(escButton, SettingBlock.ESC);
        settingsButtons.put(changeDimensionButton, SettingBlock.TOGGLE_DIMENSION);

        enableVSyncDisplay =
                new GameButton(
                        Game.settings.ENABLE_VSYNC ? "Yes" : "No",
                        displayXLocation,
                        V_HEIGHT / 3f + 400);
        showFpsDisplay =
                new GameButton(
                        Game.settings.SHOW_FPS ? "Yes" : "No",
                        displayXLocation,
                        V_HEIGHT / 3f + 360);
        enableDevMapsDisplay =
                new GameButton(
                        Game.settings.ENABLE_DEV_MAPS ? "Yes" : "No",
                        displayXLocation,
                        V_HEIGHT / 3f + 320);

        moveLeftDisplay =
                new GameButton(
                        Input.Keys.toString(Game.settings.MOVE_LEFT),
                        displayXLocation,
                        V_HEIGHT / 3f + 200);
        moveRightDisplay =
                new GameButton(
                        Input.Keys.toString(Game.settings.MOVE_RIGHT),
                        displayXLocation,
                        V_HEIGHT / 3f + 160);
        jumpDisplay =
                new GameButton(
                        Input.Keys.toString(Game.settings.JUMP),
                        displayXLocation,
                        V_HEIGHT / 3f + 120);
        escDisplay =
                new GameButton(
                        Input.Keys.toString(Game.settings.ESC),
                        displayXLocation,
                        V_HEIGHT / 3f + 80);
        changeDimensionDisplay =
                new GameButton(
                        Input.Keys.toString(Game.settings.CHANGE_DIMENSION),
                        displayXLocation,
                        V_HEIGHT / 3f + 40);

        keyBindButtons.put(enableVSyncButton, enableVSyncDisplay);
        keyBindButtons.put(showFpsButton, showFpsDisplay);
        keyBindButtons.put(enableDevMapsButton, enableDevMapsDisplay);

        keyBindButtons.put(moveLeftButton, moveLeftDisplay);
        keyBindButtons.put(moveRightButton, moveRightDisplay);
        keyBindButtons.put(jumpButton, jumpDisplay);
        keyBindButtons.put(escButton, escDisplay);
        keyBindButtons.put(changeDimensionButton, changeDimensionDisplay);

        for (GameButton btn : settingsButtons.keySet()) btn.setOnAction(this::handleSettingsButtonClick);
        buttons.addAll(settingsButtons.keySet());

        for (GameButton btn : keyBindButtons.values()) {
            btn.setAcceptHover(false);
        }
        buttons.addAll(keyBindButtons.values());

        for (GameButton button : buttons) played.put(button, false);

        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
    }

    @Override
    public void handleInput() {
        handleKey(MyInput.getKeyDown());
    }

    public void handleSettingsButtonClick() {
        for (Map.Entry<GameButton, GameButton> entry : keyBindButtons.entrySet()) {
            if ((entry.getKey().hoverOver() || entry.getValue().hoverOver())
                    && checkNonKeyBindButtons(entry.getKey())) {
                btnWaitingInput = entry.getKey();
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
                Game.settings.CHANGE_DIMENSION = key;
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
        changeDimensionDisplay.setText(Input.Keys.toString(Game.settings.CHANGE_DIMENSION));
        enableVSyncDisplay.setText(Game.settings.ENABLE_VSYNC ? "Yes" : "No");
        showFpsDisplay.setText(Game.settings.SHOW_FPS ? "Yes" : "No");
        enableDevMapsDisplay.setText(Game.settings.ENABLE_DEV_MAPS ? "Yes" : "No");
    }

    private boolean checkNonKeyBindButtons(GameButton btn) {
        switch (settingsButtons.get(btn)) {
            case ENABLE_VSYNC:
                Game.settings.ENABLE_VSYNC = !Game.settings.ENABLE_VSYNC;
                Gdx.graphics.setVSync(Game.settings.ENABLE_VSYNC);
                break;
            case SHOW_FPS:
                Game.settings.SHOW_FPS = !Game.settings.SHOW_FPS;
                break;
            case ENABLE_DEV_MAPS:
                Game.settings.ENABLE_DEV_MAPS = !Game.settings.ENABLE_DEV_MAPS;
                break;
            default:
                return true;
        }
        updateAllBindsDisplayed();
        return false;
    }

    private void updateKeyBindsDisplayed(GameButton btn, int key) {
        keyBindButtons.get(btn).setText(Input.Keys.toString(key));
    }

    @Override
    protected void updateCurrentBlock(GameButton button) { }

    @Override
    public void dispose() {
        super.dispose();
    }

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
}
