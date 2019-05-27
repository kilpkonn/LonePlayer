package ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.input.MyInput;

public class TextField extends GameButton {

    private boolean focused = false;

    public TextField(String text, float x, float y, float width, float height) {
        super(text, x, y);
        this.width = width;
        this.height = height;
        setAcceptHover(false);
        setAutoScale(false);
    }

    @Override
    public void update(Vector2 mousePos) {
        super.update(mousePos);

        if (MyInput.isMouseClicked(Game.settings.SHOOT)) {
            if (hoverOver()) {
                MyInput.startListeningText();
                focused = true;
            } else {
                MyInput.stopListeningText();
                focused = false;
            }
        }

        if (!MyInput.getTextInput().equals("")) {
            setText(getText() + MyInput.getTextInput());
            MyInput.startListeningText();
        }

        if (MyInput.getKeyDown() == Input.Keys.BACKSPACE) {
            setText(getText().substring(0, getText().length() - 1));
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setColor(new Color(0.01f, 0.01f, 0.01f, 0.3f));
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        roundedRect(x - 5, y + 5, width, height, 5);
        shapeRenderer.end();
        super.render(sb);
    }
}
