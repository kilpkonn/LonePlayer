package ee.taltech.iti0202.gui.game.desktop.handlers.gdx.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class MyInputProcessor extends InputAdapter {

    public boolean keyDown(int key) {
        MyInput.setKeyDown(key);
        return true;
    }

    public boolean keyUp(int key) {
        if (key == MyInput.getKeyDown()) MyInput.setKeyDown(-1);
        return true;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        if (button == Input.Buttons.LEFT) {
            MyInput.setMouseDown(true);
        }

        return true;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if (button == Input.Buttons.LEFT) {
            MyInput.setMouseDown(false);
        }
        return true;
    }

}
