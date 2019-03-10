package ee.taltech.iti0202.gui.game;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars;
import ee.taltech.iti0202.gui.game.desktop.handlers.Content;
import ee.taltech.iti0202.gui.game.desktop.handlers.GameStateManager;
import ee.taltech.iti0202.gui.game.desktop.handlers.MyInput;
import ee.taltech.iti0202.gui.game.desktop.handlers.MyInputProcessor;
import ee.taltech.iti0202.gui.game.desktop.states.Play;

import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.MAIN_SCREENS;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.PATH;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.STEP;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.V_WIDTH;


public class Game extends ApplicationAdapter {
    public static final String TITLE = "Alone at Night";

    private float accum;

    private SpriteBatch sb;
    private OrthographicCamera cam;
    private OrthographicCamera hudCam;
    private Viewport viewport;

    private GameStateManager gsm;
    public static Content res;

    public void create() {

        switch(Gdx.app.getType()) {
            case Android:
                // android specific code
                B2DVars.SCALE = 2;
                B2DVars.PATH = "";
                B2DVars.V_WIDTH = Gdx.graphics.getWidth() / 2;
                B2DVars.V_HEIGHT = Gdx.graphics.getHeight() / 2;
                break;
            case Desktop:
                // desktop specific code
                B2DVars.PATH = "android/assets/";

                break;
            case HeadlessDesktop:
                break;
            case Applet:
                break;
            case WebGL:
                break;
            case iOS:
                break;
        }

        System.out.println(MAIN_SCREENS[3]);
        System.out.println(PATH);
        Gdx.input.setInputProcessor(new MyInputProcessor());

        // load textures
        res = new Content();

        res.loadTexture(PATH + "images/menu.png", "buttons");
        res.loadTexture(PATH + "images/backLayer.png", "backLayer");
        res.loadTexture(PATH + "images/BackgroundStars/Stars-Big_1_2_PC.png", "starBackground");

        res.loadTexture(PATH + "images/Player/Player.png", "Player");
        res.loadTexture(PATH + "maps/Flag.png", "Checkpoint");

        sb = new SpriteBatch();

        cam = new OrthographicCamera();
        viewport = new FitViewport(V_WIDTH, V_HEIGHT, cam);

        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);

        hudCam = new OrthographicCamera();
        hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);

        gsm = new GameStateManager(this);
    }

    public SpriteBatch getSpriteBatch() {
        return sb;
    }

    public OrthographicCamera getCamera() {
        return cam;
    }

    public OrthographicCamera getHUDCamera() {
        return hudCam;
    }

    @Override
    public void render() {
        accum += Gdx.graphics.getDeltaTime();
        while(accum >= STEP) {
            accum -= STEP;
            gsm.update(STEP);
            gsm.render();
            MyInput.update();
            //addSystem.out.println(Gdx.graphics.getFramesPerSecond());
        }
    }

    @Override
    public void dispose() {
    }

    public void resize(int w, int h) {
        viewport.update(w, h);
    }

    public void pause() {
    }

    public void resume() {
    }
}
