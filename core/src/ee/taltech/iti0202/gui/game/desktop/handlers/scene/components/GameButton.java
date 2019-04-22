package ee.taltech.iti0202.gui.game.desktop.handlers.scene.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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

    private BitmapFont font; // = new BitmapFont(Gdx.files.internal(PATH + "fonts/bullfrog.fnt"), false);

    private String text;

    public GameButton(String text, float x, float y) {
        this.x = x;
        this.y = y;
        setSize(32);
        font.setColor(new Color(0.47f, 1f, 1f, 1));
        setText(text);
    }

    public boolean hoverOver() {
        return hoverOver;
    }

    public void update(Vector2 mousePos) {
        hoverOver = (mousePos.x / SCALE >= x
                && mousePos.x / SCALE <= x + width)
                && (V_HEIGHT - mousePos.y / SCALE >= y - height
                && V_HEIGHT - mousePos.y / SCALE <= y);
    }

    public void setColor(Color color) {
        font.setColor(color);
    }

    public void render(SpriteBatch sb) {
        if (acceptHover && hoverOver) {
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rectLine(x - 100, y - height / 2, x - 5, y - height / 2, 2, Color.MAGENTA, Color.CYAN);
            shapeRenderer.rectLine(x + 420, y - height / 2, x + width + 10, y - height / 2, 2, Color.MAGENTA, Color.CYAN);
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
        GlyphLayout layout = new GlyphLayout(); //dont do this every frame!
        layout.setText(font, text);
        width = layout.width;// contains the width of the current set text
        height = layout.height; // contains the height of the current set text
        this.text = text;
    }

    public void setSize(int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(PATH + "fonts/bullfrog.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        font = generator.generateFont(parameter);
        generator.dispose();
    }

    public String getText() {
        return text;
    }
}

