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


public class EndMenu extends Scene{

    // Active
    private GameButton exitButtonActive;
    private GameButton settingsButtonActive;
    private GameButton nextButtonActive;

    public EndMenu(int act, int map, OrthographicCamera cam) {
        super(act, map, cam);
        pauseState = B2DVars.pauseState.RUN; //TODO: change?

        hudCam.update();

        Texture backLayer = Game.res.getTexture("backLayer");

        nextButtonActive = new GameButton(new TextureRegion(Game.res.getTexture("buttons"), 0, 352, V_WIDTH, 32), V_WIDTH / 2f, V_HEIGHT / 1.5f - 40);
        GameButton nextButtonInactive = new GameButton(new TextureRegion(Game.res.getTexture("buttons"), 0, 320, V_WIDTH, 32), V_WIDTH / 2f, V_HEIGHT / 1.5f - 40);
        settingsButtonActive = new GameButton(new TextureRegion(Game.res.getTexture("buttons"), 0, 160, V_WIDTH, 32), V_WIDTH / 2f, V_HEIGHT / 1.5f - 80);
        GameButton settinsButtonInactive = new GameButton(new TextureRegion(Game.res.getTexture("buttons"), 0, 128, V_WIDTH, 32), V_WIDTH / 2f, V_HEIGHT / 1.5f - 80);
        exitButtonActive = new GameButton(new TextureRegion(Game.res.getTexture("buttons"), 0, 224, V_WIDTH, 32), V_WIDTH / 2f, V_HEIGHT / 1.5f - 120);
        GameButton exitButtonInactive = new GameButton(new TextureRegion(Game.res.getTexture("buttons"), 0, 192, V_WIDTH, 32), V_WIDTH / 2f, V_HEIGHT / 1.5f - 120);

        buttons = new HashSet<>(Arrays.asList(Arrays.asList(nextButtonActive, nextButtonInactive), Arrays.asList(settingsButtonActive, settinsButtonInactive), Arrays.asList(exitButtonActive, exitButtonInactive)));

        buttonType = new HashMap<GameButton, block>() {{
            put(nextButtonActive, block.NEXT);
            put(settingsButtonActive, block.SETTINGS);
            put(exitButtonActive, block.EXIT);
        }};
        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
    }
}
