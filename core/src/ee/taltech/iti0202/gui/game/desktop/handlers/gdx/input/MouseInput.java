package ee.taltech.iti0202.gui.game.desktop.handlers.gdx.input;


public class MouseInput {

    private static boolean mouseDown;
    private static boolean mouseDownPrev = true;

    public static void update() {
        if (mouseDownPrev && !mouseDown) {
            mouseDownPrev = false;
        }
    }

    public static void setMouseDown(boolean isdown) {
        mouseDownPrev = mouseDown;
        mouseDown = isdown;
    }

    public static boolean isDown(int key) {
        return mouseDown;
    }

    public static boolean isPressed(int btn) {
        return mouseDownPrev && !mouseDown;
    }
}
