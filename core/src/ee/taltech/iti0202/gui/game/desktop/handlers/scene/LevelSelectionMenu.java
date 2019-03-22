package ee.taltech.iti0202.gui.game.desktop.handlers.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PATH;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_WIDTH;

public class LevelSelectionMenu extends Scene {

    private GameButton backButton;
    private HashMap<GameButton, Integer> actButtons = new HashMap<>();
    private HashMap<GameButton, String> mapButtons = new HashMap<>();
    private int selectedAct;
    private String selectedMap;

    public LevelSelectionMenu(OrthographicCamera cam) {
        super(cam);

        backButton = new GameButton("Back", V_WIDTH / 6f, V_HEIGHT / 1.2f - 40);

        buttons = new HashSet<>(Arrays.asList(backButton));

        buttonType = new HashMap<GameButton, block>() {{
            put(backButton, block.EXIT);
        }};

        List<String> acts = loadActs();
        for (int i = 0; i < acts.size(); i++) {
            GameButton btn = new GameButton(acts.get(i).replace("_", " "), V_WIDTH / 3f, V_HEIGHT / 2f - i * 40);
            actButtons.put(btn, i + 1);
            buttonType.put(btn, block.ACT);
        }
        buttons.addAll(actButtons.keySet());

        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);

    }

    @Override
    public void update(float dt) {
        mouseInWorld2D.x = Gdx.input.getX();
        mouseInWorld2D.y = Gdx.input.getY();

        int actChanged = -1;
        for (GameButton button : buttons){
            button.update(mouseInWorld2D);
            if (button.hoverOver() && buttonType.get(button) == block.ACT && selectedAct != actButtons.get(button)) actChanged = actButtons.get(button);
            if (button.hoverOver() && buttonType.get(button) == block.MAP) selectedMap = mapButtons.get(button);
        }
        if (actChanged != -1) setSelectedAct(actChanged);
    }

    public void showMaps() {
        List<String> maps = loadMapNames(selectedAct);

        for (int i = 0; i < maps.size(); i++) {
            GameButton btn = new GameButton(maps.get(i)
                    .replace(".tmx", "")
                    .replace("_", " "), V_WIDTH * 2 / 3f, V_HEIGHT / 2f - i * 40);
            mapButtons.put(btn, maps.get(i));
            buttonType.put(btn, block.MAP);
        }
        buttons.addAll(mapButtons.keySet());
    }

    private List<String> loadMapNames(int act) {
        String[] maps = new File(PATH + "maps/levels/Act_" + act).list(new FilenameFilter() {
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

    public String getSelectedMap() {
        return selectedMap;
    }

    public int getSelectedAct() {
        return selectedAct;
    }

    public void setSelectedAct(int selectedAct) {
        this.selectedAct = selectedAct;
        buttons.removeAll(mapButtons.keySet());
        mapButtons.clear();
        showMaps();
    }
}
