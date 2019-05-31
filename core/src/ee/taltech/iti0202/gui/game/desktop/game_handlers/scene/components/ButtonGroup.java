package ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashSet;
import java.util.Set;

public class ButtonGroup implements Disposable {

    private Set<GameButton> buttons = new HashSet<>();

    public void update(Vector2 mousePos) {
        for (GameButton btn : buttons) {
            btn.update(mousePos);
        }
    }

    public void render(SpriteBatch sb) {
        for (GameButton btn : buttons) {
            btn.render(sb);
        }
    }

    public void setAcceptHover(boolean acceptHover) {
        for (GameButton btn : buttons) {
            btn.setAcceptHover(acceptHover);
        }
    }

    public void addButton(GameButton btn) {
        buttons.add(btn);
    }

    public void removeButton(GameButton btn) {
        buttons.remove(btn);
    }

    public void clearButtons() {
        buttons.clear();
    }

    public Set<GameButton> getButtons() {
        return buttons;
    }

    @Override
    public void dispose() {
        for (GameButton btn : buttons) {
            btn.dispose();
        }
        System.gc();
    }
}
