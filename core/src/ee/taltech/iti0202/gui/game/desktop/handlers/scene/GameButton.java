package ee.taltech.iti0202.gui.game.desktop.handlers.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PATH;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.SCALE;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_HEIGHT;

public class GameButton {

    // center at x, y
    public float x;
    public float y;
    public float width;
    public float height;

    private boolean hoverOver;
    private boolean acceptHover = true;

    private BitmapFont font = new BitmapFont(Gdx.files.internal(PATH + "fonts/bullfrog.fnt"), false);

    private String text;

    public GameButton(String text, float x, float y) {
        this.x = x;
        this.y = y;

        setText(text);
        height = 36;
    }

    public boolean hoverOver() {
        return hoverOver;
    }

    public void update(Vector2 mousePos) {
        hoverOver = acceptHover && (mousePos.x / SCALE >= x
                && mousePos.x / SCALE <= x + width)
                && (V_HEIGHT - mousePos.y / SCALE >= y - height
                && V_HEIGHT - mousePos.y / SCALE <= y);
    }

    public void render(SpriteBatch sb) {
        if (hoverOver) {
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rectLine(x - 100, y - height / 2 - 10, x - 5, y - height / 2 - 10, 2, Color.MAGENTA, Color.CYAN);
            shapeRenderer.rectLine(x + 400, y - height / 2 - 10, x + width + 5, y - height / 2 - 10, 2, Color.MAGENTA, Color.CYAN);
            shapeRenderer.end();
        }
        sb.begin();
        font.draw(sb, text, x, y);
        sb.end();
    }

    public void setAcceptHover(boolean acceptHover) {
        this.acceptHover = acceptHover;
    }

    public void setText(String text) {
        this.text = text;
        width = text.length() * 20;
    }

    public String getText() {
        return text;
    }
}

