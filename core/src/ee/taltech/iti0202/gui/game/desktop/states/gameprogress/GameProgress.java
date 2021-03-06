package ee.taltech.iti0202.gui.game.desktop.states.gameprogress;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameProgress {
    /*public float playerLocationX;
    public float playerLocationY;
    public float playerVelocityX;
    public float playerVelocityY;*/
    public float checkpointX;
    public float checkpointY;
    public float time;
    public List<BossData> bosses = new ArrayList<>();
    public boolean dimension;
    public B2DVars.GameDifficulty difficulty;
    public String act;
    public String map;

    public static GameProgress load(String path) {
        Gson gson =
                new GsonBuilder()
                        .registerTypeAdapter(GameProgress.class, new GameProgressSerializer())
                        .create();
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

    public void save(String path) {
        Gson gson =
                new GsonBuilder()
                        .registerTypeAdapter(GameProgress.class, new GameProgressSerializer())
                        .create();
        String jsonString = gson.toJson(this);
        try {
            File f = new File(path);
            if (!f.exists()) {
                boolean newFile = f.getParentFile().mkdirs();
                newFile = newFile || f.createNewFile();
                System.out.println(
                        newFile ? "Created new game progress file!" : "Failed to create file.");
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
}
