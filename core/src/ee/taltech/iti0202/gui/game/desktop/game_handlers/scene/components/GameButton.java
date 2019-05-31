package ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.input.MyInput;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.*;

public class GameButton implements Disposable {

    public float x;
    public float y;
    public float width;
    public float height;

    private float lineLengthMultiplier = 1;
    private boolean hoverOver;
    private boolean acceptHover = true;
    private boolean autoScale = true;
    private Runnable onAction;
    private Runnable onHover;
    protected ShapeRenderer shapeRenderer = new ShapeRenderer();

    private BitmapFont font;

    private String text;

    public GameButton(String text, float x, float y) {
        this.x = x;
        this.y = y;
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 32;
        setFontParameters(parameter);
        font.setColor(new Color(0.47f, 1f, 1f, 1));
        setText(text);
    }

    public boolean hoverOver() {
        return hoverOver;
    }

    public void update(Vector2 mousePos) {
        hoverOver =
                (mousePos.x / SCALE >= x && mousePos.x / SCALE <= x + width)
                        && (V_HEIGHT - mousePos.y / SCALE >= y - height
                                && V_HEIGHT - mousePos.y / SCALE <= y);
        if (hoverOver && onHover != null) {
            onHover.run();
        }
        if (MyInput.isMouseClicked(Game.settings.SHOOT) && hoverOver) {
            if (onAction != null) {
                playSoundOnce("sounds/menu_click.wav", 0.5f);
                onAction.run();
            }
        }
    }

    public void setColor(Color color) {
        font.setColor(color);
    }

    public void render(SpriteBatch sb) {
        if (acceptHover && hoverOver) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rectLine(
                    x - 100 * lineLengthMultiplier, y - height / 2, x - 5, y - height / 2, 2, Color.MAGENTA, Color.CYAN);
            shapeRenderer.rectLine(
                    x + 420 * lineLengthMultiplier,
                    y - height / 2,
                    x + width + 10,
                    y - height / 2,
                    2,
                    Color.MAGENTA,
                    Color.CYAN);
            shapeRenderer.end();
        }
        sb.begin();
        font.draw(sb, text, x, y);
        sb.end();
    }

    public void setAcceptHover(boolean acceptHover) {
        this.acceptHover = acceptHover;
    }

    public void setFontParameters(FreeTypeFontGenerator.FreeTypeFontParameter parameters) {
        FreeTypeFontGenerator generator =
                new FreeTypeFontGenerator(Gdx.files.local(PATH + "fonts/bullfrog.ttf"));
        font = generator.generateFont(parameters);
        generator.dispose();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (autoScale) {
            GlyphLayout layout = new GlyphLayout(); // dont do this every frame!
            layout.setText(font, text);
            width = layout.width; // contains the width of the current set text
            height = layout.height; // contains the height of the current set text
        }
        this.text = text;
    }

    public void setLineLengthMultiplier(float multiplier) {
        this.lineLengthMultiplier = multiplier;
    }

    public void setOnAction(Runnable onAction) {
        this.onAction = onAction;
    }

    public void setOnHover(Runnable onHover) {
        this.onHover = onHover;
    }

    @Override
    public void dispose() {
        font.dispose();
        System.gc();
    }

    protected void roundedRect(float x, float y, float width, float height, float radius){
        // Central rectangle
        shapeRenderer.rect(x + radius, y - height + radius, width - 2*radius, height - 2*radius);

        // Four side rectangles, in clockwise order
        shapeRenderer.rect(x + radius, y - height, width - 2*radius, radius);
        shapeRenderer.rect(x + width - radius, y - height + radius, radius, height - 2*radius);
        shapeRenderer.rect(x + radius, y - height + height - radius, width - 2*radius, radius);
        shapeRenderer.rect(x, y - height + radius, radius, height - 2*radius);

        // Four arches, clockwise too
        shapeRenderer.arc(x + radius, y - height + radius, radius, 180f, 90f);
        shapeRenderer.arc(x + width - radius, y - height + radius, radius, 270f, 90f);
        shapeRenderer.arc(x + width - radius, y - radius, radius, 0f, 90f);
        shapeRenderer.arc(x + radius, y - radius, radius, 90f, 90f);
    }

    public void setAutoScale(boolean autoScale) {
        this.autoScale = autoScale;
    }

    protected void playSoundOnce(String source, float db) {
        try {
            Sound sound = Gdx.audio.newSound(Gdx.files.local(PATH + source));
            sound.play(db);
        } catch (Exception e) {
            System.out.println("Sound couldn't be located.");
        }
    }
}
