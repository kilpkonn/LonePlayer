package ee.taltech.iti0202.gui.game.desktop.handlers.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars;

public abstract class Scene {

    protected OrthographicCamera hudCam;

    Scene(int act, int map, OrthographicCamera cam){
        this.hudCam = cam;
    }

    public enum block {
        RESUME,
        SETTINGS,
        SAVE,
        SAVEANDEXIT,
        EXIT,
        DEFAULT
    }

    public abstract block getCur_block();

    public abstract void setGameState(B2DVars.pauseState s);

    public abstract B2DVars.pauseState getPauseState();

    public abstract void update(float dt);

    public abstract void render(SpriteBatch sb);
}
