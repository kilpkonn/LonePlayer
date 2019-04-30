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
import ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PATH;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_WIDTH;

public class LevelSelectionMenu extends Scene {

    private enum block {
        MAP, ACT, EXIT, DIFFICULTY
    }
    private Runnable backFunc;

    private LevelSelectionMenu.block currBlock;

    private GameButton backButton;
    private GameButton difficultyButton;
    private HashMap<GameButton, String> actButtons = new HashMap<>();
    private HashMap<GameButton, String> mapButtons = new HashMap<>();
    private HashMap<GameButton, LevelSelectionMenu.block> buttonType;

    private String selectedAct = "";
    private String selectedMap;
    private boolean showDevMaps = Game.settings.ENABLE_DEV_MAPS;
    private B2DVars.gameDifficulty difficulty = B2DVars.gameDifficulty.HARD;
    //private

    public LevelSelectionMenu(OrthographicCamera cam, Runnable backFunc) {
        super(cam);
        this.backFunc = backFunc;

        backButton = new GameButton("Back", V_WIDTH / 6f, V_HEIGHT / 1.2f - 40);
        difficultyButton = new GameButton(difficulty.toString(), V_WIDTH * 4 / 6f, V_HEIGHT / 1.2f - 40);

        buttons = new HashSet<>(Arrays.asList(backButton, difficultyButton));

        buttonType = new HashMap<GameButton, block>() {{
            put(backButton, block.EXIT);
            put(difficultyButton, block.DIFFICULTY);
        }};

        updateActs();

        for (GameButton button : buttons) played.put(button, false);

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
        if (showDevMaps != Game.settings.ENABLE_DEV_MAPS) updateActs();
        if (!actChanged.equals("")) setSelectedAct(actChanged);
    }

    @Override
    public void handleInput() {
        try {
            if (MyInput.isMouseClicked(Game.settings.SHOOT) && currBlock != null) {
                switch (currBlock) {
                    case MAP:
                        playSoundOnce("sounds/menu_click.wav", 0.5f);
                        GameStateManager.pushState(GameStateManager.State.PLAY, selectedAct, selectedMap, difficulty);
                        break;
                    case DIFFICULTY:
                        playSoundOnce("sounds/menu_click.wav", 0.5f);
                        updateDifficulty();
                        break;
                    case EXIT:
                        playSoundOnce("sounds/negative_2.wav", 0.5f);
                        backFunc.run();
                        break;
                }
            }
        } catch (NullPointerException nuk) {
            nuk.printStackTrace();
        }
    }

    private void updateDifficulty() {
        switch (difficulty) {
            case EASY:
                difficulty = B2DVars.gameDifficulty.HARD;
                break;
            case HARD:
                difficulty = B2DVars.gameDifficulty.BRUTAL;
                break;
            case BRUTAL:
                difficulty = B2DVars.gameDifficulty.EASY;
                break;
        }
        difficultyButton.setText(difficulty.toString());
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

    public void updateActs() {
        buttons.removeAll(actButtons.keySet());
        actButtons.clear();
        showActs();
        setSelectedAct("");
        showDevMaps = Game.settings.ENABLE_DEV_MAPS;
    }

    private void showActs() {
        List<String> acts = loadActs();
        Collections.sort(acts);
        for (int i = 0; i < acts.size(); i++) {
            GameButton btn = new GameButton(acts.get(i).replace("_", " "), V_WIDTH / 3f, V_HEIGHT / 2f - i * 40);
            actButtons.put(btn, acts.get(i));
            buttonType.put(btn, block.ACT);
        }
        buttons.addAll(actButtons.keySet());
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
