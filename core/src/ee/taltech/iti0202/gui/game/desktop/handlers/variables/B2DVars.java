package ee.taltech.iti0202.gui.game.desktop.handlers.variables;

import java.util.HashMap;
import java.util.Map;

public class B2DVars {

    // pixel per meter
    public static final float PPM = 100;
    public static final float STEP = 1 / 60f;
    public static final float GRAVITY = -9.81f;
    public static final float MAX_SPEED = 2.5f;
    public static final float FRICTION = 0.6f;
    public static final float PLAYER_SPEED = 8;
    public static float PLAYER_DASH_FORCE_UP = 250 * STEP;
    public static float PLAYER_DASH_FORCE_SIDE = 150 * STEP;

    public static float PLAYER_ANIMATION_CHANGE_SPEED = 0.5f;
    public static float ROLL_ON_LANDING_SPEED = 3;
    public static float DMG_MULTIPLIER = 3; //TODO if easy then -2, hard -1, brutal stays
    public static float DMG_ON_LANDING = 7; //TODO if easy then +3, hard +1, brutal stays

    public static final float MENU_FADE_TIME = 1;
    public static final float MENU_FADE_AMOUNT = 0.7f;
    public static final float DIMENSION_FADE_TIME = 0.4f;
    public static final float DIMENSION_FADE_AMOUNT = 0.6f;

    public static final boolean DEBUG = true;
    public static boolean UPDATE = true;

    public static String PATH = "";
    public static int V_WIDTH = 1920;
    public static int V_HEIGHT = 1080;
    public static int SCALE = 1;

    // bosses
    public static final String WORM = "1";
    public static final String COLOSSEOS = "2";
    public static final int gotHitBySnek = 1;

    // category bits
    public static final short NONE = 0;
    public static final short BIT_ALL = 255;
    public static final short DIMENTSION_1 = 1;
    public static final short DIMENTSION_2 = 2;
    public static final short TERRA_SQUARES = 4;
    public static final short BIT_BOSSES = 8;
    public static final short BIT_WORM = 16;
    public static final short BACKGROUND = 32;
    public static final short TERRA_DIMENTSION_1 = 64;
    public static final short TERRA_DIMENTSION_2 = 128;

    // corner coords
    public static final int[] SQUARE_CORNERS = {-1, -1, -1, 1, 1, 1, 1, -1};

    // main screen sections
    public static final Map<String, Integer> BACKGROUND_SCREENS = new HashMap<String, Integer>() {{
        put("Desert", 5);
        put("Plains", 6);
        put("Snow", 2); //TODO: Something else here
    }};

    public static final String[] MAIN_SCREENS = {
            "images/game_background_0/layers/",
            "images/game_background_1/layers/",
            "images/game_background_2/layers/",
            "images/game_background_3/layers/",
            "images/game_background_4/layers/",
            "images/game_background_5/layers/",
            "images/game_background_6/layers/"
    };

    public static final Map<String, Integer> MAP_TO_ACT = new HashMap<String, Integer>() {{
        put("Desert", 1);
        put("Plains", 2);
        put("Snow", 3);
    }};

    public static final Map<String, Float> BACKGROUND_SPEEDS = new HashMap<String, Float>() {{
        put("Desert", 1f);
        put("Plains", 1f);
        put("Snow", 1f);
    }};

    // pause states

}
