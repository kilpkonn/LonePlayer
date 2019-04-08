package ee.taltech.iti0202.gui.game.desktop.settings;

import com.badlogic.gdx.Input;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Settings {
    public int MOVE_LEFT = Input.Keys.A;
    public int MOVE_RIGHT = Input.Keys.D;
    public int JUMP = Input.Keys.SPACE;
    public int CHANGE_DIMENTION = Input.Keys.S;
    public int MENU = Input.Keys.MENU;
    public int SHOOT = Input.Buttons.LEFT;
    public int ESC = Input.Keys.ESCAPE;

    public boolean ENABLE_DEV_MAPS = false;

    public void save(String path) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Settings.class, new SettingsSerializer()).create();
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
            Gson gson = new GsonBuilder().registerTypeAdapter(Settings.class, new SettingsSerializer()).create();
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
