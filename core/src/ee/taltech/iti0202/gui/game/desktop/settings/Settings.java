package ee.taltech.iti0202.gui.game.desktop.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


import ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars;

public class Settings {
    public int NUM_KEYS = 7;
    public int MOVE_LEFT = 0;
    public int MOVE_RIGHT = 1;
    public int JUMP = 2;
    public int CHANGE_DIMENTION = 3;
    public int MENU = 4;
    public int SHOOT = 5;
    public int ESC = 6;

    public void Save() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Settings.class, new SettingsSerializer()).create();
        String jsonString = gson.toJson(this);
        try {
            FileWriter writer = new FileWriter(B2DVars.PATH + "settings/settings.json");
            System.out.println(jsonString);
            writer.write(jsonString);
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private Settings Load() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(B2DVars.PATH + "settings/settings.json"));
            Gson gson = new GsonBuilder().create();
            Settings s = gson.fromJson(br, Settings.class);
            System.out.println("Done loading settings!");
            return s;
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return new Settings();
    }
}
