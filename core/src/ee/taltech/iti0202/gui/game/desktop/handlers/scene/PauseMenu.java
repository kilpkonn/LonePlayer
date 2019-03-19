package ee.taltech.iti0202.gui.game.desktop.handlers.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

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

        resumeButtonActive = new GameButton(new TextureRegion(Game.res.getTexture("buttons"), 0, 416, V_WIDTH, 32), V_WIDTH / 2f, V_HEIGHT / 1.5f + 40);
        GameButton resumeButtonInactive = new GameButton(new TextureRegion(Game.res.getTexture("buttons"), 0, 384, V_WIDTH, 32), V_WIDTH / 2f, V_HEIGHT / 1.5f + 40);
        saveButtonActive = new GameButton(new TextureRegion(Game.res.getTexture("buttons"), 0, 288, V_WIDTH, 32), V_WIDTH / 2f, V_HEIGHT / 1.5f);
        GameButton saveButtonInactive = new GameButton(new TextureRegion(Game.res.getTexture("buttons"), 0, 256, V_WIDTH, 32), V_WIDTH / 2f, V_HEIGHT / 1.5f);
        saveAndExitButtonActive = new GameButton(new TextureRegion(Game.res.getTexture("buttons"), 0, 352, V_WIDTH, 32), V_WIDTH / 2f, V_HEIGHT / 1.5f - 40);
        GameButton saveAndExitButtonInactive = new GameButton(new TextureRegion(Game.res.getTexture("buttons"), 0, 320, V_WIDTH, 32), V_WIDTH / 2f, V_HEIGHT / 1.5f - 40);
        settingsButtonActive = new GameButton(new TextureRegion(Game.res.getTexture("buttons"), 0, 160, V_WIDTH, 32), V_WIDTH / 2f, V_HEIGHT / 1.5f - 80);
        GameButton settinsButtonInactive = new GameButton(new TextureRegion(Game.res.getTexture("buttons"), 0, 128, V_WIDTH, 32), V_WIDTH / 2f, V_HEIGHT / 1.5f - 80);
        exitButtonActive = new GameButton(new TextureRegion(Game.res.getTexture("buttons"), 0, 224, V_WIDTH, 32), V_WIDTH / 2f, V_HEIGHT / 1.5f - 120);
        GameButton exitButtonInactive = new GameButton(new TextureRegion(Game.res.getTexture("buttons"), 0, 192, V_WIDTH, 32), V_WIDTH / 2f, V_HEIGHT / 1.5f - 120);

        buttons = new HashSet<>(Arrays.asList(Arrays.asList(resumeButtonActive, resumeButtonInactive), Arrays.asList(saveButtonActive, saveButtonInactive),
                Arrays.asList(saveAndExitButtonActive, saveAndExitButtonInactive), Arrays.asList(settingsButtonActive, settinsButtonInactive), Arrays.asList(exitButtonActive, exitButtonInactive)));

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
