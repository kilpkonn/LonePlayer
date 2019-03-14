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
import java.util.List;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_WIDTH;


public class PauseMenu {

    public block getCur_block() {
        return cur_block;
    }

    public enum block {
        RESUME,
        SETTINGS,
        SAVE,
        SAVEANDEXIT,
        EXIT,
        DEFAULT
    }

    private OrthographicCamera hudCam;
    private B2DVars.pauseState pauseState;
    private Stage stage;

    // Active
    private GameButton exitButtonActive;
    private GameButton settingsButtonActive;
    private GameButton resumeButtonActive;
    private GameButton saveButtonActive;
    private GameButton saveAndExitButtonActive;

    private Vector2 mouseInWorld2D;
    public List<List<GameButton>> buttons;
    private HashMap<GameButton, block> buttonType;
    private block cur_block = block.DEFAULT;

    public PauseMenu(int act, int map, OrthographicCamera cam) {

        this.pauseState = B2DVars.pauseState.RUN;
        this.hudCam = cam;
        stage = new Stage(new ScreenViewport());
        mouseInWorld2D = new Vector2();

        hudCam.update();

        Texture backLayer = Game.res.getTexture("backLayer");

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

        buttons = new ArrayList<>(Arrays.asList(Arrays.asList(resumeButtonActive, resumeButtonInactive), Arrays.asList(saveButtonActive, saveButtonInactive),
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

    public void setGameState(B2DVars.pauseState s) {
        this.pauseState = s;
    }

    public B2DVars.pauseState getPauseState() {
        return pauseState;
    }

    public void update(float dt) {
        mouseInWorld2D.x = Gdx.input.getX();
        mouseInWorld2D.y = Gdx.input.getY();

        for (List<GameButton> button : buttons) button.get(1).update(mouseInWorld2D);
    }

    public void render(SpriteBatch sb) {

        hudCam.update();
        sb.setProjectionMatrix(hudCam.combined);
        sb.begin();
        //Draw pause screen
        //sb.draw(backLayer, hudCam.position.x - (tw / 2f), hudCam.position.y - (th / 3f));
        sb.end();

        stage.act();
        stage.draw();

        for (List<GameButton> button : buttons) {
            if (button.get(1).hoverOver()) {
                cur_block = buttonType.get(button.get(0));
                button.get(0).render(sb);
            } else {
                button.get(1).render(sb);
            }
        }
    }
}
