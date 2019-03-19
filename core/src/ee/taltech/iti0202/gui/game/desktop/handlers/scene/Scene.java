package ee.taltech.iti0202.gui.game.desktop.handlers.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars;

public abstract class Scene {

    protected block cur_block = block.DEFAULT;
    protected Stage stage;
    protected B2DVars.pauseState pauseState;
    protected OrthographicCamera hudCam;

    protected Vector2 mouseInWorld2D;
    protected HashSet<List<GameButton>> buttons;
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
        NEXT,
        DEFAULT
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

        for (List<GameButton> button : buttons)
            button.get(1).update(mouseInWorld2D);
    }

    public void render(SpriteBatch sb) {

        hudCam.update();
        sb.setProjectionMatrix(hudCam.combined);
        //sb.begin();
        //Draw pause screen
        //sb.draw(backLayer, hudCam.position.x - (tw / 2f), hudCam.position.y - (th / 3f));
        //sb.end();
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

    public block getCur_block() {
        return cur_block;
    }
}
