package ee.taltech.iti0202.gui.game.desktop.handlers.gdx.input;

public class MyInput {

    public static boolean[] keys;
    public static boolean[] pkeys;

    public static final int NUM_KEYS = 7;
    public static final int MOVE_LEFT = 0;
    public static final int MOVE_RIGHT = 1;
    public static final int JUMP = 2;
    public static final int CHANGE_DIMENTION = 3;
    public static final int MENU = 4;
    public static final int SHOOT = 5;
    public static final int ESC = 6;

    static {
        keys = new boolean[NUM_KEYS];
        pkeys = new boolean[NUM_KEYS];
    }

    public static void update() {
        for (int i = 0; i < NUM_KEYS; i++) {
            pkeys[i] = keys[i];
        }
    }

    public static void setKey(int i, boolean b) {
        keys[i] = b;
    }

    public static boolean isDown(int i) {
        return keys[i];
    }

    public static boolean isPressed(int i) {

        return keys[i] && !pkeys[i];
    }
}
