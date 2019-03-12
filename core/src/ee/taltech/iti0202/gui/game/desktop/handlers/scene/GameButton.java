package ee.taltech.iti0202.gui.game.desktop.handlers.scene;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.SCALE;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_WIDTH;

public class GameButton {

    // center at x, y
    private float x;
    private float y;
    private float width;
    private float height;
    private TextureRegion reg;

    private boolean hoverOver;

    private String text;
    private TextureRegion[] font;

    public GameButton(TextureRegion reg, float x, float y) {

        this.reg = reg;
        this.x = x;
        this.y = y;

        width = reg.getRegionWidth();
        height = reg.getRegionHeight();

        font = new TextureRegion[11];
        for (int i = 0; i < 6; i++) {
            font[i] = new TextureRegion(reg, 32 + i * 9, 16, 9, 9);
        }
        for (int i = 0; i < 5; i++) {
            font[i + 6] = new TextureRegion(reg, 32 + i * 9, 25, 9, 9);
        }

    }

    public boolean hoverOver() {
        return hoverOver;
    }

    public void update(Vector2 mousePos) {
        hoverOver = (V_WIDTH - mousePos.x / SCALE >= x - width / 2
                && V_WIDTH - mousePos.x / SCALE <= x + width / 2)
                && (V_HEIGHT - mousePos.y / SCALE >= y - height / 2
                && V_HEIGHT - mousePos.y / SCALE <= y + height / 2);
    }

    public void render(SpriteBatch sb) {

        sb.begin();

        sb.draw(reg, x - width / 2, y - height / 2);

        if (text != null) {
            drawString(sb, text, x, y);
        }

        sb.end();

    }

    private void drawString(SpriteBatch sb, String s, float x, float y) {
        int len = s.length();
        float xo = len * font[0].getRegionWidth() >> 1;
        float yo = font[0].getRegionHeight() >> 1;
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (c == '/') c = 10;
            else if (c >= '0' && c <= '9') c -= '0';
            else continue;
            sb.draw(font[c], x + i * 9 - xo, y - yo);
        }
    }

}
