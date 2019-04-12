package ee.taltech.iti0202.gui.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ee.taltech.iti0202.gui.game.desktop.handlers.gdx.Content;
import ee.taltech.iti0202.gui.game.desktop.handlers.gdx.GameStateManager;
import ee.taltech.iti0202.gui.game.desktop.handlers.gdx.input.MyInput;
import ee.taltech.iti0202.gui.game.desktop.handlers.gdx.input.MyInputProcessor;
import ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.desktop.settings.Settings;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PATH;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_WIDTH;


public class Game extends ApplicationAdapter {
    public static final String TITLE = "Alone at Night";

    private float accum;

    private SpriteBatch sb;
    private OrthographicCamera cam;
    private OrthographicCamera hudCam;
    public static Settings settings;

    private GameStateManager gsm;
    public static Content res;

    public Game(Settings s) {
        settings = s;
    }

    public void create() {

        switch (Gdx.app.getType()) {
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

        Gdx.input.setInputProcessor(new MyInputProcessor());

        // load textures
        res = new Content();

        res.loadTexture(PATH + "images/menu.png", "buttons");
        res.loadTexture(PATH + "images/backLayer.png", "backLayer");
        res.loadTexture(PATH + "images/BackgroundStars/Stars-Big_1_2_PC.png", "starBackground");

        res.loadTexture(PATH + "images/bosses/magmawormbody.png", "magmawormbody");
        res.loadTexture(PATH + "images/bosses/magmawormhead.png", "magmawormhead");
        res.loadTexture(PATH + "images/Player/Player.png", "Player");
        res.loadTexture(PATH + "images/Player/Llama.png", "Llama");
        res.loadTexture(PATH + "maps/tilesets/images/Flag.png", "Checkpoint");
        res.loadTexture(PATH + "maps/background/rock.png", "rock");
        res.loadTexture(PATH + "maps/background/snow.png", "snow");
        res.loadTexture(PATH + "maps/background/grass.png", "grass");

        sb = new SpriteBatch();

        cam = new OrthographicCamera();

        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);

        hudCam = new OrthographicCamera();
        hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);

        gsm = new GameStateManager(this);

        Gdx.graphics.setVSync(settings.ENABLE_VSYNC);
        setForegroundFPS(settings.MAX_FPS);
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

        /*accum += Gdx.graphics.getDeltaTime();
        while (accum >= STEP) {
            accum -= STEP;
            gsm.update(STEP);
            gsm.render();
            MyInput.update(); // this is to define the end state of current iteration
        }*/
        float dt = Gdx.graphics.getDeltaTime();
        gsm.update(dt);
        gsm.render();
        MyInput.update();
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        System.gc();
    }

    @Override
    public void resize(int w, int h) {
        B2DVars.V_HEIGHT = h;
        B2DVars.V_WIDTH = w;
        System.out.println("resized");
       
    }

    public void pause() {
    }

    public void resume() {
    }

    public void setForegroundFPS(int value) {}
}
