package ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.input;

import com.badlogic.gdx.math.Vector2;

import java.util.HashSet;

public class MyInput {

    private static HashSet<Integer> keysDown = new HashSet<>();
    private static HashSet<Integer> keysPressed = new HashSet<>();

    private static boolean mouseDown;
    private static Vector2 mouseLocation;
    private static boolean mouseDownPrev = true;

    public static void update() {
        for (Integer key : keysPressed) setKeyDown(key, true);
        if (!mouseDown) mouseDownPrev = false;
    }

    public static void setKeyDown(int key, boolean isdown) {
        if (isdown) {
            if (!keysPressed.contains(key)) {
                keysPressed.add(key);
            } else {
                keysDown.add(key);
            }
        } else {
            keysDown.remove(key);
            keysPressed.remove(key);
        }
    }

    public static int getKeyDown() {
        return !keysDown.isEmpty() ? keysDown.iterator().next() : -1;
    }

    public static boolean isDown(Integer key) {
        if (key == -1) return !keysDown.isEmpty();
        return keysDown.contains(key);
    }

    public static boolean isPressed(Integer key) {
        return keysPressed.contains(key) && !keysDown.contains(key);
    }

    public static void setMouseDown(boolean isdown) {
        mouseDownPrev = mouseDown;
        mouseDown = isdown;
    }

    public static void setMouseLoc(int x, int y) {
        mouseLocation = new Vector2(x, y);
    }

    public static Vector2 getMouseLocation() {
        return mouseLocation;
    }

    public static boolean isMouseDown(int key) {
        return mouseDown;
    }

    public static boolean isMouseClicked(int btn) {
        return mouseDownPrev && !mouseDown;
    }
}
