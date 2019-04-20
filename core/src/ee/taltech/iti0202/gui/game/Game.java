package ee.taltech.iti0202.gui.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import ee.taltech.iti0202.gui.game.desktop.handlers.gdx.Content;
import ee.taltech.iti0202.gui.game.desktop.handlers.gdx.GameStateManager;
import ee.taltech.iti0202.gui.game.desktop.handlers.gdx.input.MyInput;
import ee.taltech.iti0202.gui.game.desktop.handlers.gdx.input.MyInputProcessor;
import ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.desktop.settings.Settings;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PATH;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PORT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_WIDTH;


public class Game extends ApplicationAdapter {

    public static final String TITLE = "Alone at Night";
    private SpriteBatch sb;
    private OrthographicCamera cam;
    private OrthographicCamera hudCam;
    public static Settings settings;
    private Music sound;
    private GameStateManager gsm;
    public static Content res;
    private String readString;

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

        // i am seriously dumb.
        // try {
        //     SocketHints socketHints = new SocketHints();
        //     // Socket will time our in 4 seconds
        //     socketHints.connectTimeout = 4000;
        //     //create the socket and connect to the server entered in the text box on port 9966
        //     connection = Gdx.net.newClientSocket(Net.Protocol.TCP, "localhost", 9966, socketHints);
        //     byte[] read = new byte[1024]; // some bytes
        //     int read1 = connection.getInputStream().read(new byte[1024]);
        //     readString = new String(read).trim();
        //     System.out.println(readString);
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }


        new Thread(new Runnable() {

            @Override
            public void run() {
                ServerSocketHints serverSocketHint = new ServerSocketHints();
                // 0 means no timeout.  Probably not the greatest idea in production!
                serverSocketHint.acceptTimeout = 0;

                // Create the socket server using TCP protocol and listening on 9021
                ServerSocket serverSocket = Gdx.net.newServerSocket(Net.Protocol.TCP, PORT, serverSocketHint);

                // Loop forever
                while (true) {
                    // Create a socket
                    Socket socket = serverSocket.accept(null);

                    // Read data from the socket into a BufferedReader
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    try {
                        // Read to the next newline (\n) and display that text on labelMessage
                        readString = buffer.readLine();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start(); // And, start the thread running


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

        res.loadTexture(PATH + "images/bosses/magmawormbody0.5.png", "magmawormbody0.5");
        res.loadTexture(PATH + "images/bosses/magmawormhead0.5.png", "magmawormhead0.5");
        res.loadTexture(PATH + "images/bosses/magmawormtail0.5.png", "magmawormtail0.5");
        res.loadTexture(PATH + "images/bosses/magmawormbody1.0.png", "magmawormbody1.0");
        res.loadTexture(PATH + "images/bosses/magmawormhead1.0.png", "magmawormhead1.0");
        res.loadTexture(PATH + "images/bosses/magmawormtail1.0.png", "magmawormtail1.0");

        res.loadTexture(PATH + "images/bosses/head1.png", "head1");
        res.loadTexture(PATH + "images/bosses/head2.png", "head2");
        res.loadTexture(PATH + "images/bosses/claw.png", "claw");
        res.loadTexture(PATH + "images/bosses/hook.png", "hook");
        res.loadTexture(PATH + "images/bosses/base1.png", "base1");
        res.loadTexture(PATH + "images/bosses/base2.png", "base2");

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

        /*accum += Gdx.graphics.getDeltaTime();
        while (accum >= STEP) {
            accum -= STEP;
            gsm.update(STEP);
            gsm.render();
            MyInput.update(); // this is to define the end state of current iteration
        }*/

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

    public String getReadString() {
        return readString;
    }
}
