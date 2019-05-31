package ee.taltech.iti0202.gui.game.desktop.game_handlers.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.GameStateManager;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.input.MyInput;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.components.GameButton;
import ee.taltech.iti0202.gui.game.desktop.states.gameprogress.GameProgress;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.*;

public class LoadGameMenu extends Scene {

    private Runnable backFunc;
    private GameButton backButton;
    private Set<GameButton> savesButtons = new HashSet<>();

    public LoadGameMenu(OrthographicCamera cam, Runnable backFunc) {
        super(cam);
        this.backFunc = backFunc;

        backButton = new GameButton("Back", V_WIDTH / 6f, V_HEIGHT / 1.2f - 40);

        backButton.setOnAction(() -> {
            playSoundOnce("sounds/negative_2.wav", 0.5f);
            backFunc.run();
        });

        buttons = new HashSet<>(Arrays.asList(backButton));

        showSaves();

        for (GameButton button : buttons) played.put(button, false);

        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
    }

    @Override
    public void update(float dt) {
        mouseInWorld2D.x = Gdx.input.getX();
        mouseInWorld2D.y = Gdx.input.getY();

        for (GameButton button : buttons) {
            button.update(mouseInWorld2D);
        }
    }

    @Override
    public void handleInput() { }

    public GameProgress getGameProgress(String selectedMap) {
        return GameProgress.load(PATH + "saves/" + selectedMap);
    }

    private void showSaves() {
        buttons.removeAll(savesButtons);
        savesButtons.clear();
        List<String> saves = loadSaves();
        Collections.sort(saves);
        float y = V_HEIGHT / 2f + 40 * (int) Math.floor(saves.size() / 2f);

        for (int i = 0; i < saves.size(); i++) {
            GameButton btn =
                    new GameButton(
                            saves.get(i).replace(".json", "").replace("_", " "),
                            V_WIDTH * 3 / 5f,
                            y - i * 40);
            savesButtons.add(btn);
            String load = saves.get(i);
            btn.setOnAction(() -> {
                playSoundOnce("sounds/menu_click.wav", 0.5f);
                GameStateManager.pushState(GameStateManager.State.PLAY, getGameProgress(load));
            });
        }
        buttons.addAll(savesButtons);
    }

    private List<String> loadSaves() {
        String[] maps =
                new File(PATH + "saves/")
                        .list(
                                (file, s) -> !new File(file, s).isDirectory());
        if (maps != null) {
            return Arrays.asList(maps);
        }
        return new ArrayList<>();
    }

    @Override
    protected void updateCurrentBlock(GameButton button) { }

}
