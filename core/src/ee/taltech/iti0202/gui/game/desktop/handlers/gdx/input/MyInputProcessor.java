package ee.taltech.iti0202.gui.game.desktop.handlers.gdx.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class MyInputProcessor extends InputAdapter {

    @Override
    public boolean keyDown(int k) {
        MyInput.setKeyDown(k, true);
        return true;
    }

    @Override
    public boolean keyUp(int k) {
        MyInput.setKeyDown(k, false);
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        if (button == Input.Buttons.LEFT) {
            MyInput.setMouseDown(true);
            MyInput.setMouseLoc(screenX, screenY);
        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if (button == Input.Buttons.LEFT) {
            MyInput.setMouseDown(false);
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        MyInput.setMouseLoc(screenX, screenY);
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        //MyInput.setMouseLoc(screenX, screenY);
        return true;
    }
}
