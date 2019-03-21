package ee.taltech.iti0202.gui.game.desktop.handlers.gdx.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class MyInputProcessor extends InputAdapter {

    public boolean keyDown(int k) {
        MyInput.setKeyDown(Input.Keys.toString(k), true);
        return true;
    }

    public boolean keyUp(int k) {
        MyInput.setKeyDown(Input.Keys.toString(k), false);
        MyInput.setKeyPressed(Input.Keys.toString(k));
        return true;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        if (button == Input.Buttons.LEFT) {
            MyInput.setKeyDown("MOUSE1", true);
        }

        return true;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if (button == Input.Buttons.LEFT) {
            MyInput.setKeyDown("MOUSE1", false);
            MyInput.setKeyPressed("MOUSE1");
        }
        return true;
    }

}
