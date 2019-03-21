package ee.taltech.iti0202.gui.game.desktop.handlers.gdx.input;

import java.util.HashSet;

public class MyInput {

    private static HashSet<String> keysDown = new HashSet<>();
    private static HashSet<String> keysPressed = new HashSet<>();

    public static void update() {
        //keysDown.clear();
        keysPressed.clear();
    }

    public static void setKeyDown(String key, boolean isdown) {
        if (isdown)
            keysDown.add(key);
        else
            keysDown.remove(key);
    }

    public static void setKeyPressed(String key) {
        keysPressed.add(key);
    }

    public static boolean isDown(String key) {
        return keysDown.contains(key);
    }

    public static boolean isPressed(String key) {
        return keysPressed.contains(key);
    }
}
