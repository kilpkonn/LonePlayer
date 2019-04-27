package ee.taltech.iti0202.gui.game.desktop.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.handlers.gdx.GameStateManager;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PATH;


public abstract class GameState {

    //protected GameStateManager gsm;
    protected Game game;
    protected SpriteBatch sb;
    protected OrthographicCamera cam; // follows the player
    protected OrthographicCamera hudCam; // stationary hud

    protected  GameState(){
        game = GameStateManager.game();
        sb = game.getSpriteBatch();
        cam = game.getCamera();
        hudCam = game.getHUDCamera();
    }

    void playSoundOnce(String source) {
        playSoundOnce(source, 1.0f);
    }

    void playSoundOnce(String source, float db) {
        try {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal(PATH + source));
            sound.play(db);
        } catch (Exception e) {
            System.out.println(e.getCause().getCause().toString());
        }
    }

    public abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render();
    public abstract void dispose();
}
