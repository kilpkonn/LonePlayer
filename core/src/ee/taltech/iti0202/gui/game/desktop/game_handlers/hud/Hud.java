package ee.taltech.iti0202.gui.game.desktop.game_handlers.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.components.GameButton;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.desktop.states.Play;

public class Hud {
    protected OrthographicCamera cam;
    protected Stage stage;
    protected GameButton health;
    protected GameButton fps;
    protected GameButton time;

    private boolean gameFadeDone = false;
    private boolean gameFadeOut = false;
    private float currentMenuFade;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    protected int hp;
    private float playTime;

    public Hud(OrthographicCamera cam) {
        this.cam = cam;
        stage = new Stage(new ScreenViewport());   // Ist this needed ?!?

        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 64;
        fontParameter.borderColor = Color.GRAY;
        fontParameter.renderCount = 3;
        fontParameter.borderWidth = 1f;

        health = new GameButton("100hp", B2DVars.V_WIDTH - 250, B2DVars.V_HEIGHT - 50);
        fps = new GameButton("0", 100, B2DVars.V_HEIGHT - 50);
        time = new GameButton("0s", B2DVars.V_WIDTH / 2f - 60, B2DVars.V_HEIGHT - 50);

        health.setFontParameters(fontParameter);
        fps.setFontParameters(fontParameter);

        fontParameter.color = Color.CYAN;
        time.setFontParameters(fontParameter);
    }

    public void update(float dt) {
        health.setText(hp + "hp");
        health.setColor(new Color((100 - hp) / 70f, hp / 70f, 0, 1));

        int currentFPS = Gdx.graphics.getFramesPerSecond();
        fps.setText(Integer.toString(currentFPS));
        int prefFPS = (int) (Game.settings.MAX_FPS * 0.8);
        fps.setColor(
                new Color(
                        (float) (prefFPS - currentFPS) / prefFPS,
                        (float) currentFPS / prefFPS,
                        0,
                        1));

        time.setText(Math.round(playTime * 10) / 10f + "s");
        updateGameFade(dt);
    }

    public void render(SpriteBatch sb) {
        cam.update();
        sb.setProjectionMatrix(cam.combined);
        stage.act();
        stage.draw();

        health.render(sb);
        if (Game.settings.SHOW_FPS) fps.render(sb);
        time.render(sb);

        if (currentMenuFade > 0) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0, 0, 0, currentMenuFade);
            shapeRenderer.rect(0, 0, B2DVars.V_WIDTH, B2DVars.V_HEIGHT);
            shapeRenderer.end();
        }
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setGameFade(boolean fadeOut) {
        if (fadeOut != gameFadeOut) {
            gameFadeOut = fadeOut;
            gameFadeDone = false;
        }
    }

    public void setPlayTime(float playTime) {
        this.playTime = playTime;
    }

    public void updateGameFade(float dt) {
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
        }
    }

    public void dispose() {
        health.dispose();
        fps.dispose();
        time.dispose();
        shapeRenderer.dispose();
    }
}
