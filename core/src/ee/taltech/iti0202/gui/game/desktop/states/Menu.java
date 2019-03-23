package ee.taltech.iti0202.gui.game.desktop.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.List;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.handlers.gdx.GameStateManager;
import ee.taltech.iti0202.gui.game.desktop.handlers.gdx.input.MyInput;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.GameButton;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.LevelSelectionMenu;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.MainMenu;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.Scene;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.SettingsMenu;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.animations.Animation;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.animations.Background;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.animations.ParallaxBackground;
import ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.MAIN_SCREENS;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PATH;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_WIDTH;

public class Menu extends GameState {

    enum sceneState {
        MAIN,
        LEVELS,
        SETTINGS,
        DEFAULT
    }

    private Background bg;
    private Animation animation;
    private Stage stage;
    private LevelSelectionMenu levelSelectionMenu;
    private SettingsMenu settingsMenu;
    private Scene mainMenuScene;

    private World world;
    private Texture player;
    private Texture logo;
    private Texture backgroundTexture;
    private List<GameButton> buttons;
    private Vector2 mouseInWorld2D;
    private sceneState menuState;

    public Menu(GameStateManager gsm) {

        super(gsm);

        menuState = sceneState.MAIN;

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

        levelSelectionMenu = new LevelSelectionMenu(cam);
        settingsMenu = new SettingsMenu(cam);
        mainMenuScene = new MainMenu(cam);

        // play button

        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);

        world = new World(new Vector2(0, -9.8f * 5), true);

    }

    private void addParallax(String path) {
        backgroundTexture = new Texture(Gdx.files.internal(PATH + path + ".png"));
        stage = new Stage(new ScreenViewport());
        Array<Texture> textures = new Array<>();
        for (int i = 1; i <= 6; i++) {
            textures.add(new Texture(Gdx.files.internal(PATH + path + i + ".png")));
            textures.get(textures.size - 1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }
        ParallaxBackground parallaxBackground = new ParallaxBackground(textures);
        parallaxBackground.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        parallaxBackground.setSpeed(1f);
        stage.addActor(parallaxBackground);
    }

    @Override
    public void handleInput() {
        switch (menuState) {
            case MAIN:
                handleMainMenuInput();
                break;
            case LEVELS:
                handleLevelsMenuInput();
                break;
            case SETTINGS:
                handleSettingsMenuInput();
                break;
                default:
                    System.out.println("Error with menuState!!!");

        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        world.step(dt / 5, 8, 3);

        mouseInWorld2D.x = Gdx.input.getX();
        mouseInWorld2D.y = Gdx.input.getY();

        animation.update(dt);

        switch (menuState) {
            case MAIN:
                updateMainMenu(dt);
                break;
            case LEVELS:
                updateLevelsMenu(dt);
                break;
            case SETTINGS:
                updateSettingsMenu(dt);
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

        // draw button

        switch (menuState) {
            case MAIN:
                drawMainMenu();
                break;
            case LEVELS:
                drawLevelsMenu();
                break;
            case SETTINGS:
                drawSettingsMenu();
                break;
                default:
                    System.out.println("Error with menuState!");
        }

        // draw player
        sb.begin();
        sb.draw(animation.getFrame(), 146, 151);
        sb.end();
    }

    private void updateMainMenu(float dt) {
        mainMenuScene.update(dt);
    }

    private void updateLevelsMenu(float dt) {
        levelSelectionMenu.update(dt);
    }

    private void updateSettingsMenu(float dt) {
        settingsMenu.update(dt);
    }

    private void drawMainMenu() {
        mainMenuScene.render(sb);
    }

    private void drawLevelsMenu() {
        levelSelectionMenu.render(sb);
    }

    private void drawSettingsMenu() {
        settingsMenu.render(sb);
    }

    private void handleMainMenuInput() {
        if (MyInput.isMouseClicked(Game.settings.SHOOT)) {
            switch (mainMenuScene.getCur_block()) {
                case NEWGAME:
                    menuState = sceneState.LEVELS;
                    break;
                case RESUME:
                    break;
                case SETTINGS:
                    menuState = sceneState.SETTINGS;
                    break;
                case EXIT:
                    Gdx.app.exit();
                    break;
            }
        }
    }

    private void handleLevelsMenuInput() {
        if (MyInput.isMouseClicked(Game.settings.SHOOT)) {
            switch (levelSelectionMenu.getCur_block()) {
                case MAP:
                    gsm.pushState(GameStateManager.State.PLAY, levelSelectionMenu.getSelectedAct(), levelSelectionMenu.getSelectedMap());
                    break;
                case EXIT:
                    menuState = sceneState.MAIN;
                    break;
            }
        }
    }

    private void handleSettingsMenuInput() {
        settingsMenu.handleKey(MyInput.getKeyDown());

        if (MyInput.isMouseClicked(Game.settings.SHOOT)) {
            switch (settingsMenu.getCur_block()) {
                case EXIT:
                    menuState = sceneState.MAIN;
                    break;
                case SAVE:
                    Game.settings.save(B2DVars.PATH + "settings/settings.json");
                    break;
                case NEXT:
                    Game.settings = Game.settings.loadDefault();
                    settingsMenu.updateAllBindsDisplayed();
                    break;
                case LOAD:
                    Game.settings = Game.settings.load(B2DVars.PATH + "settings/settings.json");
                    settingsMenu.updateAllBindsDisplayed();
                    break;
                case SETTINGS:
                    settingsMenu.handleSettingsButtonClick();
                    break;
            }
        }
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        stage.dispose();
    }
}
