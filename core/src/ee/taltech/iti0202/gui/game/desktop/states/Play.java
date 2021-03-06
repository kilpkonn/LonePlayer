package ee.taltech.iti0202.gui.game.desktop.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.Boss;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.handler.BossHander;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.handler.logic.BossLogic;
import ee.taltech.iti0202.gui.game.desktop.entities.checkpoints.handler.CheckpointHandler;
import ee.taltech.iti0202.gui.game.desktop.entities.player.handler.PlayerHandler;
import ee.taltech.iti0202.gui.game.desktop.entities.player.loader.PlayerLoader;
import ee.taltech.iti0202.gui.game.desktop.entities.projectile.bullet.handler.BulletHandler;
import ee.taltech.iti0202.gui.game.desktop.entities.weapons.handler.WeaponHandler;
import ee.taltech.iti0202.gui.game.desktop.entities.weapons.loader.WeaponLoader;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.GameStateManager;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.MyContactListener;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.input.MyInput;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.hud.Hud;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.EndMenu;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.PauseMenu;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.SettingsMenu;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.animations.ParallaxBackground;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.canvas.Draw;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.desktop.states.gameprogress.BossData;
import ee.taltech.iti0202.gui.game.desktop.states.gameprogress.GameProgress;

import java.text.SimpleDateFormat;
import java.util.*;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.sound.Sound.playSoundOnce;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.*;

public class Play extends GameState {

    // Play is the root node for most of the action

    // LibGdx variables
    private World world = new World(new Vector2(0, GRAVITY), true);
    private Draw draw;
    private MyContactListener cl = new MyContactListener();
    private Box2DDebugRenderer b2dr = new Box2DDebugRenderer();
    private OrthographicCamera b2dcam = new OrthographicCamera();
    private OrthographicCamera hudCam = new OrthographicCamera();
    private Hud hud;
    private OrthogonalTiledMapRenderer renderer;
    // handlers
    private PlayerHandler playerHandler;
    private BossHander bossHander;
    private WeaponHandler weaponHandler;
    private CheckpointHandler checkpointHandler;
    private BulletHandler bulletHandler;
    // States
    private GameProgress progress;
    private PauseMenu pauseMenu;
    private pauseState playState = pauseState.DEFAULT;
    private SettingsMenu settingsMenu;
    private EndMenu endMenu;
    private boolean executeEnd = true;
    private Stage stage = new Stage(new ScreenViewport());
    private String act;
    private String map;
    private GameDifficulty difficulty;
    // Background based variables
    private Texture backgroundTexture;
    private ParallaxBackground parallaxBackground;
    private float backgroundSpeed;
    // Boss logic, helpful variables
    private Vector2 camSpeed = new Vector2(0, 0);
    private float playTime = 0;
    private boolean loading = true;

    private Play(String act, String map, GameDifficulty difficulty, GameProgress progress) {
        this.act = act;
        this.map = map;
        this.difficulty = difficulty;

        // set the difficulty
        System.out.println(difficulty);

        switch (difficulty) {
            case EASY:
                DMG_MULTIPLIER = 1;
                DMG_ON_LANDING_SPEED = 10;
                CHECKPOINTS = true;
                BOSSES = false;
                break;

            case HARD:
                DMG_MULTIPLIER = 1.5f;
                DMG_ON_LANDING_SPEED = 9;
                CHECKPOINTS = true;
                BOSSES = true;
                break;

            case BRUTAL:
                DMG_MULTIPLIER = 2;
                DMG_ON_LANDING_SPEED = 8;
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

        // sey up world
        world.setContactListener(cl);

        // set up cameras
        b2dcam.setToOrtho(false, V_WIDTH / PPM, V_HEIGHT / PPM);
        hudCam.setToOrtho(false, V_WIDTH / PPM, V_HEIGHT / PPM);

        // create pause state
        pauseMenu = new PauseMenu(
                act,
                map,
                hudCam,
                () -> {
                    playState = pauseState.RUN;
                    draw.setGameFadeOut(false);
                    draw.setGameFadeDone(false);
                    UPDATE = true;
                },
                this::saveGame,
                () -> playState = pauseState.SETTINGS,
                () -> {
                    GameStateManager.pushState(GameStateManager.State.MENU);
                });

        settingsMenu =
                new SettingsMenu(
                        hudCam,
                        () -> playState = pauseState.PAUSE);

        endMenu =
                new EndMenu(
                        act,
                        map,
                        hudCam,
                        difficulty,
                        () -> playState = pauseState.SETTINGS);
        hud = new Hud(hudCam);

        playState = pauseState.RUN;

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

        ////////////////////////////////    Tiled stuff here    ///////////////////////

        draw = new Draw(sb, world, act, map); // first create the "canvas" to draw onto
        playerHandler = new PlayerHandler(this, sb, progress, cl, draw);
        checkpointHandler = new CheckpointHandler();
        bossHander = new BossHander(cl);
        bulletHandler = new BulletHandler(cl);
        weaponHandler = new WeaponHandler(world, draw);
        draw.setPlayerHandler(playerHandler);
        draw.setBossHander(bossHander);
        draw.setCheckpointHandler(checkpointHandler);
        draw.setBulletHandler(bulletHandler);
        draw.setWeaponHandler(weaponHandler);

        // and then load the player in
        if (progress != null) {
            draw.setDimension(progress.dimension);
            draw.drawLayers(false, progress.bosses);
            playerHandler.setPlayer(
                    PlayerLoader.initPlayer(progress, sb, playerHandler, draw, world));
        } else {
            draw.drawLayers(true, null);
            playerHandler.setPlayer(PlayerLoader.initPlayer(sb, playerHandler, draw, world, cl));
            playerHandler
                    .getPlayer()
                    .addWeapon(WeaponLoader.buildWeapon("M4", sb, draw.getWeaponHandler(), draw));
            playerHandler
                    .getPlayer()
                    .addWeapon(WeaponLoader.buildWeapon("Deagle", sb, draw.getWeaponHandler(), draw));
            playerHandler
                    .getPlayer()
                    .addWeapon(WeaponLoader.buildWeapon("Shotgun", sb, draw.getWeaponHandler(), draw));
            playerHandler.getPlayer().setCurrentWeapon(playerHandler.getPlayer().getWeapons().get(0));
        }

        cam.position.set(
                playerHandler.getPlayer().getPosition().x * PPM,
                playerHandler.getPlayer().getPosition().y * PPM,
                0);
        UPDATE = true;
        cam.zoom = 1;

        Game.setCursor(true);
    }

    public Play(String act, String map, GameDifficulty difficulty) {
        this(act, map, difficulty, null);
    }

    public Play(GameProgress progress) {
        this(progress.act, progress.map, progress.difficulty, progress);
    }

    public void handleInput() {

        playerHandler.handlePlayerInput(playState);

        // pause screen
        if (MyInput.isPressed(Game.settings.ESC)) {
            if (playState == pauseState.RUN) {
                UPDATE = false;
                parallaxBackground.setSpeed(0);
                playState = pauseState.PAUSE;
                draw.setGameFadeOut(true);
                draw.setGameFadeDone(false);
                Game.setCursor(false);
            } else {
                UPDATE = true;
                playState = pauseState.RUN;
                draw.setGameFadeOut(false);
                draw.setGameFadeDone(false);
                Game.setCursor(true);
            }
        }
    }

    public void update(float dt) {
        if (loading && playTime < MAX_LOAD_TIME) {
            playTime += dt;
            return;
        } else if (loading) {
            loading = false;
            playTime = (progress != null) ? progress.time : 0; // Start counting
        }

        if (playerHandler.isNewPlayer()) {
            if (Math.abs(playerHandler.getPlayer().getPosition().x - cam.position.x / PPM) < 1
                    && Math.abs(playerHandler.getPlayer().getPosition().y - cam.position.y / PPM)
                    < 1) playerHandler.setNewPlayer(false);
        } else handleInput();

        if (UPDATE) world.step(dt, 10, 2); // recommended values

        if (cl.isEnd() && (playState == pauseState.RUN || playState == pauseState.PAUSE)) {
            UPDATE = false;
            draw.setGameFadeOut(true);
            draw.setGameFadeDone(false);
            endMenu.setTime(playTime);
            playState = pauseState.END;
            Game.setCursor(false);
        }

        draw.updateGameFade(dt);
        draw.updateDimensionFade(dt);

        switch (playState) {
            case RUN:
                updateProps(dt);
                hud.setHp(draw.getPlayerHandler().getPlayer().getHealth());
                hud.setPlayTime(playTime);
                hud.update(dt);
                break;

            case PAUSE:
                pauseMenu.update(dt);
                break;

            case RESUME:
                break;

            case SETTINGS:
                settingsMenu.update(dt);
                break;

            case END:
                if (cam.zoom < 5) cam.zoom += 0.01; // TODO: Fix this
                game.getSound().stop();
                if (executeEnd) playSoundOnce("sounds/end.ogg", 0.1f);
                executeEnd = false;
                // gameFadeOut = true;
                // gameFadeDone = false;
                // drawAndSetCamera();
                cam.update();
                endMenu.update(dt);
                if (endMenu.done) {
                    cam.zoom = 1f;
                    game.setSound(
                            Gdx.audio.newMusic(Gdx.files.internal(PATH + "sounds/intro.ogg")));
                    game.getSound().setLooping(true);
                    game.getSound().play();
                    game.getSound().setVolume(0.1f);
                    GameStateManager.pushState(GameStateManager.State.MENU);
                }

            default:
                break;
        }
    }

    private void updateProps(float dt) {
        playTime += dt;

        // update camera
        if (DEBUG) {
            b2dcam.position.set(
                    playerHandler.getPlayer().getPosition().x,
                    playerHandler.getPlayer().getPosition().y,
                    0);

            cam.position.set(
                    playerHandler.getPlayer().getPosition().x * PPM,
                    playerHandler.getPlayer().getPosition().y * PPM,
                    0);

            b2dcam.update();
            cl.setDoubleJump(true);

        } else {

            camSpeed =
                    new Vector2(
                            (playerHandler.getPlayer().getPosition().x - cam.position.x / PPM)
                                    * 2
                                    * PPM,
                            (playerHandler.getPlayer().getPosition().y - cam.position.y / PPM)
                                    * 4
                                    * PPM);

            cam.position.x += camSpeed.x * dt;
            cam.position.y += camSpeed.y * dt;
        }

        cam.position.x = Math.round(cam.position.x);
        cam.position.y = Math.round(cam.position.y);

        // System.out.println(cam.position.x);
        // System.out.println(cam.position.y);
        // System.out.println();

        cam.update();

        // update animated cells
        draw.update(dt);

        // calculate falling dmg
        playerHandler
                .getPlayer()
                .onLanded(
                        playerHandler.getPlayer().getBody().getLinearVelocity(),
                        cl.isPlayerOnGround());

        // set new checkpoint
        checkpointHandler.setPlayerNewCheckpoint(cl, playerHandler);
    }

    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        switch (playState) {
            case RUN:
                drawAndSetCamera();
                break;

            case PAUSE:
                pauseMenu.handleInput();
                drawPauseScreen();
                break;

            case RESUME:
                break;

            case SETTINGS:
                settingsMenu.handleInput();
                drawPauseScreen();
                break;
            case END:
                drawPauseScreen();
                endMenu.handleInput();
                endMenu.render(sb);
                break;
            default:
                break;
        }
    }

    private void drawPauseScreen() {
        // render pauseMenu

        drawAndSetCamera();

        if (playState == pauseState.SETTINGS) settingsMenu.render(sb);
        else if (playState == pauseState.END) endMenu.render(sb);
        else pauseMenu.render(sb);
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
        draw.render(cam);
        draw.render(sb);

        hud.render(sb);

        if (DEBUG) b2dr.render(world, b2dcam.combined);

        draw.renderFade();
    }

    private void saveGame() {
        GameProgress progress = new GameProgress();
        /*progress.playerLocationX = player.getPosition().x;
        progress.playerLocationY = player.getPosition().y;
        progress.playerVelocityX = player.getBody().getLinearVelocity().x;
        progress.playerVelocityY = player.getBody().getLinearVelocity().y;*/
        progress.checkpointX = playerHandler.getCheckpoint().getBody().getPosition().x;
        progress.checkpointY = playerHandler.getCheckpoint().getBody().getPosition().y;
        progress.act = act;
        progress.map = map;
        progress.time = playTime;
        progress.dimension = draw.isDimension();
        progress.difficulty = difficulty;

        for (BossLogic bossLogic : bossHander.getLogicHandlers()) {
            switch (bossLogic.getLogic()) {
                case "worm":
                    Boss worm = bossLogic.getBossArray().get(1);
                    BossData wormData =
                            new BossData(
                                    act.equals("Snow") ? "1_snow" : "1",
                                    bossLogic.getBossArray().size,
                                    worm.getPosition().x,
                                    worm.getPosition().y,
                                    worm.isDecider());
                    progress.bosses.add(wormData);

                    break;
                case "hydra":
                    Boss hydra = bossLogic.getBossArray().get(1);
                    BossData hydraData =
                            new BossData(
                                    "2",
                                    bossLogic.getBossArray().size,
                                    hydra.getPosition().x,
                                    hydra.getPosition().y,
                                    hydra.isDecider());
                    progress.bosses.add(hydraData);
                    break;
                case "snowman":
                    for (Boss boss : bossLogic.getBossArray()) {
                        BossData bossData =
                                new BossData(
                                        "3",
                                        1,
                                        boss.getPosition().x,
                                        boss.getPosition().y,
                                        boss.isDecider());
                        progress.bosses.add(bossData);
                    }
                    break;
            }
        }

        progress.save(
                B2DVars.PATH
                        + "saves/"
                        + new SimpleDateFormat("dd-MM-YYYY_HH-mm-ss", Locale.ENGLISH)
                        .format(new Date())
                        + ".json");
    }

    public World getWorld() {
        return world;
    }

    @Override
    public void dispose() {
        stage.dispose();
        hud.dispose();
        settingsMenu.dispose();
        pauseMenu.dispose();
        endMenu.dispose();
        backgroundTexture.dispose();
        System.gc();
    }

    public enum pauseState {
        PAUSE,
        RUN,
        RESUME,
        SETTINGS,
        END,
        DEFAULT,
    }
}
