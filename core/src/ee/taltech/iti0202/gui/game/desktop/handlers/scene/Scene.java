package ee.taltech.iti0202.gui.game.desktop.handlers.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.HashMap;
import java.util.HashSet;

import ee.taltech.iti0202.gui.game.desktop.handlers.scene.components.GameButton;
import ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars;

public abstract class Scene {
    protected Stage stage;
    protected B2DVars.pauseState pauseState;
    protected OrthographicCamera hudCam;

    protected Vector2 mouseInWorld2D;
    protected HashSet<GameButton> buttons;
    protected String act, map;
    //protected HashMap<GameButton, block> buttonType;

    public Scene(OrthographicCamera cam) {
        this("", "", cam);
    }

    public Scene(String act, String map, OrthographicCamera cam){
        this.hudCam = cam;
        this.act = act;
        this.map = map;
        stage = new Stage(new ScreenViewport());
        mouseInWorld2D = new Vector2();
    }

    /*public enum block {
        NEWGAME,
        RESUME,
        SETTINGS,
        SAVE,
        SAVEANDEXIT,
        EXIT,
        NEXT,
        ACT,
        MAP,
        LOAD,
        DEFAULT
    }*/

    public abstract void handleInput();

    public void update(float dt) {
        mouseInWorld2D.x = Gdx.input.getX();
        mouseInWorld2D.y = Gdx.input.getY();

        for (GameButton button : buttons)
            button.update(mouseInWorld2D);
    }

    public void render(SpriteBatch sb) {

        hudCam.update();
        sb.setProjectionMatrix(hudCam.combined);
        stage.act();
        stage.draw();

        for (GameButton button : buttons) {
            if (button.hoverOver()) {
                updateCurrentBlock(button);
            }
            button.render(sb);
        }
    }

    protected abstract void updateCurrentBlock(GameButton btn);

    /*public block getCur_block() {
        return cur_block;
    }*/
}
