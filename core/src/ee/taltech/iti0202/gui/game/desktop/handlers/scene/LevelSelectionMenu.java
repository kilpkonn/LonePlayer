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

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.handlers.gdx.GameStateManager;
import ee.taltech.iti0202.gui.game.desktop.handlers.gdx.input.MyInput;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.components.GameButton;
import ee.taltech.iti0202.gui.game.desktop.states.Menu;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PATH;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_WIDTH;

public class LevelSelectionMenu extends Scene {

    private enum block {
        MAP, ACT, EXIT
    }
    private Runnable backFunc;

    private LevelSelectionMenu.block currBlock;

    private GameButton backButton;
    private HashMap<GameButton, String> actButtons = new HashMap<>();
    private HashMap<GameButton, String> mapButtons = new HashMap<>();
    private HashMap<GameButton, LevelSelectionMenu.block> buttonType;

    private String selectedAct = "";
    private String selectedMap;

    public LevelSelectionMenu(OrthographicCamera cam, Runnable backFunc) {
        super(cam);
        this.backFunc = backFunc;

        backButton = new GameButton("Back", V_WIDTH / 6f, V_HEIGHT / 1.2f - 40);

        buttons = new HashSet<>(Arrays.asList(backButton));

        buttonType = new HashMap<GameButton, block>() {{
            put(backButton, block.EXIT);
        }};

        List<String> acts = loadActs();
        Collections.sort(acts);
        for (int i = 0; i < acts.size(); i++) {
            GameButton btn = new GameButton(acts.get(i).replace("_", " "), V_WIDTH / 3f, V_HEIGHT / 2f - i * 40);
            actButtons.put(btn, acts.get(i));
            buttonType.put(btn, block.ACT);
        }
        buttons.addAll(actButtons.keySet());

        //TODO: Load acts on every menu opening...

        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);

    }

    @Override
    public void update(float dt) {
        mouseInWorld2D.x = Gdx.input.getX();
        mouseInWorld2D.y = Gdx.input.getY();

        String actChanged = "";
        for (GameButton button : buttons){
            button.update(mouseInWorld2D);
            if (button.hoverOver() && buttonType.get(button) == block.ACT && !selectedAct.equals(actButtons.get(button))) actChanged = actButtons.get(button);
            if (button.hoverOver() && buttonType.get(button) == block.MAP) selectedMap = mapButtons.get(button);
        }
        if (!actChanged.equals("")) setSelectedAct(actChanged);
    }

    @Override
    public void handleInput() {
        if (MyInput.isMouseClicked(Game.settings.SHOOT)) {
            switch (currBlock) {
                case MAP:
                    GameStateManager.pushState(GameStateManager.State.PLAY, selectedAct, selectedMap);
                    break;
                case EXIT:
                    backFunc.run();
                    break;
            }
        }
    }

    public void showMaps() {
        List<String> maps = loadMapNames(selectedAct);
        Collections.sort(maps);

        for (int i = 0; i < maps.size(); i++) {
            GameButton btn = new GameButton(maps.get(i)
                    .replace(".tmx", "")
                    .replace("_", " "), V_WIDTH * 2 / 3f, V_HEIGHT / 2f - i * 40);
            mapButtons.put(btn, maps.get(i));
            buttonType.put(btn, block.MAP);
        }
        buttons.addAll(mapButtons.keySet());
    }

    private List<String> loadMapNames(final String act) {
        String[] maps = new File(PATH + "maps/levels/" + act).list(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return !new File(file, s).isDirectory() && (!act.equals("DEV") || Game.settings.ENABLE_DEV_MAPS);
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
                return new File(file, s).isDirectory() && (!s.equals("DEV") || Game.settings.ENABLE_DEV_MAPS);
            }
        });
        if (acts != null) {
            return Arrays.asList(acts);
        }
        return new ArrayList<>();
    }

    public void setSelectedAct(String selectedAct) {
        this.selectedAct = selectedAct;
        buttons.removeAll(mapButtons.keySet());
        mapButtons.clear();
        showMaps();
    }

    @Override
    protected void updateCurrentBlock(GameButton button) {
        currBlock = buttonType.get(button);
    }
}
