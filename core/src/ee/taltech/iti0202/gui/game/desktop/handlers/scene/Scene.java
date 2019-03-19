package ee.taltech.iti0202.gui.game.desktop.handlers.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.HashMap;
import java.util.List;

import ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars;

public abstract class Scene {

    protected block cur_block = block.DEFAULT;
    protected Stage stage;
    protected B2DVars.pauseState pauseState;
    protected OrthographicCamera hudCam;

    protected Vector2 mouseInWorld2D;
    protected List<List<GameButton>> buttons;
    protected HashMap<GameButton, block> buttonType;

    public Scene(int act, int map, OrthographicCamera cam){
        this.hudCam = cam;
        stage = new Stage(new ScreenViewport());
        mouseInWorld2D = new Vector2();
    }

    public enum block {
        RESUME,
        SETTINGS,
        SAVE,
        SAVEANDEXIT,
        EXIT,
        DEFAULT
    }


    public abstract void setGameState(B2DVars.pauseState s);

    public abstract B2DVars.pauseState getPauseState();

    public abstract void update(float dt);

    public abstract void render(SpriteBatch sb);

    public block getCur_block() {
        return cur_block;
    }
}
