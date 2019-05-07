package ee.taltech.iti0202.gui.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.Content;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.GameStateManager;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.input.MyInput;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.input.MyInputProcessor;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.desktop.settings.Settings;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.PATH;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.V_WIDTH;


public class Game extends ApplicationAdapter {

    public static final String TITLE = "Alone at Night";
    private SpriteBatch sb;
    private OrthographicCamera cam;
    private OrthographicCamera hudCam;
    private Music sound;
    private GameStateManager gsm;
    public static Content res;
    public static Settings settings;
    // private String readString;
//
    // private Server aiServer;
    // private int aiPort = 9967;
    // private String aiIpAddress = "localhost";
    // private int aiMaxRetries = 2;

    public Game(Settings s) {
        settings = s;
    }

    public void create() {

        // set up relative paths for devices
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

        // B2DVars.PATH = "assets/"; //TODO: this?

        // aiServer = new Server();
        // aiServer.setPortAndIp(aiPort, aiIpAddress); // Listen to port 55433 and ip 127.0.0.1
        // aiServer.setWaitingQue(true); // Use a listen queue
        // aiServer.startServer(); // Start the server
        // aiServer.registerServerListerner(new AiServerListener());


        // set up music player
        try {
            this.sound = Gdx.audio.newMusic(Gdx.files.internal(PATH + "sounds/forest.ogg"));
            this.sound.play();
            System.out.println("Music");
        } catch (Exception e) {
            System.out.println(PATH + "sounds/forest.ogg");
            System.out.println("Sound couldn't be located.");
        }

        Gdx.input.setInputProcessor(new MyInputProcessor());

        // load textures
        res = new Content();
        res.loadTexture(PATH + "images/BackgroundStars/Stars-Big_1_2_PC.png", "starBackground");

        /*res.loadTexture(PATH + "images/bosses/magmawormbody0.5.png", "magmawormbody0.5");
        res.loadTexture(PATH + "images/bosses/magmawormhead0.5.png", "magmawormhead0.5");
        res.loadTexture(PATH + "images/bosses/magmawormtail0.5.png", "magmawormtail0.5");
        res.loadTexture(PATH + "images/bosses/magmawormbody1.0.png", "magmawormbody1.0");
        res.loadTexture(PATH + "images/bosses/magmawormhead1.0.png", "magmawormhead1.0");
        res.loadTexture(PATH + "images/bosses/magmawormtail1.0.png", "magmawormtail1.0");*/

        /*res.loadTexture(PATH + "images/bosses/plantworm/head1.png", "head1");
        res.loadTexture(PATH + "images/bosses/plantworm/head2.png", "head2");
        res.loadTexture(PATH + "images/bosses/plantworm/claw.png", "claw");
        res.loadTexture(PATH + "images/bosses/plantworm/hook.png", "hook");
        res.loadTexture(PATH + "images/bosses/plantworm/base1.png", "base1");
        res.loadTexture(PATH + "images/bosses/plantworm/base2.png", "base2");*/

        //res.loadTexture(PATH + "images/player/Llama.png", "Llama");
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
        float dt = Gdx.graphics.getDeltaTime();
        GameStateManager.update(dt);
        GameStateManager.render();
        MyInput.update();

    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        System.gc();
    }

    @Override
    public void resize(int w, int h) {
        if ((Gdx.app.getType().equals(Application.ApplicationType.Desktop))) {
            B2DVars.V_HEIGHT = h;
            B2DVars.V_WIDTH = w;
            super.resize(w, h);
            cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            cam.update();
            sb.setProjectionMatrix(cam.combined);
            System.out.println("resized");
        }
    }

    public void pause() {
    }

    public void resume() {
    }

    public void setForegroundFPS(int value) {
    }

    public Music getSound() {
        return sound;
    }

    public void setSound(Music sound) {
        this.sound = sound;
    }

    // public String getReadString() {
    //     return readString;
    // }
}
