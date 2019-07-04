package ee.taltech.iti0202.gui.game.desktop.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import ee.taltech.iti0202.gui.game.desktop.game_handlers.hud.Hud;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.animations.ParallaxBackground;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.desktop.physics.GameWorld;
import ee.taltech.iti0202.gui.game.desktop.render.WorldRenderer;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BOSSES;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BOSS_BASE_HP;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.CHECKPOINTS;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.DMG_MULTIPLIER;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.DMG_ON_LANDING;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.PATH;

public class Multiplayer extends GameState {

    private GameWorld gameWorld;
    private Hud hud;
    private OrthogonalTiledMapRenderer renderer;

    private Stage stage = new Stage(new ScreenViewport());
    private String act;
    private String map;
    private B2DVars.GameDifficulty difficulty;

    // Background based variables
    private Texture backgroundTexture;
    private ParallaxBackground parallaxBackground;
    private float backgroundSpeed;
    // Boss logic, helpful variables
    private Vector2 camSpeed = new Vector2(0, 0);
    private float playTime = 0;
    private boolean loading = true;

    private WorldRenderer worldRenderer;

    private State state = State.RUN;

    public Multiplayer(String act, String map, B2DVars.GameDifficulty difficulty) {
        this.act = act;
        this.map = map;
        this.difficulty = difficulty;

        System.out.println(String.format("Multiplayer game: %s - %s - %s", act, map, difficulty));

        switch (difficulty) {
            case EASY:
                DMG_MULTIPLIER = 1;
                DMG_ON_LANDING = 10;
                CHECKPOINTS = true;
                BOSSES = false;
                break;

            case HARD:
                DMG_MULTIPLIER = 1.5f;
                DMG_ON_LANDING = 9;
                CHECKPOINTS = true;
                BOSSES = true;
                break;

            case BRUTAL:
                DMG_MULTIPLIER = 2;
                DMG_ON_LANDING = 8;
                CHECKPOINTS = true;
                BOSS_BASE_HP *= 2;
                BOSSES = true;
                break;
        }

        game.getSound().stop();
        List<String> songs =
                new ArrayList<>(
                        Arrays.asList(
                                "ObservingTheStar.ogg",
                                "Soliloquy.ogg",
                                "Rise of spirit.ogg",
                                "Cyberpunk Moonlight Sonata.ogg",
                                "Hero Immortal.ogg", // Please credit me "Trevor Lentz" if you choose to use this. :)
                                "Vilified (2012).ogg",
                                "xeon6.ogg"));

        String song = songs.get(new Random().nextInt(songs.size()));

        game.setSound(Gdx.audio.newMusic(Gdx.files.internal(PATH + "sounds/" + song)));
        game.getSound().setLooping(true);
        game.getSound().play();
        game.getSound().setVolume(0.12f);
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        switch (state) {
            case RUN:
                drawAndSetCamera();
                break;
        }
    }

    @Override
    public void dispose() {

    }

    private void drawAndSetCamera() {

        // clear screen
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.begin();
        sb.draw(backgroundTexture, 0, 0);
        sb.end();
        parallaxBackground.setSpeed(backgroundSpeed + camSpeed.x / 10);
        stage.act();
        stage.draw();

        // draw tilemap

        worldRenderer.render(sb);

        hud.render(sb);

        //TODO: Render fade over hud
    }

    private enum State {
        PAUSE,
        RUN,
        RESUME,
        SETTINGS,
        END,
        DEFAULT,
    }
}
