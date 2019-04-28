package ee.taltech.iti0202.gui.game.desktop.states.gameprogress;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class BossData {

    public BossData(String type, int size, float locationX, float locationY, float speedX, float speedY, boolean decider) {
        this.type = type;
        this.size = size;
        this.locationX = locationX;
        this.locationY = locationY;
        this.speedX = speedX;
        this.speedY = speedY;
        this.decider = decider;
    }

    @SerializedName("type")
    public String type;
    @SerializedName("size")
    public int size;
    @SerializedName("location_x")
    public float locationX;
    @SerializedName("location_y")
    public float locationY;
    @SerializedName("speed_x")
    public float speedX;
    @SerializedName("speed_y")
    public float speedY;
    @SerializedName("decider")
    public boolean decider;

    public static String toJson(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }
}
