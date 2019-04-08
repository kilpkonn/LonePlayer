package ee.taltech.iti0202.gui.game.desktop.handlers.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import ee.taltech.iti0202.gui.game.desktop.handlers.scene.GameButton;
import ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.desktop.states.Play;

public class Hud {
    protected OrthographicCamera cam;
    protected Stage stage;
    protected GameButton health;
    protected GameButton fps;
    protected Play play;

    public Hud(OrthographicCamera cam, Play play) {
        this.cam = cam;
        stage = new Stage(new ScreenViewport());
        this.play = play;

        health = new GameButton("100hp", B2DVars.V_WIDTH - 150, B2DVars.V_HEIGHT - 50);
        fps = new GameButton("0", 100, B2DVars.V_HEIGHT - 50);
    }

    public void update(float dt) {
        int hp = play.getPlayer().getHealth();
        health.setText(hp + "hp");
        health.setColor(new Color((100 - hp) / 70f, hp / 70f, 0, 1));

        int currentFPS = Gdx.graphics.getFramesPerSecond();
        fps.setText(Integer.toString(currentFPS));
        fps.setColor(new Color((60 - currentFPS) / 50f, currentFPS / 50f, 0, 1));
    }

    public void render(SpriteBatch sb){
        cam.update();
        sb.setProjectionMatrix(cam.combined);
        stage.act();
        stage.draw();

        health.render(sb);
        fps.render(sb);
    }
}
