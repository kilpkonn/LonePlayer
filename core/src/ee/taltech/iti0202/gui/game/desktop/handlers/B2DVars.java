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
    public static final int V_WIDTH = 1920;
    public static final int V_HEIGHT = 1080;
    public static final int SCALE = 1;

    // map variables
    public static final String TYPE = "1";
    public static final String BOSS = "2";
    public static final String CORNER_LOCATION = "2";

    // category bits
    public static final short BIT_ALL = -1;
    public static final short BIT_PLAYER = 255;
    public static final short TERRA_SQUARES = 4;
    public static final short TERRA_OBJECTS = 8;
    public static final short BACKGROUND = 256;
    public static final short NONE = 0;

    // corner coords
    public static final int[] SQUARE_CORNERS = {-1, -1, -1, 1, 1, 1, 1, -1};
    public static final int[] TRIANGLE_BOTTOM_LEFT = {-1, -1, -1, 1, 1, -1, 1, -1};
    public static final int[] TRIANGLE_BOTTOM_RIGHT = {-1, -1, -1, -1, 1, 1, 1, -1};
    public static final int[] TRIANGLE_TOP_RIGHT = {-1, 1, -1, 1, 1, 1, 1, -1};
    public static final int[] TRIANGLE_TOP_LEFT = {-1, -1, -1, 1, 1, 1, 1, 1};

    // main screen sections
    public static final String[] MAIN_SCREENS = {
            "android/res/images/game_background_0/layers/backgroundLayer",
            "android/res/images/game_background_1/layers/backgroundLayer",
            "android/res/images/game_background_2/layers/backgroundLayer",
            "android/res/images/game_background_3/layers/backgroundLayer",
    };

}
