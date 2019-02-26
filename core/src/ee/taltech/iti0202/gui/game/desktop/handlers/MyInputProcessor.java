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
        if (k == Input.Keys.K) {
            MyInput.setKey(MyInput.SHOOT_LEFT, true);
        }
        if (k == Input.Keys.L) {
            MyInput.setKey(MyInput.SHOOT_RIGHT, true);
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
        if (k == Input.Keys.K) {
            MyInput.setKey(MyInput.SHOOT_LEFT, false);
        }
        if (k == Input.Keys.L) {
            MyInput.setKey(MyInput.SHOOT_RIGHT, false);
        }
        return true;
    }

}
