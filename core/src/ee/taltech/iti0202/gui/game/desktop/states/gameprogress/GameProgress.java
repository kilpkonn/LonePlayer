package ee.taltech.iti0202.gui.game.desktop.states.gameprogress;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class GameProgress {
    public float playerLocationX;
    public float playerLocationY;
    public float playerVelocityX;
    public float playerVelocityY;
    public boolean dimension;
    public String act;
    public String map;

    public void save(String path) {
        Gson gson = new GsonBuilder().registerTypeAdapter(GameProgress.class, new GameProgressSerializer()).create();
        String jsonString = gson.toJson(this);
        try {
            File f = new File(path);
            if (!f.exists()) {
                boolean newFile = f.createNewFile();
                System.out.println(newFile ? "Created new game progress file!" : "Failed to create file.");
            }
            FileWriter writer = new FileWriter(f, false);
            System.out.println(jsonString);
            writer.write(jsonString);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving progress!");
            System.out.println(e.getMessage());
        }
    }

    public static GameProgress load(String path) {
        Gson gson = new GsonBuilder().registerTypeAdapter(GameProgress.class, new GameProgressSerializer()).create();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(path)));
            GameProgress p = gson.fromJson(br, GameProgress.class);
            System.out.println("Done loading game!");
            return p;
        } catch (FileNotFoundException e) {
            System.out.println("Error loading game!");
            System.out.println(e.getMessage());
        }
        return new GameProgress();
    }
}
