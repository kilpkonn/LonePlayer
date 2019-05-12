package ee.taltech.iti0202.gui.game.desktop.game_handlers.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.components.GameButton;

import java.util.HashMap;
import java.util.HashSet;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.PATH;

public abstract class Scene {
    protected Stage stage;
    protected OrthographicCamera hudCam;
    protected Game game;

    protected Vector2 mouseInWorld2D;
    protected HashSet<GameButton> buttons;
    protected String act, map;
    protected HashMap<GameButton, Boolean> played = new HashMap<>();

    public Scene(OrthographicCamera cam) {
        this("", "", cam);
    }

    public Scene(String act, String map, OrthographicCamera cam) {
        this.hudCam = cam;
        this.act = act;
        this.map = map;
        stage = new Stage(new ScreenViewport());
        mouseInWorld2D = new Vector2();
    }

    public abstract void handleInput();

    public void update(float dt) {
        mouseInWorld2D.x = Gdx.input.getX();
        mouseInWorld2D.y = Gdx.input.getY();

        for (GameButton button : buttons) button.update(mouseInWorld2D);
    }

    public void render(SpriteBatch sb) {

        hudCam.update();
        sb.setProjectionMatrix(hudCam.combined);
        stage.act();
        stage.draw();

        for (GameButton button : buttons) {
            if (button.hoverOver()) {
                if (!played.get(button)) {
                    played.put(button, true);
                    playSoundOnce("sounds/menu_hover.wav", 1.0f);
                }
                updateCurrentBlock(button);
            } else {
                played.put(button, false);
            }
            button.render(sb);
        }
    }

    void playSoundOnce(String source, float db) {
        try {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal(PATH + source));
            sound.play(db);
        } catch (Exception e) {
            System.out.println("Sound couldn't be located.");
        }
    }

    protected abstract void updateCurrentBlock(GameButton btn);
}
