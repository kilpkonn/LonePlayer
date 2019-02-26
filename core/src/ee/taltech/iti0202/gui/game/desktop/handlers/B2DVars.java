package ee.taltech.iti0202.gui.game.desktop.handlers;

public class B2DVars {

    // pixel per meter
    public static final float PPM = 100;
    public static final float GRAVITY = -9.81f;
    public static final float MAX_SPEED = 2.5f;
    public static final float FRICTION = 0.4f;
    public static final float PLAYER_SPEED = 8;
    public static final float PLAYER_DASH_FORCE_UP = 250;
    public static final float PLAYER_DASH_FORCE_SIDE = 200;
    public static final float ShiftX = 1.60f;
    public static final float ShiftY = 2.00f;
    public static final int V_WIDTH = 960;
    public static final int V_HEIGHT = 540;

    // category bits
    public static final short BIT_ALL = -1;
    public static final short BIT_PLAYER = 255;
    public static final short TERRA_SQUARES = 4;
    public static final short TERRA_OBJECTS = 8;
    public static final short BACKGROUND = 256;

    // corner coords
    public static final int[] SQUARE_CORNERS = {-1, -1, -1, 1, 1, 1, 1, -1};
    public static final int[] TRIANGLE_BOTTOM_LEFT = {-1, -1, -1, 1, 1, -1, 1, -1};
    public static final int[] TRIANGLE_BOTTOM_RIGHT = {-1, -1, -1, -1, 1, 1, 1, -1};
    public static final int[] TRIANGLE_TOP_RIGHT = {-1, 1, -1, 1, 1, 1, 1, -1};
    public static final int[] TRIANGLE_TOP_LEFT = {-1, -1, -1, 1, 1, 1, 1, 1};


}
