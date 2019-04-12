package ee.taltech.iti0202.gui.game.desktop.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.List;
import java.util.concurrent.Callable;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.components.GameButton;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.LevelSelectionMenu;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.LoadGameMenu;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.MainMenu;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.Scene;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.SettingsMenu;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.animations.Animation;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.animations.Background;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.animations.ParallaxBackground;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.MAIN_SCREENS;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PATH;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_WIDTH;

public class Menu extends GameState {

    private enum menuState {
        MAIN,
        LEVELS,
        SETTINGS,
        RESUME,
        DEFAULT
    }

    private Background bg;
    private Animation animation;
    private Stage stage;
    private LevelSelectionMenu levelSelectionMenu;
    private SettingsMenu settingsMenu;
    private LoadGameMenu loadGameMenu;
    private Scene mainMenuScene;

    private World world;
    private Texture player;
    private Texture logo;
    private Texture backgroundTexture;
    private List<GameButton> buttons;
    private Vector2 mouseInWorld2D;
    private menuState menuState;

    public Menu() {
        menuState = menuState.MAIN;

        // get textures
        player = Game.res.getTexture("Llama");
        logo = Game.res.getTexture("Logo");

        // init parallax background
        addParallax(MAIN_SCREENS[3]);

        // init player animation
        TextureRegion[] reg = new TextureRegion[4];
        for (int i = 0; i < reg.length; i++) {
            reg[i] = new TextureRegion(player, i * 32, 0, 32, 32);
        }
        animation = new Animation(reg, 1 / 12f);
        mouseInWorld2D = new Vector2();

        levelSelectionMenu = new LevelSelectionMenu(cam, new Runnable() {
            @Override
            public void run() {
                menuState = menuState.MAIN;
            }
        });
        settingsMenu = new SettingsMenu(cam, game, new Runnable() {
            @Override
            public void run() {
                menuState = menuState.MAIN;
            }
        });
        loadGameMenu = new LoadGameMenu(cam, new Runnable() {
            @Override
            public void run() {
                menuState = menuState.MAIN;
            }
        });
        mainMenuScene = new MainMenu(cam, new Runnable() {
            @Override
            public void run() {
                menuState = menuState.LEVELS;
            }
        }, new Runnable() {
            @Override
            public void run() {
                menuState = menuState.RESUME;
            }
        }, new Runnable() {
            @Override
            public void run() {
                menuState = menuState.SETTINGS;
            }
        });

        // play button

        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);

        world = new World(new Vector2(0, -9.8f * 5), true);
    }

    private void addParallax(String path) {
        backgroundTexture = new Texture(Gdx.files.internal(PATH + path + "backgroundLayer.png"));
        stage = new Stage(new ScreenViewport());
        Array<Texture> textures = new Array<>();
        int layersCount = Gdx.files.internal(PATH + path).list().length;
        for (int i = 1; i < layersCount; i++) {
            textures.add(new Texture(Gdx.files.internal(PATH + path + "backgroundLayer" + i + ".png")));
            textures.get(textures.size - 1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }
        ParallaxBackground parallaxBackground = new ParallaxBackground(textures);
        parallaxBackground.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        parallaxBackground.setSpeed(50f);
        stage.addActor(parallaxBackground);
    }

    @Override
    public void handleInput() {
        switch (menuState) {
            case MAIN:
                mainMenuScene.handleInput();
                break;
            case RESUME:
                loadGameMenu.handleInput();
                break;
            case LEVELS:
                levelSelectionMenu.handleInput();
                break;
            case SETTINGS:
                settingsMenu.handleInput();
                break;
                default:
                    System.out.println("Error with menuState!!!");

        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        world.step(dt, 8, 3);

        mouseInWorld2D.x = Gdx.input.getX();
        mouseInWorld2D.y = Gdx.input.getY();

        //animation.update(dt);

        switch (menuState) {
            case MAIN:
                mainMenuScene.update(dt);
                break;
            case RESUME:
                loadGameMenu.update(dt);
                break;
            case LEVELS:
                levelSelectionMenu.update(dt);
                break;
            case SETTINGS:
                settingsMenu.update(dt);
                break;
                default:
                    System.out.println("Error with menuState!");
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sb.setProjectionMatrix(cam.combined);

        // draw background
        sb.begin();
        sb.draw(backgroundTexture, 0, 0);
        sb.end();
        stage.act();
        stage.draw();

        // draw buttons

        switch (menuState) {
            case MAIN:
                mainMenuScene.render(sb);
                break;
            case RESUME:
                loadGameMenu.render(sb);
                break;
            case LEVELS:
                levelSelectionMenu.render(sb);
                break;
            case SETTINGS:
                settingsMenu.render(sb);
                break;
                default:
                    System.out.println("Error with menuState!");
        }

        // draw player
        /*sb.begin();
        sb.draw(animation.getFrame(), 146, 151);
        sb.end();*/
    }

    @Override
    public void dispose() {
        stage.dispose();
        System.gc();
    }
}
