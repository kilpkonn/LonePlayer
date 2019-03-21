package ee.taltech.iti0202.gui.game.desktop.handlers.gdx.input;

import java.util.HashSet;

public class MyInput {

    private static HashSet<String> keysDown = new HashSet<>();
    private static HashSet<String> keysPressed = new HashSet<>();

    public static void update() {
        for (String key : keysPressed) setKeyDown(key, true);
    }

    public static void setKeyDown(String key, boolean isdown) {
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

    public static boolean isDown(String key) {
        return keysDown.contains(key);
    }

    public static boolean isPressed(String key) {
        return keysPressed.contains(key) && !keysDown.contains(key);
    }
}
