package ee.taltech.iti0202.gui.game.desktop.handlers.gdx.input;


public class MyInput {

    private static boolean mouseDown;
    private static boolean mouseDownPrev = true;
    private static int keyDown;

    public static void update() {
        if (mouseDownPrev && !mouseDown) {
            mouseDownPrev = false;
        }
    }

    public static void setKeyDown(int keyDown) {
        MyInput.keyDown = keyDown;
    }

    public static int getKeyDown() {
        return keyDown;
    }

    public static void setMouseDown(boolean isdown) {
        mouseDownPrev = mouseDown;
        mouseDown = isdown;
    }

    public static boolean isMouseDown(int key) {
        return mouseDown;
    }

    public static boolean isMouseClicked(int btn) {
        return mouseDownPrev && !mouseDown;
    }
}
