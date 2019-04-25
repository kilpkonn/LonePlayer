package ee.taltech.iti0202.gui.game.desktop.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.Boss;
import ee.taltech.iti0202.gui.game.desktop.entities.player.Player;
import ee.taltech.iti0202.gui.game.desktop.entities.player.PlayerLoader;
import ee.taltech.iti0202.gui.game.desktop.entities.staticobjects.Checkpoint;
import ee.taltech.iti0202.gui.game.desktop.handlers.gdx.MyContactListener;
import ee.taltech.iti0202.gui.game.desktop.handlers.gdx.input.MyInput;
import ee.taltech.iti0202.gui.game.desktop.handlers.hud.Hud;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.EndMenu;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.PauseMenu;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.SettingsMenu;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.animations.Animation;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.animations.ParallaxBackground;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.layers.Draw;
import ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.desktop.states.gameprogress.GameProgress;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BACKGROUND;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BACKGROUND_SCREENS;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BACKGROUND_SPEEDS;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BIT_BOSSES;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BIT_WORM;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.DEBUG;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.DIMENTSION_1;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.DIMENTSION_2;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.DMG_MULTIPLIER;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.DMG_ON_LANDING;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.GRAVITY;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.MAIN_SCREENS;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.MAX_SPEED;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PATH;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PLAYER_DASH_FORCE_SIDE;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PLAYER_DASH_FORCE_UP;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PLAYER_SPEED;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PPM;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.TERRA_DIMENTSION_1;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.TERRA_DIMENTSION_2;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.TERRA_SQUARES;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.UPDATE;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_WIDTH;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.gotHitBySnek;

@EqualsAndHashCode(callSuper = true)
@Data
public class Play extends GameState {

    public enum pauseState {
        PAUSE,
        RUN,
        RESUME,
        SETTINGS,
        END,
        DEFAULT,
    }

    private World world;
    private Box2DDebugRenderer b2dr;
    private OrthographicCamera b2dcam;
    private OrthographicCamera hudCam;
    private MyContactListener cl;
    private TiledMap tiledMap;
    private Map<TiledMapTileLayer.Cell, Animation> animatedCells;
    private OrthogonalTiledMapRenderer renderer;
    private Player player;
    private boolean dimension;
    private Array<Boss> SnowManArray;
    private Array<Array<Boss>> MagmabossArray;
    private Array<Array<Boss>> PlantbossArray;
    private Array<Checkpoint> checkpointList;
    private Checkpoint activeCheckpoint;
    private Vector2 tempPlayerLocation;
    private Vector2 initPlayerLocation;
    private BodyDef bdef;
    private PolygonShape polyShape;
    private CircleShape circle;
    private FixtureDef fdef;
    private PauseMenu pauseMenu;
    private SettingsMenu settingsMenu;
    private EndMenu endMenu;
    private Hud hud;
    private Stage stage;
    private Texture backgroundTexture;
    private ParallaxBackground parallaxBackground;
    private Vector2 current_force = new Vector2(0, 0);
    private TiledMapTileLayer background;
    private TiledMapTileLayer foreground;
    private TiledMapTileLayer dimension_2;
    private TiledMapTileLayer dimension_1;
    private pauseState playState = pauseState.DEFAULT;
    private boolean gameFadeOut = false;
    private boolean gameFadeDone = true;
    private boolean dimensionFadeDone = false;
    private boolean newPlayer;
    private float currentDimensionFade = B2DVars.DIMENSION_FADE_AMOUNT;
    private float currentMenuFade = 1;
    private float backgroundSpeed;
    private String act;
    private String map;
    private int gracePeriod = 60;
    private int takingTurnsBase = 10; // how long one boss attacks
    private int curtentlyActiveBoss = 0;
    private int timeElapsed = 0;
    private int PlantBossSize = 1;
    private boolean executeEnd = true;
    private B2DVars.gameDifficulty difficulty;
    private boolean checkpoints = true;
    private boolean bosses = true;


    ////////////////////////////////////////////////////////////////////         Set up game        ////////////////////////////////////////////////////////////////////

    Play(String act, String map, B2DVars.gameDifficulty difficulty, GameProgress progress) {
        this.act = act;
        this.map = map;
        this.difficulty = difficulty;

        // set the difficulty
        System.out.println(difficulty);

        switch (difficulty) {
            case EASY:
                DMG_MULTIPLIER = 1;
                DMG_ON_LANDING = 10;
                checkpoints = true;
                bosses = false;
                break;

            case HARD:
                DMG_MULTIPLIER = 1.5f;
                DMG_ON_LANDING = 9;
                checkpoints = true;
                bosses = true;
                break;

            case BRUTAL:
                DMG_MULTIPLIER = 2;
                DMG_ON_LANDING = 8;
                checkpoints = false;
                bosses = true;
                break;
        }

        game.getSound().stop();
        switch (act) {
            case "Desert":
                break;
            case "Plains":
                break;
            case "Snow":
                game.setSound(Gdx.audio.newMusic(Gdx.files.internal(PATH + "sounds/wind1.wav")));
                game.getSound().setLooping(true);
                game.getSound().play();
                game.getSound().setVolume(0.2f);
                break;
        }
        // sey up world
        world = new World(new Vector2(0, GRAVITY), true);
        cl = new MyContactListener();
        world.setContactListener(cl);
        if (DEBUG) b2dr = new Box2DDebugRenderer();
        checkpointList = new Array<>();

        // create shapes
        bdef = new BodyDef();
        polyShape = new PolygonShape();
        circle = new CircleShape();
        fdef = new FixtureDef();

        // create array for bosses
        dimension = true;
        tempPlayerLocation = new Vector2();
        SnowManArray = new Array<>();
        MagmabossArray = new Array<>();
        PlantbossArray = new Array<>();

        //set up cameras
        b2dcam = new OrthographicCamera();
        b2dcam.setToOrtho(false, V_WIDTH / PPM, V_HEIGHT / PPM);
        hudCam = new OrthographicCamera();
        hudCam.setToOrtho(false, V_WIDTH / PPM, V_HEIGHT / PPM);

        // create pause state
        pauseMenu = new PauseMenu(act, map, hudCam, new Runnable() {
            @Override
            public void run() {
                playState = pauseState.RUN;
                UPDATE = true;
                gameFadeOut = false;
                gameFadeDone = false;
            }
        }, new Runnable() {
            @Override
            public void run() {
                saveGame();
            }
        }, new Runnable() {
            @Override
            public void run() {
                playState = pauseState.SETTINGS;
            }
        });

        settingsMenu = new SettingsMenu(hudCam, game, new Runnable() {
            @Override
            public void run() {
                playState = pauseState.PAUSE;
            }
        });

        endMenu = new EndMenu(act, map, hudCam, difficulty, new Runnable() {
            @Override
            public void run() {
                playState = pauseState.SETTINGS;
            }
        });
        hud = new Hud(hudCam, this);

        //ShapeRenderer shapeRenderer = new ShapeRenderer();
        playState = pauseState.RUN;

        // set up background
        stage = new Stage(new ScreenViewport());

        String backgroundPath = MAIN_SCREENS[BACKGROUND_SCREENS.get(act)];
        backgroundTexture = new Texture(Gdx.files.internal(PATH + backgroundPath + "backgroundLayer.png"));

        backgroundSpeed = BACKGROUND_SPEEDS.get(act);
        Array<Texture> textures = new Array<>();
        int layersCount = Gdx.files.internal(PATH + backgroundPath).list().length;
        for (int i = 1; i < layersCount; i++) {
            textures.add(new Texture(Gdx.files.internal(PATH + backgroundPath + "backgroundLayer" + i + ".png")));
            textures.get(textures.size - 1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }
        stage = new Stage();

        parallaxBackground = new ParallaxBackground(textures);
        parallaxBackground.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        parallaxBackground.setSpeed(0f);
        stage.addActor(parallaxBackground);
        ////////////////////////////////    Tiled stuff here    ///////////////////////


        // load tiled map
        String path = PATH + "maps/levels/" + act + "/" + map;
        tiledMap = new TmxMapLoader().load(path);
        renderer = new OrthogonalTiledMapRenderer(tiledMap);
        animatedCells = new HashMap<>();

        Draw draw = new Draw(this, sb);

        PlayerLoader playerLoader = new PlayerLoader(this, sb);
        if (progress != null) {
            dimension = progress.dimension;
            draw.drawLayers();
            playerLoader.initPlayer(progress);
        } else {
            playerLoader.initPlayer();
            draw.drawLayers();
        }


        cam.position.set(
                player.getPosition().x * PPM,
                player.getPosition().y * PPM,
                0);
        newPlayer = true;
        UPDATE = true;
        cam.zoom = 1;

        currentMenuFade = 1;
        gameFadeDone = false;
        gameFadeOut = false;
    }

    public Play(String act, String map, B2DVars.gameDifficulty difficulty) {
        this(act, map, difficulty, null);
    }

    public Play(GameProgress progress) {
        this(progress.act, progress.map, progress.difficulty, progress);
    }

    ////////////////////////////////////////////////////////////////////      Handle I/O devices    ////////////////////////////////////////////////////////////////////

    public void handleInput() {

        current_force = player.getBody().getLinearVelocity();

        //pause screen
        if (MyInput.isPressed(Game.settings.ESC)) {
            if (playState == pauseState.RUN) {
                UPDATE = false;
                playState = pauseState.PAUSE;
                gameFadeOut = true;
                gameFadeDone = false;
            } else {
                UPDATE = true;
                playState = pauseState.RUN;
                gameFadeOut = false;
                gameFadeDone = false;
            }
        }

        if (playState == pauseState.RUN) {
            //change dimension
            if (MyInput.isPressed(Game.settings.CHANGE_DIMENSION)) {
                System.out.println("changed dimension");
                dimensionFadeDone = false;
                dimension = !dimension;

                short mask;
                if (dimension) {
                    mask = BIT_BOSSES | BIT_WORM | DIMENTSION_1 | DIMENTSION_2 | TERRA_SQUARES | BACKGROUND | TERRA_DIMENTSION_1;
                } else {
                    mask = BIT_BOSSES | BIT_WORM | DIMENTSION_1 | DIMENTSION_2 | TERRA_SQUARES | BACKGROUND | TERRA_DIMENTSION_2;
                }

                Filter filter = new Filter();
                for (Fixture playerFixture : player.getBody().getFixtureList()) {
                    filter.groupIndex = playerFixture.getFilterData().groupIndex;
                    filter.categoryBits = playerFixture.getFilterData().categoryBits;
                    filter.maskBits = mask;
                    playerFixture.setFilterData(filter);
                }

            }

            //player jump / double jump / dash
            if (MyInput.isPressed(Game.settings.JUMP)) {
                if (cl.isPlayerOnGround()) {
                    player.getBody().applyLinearImpulse(new Vector2(0, PLAYER_DASH_FORCE_UP), tempPlayerLocation, true);//.applyForceToCenter(0, PLAYER_DASH_FORCE_UP, true);
                    player.setAnimation(Player.PlayerAnimation.JUMP);
                } else if (cl.getWallJump() != 0) {
                    System.out.println("Walljump");
                    player.getBody().applyLinearImpulse(new Vector2(cl.getWallJump() * PLAYER_DASH_FORCE_UP, PLAYER_DASH_FORCE_UP), tempPlayerLocation, true);
                    cl.setWallJump(0);
                } else if (cl.isDoubleJump()) {
                    System.out.println("Double jump");
                    player.getBody().applyLinearImpulse(new Vector2(0, PLAYER_DASH_FORCE_UP), tempPlayerLocation, true);
                    cl.setDoubleJump(false);
                    player.setAnimation(Player.PlayerAnimation.ROLL, true);
                }
            }

            //player move left
            if (MyInput.isDown(Game.settings.MOVE_LEFT)) {
                if (current_force.x > -MAX_SPEED) {
                    if (cl.isPlayerOnGround()) {
                        player.getBody().applyForceToCenter(-PLAYER_SPEED, 0, true);
                        player.setAnimation(Player.PlayerAnimation.RUN);
                    } else {
                        player.getBody().applyForceToCenter(-PLAYER_SPEED * 1.25f, 0, true);
                    }

                }
                player.setFlipX(true);
            }

            //player dash left
            if (MyInput.isPressed(Game.settings.MOVE_LEFT)) {
                if (!cl.isPlayerOnGround() && cl.isDash()) {
                    current_force = player.getBody().getLinearVelocity();
                    if (current_force.x > 0) {
                        player.getBody().applyLinearImpulse(new Vector2(-current_force.x, 0), tempPlayerLocation, true);
                    } else {
                        player.getBody().applyLinearImpulse(new Vector2(-PLAYER_DASH_FORCE_SIDE, 0), tempPlayerLocation, true);
                    }
                    cl.setDash(false);
                    player.setAnimation(Player.PlayerAnimation.DASH);
                }
                player.setFlipX(true);
            }

            //player move right
            if (MyInput.isDown(Game.settings.MOVE_RIGHT)) {
                if (current_force.x < MAX_SPEED) {
                    if (cl.isPlayerOnGround()) {
                        player.setAnimation(Player.PlayerAnimation.RUN);
                        player.getBody().applyForceToCenter(PLAYER_SPEED, 0, true);
                    } else {
                        player.getBody().applyForceToCenter(PLAYER_SPEED * 1.25f, 0, true);
                    }
                }
                player.setFlipX(false);
            }

            //player dash right
            if (MyInput.isPressed(Game.settings.MOVE_RIGHT)) {
                if (!cl.isPlayerOnGround() && cl.isDash()) {
                    if (current_force.x < 0) {
                        player.getBody().applyLinearImpulse(new Vector2(-current_force.x, 0), tempPlayerLocation, true);
                    } else {
                        player.getBody().applyLinearImpulse(new Vector2(PLAYER_DASH_FORCE_SIDE, 0), tempPlayerLocation, true);
                    }
                    cl.setDash(false);
                    player.setAnimation(Player.PlayerAnimation.DASH);
                }
                player.setFlipX(false);
            }

            if (!MyInput.isDown(-1) && cl.isPlayerOnGround()) {
                player.setAnimation(Player.PlayerAnimation.IDLE);
            }
        }
    }

    public void update(float dt) {

        if (newPlayer) {
            if (Math.abs(player.getPosition().x - cam.position.x / PPM) < 1 && Math.abs(player.getPosition().y - cam.position.y / PPM) < 1)
                newPlayer = false;
        } else handleInput();

        if (UPDATE) world.step(dt, 10, 2); // recommended values

        if (cl.isEnd() && (playState == pauseState.RUN || playState == pauseState.PAUSE)) {
            UPDATE = false;
            gameFadeOut = true;
            gameFadeDone = false;
            playState = pauseState.END;
        }

        updateGameFade(dt);
        updateDimensionFade(dt);

        switch (playState) {
            case RUN:
                updateProps(dt);
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
                // if (cam.zoom < 5)
                //     cam.zoom += 0.01; //TODO: Fix this
                if (executeEnd) playSoundOnce("sounds/end.ogg");
                executeEnd = false;
                //gameFadeOut = true;
                //gameFadeDone = false;
                //drawAndSetCamera();
                cam.update();
                endMenu.update(dt);

            default:
                break;
        }
    }

    private void updateProps(float dt) {
        //update camera
        if (DEBUG) {

            b2dcam.position.set(
                    player.getPosition().x,
                    player.getPosition().y,
                    0);

            cam.position.set(
                    player.getPosition().x * PPM,
                    player.getPosition().y * PPM,
                    0);

            b2dcam.update();

            cl.setDoubleJump(true);

        } else {

            cam.position.x += (player.getPosition().x - cam.position.x / PPM) * 2 * PPM * dt;
            cam.position.y += (player.getPosition().y - cam.position.y / PPM) * 2 * PPM * dt;
        }

        cam.position.x = Math.round(cam.position.x);
        cam.position.y = Math.round(cam.position.y);

        // System.out.println(cam.position.x);
        // System.out.println(cam.position.y);
        // System.out.println();

        cam.update();

        //call update animation
        if (player.getHealth() == 0) {
            cl.setDeathState((short) 3);
        }
        if (cl.getDeathState() == 0) {
            player.update(dt);
        } else if (gracePeriod == 0) {
            if (cl.getDeathState() == 2) {
                player.setHealth(player.getHealth() - gotHitBySnek * 10);
                playSoundOnce("sounds/sfx_deathscream_alien1.wav");
            }
            if (cl.getDeathState() == 3) {
                if (!newPlayer)
                    player.setHealth(0); //TODO: Fix instant death on load game
            } else {
                playSoundOnce("sounds/sfx_damage_hit2.wav", 0.1f);
                player.setHealth(player.getHealth() - gotHitBySnek);
            }
            if (player.getHealth() <= 0) {
                playSoundOnce("sounds/sfx_sound_shutdown1.wav");
                PlayerLoader playerLoader = new PlayerLoader(this, sb);
                playerLoader.initPlayer();
            }
            cl.setDeathState((short) 0);
        }
        player.update(dt);

        //calculate falling dmg
        player.onLanded(player.getBody().getLinearVelocity(), cl.isPlayerOnGround());


        //update bosses
        updateBosses(dt);

        //draw tilemap animations
        if (animatedCells != null) {
            for (Animation animation : animatedCells.values()) {
                animation.update(dt);
            }
        }

        // create a new checkpoint if needed
        if (checkpointList.size != 0) {
            if (cl.isNewCheckpoint()) {
                Checkpoint curTemp = checkpointList.get(0); // just in case
                for (Checkpoint checkpoint : checkpointList) {
                    if (Math.abs(checkpoint.getPosition().x - player.getBody().getPosition().x) < 1 &&
                            Math.abs(checkpoint.getPosition().x - player.getBody().getPosition().x) < 1) {
                        curTemp = checkpoint;
                    }
                }
                cl.setCurCheckpoint(curTemp.getBody());
                curTemp.onReached();
                if (activeCheckpoint != null)
                    activeCheckpoint.lostItsPurposeAsANewBetterCheckpointWasFoundAndTheOldOneWasTossedAwayLikeAnOldCondom();
                activeCheckpoint = curTemp;

                playSoundOnce("sounds/checkpoint.ogg");
            }
            for (Checkpoint checkpoint : checkpointList)
                checkpoint.update(dt);
        }

        if (gracePeriod > 0)
            gracePeriod -= 1;
    }

    private void updateBosses(float dt) {
        if (SnowManArray.size != 0) {
            for (Boss boss : SnowManArray) {
                boss.update(dt);
            }
        }

        if (MagmabossArray.size != 0) {
            for (Array<Boss> bossList : MagmabossArray)
                for (int i = 0; i < bossList.size; i++) {
                    if (bossList.size > 110) {
                        if (i == bossList.size - 1) {
                            bossList.get(i).updateHeadBig(dt);
                        } else {
                            bossList.get(i).update(dt);
                        }
                    } else {
                        if (i == bossList.size - 1) {
                            bossList.get(i).updateHeadSmall(dt);
                        } else {
                            bossList.get(i).update(dt);
                        }
                    }
                }
        }


        if (PlantbossArray.size != 0) {
            int takingTurns = takingTurnsBase * Gdx.graphics.getFramesPerSecond();
            timeElapsed++;
            if (timeElapsed > takingTurns) {
                timeElapsed = 0;
                curtentlyActiveBoss = (curtentlyActiveBoss + 1) % PlantBossSize;
            }
            int j = 0;
            for (Array<Boss> bossList : PlantbossArray) {
                for (int i = 0; i < bossList.size; i++) {
                    if (i == 0) {
                        if (j == curtentlyActiveBoss) {
                            bossList.get(i).updateHeadBig(dt);
                        } else {
                            bossList.get(i).updateCircularMotion(dt);
                        }
                        bossList.get(i).updateRotation(dt);
                    } else if (i == bossList.size - 1) {
                        bossList.get(i).updateRotation(dt);
                    } else {
                        bossList.get(i).update(dt);
                    }

                }
                j++;
            }
        }
    }

    public void render() {
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
        //render pauseMenu

        drawAndSetCamera();

        if (playState == pauseState.SETTINGS) settingsMenu.render(sb);
        else if (playState == pauseState.END) endMenu.render(sb);
        else pauseMenu.render(sb);
    }

    private void updateGameFade(float dt) {
        if (!gameFadeDone) {
            if (gameFadeOut) {
                if (currentMenuFade < B2DVars.MENU_FADE_AMOUNT) {
                    currentMenuFade += (B2DVars.MENU_FADE_AMOUNT / B2DVars.MENU_FADE_TIME) * dt;
                } else {
                    currentMenuFade = B2DVars.MENU_FADE_AMOUNT;
                    gameFadeDone = true;
                }
            } else {
                if (currentMenuFade > 0) {
                    currentMenuFade -= (B2DVars.MENU_FADE_AMOUNT / B2DVars.MENU_FADE_TIME) * dt;
                } else {
                    currentMenuFade = 0;
                    gameFadeDone = true;
                }
            }

            /*parallaxBackground.setColor(1, 1, 1, 1 - currentMenuFade);
            player.setOpacity(1 - currentMenuFade);
            if (checkpoint != null) checkpoint.setOpacity(1 - currentMenuFade);*/
        }
    }

    private void updateDimensionFade(float dt) {
        if (!dimensionFadeDone) {
            if (dimension) {
                if (currentDimensionFade > 0) {
                    currentDimensionFade -= (B2DVars.DIMENSION_FADE_AMOUNT / B2DVars.DIMENSION_FADE_TIME) * dt;
                } else {
                    currentDimensionFade = 0;
                    dimensionFadeDone = true;
                }
            } else {
                if (currentDimensionFade < B2DVars.DIMENSION_FADE_AMOUNT) {
                    currentDimensionFade += (B2DVars.DIMENSION_FADE_AMOUNT / B2DVars.DIMENSION_FADE_TIME) * dt;
                } else {
                    currentDimensionFade = B2DVars.DIMENSION_FADE_AMOUNT;
                    dimensionFadeDone = true;
                }
            }
            if (dimension_1 != null) dimension_1.setOpacity(1 - currentDimensionFade);
            if (dimension_2 != null)
                dimension_2.setOpacity((1 - B2DVars.DIMENSION_FADE_AMOUNT) + currentDimensionFade);
        }
    }

    private void drawAndSetCamera() {

        //clear screen
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //set camera to follow player
        /*if (current_force.x < -1) parallaxBackground.setSpeed(-60f);
        else if (current_force.x > 1) parallaxBackground.setSpeed(60f);
        else parallaxBackground.setSpeed(0);
        System.out.println(current_force.x);*/
        sb.begin();
        sb.draw(backgroundTexture, 0, 0);
        sb.end();
        parallaxBackground.setSpeed(backgroundSpeed * (current_force.x * 5 + 4)); //TODO: more advance stuff here, move with camera...
        stage.act();
        stage.draw();

        //render animations
        if (animatedCells != null) for (TiledMapTileLayer.Cell cell : animatedCells.keySet())
            cell.setTile(new StaticTiledMapTile(animatedCells.get(cell).getFrame()));

        //draw tilemap
        renderer.setView(cam);
        renderer.getBatch().begin();
        if (background != null) renderer.renderTileLayer(background);
        if (foreground != null) renderer.renderTileLayer(foreground);
        if (dimension_1 != null) renderer.renderTileLayer(dimension_1);
        if (dimension_2 != null) renderer.renderTileLayer(dimension_2);
        renderer.getBatch().end();

        if (DEBUG) b2dr.render(world, b2dcam.combined);

        sb.setProjectionMatrix(cam.combined);

        // draw checkpoint
        if (checkpointList.size != 0)
            for (Checkpoint checkpoint : checkpointList) {
                checkpoint.render(sb);
            }

        //draw player
        if (player != null) player.render(sb);

        if (SnowManArray != null)
            for (Boss boss : SnowManArray)
                boss.render(sb);

        if (MagmabossArray != null) {
            for (Array<Boss> bossList : MagmabossArray)
                for (Boss boss : bossList) boss.render(sb);
        }

        if (PlantbossArray != null) {
            for (Array<Boss> bossList : PlantbossArray) {
                for (Boss boss : bossList) boss.render(sb);
                bossList.get(0).render(sb);
            }
        }

        hud.render(sb);

        if (currentMenuFade > 0) {
            /*renderer.getBatch().setColor(0, 0, 0, 1);
            if (background != null) renderer.renderTileLayer(background);
            if (foreground != null) renderer.renderTileLayer(foreground);
            if (dimension_1 != null) renderer.renderTileLayer(dimension_1);
            if (dimension_2 != null) renderer.renderTileLayer(dimension_2);
            renderer.getBatch().setColor(1, 1, 1, 1 - currentMenuFade);*/
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0, 0, 0, currentMenuFade);
            shapeRenderer.rect(0, 0, B2DVars.V_WIDTH, B2DVars.V_HEIGHT);
            shapeRenderer.end();
        }
    }

    private void saveGame() {
        GameProgress progress = new GameProgress();
        progress.playerLocationX = player.getPosition().x;
        progress.playerLocationY = player.getPosition().y;
        progress.playerVelocityX = player.getBody().getLinearVelocity().x;
        progress.playerVelocityY = player.getBody().getLinearVelocity().y;
        progress.act = act;
        progress.map = map;
        progress.dimension = dimension;
        progress.difficulty = difficulty;

        progress.save(B2DVars.PATH + "saves/" + new SimpleDateFormat("dd-MM-YYYY_HH-mm-ss", Locale.ENGLISH).format(new Date()) + ".json");
    }

    public void dispose() {
        stage.dispose();
        System.gc();
    }
}
