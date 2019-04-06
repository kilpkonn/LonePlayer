package ee.taltech.iti0202.gui.game.desktop.handlers.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import ee.taltech.iti0202.gui.game.desktop.handlers.scene.GameButton;
import ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.desktop.states.Play;

public class Hud {
    protected OrthographicCamera cam;
    protected Stage stage;
    protected GameButton health;
    protected Play play;

    public Hud(OrthographicCamera cam, Play play) {
        this.cam = cam;
        stage = new Stage(new ScreenViewport());
        this.play = play;

        health = new GameButton("100hp", B2DVars.V_WIDTH - 150, B2DVars.V_HEIGHT - 50);
    }

    public void update(float dt) {
        int hp = play.getPlayer().getHealth();
        health.setText(hp + "hp");
        health.setColor(new Color((100 - hp) / 70f, hp / 70f, 0, 1));
    }

    public void render(SpriteBatch sb){
        cam.update();
        sb.setProjectionMatrix(cam.combined);
        stage.act();
        stage.draw();

        health.render(sb);
    }
}
