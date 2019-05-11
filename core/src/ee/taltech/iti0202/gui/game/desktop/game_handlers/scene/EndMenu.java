package ee.taltech.iti0202.gui.game.desktop.game_handlers.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;
import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.GameStateManager;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.input.MyInput;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.components.GameButton;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.V_WIDTH;

public class EndMenu extends Scene {

    private block currBlock = block.DEFAULT;
    private HashMap<GameButton, block> buttonType;
    private Runnable openSettingsFunc;
    private B2DVars.gameDifficulty difficulty;
    private GameButton exitButton;
    private GameButton settingsButton;
    private GameButton nextButton;
    private GameButton playAgainButton;
    private GameButton timeButton;
	private List<String> levels =
			Arrays.asList(
					"Desert@Big_leaps.tmx",
					"Desert@Desert_1.tmx",
					"Desert@Desert_2.tmx",
					"Desert@Parkour_madness.tmx",
					"Desert@Toggle_Drop.tmx",
					"Plains@Dimension_parkour.tmx",
					"Plains@Islands.tmx",
					"Plains@Plains_1.tmx",
					"Plains@Plains_2.tmx",
					"Plains@The_Drop.tmx",
					"Snow@Santa's_Fort.tmx",
					"Snow@Snow_1.tmx",
					"Snow@Snow_2.tmx",
					"Snow@Snowy_climb.tmx",
					"Snow@Snowy_drop.tmx");

	public EndMenu(
			String act,
			String map,
			OrthographicCamera cam,
			B2DVars.gameDifficulty difficulty,
			Runnable openSettingsFunc) {
        super(act, map, cam);
        this.openSettingsFunc = openSettingsFunc;
        this.difficulty = difficulty;

        hudCam.update();

        nextButton = new GameButton("Next level", V_WIDTH / 6f, V_HEIGHT / 1.5f - 40);
        playAgainButton = new GameButton("Play again", V_WIDTH / 6f, V_HEIGHT / 1.5f - 80);
        settingsButton = new GameButton("Settings", V_WIDTH / 6f, V_HEIGHT / 1.5f - 120);
        exitButton = new GameButton("Exit", V_WIDTH / 6f, V_HEIGHT / 1.5f - 160);
        timeButton = new GameButton("Time: 0s", V_WIDTH * 2 / 3f, V_HEIGHT / 1.5f - 40);
        timeButton.setAcceptHover(false);

		buttons =
				new HashSet<>(
						Arrays.asList(
								nextButton,
								playAgainButton,
								settingsButton,
								exitButton,
								timeButton));

		buttonType =
				new HashMap<GameButton, block>() {
					{
						put(nextButton, block.NEXT);
						put(playAgainButton, block.NEWGAME);
						put(settingsButton, block.SETTINGS);
						put(exitButton, block.EXIT);
						put(timeButton, block.DEFAULT);
					}
				};
        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);

        for (GameButton button : buttons) played.put(button, false);
    }

    @Override
    public void handleInput() {
        if (MyInput.isMouseClicked(Game.settings.SHOOT) && currBlock != null) {
            switch (currBlock) {
                case NEXT:
                    System.out.println(act);
                    System.out.println(map);
                    String[] newLevel = levels.get(levels.indexOf(act + "@" + map) + 1).split("@");
					GameStateManager.pushState(
							GameStateManager.State.PLAY, newLevel[0], newLevel[1], difficulty);
                    break;
                case NEWGAME:
                    playSoundOnce("sounds/menu_click.wav", 0.5f);
                    GameStateManager.pushState(GameStateManager.State.PLAY, act, map, difficulty);
                    break;
                case SETTINGS:
                    playSoundOnce("sounds/menu_click.wav", 0.5f);
                    openSettingsFunc.run();
                    break;
                case EXIT:
                    playSoundOnce("sounds/negative_2.wav", 0.5f);
                    GameStateManager.pushState(GameStateManager.State.MENU);
                    break;
            }
        }
    }

    @Override
    protected void updateCurrentBlock(GameButton button) {
        currBlock = buttonType.get(button);
    }

    public void setTime(float time) {
        timeButton.setText("Time: " + Math.round(time * 100) / 100f + "s");
    }

	private enum block {
		NEWGAME,
		NEXT,
		EXIT,
		SETTINGS,
		DEFAULT
	}
}
