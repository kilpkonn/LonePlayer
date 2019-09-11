package ee.taltech.iti0202.gui.game.desktop.settings;

import com.badlogic.gdx.Input;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.*;

public class Settings {
    @SerializedName("move_left")
    public int MOVE_LEFT = Input.Keys.A;
    @SerializedName("move_right")
    public int MOVE_RIGHT = Input.Keys.D;
    @SerializedName("jump")
    public int JUMP = Input.Keys.SPACE;
    @SerializedName("toggle_dimension")
    public int CHANGE_DIMENSION = Input.Keys.S;
    @SerializedName("attack")
    public int SHOOT = Input.Buttons.LEFT;
    @SerializedName("escape_button")
    public int ESC = Input.Keys.ESCAPE;
    @SerializedName("next_weapon")
    public int NEXT_WEAPON = Input.Keys.E;
    @SerializedName("previous_weapon")
    public int PREVIOUS_WEAPON = Input.Keys.Q;
    @SerializedName("tab")
    public int TAB = Input.Keys.TAB;

    @SerializedName("enable_dev_maps")
    public boolean ENABLE_DEV_MAPS = false;
    @SerializedName("show_fps")
    public boolean SHOW_FPS = false;
    @SerializedName("max_fps")
    public int MAX_FPS = 300;
    @SerializedName("enable_vsync")
    public boolean ENABLE_VSYNC = true;

    @SerializedName("name")
    public String NAME = "";

    public void save(String path) {
        Gson gson =
                new GsonBuilder()
                        .registerTypeAdapter(Settings.class, new SettingsSerializer())
                        .setPrettyPrinting()
                        .create();
        String jsonString = gson.toJson(this);
        try {
            File f = new File(path);
            if (!f.exists()) {
                boolean newFile = f.createNewFile();
                System.out.println(newFile ? "Created new settings file!" : "failed");
            }
            FileWriter writer = new FileWriter(f, false);
            System.out.println(jsonString);
            writer.write(jsonString);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving settings!");
            System.out.println(e.getMessage());
        }
    }

    public Settings loadDefault() {
        // Add settings-default.json to store default values?
        return new Settings();
    }

    public Settings load(String path) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(path)));
            Gson gson =
                    new GsonBuilder()
                            .registerTypeAdapter(Settings.class, new SettingsSerializer())
                            .create();
            Settings s = gson.fromJson(br, Settings.class);
            System.out.println("Done loading settings!");
            return s;
        } catch (FileNotFoundException e) {
            System.out.println("Error loading settings!");
            System.out.println(e.getMessage());
        }
        return new Settings();
    }
}
