package ee.taltech.iti0202.gui.game.desktop.game_handlers.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.input.MyInput;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.components.GameButton;
import ee.taltech.iti0202.gui.game.networking.client.GameClient;
import ee.taltech.iti0202.gui.game.networking.server.GameServer;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.V_WIDTH;

public class MatchmakingMenu extends Scene {

    private block currBlock;
    private Runnable backFunc;

    private GameButton backButton;
    private GameButton startButton;
    private GameButton connectButton;
    private GameButton playersCountLabel;
    private Map<String, GameButton> playerNameButtons = new HashMap<>();
    private Map<GameButton, block> buttonType;

    public MatchmakingMenu(OrthographicCamera cam, Runnable backFunc) {
        super(cam);

        this.backFunc = backFunc;

        backButton = new GameButton("Back", V_WIDTH / 6f, V_HEIGHT / 1.2f - 40);
        startButton = new GameButton("Start", V_WIDTH * 5 / 6f, V_HEIGHT / 1.2f - 40);
        connectButton = new GameButton("Connect", V_WIDTH * 2 / 6f, V_HEIGHT / 1.2f - 40);
        playersCountLabel = new GameButton("Players: 0", V_WIDTH / 6f, V_HEIGHT / 1.2f - 80);
        playersCountLabel.setAcceptHover(false);

        buttons = new HashSet<>(Arrays.asList(backButton,
                startButton,
                connectButton,
                playersCountLabel));

        buttonType =
                new HashMap<GameButton, block>() {
                    {
                        put(backButton, block.BACK);
                        put(startButton, block.START);
                        put(connectButton, block.CONNNECT);
                    }
                };

        for (GameButton button : buttons) played.put(button, false);

        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);

        Game.server = new GameServer();
    }

    @Override
    public void handleInput() {
        if (MyInput.isMouseClicked(Game.settings.SHOOT) && currBlock != null) {
            switch (currBlock) {
                case START:
                    playSoundOnce("sounds/menu_click.wav", 0.5f);
                    // TODO: Start game
                    break;
                case CONNNECT:
                    Game.client = new GameClient();
                    Game.client.sendMessage("Testy!");
                    break;
                case BACK:
                    playSoundOnce("sounds/negative_2.wav", 0.5f);
                    backFunc.run();
                    break;
            }
        }
    }

    @Override
    protected void updateCurrentBlock(GameButton btn) {
        currBlock = buttonType.get(btn);
    }

    public void addPlayer(String name) {  //TODO: Make some actually working function here
        GameButton btn = new GameButton(name, V_WIDTH / 6f, V_HEIGHT / 1.2f - 120 - playerNameButtons.size() * 40);
        btn.setAcceptHover(false);
        playerNameButtons.put(name, btn);
        buttons.add(btn);
    }

    private enum block {
        START,
        CONNNECT,
        BACK
    }
}
