package ee.taltech.iti0202.gui.game.desktop.handlers.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import ee.taltech.iti0202.gui.game.desktop.states.gameprogress.GameProgress;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PATH;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_WIDTH;

public class LoadGameMenu extends Scene {

    private GameButton backButton;
    private HashMap<GameButton, String> savesButtons = new HashMap<>();
    private String selectedMap;

    public LoadGameMenu(OrthographicCamera cam) {
        super(cam);

        backButton = new GameButton("Back", V_WIDTH / 6f, V_HEIGHT / 1.2f - 40);

        buttons = new HashSet<>(Arrays.asList(backButton));

        buttonType = new HashMap<GameButton, block>() {{
            put(backButton, block.EXIT);
        }};

        showSaves();

        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);

    }

    @Override
    public void update(float dt) {
        mouseInWorld2D.x = Gdx.input.getX();
        mouseInWorld2D.y = Gdx.input.getY();

        for (GameButton button : buttons){
            button.update(mouseInWorld2D);
            if (button.hoverOver() && buttonType.get(button) == block.LOAD) selectedMap = savesButtons.get(button);
        }
    }

    public GameProgress getGameProgress() {
        return GameProgress.load(PATH + "saves/" + selectedMap);
    }

    private void showSaves() {
        savesButtons.clear();
        List<String> saves = loadSaves();
        Collections.sort(saves);
        float y = V_HEIGHT / 2f + 40 * (int)Math.floor(saves.size() / 2f);

        for (int i = 0; i < saves.size(); i++) {
            GameButton btn = new GameButton(saves.get(i)
                    .replace(".json", "")
                    .replace("_", " "), V_WIDTH * 3 / 5f, y - i * 40);
            savesButtons.put(btn, saves.get(i));
            buttonType.put(btn, block.LOAD);
        }
        buttons.addAll(savesButtons.keySet());
    }

    private List<String> loadSaves() {
        String[] maps = new File(PATH + "saves/").list(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return !new File(file, s).isDirectory();
            }
        });
        if (maps != null) {
            return Arrays.asList(maps);
        }
        return new ArrayList<>();
    }
}
