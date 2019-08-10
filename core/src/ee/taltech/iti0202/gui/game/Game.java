package ee.taltech.iti0202.gui.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.Content;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.GameStateManager;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.input.MyInput;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.input.MyInputProcessor;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.desktop.settings.Settings;
import ee.taltech.iti0202.gui.game.networking.client.GameClient;
import ee.taltech.iti0202.gui.game.networking.server.GameServer;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.*;

public class Game extends ApplicationAdapter {

    public static final String TITLE = "Lone PlayerEntity";
    public static Content res;
    public static Settings settings;
    public static GameServer server;
    public static GameClient client;
    private SpriteBatch sb;
    private OrthographicCamera cam;
    private OrthographicCamera hudCam;
    private Music sound;
    private GameStateManager gsm;
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
        if (!JAR) {
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
        }

        //B2DVars.PATH = "assets/";

        // aiServer = new Server();
        // aiServer.setPortAndIp(aiPort, aiIpAddress); // Listen to port 55433 and ip 127.0.0.1
        // aiServer.setWaitingQue(true); // Use a listen queue
        // aiServer.startServer(); // Start the server
        // aiServer.registerServerListerner(new AiServerListener());

        // set up music player
        try {
            this.sound = Gdx.audio.newMusic(Gdx.files.internal(PATH + "sounds/intro.ogg"));
            this.sound.play();
            this.sound.setVolume(0.2f);
            System.out.println("Music");
        } catch (Exception e) {
            System.out.println(PATH + "sounds/intro.ogg");
            System.out.println("Sound couldn't be located.");
        }

        Gdx.input.setInputProcessor(new MyInputProcessor());

        // load textures
        res = new Content();
        res.loadTexture(PATH + "images/BackgroundStars/Stars-Big_1_2_PC.png", "starBackground");
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

        Pixmap pm = new Pixmap(Gdx.files.local(PATH + "images/crosshair/arrow.png"));
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
        pm.dispose();

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
            cam.viewportHeight = h;
            cam.viewportWidth = w;
            hudCam.viewportWidth = h;
            hudCam.viewportWidth = w;
            cam.setToOrtho(false, B2DVars.V_WIDTH, B2DVars.V_HEIGHT);
            hudCam.setToOrtho(false, B2DVars.V_WIDTH, B2DVars.V_HEIGHT);
            cam.update();
            hudCam.update();
            sb.setProjectionMatrix(cam.combined);
            System.out.println("resized");
        }
    }

    public void pause() {}

    public void resume() {}

    public void setForegroundFPS(int value) {}

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
