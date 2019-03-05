package ee.taltech.iti0202.gui.game.desktop.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.handlers.Animation;
import ee.taltech.iti0202.gui.game.desktop.handlers.GameButton;
import ee.taltech.iti0202.gui.game.desktop.handlers.GameStateManager;
import ee.taltech.iti0202.gui.game.desktop.handlers.MyInput;
import ee.taltech.iti0202.gui.game.desktop.handlers.ParallaxBackground;

import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.MAIN_SCREENS;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.PATH;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.SCALE;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.V_WIDTH;

public class Menu extends GameState {


    private ee.taltech.iti0202.gui.game.desktop.handlers.Background bg;
    private Animation animation;
    private Stage stage;
    private GameButton newGameButtonActive;
    private GameButton newGameButtonInactive;
    private GameButton loadGameButtonActive;
    private GameButton loadGameButtonInactive;
    private GameButton settingsButtonActive;
    private GameButton settinsButtonInactive;
    private GameButton exitButtonActive;
    private GameButton exitButtonInactive;

    private World world;
    private Texture newGameTextureActive;
    private Texture newGameTextureInactive;
    private Texture loadGameTextureActive;
    private Texture loadGameTextureInactive;
    private Texture settingsTextureActive;
    private Texture settinsTextureInactive;
    private Texture exitTextureActive;
    private Texture exitTextureInactive;
    private Texture player;
    private Texture logo;
    private Texture backgroundTexture;

    private Vector2 mouseInWorld2D;
    private block cur_block = block.DEFAULT;

    public Menu(GameStateManager gsm) {

        super(gsm);

        // get button textures
        newGameTextureActive = Game.res.getTexture("newGameTextureActive");
        newGameTextureInactive = Game.res.getTexture("newGameTextureInactive");
        loadGameTextureActive = Game.res.getTexture("loadGameTextureActive");
        loadGameTextureInactive = Game.res.getTexture("loadGameTextureInactive");
        settingsTextureActive = Game.res.getTexture("settingsTextureActive");
        settinsTextureInactive = Game.res.getTexture("settingsTextureInactive");
        exitTextureActive = Game.res.getTexture("exitTextureActive");
        exitTextureInactive = Game.res.getTexture("exitTextureInactive");
        player = Game.res.getTexture("Player");
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
        SpriteBatch sb = new SpriteBatch();

        // play button
        newGameButtonActive = new GameButton(new TextureRegion(newGameTextureActive, 0, 32, V_WIDTH, 32), V_WIDTH / 2f, V_HEIGHT / 1.5f, cam);
        newGameButtonInactive = new GameButton(new TextureRegion(newGameTextureInactive, 0, 0, V_WIDTH, 32), V_WIDTH / 2f, V_HEIGHT / 1.5f, cam);
        loadGameButtonActive = new GameButton(new TextureRegion(loadGameTextureActive, 0, 96, V_WIDTH, 32), V_WIDTH / 2f, V_HEIGHT / 1.5f - 40, cam);
        loadGameButtonInactive = new GameButton(new TextureRegion(loadGameTextureInactive, 0, 64, V_WIDTH, 32), V_WIDTH / 2f, V_HEIGHT / 1.5f - 40, cam);
        settingsButtonActive = new GameButton(new TextureRegion(settingsTextureActive, 0, 160, V_WIDTH, 32), V_WIDTH / 2f, V_HEIGHT / 1.5f - 80, cam);
        settinsButtonInactive = new GameButton(new TextureRegion(settinsTextureInactive, 0, 128, V_WIDTH, 32), V_WIDTH / 2f, V_HEIGHT / 1.5f - 80, cam);
        exitButtonActive = new GameButton(new TextureRegion(exitTextureActive, 0, 224, V_WIDTH, 32), V_WIDTH / 2f, V_HEIGHT / 1.5f - 120, cam);
        exitButtonInactive = new GameButton(new TextureRegion(exitTextureInactive, 0, 192, V_WIDTH, 32), V_WIDTH / 2f, V_HEIGHT / 1.5f - 120, cam);

        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);

        world = new World(new Vector2(0, -9.8f * 5), true);
        //world = new World(new Vector2(0, 0), true);

    }

    private void addParallax(String path) {
        backgroundTexture = new Texture(Gdx.files.internal(PATH + path + ".png"));
        stage = new Stage(new ScreenViewport());
        Array<Texture> textures = new Array<Texture>();
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
    public void handleInput() {   // TODO: MAKE A ACT SELECTOR
        if (MyInput.isPressed(MyInput.SHOOT) && cur_block == block.NEW_GAME) {
            gsm.pushState(GameStateManager.State.PLAY, 0, 0);
        }

        if (MyInput.isPressed(MyInput.SHOOT) && cur_block == block.EXIT) {
            Gdx.app.exit();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        world.step(dt / 5, 8, 3);

        mouseInWorld2D.x = Gdx.input.getX();
        mouseInWorld2D.y = Gdx.input.getY();

        animation.update(dt);

        newGameButtonInactive.update(mouseInWorld2D);
        loadGameButtonInactive.update(mouseInWorld2D);
        settinsButtonInactive.update(mouseInWorld2D);
        exitButtonInactive.update(mouseInWorld2D);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //System.out.println(mouseInWorld2D);

        sb.setProjectionMatrix(cam.combined);

        // draw background
        sb.begin();
        sb.draw(backgroundTexture, 0, 0);
        sb.end();
        stage.act();
        stage.draw();

        // draw button
        if (mouseInWorld2D.x < V_WIDTH * SCALE / 3) {

            if (newGameButtonInactive.hoverOver()) {
                cur_block = block.NEW_GAME;
                newGameButtonActive.render(sb);
            } else {
                newGameButtonInactive.render(sb);
            }

            if (loadGameButtonInactive.hoverOver()) {
                cur_block = block.lOAD_GAME;
                loadGameButtonActive.render(sb);
            } else {
                loadGameButtonInactive.render(sb);
            }

            if (settinsButtonInactive.hoverOver()) {
                cur_block = block.SETTINGS;
                settingsButtonActive.render(sb);
            } else {
                settinsButtonInactive.render(sb);
            }

            if (exitButtonInactive.hoverOver()) {
                cur_block = block.EXIT;
                exitButtonActive.render(sb);
            } else {
                exitButtonInactive.render(sb);
            }

        } else {
            newGameButtonInactive.render(sb);
            loadGameButtonInactive.render(sb);
            settinsButtonInactive.render(sb);
            exitButtonInactive.render(sb);
        }


        // draw player
        sb.begin();
        sb.draw(animation.getFrame(), 146, 151);
        sb.end();

    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        stage.dispose();
    }

    enum block {
        NEW_GAME,
        lOAD_GAME,
        SETTINGS,
        EXIT,
        DEFAULT
    }
}
