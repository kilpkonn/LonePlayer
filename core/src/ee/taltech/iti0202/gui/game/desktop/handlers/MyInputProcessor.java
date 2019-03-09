package ee.taltech.iti0202.gui.game.desktop.handlers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class MyInputProcessor extends InputAdapter {

    public boolean keyDown(int k) {
        if (k == Input.Keys.A) {
            MyInput.setKey(MyInput.MOVE_LEFT, true);
        }
        if (k == Input.Keys.D) {
            MyInput.setKey(MyInput.MOVE_RIGHT, true);
        }
        if (k == Input.Keys.SPACE) {
            MyInput.setKey(MyInput.JUMP, true);
        }
        if (k == Input.Keys.S) {
            MyInput.setKey(MyInput.CHANGE_DIMENTION, true);
        }
        if (k == Input.Keys.SHIFT_LEFT) {
            MyInput.setKey(MyInput.MENU, true);
        }

        if (k == Input.Keys.ESCAPE) {
            MyInput.setKey(MyInput.ESC, true);
        }

        return true;
    }

    public boolean keyUp(int k) {
        if (k == Input.Keys.A) {
            MyInput.setKey(MyInput.MOVE_LEFT, false);
        }
        if (k == Input.Keys.D) {
            MyInput.setKey(MyInput.MOVE_RIGHT, false);
        }
        if (k == Input.Keys.SPACE) {
            MyInput.setKey(MyInput.JUMP, false);
        }
        if (k == Input.Keys.S) {
            MyInput.setKey(MyInput.CHANGE_DIMENTION, false);
        }
        if (k == Input.Keys.SHIFT_LEFT) {
            MyInput.setKey(MyInput.MENU, false);
        }

        if (k == Input.Keys.ESCAPE) {
            MyInput.setKey(MyInput.ESC, false);
        }

        return true;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        if (button == Input.Buttons.LEFT) {
            MyInput.setKey(MyInput.SHOOT, true);
        }

        return true;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if (button == Input.Buttons.LEFT) {
            MyInput.setKey(MyInput.SHOOT, false);
        }

        return true;
    }

}
