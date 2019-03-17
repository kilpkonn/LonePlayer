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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.animations.Animation;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.GameButton;
import ee.taltech.iti0202.gui.game.desktop.handlers.gdx.GameStateManager;
import ee.taltech.iti0202.gui.game.desktop.handlers.gdx.input.MyInput;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.animations.Background;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.animations.ParallaxBackground;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.MAIN_SCREENS;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PATH;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_WIDTH;

public class Menu extends GameState {

    enum block {
        NEW_GAME,
        lOAD_GAME,
        SETTINGS,
        EXIT,
        DEFAULT
    }

    private Background bg;
    private Animation animation;
    private Stage stage;
    private GameButton newGameButtonActive;
    private GameButton loadGameButtonActive;
    private GameButton settingsButtonActive;
    private GameButton exitButtonActive;

    private World world;
    private Texture player;
    private Texture logo;
    private Texture backgroundTexture;
    private List<List<GameButton>> buttons;
    private HashMap<GameButton, block> buttonType;
    private Vector2 mouseInWorld2D;
    private block cur_block = block.DEFAULT;

    public Menu(GameStateManager gsm) {

        super(gsm);

        // get textures
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

        // play button
        newGameButtonActive = new GameButton(new TextureRegion(Game.res.getTexture("buttons"), 0, 32, V_WIDTH, 32), V_WIDTH / 2f, V_HEIGHT / 1.5f);
        GameButton newGameButtonInactive = new GameButton(new TextureRegion(Game.res.getTexture("buttons"), 0, 0, V_WIDTH, 32), V_WIDTH / 2f, V_HEIGHT / 1.5f);
        loadGameButtonActive = new GameButton(new TextureRegion(Game.res.getTexture("buttons"), 0, 96, V_WIDTH, 32), V_WIDTH / 2f, V_HEIGHT / 1.5f - 40);
        GameButton loadGameButtonInactive = new GameButton(new TextureRegion(Game.res.getTexture("buttons"), 0, 64, V_WIDTH, 32), V_WIDTH / 2f, V_HEIGHT / 1.5f - 40);
        settingsButtonActive = new GameButton(new TextureRegion(Game.res.getTexture("buttons"), 0, 160, V_WIDTH, 32), V_WIDTH / 2f, V_HEIGHT / 1.5f - 80);
        GameButton settinsButtonInactive = new GameButton(new TextureRegion(Game.res.getTexture("buttons"), 0, 128, V_WIDTH, 32), V_WIDTH / 2f, V_HEIGHT / 1.5f - 80);
        exitButtonActive = new GameButton(new TextureRegion(Game.res.getTexture("buttons"), 0, 224, V_WIDTH, 32), V_WIDTH / 2f, V_HEIGHT / 1.5f - 120);
        GameButton exitButtonInactive = new GameButton(new TextureRegion(Game.res.getTexture("buttons"), 0, 192, V_WIDTH, 32), V_WIDTH / 2f, V_HEIGHT / 1.5f - 120);

        buttons = new ArrayList<>(Arrays.asList(Arrays.asList(newGameButtonActive, newGameButtonInactive), Arrays.asList(loadGameButtonActive, loadGameButtonInactive),
                Arrays.asList(settingsButtonActive, settinsButtonInactive), Arrays.asList(exitButtonActive, exitButtonInactive)));

        buttonType = new HashMap<GameButton, block>() {{
            put(newGameButtonActive, block.NEW_GAME);
            put(loadGameButtonActive, block.lOAD_GAME);
            put(settingsButtonActive, block.SETTINGS);
            put(exitButtonActive, block.EXIT);
        }};

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
    public void handleInput() {   // TODO: MAKE A ACT SELECTOR
        if (MyInput.isPressed(MyInput.SHOOT) && cur_block == block.NEW_GAME) {
            gsm.pushState(GameStateManager.State.PLAY, 0, 1);
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

        for (List<GameButton> button : buttons) button.get(1).update(mouseInWorld2D);
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


        for (List<GameButton> button : buttons) {
            if (button.get(1).hoverOver()) {
                cur_block = buttonType.get(button.get(0));
                button.get(0).render(sb);
            } else {
                button.get(1).render(sb);
            }
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
}
