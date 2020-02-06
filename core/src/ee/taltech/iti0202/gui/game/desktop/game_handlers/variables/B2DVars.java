package ee.taltech.iti0202.gui.game.desktop.game_handlers.variables;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
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
    public static final float MENU_FADE_TIME = 1;
    public static final float MENU_FADE_AMOUNT = 0.7f;
    public static final float DIMENSION_FADE_TIME = 0.4f;
    public static final float DIMENSION_FADE_AMOUNT = 0.6f;
    public static final float MAX_LOAD_TIME = 3f; // 3 seconds
    public static final float MIN_FPS_EXPECTED = 60f; // or 60 fps
    public static boolean DEBUG = false;
    // public static final boolean JAR = false;
    // bosses
    public static final String BOSS = "1";
    public static final int gotHitBySnek = 1;
    // category bits
    public static final short NONE = 0;
    public static final short BIT_ALL = 1023;
    public static final short DIMENSION_1 = 1;
    public static final short DIMENSION_2 = 2;
    public static final short TERRA_SQUARES = 4;
    public static final short BIT_BOSSES = 8;
    public static final short BIT_WORM = 16;
    public static final short BACKGROUND = 32;
    public static final short TERRA_DIMENSION_1 = 64;
    public static final short TERRA_DIMENSION_2 = 128;
    public static final short BIT_BULLET = 256;
    public static final short BIT_WEAPON = 512;
    // corner coords
    public static final int[] SQUARE_CORNERS = {-1, -1, -1, 1, 1, 1, 1, -1};
    // main screen sections
    public static final Map<String, Integer> BACKGROUND_SCREENS =
            new HashMap<String, Integer>() {
                {
                    put("Desert", 5);
                    put("Plains", 6);
                    put("Snow", 2);
                }
            };
    public static final String[] MAIN_SCREENS = {
        "images/game_background_0/layers/",
        "images/game_background_1/layers/",
        "images/game_background_2/layers/",
        "images/game_background_3/layers/",
        "images/game_background_4/layers/",
        "images/game_background_5/layers/",
        "images/game_background_6/layers/"
    };
    public static final Map<String, Integer> MAP_TO_ACT =
            new HashMap<String, Integer>() {
                {
                    put("Desert", 1);
                    put("Plains", 2);
                    put("Snow", 3);
                }
            };
    public static final Map<String, Float> BACKGROUND_SPEEDS =
            new HashMap<String, Float>() {
                {
                    put("Desert", 1f);
                    put("Plains", 1f);
                    put("Snow", 1f);
                }
            };
    public static float PLAYER_DASH_FORCE_UP = 250 * STEP;
    public static float PLAYER_DASH_FORCE_SIDE = 150 * STEP;
    // difficulty variables
    public static float PLAYER_ANIMATION_CHANGE_SPEED = 0.5f;
    public static float ROLL_ON_LANDING_SPEED = 3;
    public static float DMG_MULTIPLIER = 3;
    public static float DMG_ON_LANDING_SPEED = 7;
    public static boolean CHECKPOINTS = true;
    public static boolean BOSSES = true;
    public static boolean UPDATE = true;
    public static String PATH = "assets/";
    public static int V_WIDTH = 1920;
    public static int V_HEIGHT = 1080;
    public static int SCALE = 1;
    public static int BOSS_BASE_HP = 25;

    public enum GameDifficulty implements Serializable {
        @SerializedName("1")
        EASY(1),

        @SerializedName("2")
        HARD(2),

        @SerializedName("3")
        BRUTAL(3);

        private final int value;

        GameDifficulty(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    // pause states

}
