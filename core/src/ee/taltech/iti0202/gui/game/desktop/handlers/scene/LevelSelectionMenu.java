package ee.taltech.iti0202.gui.game.desktop.handlers.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PATH;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_WIDTH;

public class LevelSelectionMenu extends Scene {

    private GameButton backButton;
    private GameButton playButton;

    public LevelSelectionMenu(OrthographicCamera cam) {
        super(cam);

        backButton = new GameButton("Back", V_WIDTH / 6f, V_HEIGHT / 1.5f - 40);
        playButton = new GameButton("Play", V_WIDTH / 6f, V_HEIGHT / 1.5f);

        buttons = new HashSet<>(Arrays.asList(backButton, playButton));

        List<String> acts = loadActs();
        for (int i = 0; i < acts.size(); i++) {
            buttons.add(new GameButton(acts.get(i), V_WIDTH / 3f, V_HEIGHT / 3f + i * 40));
        }

    }

    private List<String> loadMapNames(int act) {
        String[] maps = new File(PATH + "maps/levels/act_" + act).list(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return new File(file, s).isDirectory();
            }
        });
        if (maps != null) {
            return Arrays.asList(maps);
        }
        return new ArrayList<>();
    }

    private List<String> loadActs() {
        String[] acts = new File(PATH + "maps/levels/").list(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return new File(file, s).isDirectory();
            }
        });
        if (acts != null) {
            return Arrays.asList(acts);
        }
        return new ArrayList<>();
    }
}
