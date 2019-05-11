package ee.taltech.iti0202.gui.game.desktop.states.gameprogress;

import com.google.gson.annotations.SerializedName;

public class BossData {

    @SerializedName("type")
    public String type;

    @SerializedName("size")
    public int size;

    @SerializedName("location_x")
    public float locationX;

    @SerializedName("location_y")
    public float locationY;

    @SerializedName("decider")
    public boolean decider;

    public BossData(String type, int size, float locationX, float locationY, boolean decider) {
        this.type = type;
        this.size = size;
        this.locationX = locationX;
        this.locationY = locationY;
        this.decider = decider;
    }
}
