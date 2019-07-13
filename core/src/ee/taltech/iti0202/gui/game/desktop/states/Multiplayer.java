package ee.taltech.iti0202.gui.game.desktop.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.input.MyInput;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.hud.Hud;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.animations.ParallaxBackground;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.desktop.physics.GameWorld;
import ee.taltech.iti0202.gui.game.desktop.physics.PlayerBody;
import ee.taltech.iti0202.gui.game.desktop.physics.PlayerController;
import ee.taltech.iti0202.gui.game.desktop.render.WorldRenderer;
import ee.taltech.iti0202.gui.game.networking.server.player.Player;
import ee.taltech.iti0202.gui.game.networking.server.player.PlayerControls;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BACKGROUND_SCREENS;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BACKGROUND_SPEEDS;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BOSSES;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BOSS_BASE_HP;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.CHECKPOINTS;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.DMG_MULTIPLIER;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.DMG_ON_LANDING;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.MAIN_SCREENS;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.PATH;

public class Multiplayer extends GameState {

    private GameWorld gameWorld;
    private PlayerController playerController;
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
    private float playTime = 0;
    private boolean loading = true;

    private WorldRenderer worldRenderer;

    private State state = State.RUN;

    private boolean shouldUpdate = false;
    private PlayerControls controls;
    private Player playerToFollow;
    private Set<Player> tmpPlayers = new HashSet<>();

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

        // set up background
        String backgroundPath = MAIN_SCREENS[BACKGROUND_SCREENS.get(act)];
        backgroundTexture =
                new Texture(Gdx.files.local(PATH + backgroundPath + "backgroundLayer.png"));

        backgroundSpeed = BACKGROUND_SPEEDS.get(act);
        Array<Texture> textures = new Array<>();
        int layersCount = Gdx.files.local(PATH + backgroundPath).list().length;
        for (int i = 1; i < layersCount; i++) {
            textures.add(
                    new Texture(
                            Gdx.files.local(
                                    PATH + backgroundPath + "backgroundLayer" + i + ".png")));
            textures.get(textures.size - 1)
                    .setWrap(
                            Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }

        parallaxBackground = new ParallaxBackground(textures);
        parallaxBackground.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        parallaxBackground.setSpeed(0f);
        stage.addActor(parallaxBackground);

        gameWorld = new GameWorld(act, map);
        worldRenderer = new WorldRenderer(gameWorld, cam);
        playerController = new PlayerController(gameWorld.getPlayerBodies(), gameWorld.getPlayers());

        hud = new Hud(cam);
    }

    public void updatePlayers(Set<Player> players) {
        for (Player player : players) {
            gameWorld.updatePlayer(player);
            if (playerToFollow == null && player.id == Game.client.id) {
                setPlayerToFollow(player);
            }
        }
    }

    @Override
    public void handleInput() {
        switch (state) {
            case RUN:
                handleRunInput();
                break;
        }
    }

    @Override
    public void update(float dt) {
        updatePlayers(tmpPlayers);
        handleInput();
        gameWorld.update(dt);
        worldRenderer.update(dt);

        if (shouldUpdate) {
            controls.id = playerToFollow.id;
            Game.client.updatePlayerControls(controls);
            shouldUpdate = false;
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        switch (state) {
            case RUN:
                renderWorld();
                break;
        }
    }

    @Override
    public void dispose() {
        worldRenderer.dispose();
    }

    private void handleRunInput() {
        controls = new PlayerControls();
        if (MyInput.isPressed(Game.settings.JUMP)) {
            controls.jump = true;
            shouldUpdate = true;  //TODO: Something better here
        }
        if (MyInput.isDown(Game.settings.MOVE_LEFT)) {
            controls.moveLeft = true;
            shouldUpdate = true;
        }
        if (MyInput.isDown(Game.settings.MOVE_RIGHT)) {
            controls.moveRight = true;
            shouldUpdate = true;
        }
        if (MyInput.isPressed(Game.settings.MOVE_LEFT)) {
            controls.dashLeft = true;
            shouldUpdate = true;
        }
        if (MyInput.isPressed(Game.settings.MOVE_RIGHT)) {
            controls.dashRight = true;
            shouldUpdate = true;
        }
    }

    private void renderWorld() {

        // clear screen
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.begin();
        sb.draw(backgroundTexture, 0, 0);
        sb.end();
        parallaxBackground.setSpeed(backgroundSpeed + worldRenderer.getCamSpeed().x / 10);
        stage.act();
        stage.draw();

        // draw tilemap

        worldRenderer.render(sb);

        hud.render(sb);

        //TODO: Render fade over hud
    }

    public void setPlayerToFollow(Player playerToFollow) {
        this.playerToFollow = playerToFollow;
        worldRenderer.setPlayerToFollow(playerToFollow);
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
