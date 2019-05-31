package ee.taltech.iti0202.gui.game.desktop.game_handlers.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;
import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.GameStateManager;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.components.GameButton;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars;

import java.io.File;
import java.util.*;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.*;

public class LevelSelectionMenu extends Scene {

    private Runnable backFunc;
    private GameButton backButton;
    private GameButton difficultyButton;
    private Set<GameButton> actButtons = new HashSet<>();
    private Set<GameButton> mapButtons = new HashSet<>();
    private Set<GameButton> toBeRemoved = new HashSet<>();
    private Set<GameButton> toBeAdded = new HashSet<>();
    private String selectedAct = "";
    private boolean showDevMaps = Game.settings.ENABLE_DEV_MAPS;
    private B2DVars.gameDifficulty difficulty = B2DVars.gameDifficulty.HARD;

    public LevelSelectionMenu(OrthographicCamera cam, Runnable backFunc) {
        super(cam);
        this.backFunc = backFunc;

        backButton = new GameButton("Back", V_WIDTH / 6f, V_HEIGHT / 1.2f - 40);
        difficultyButton =
                new GameButton(difficulty.toString(), V_WIDTH * 4 / 6f, V_HEIGHT / 1.2f - 40);

        difficultyButton.setOnAction(this::updateDifficulty);
        backButton.setOnAction(backFunc);

        buttons = new HashSet<>(Arrays.asList(backButton, difficultyButton));

        updateActs();

        for (GameButton button : buttons) played.put(button, false);

        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if (showDevMaps != Game.settings.ENABLE_DEV_MAPS) updateActs();
        buttons.removeAll(toBeRemoved);
        toBeRemoved.clear();
        buttons.addAll(toBeAdded);
        toBeAdded.clear();
    }

    @Override
    public void handleInput() { }

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

    public void showMaps(String selectedAct) {
        toBeRemoved.addAll(mapButtons);
        mapButtons.clear();
        if (selectedAct == null || selectedAct.equals("")) {
            return;
        }
        List<String> maps = loadMapNames(selectedAct);
        Collections.sort(maps);

        for (int i = 0; i < maps.size(); i++) {
            GameButton btn =
                    new GameButton(
                            maps.get(i).replace(".tmx", "").replace("_", " "),
                            V_WIDTH * 2 / 3f,
                            V_HEIGHT / 2f - i * 40);
            mapButtons.add(btn);
            String selectedMap = maps.get(i);
            btn.setOnAction(() -> GameStateManager.pushState(
                    GameStateManager.State.PLAY, selectedAct, selectedMap, difficulty));
        }
        toBeAdded.addAll(mapButtons);
    }

    public void updateActs() {
        toBeRemoved.addAll(actButtons);
        actButtons.clear();
        showActs();
        showMaps("");
        showDevMaps = Game.settings.ENABLE_DEV_MAPS;
    }

    private void showActs() {
        List<String> acts = loadActs();
        Collections.sort(acts);
        for (int i = 0; i < acts.size(); i++) {
            GameButton btn =
                    new GameButton(
                            acts.get(i).replace("_", " "), V_WIDTH / 3f, V_HEIGHT / 2f - i * 40);
            String act = acts.get(i);
            btn.setOnHover(() -> {
                if (!selectedAct.equals(act)) {
                    showMaps(act);
                    selectedAct = act;
                }
            });
            actButtons.add(btn);
        }
        toBeAdded.addAll(actButtons);
    }

    private List<String> loadMapNames(final String act) {
        String[] maps =
                new File(PATH + "maps/levels/" + act)
                        .list(
                                (file, s) -> !new File(file, s).isDirectory()
                                        && (!act.equals("DEV")
                                                || Game.settings.ENABLE_DEV_MAPS));
        if (maps != null) {
            return Arrays.asList(maps);
        }
        return new ArrayList<>();
    }

    private List<String> loadActs() {
        String[] acts =
                new File(PATH + "maps/levels/")
                        .list(
                                (file, s) -> new File(file, s).isDirectory()
                                        && (!s.equals("DEV")
                                                || Game.settings.ENABLE_DEV_MAPS));
        if (acts != null) {
            return Arrays.asList(acts);
        }
        return new ArrayList<>();
    }

    @Override
    protected void updateCurrentBlock(GameButton button) { }
}
