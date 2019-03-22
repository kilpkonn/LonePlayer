package ee.taltech.iti0202.gui.game.desktop.handlers.gdx.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class MyInputProcessor extends InputAdapter {

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        if (button == Input.Buttons.LEFT) {
            MouseInput.setMouseDown(true);
        }

        return true;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if (button == Input.Buttons.LEFT) {
            MouseInput.setMouseDown(false);
        }
        return true;
    }

}
