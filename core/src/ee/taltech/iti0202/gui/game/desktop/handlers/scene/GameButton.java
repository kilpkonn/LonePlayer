package ee.taltech.iti0202.gui.game.desktop.handlers.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PATH;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.SCALE;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_WIDTH;

public class GameButton {

    // center at x, y
    public float x;
    public float y;
    public float width;
    public float height;

    private boolean hoverOver;

    private String text;

    public GameButton(String text, float x, float y) {

        this.text = text;
        this.x = x;
        this.y = y;

        width = text.length() * 35;
        height = 40;
    }

    public boolean hoverOver() {
        return hoverOver;
    }

    public void update(Vector2 mousePos) {
        hoverOver = (V_WIDTH - mousePos.x / SCALE >= x - width
                && V_WIDTH - mousePos.x / SCALE <= x)
                && (V_HEIGHT - mousePos.y / SCALE >= y - height
                && V_HEIGHT - mousePos.y / SCALE <= y);
    }

    public void render(SpriteBatch sb) {
        sb.begin();

        //sb.draw(reg, x - width / 2, y - height / 2);

        if (text != null) {
            drawString(sb, text, x, y);
        }

        sb.end();

    }

    private void drawString(SpriteBatch sb, String s, float x, float y) {
        BitmapFont bfont = new BitmapFont(Gdx.files.internal(PATH + "fonts/comics.fnt"), false);
        bfont.draw(sb, s, x, y);
    }

}

