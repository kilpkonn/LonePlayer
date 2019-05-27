package ee.taltech.iti0202.gui.game.desktop.game_handlers.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.input.MyInput;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.input.MyTextListener;
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
    private GameButton newServerButton;
    private GameButton playersCountLabel;
    private GameButton ipAddressLabel;
    private Map<UUID, GameButton> playerNameButtons = new HashMap<>();
    private Map<GameButton, block> buttonType;

    private MyTextListener textListener;

    public MatchmakingMenu(OrthographicCamera cam, Runnable backFunc) {
        super(cam);

        this.backFunc = backFunc;

        backButton = new GameButton("Back", V_WIDTH / 6f, V_HEIGHT / 1.2f);
        startButton = new GameButton("Start", V_WIDTH * 5 / 6f, V_HEIGHT / 1.2f);
        connectButton = new GameButton("Connect", V_WIDTH * 2 / 6f, V_HEIGHT / 1.2f);
        newServerButton = new GameButton("Start Server", V_WIDTH * 4 / 6f, V_HEIGHT / 1.2f);
        playersCountLabel = new GameButton("Players: 0", V_WIDTH / 6f, V_HEIGHT / 1.2f - 80);
        ipAddressLabel = new GameButton("", V_WIDTH * 3 / 6f - 50, V_HEIGHT / 1.2f);

        playersCountLabel.setAcceptHover(false);

        buttons = new HashSet<>(Arrays.asList(backButton,
                startButton,
                connectButton,
                newServerButton,
                playersCountLabel,
                ipAddressLabel));

        buttonType =
                new HashMap<GameButton, block>() {
                    {
                        put(backButton, block.BACK);
                        put(startButton, block.START);
                        put(connectButton, block.CONNECT);
                        put(newServerButton, block.NEWSERVER);
                        put(ipAddressLabel, block.IPADDRESS);
                    }
                };

        for (GameButton button : buttons) played.put(button, false);
        for (GameButton button : buttons) button.setLineLengthMultiplier(0.6f);
        ipAddressLabel.setLineLengthMultiplier(0.8f);

        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        if (Game.server != null && Game.server.getPlayers().size() != playerNameButtons.size()) {
            buttons.removeAll(playerNameButtons.values());
            playerNameButtons.clear();
            for (UUID uuid : Game.server.getPlayers()) {
                addPlayer(uuid);
            }
            playersCountLabel.setText("Players: " + playerNameButtons.size());
        }
    }

    @Override
    public void handleInput() {
        if (MyInput.isMouseClicked(Game.settings.SHOOT) && currBlock != null) {
            switch (currBlock) {
                case START:
                    playSoundOnce("sounds/menu_click.wav", 0.5f);
                    // TODO: Start game
                    break;
                case CONNECT:
                    textListener = new MyTextListener();
                    Gdx.input.getTextInput(textListener, "Connect", "", "xxxx.xxxx.xxxx.xxxx:port");
                    break;
                case NEWSERVER:
                    Game.server = new GameServer();
                    ipAddressLabel.setText(Game.server.getConnect());
                    break;
                case IPADDRESS:
                    Gdx.app.getClipboard().setContents(Game.server.getConnect());
                    break;
                case BACK:
                    playSoundOnce("sounds/negative_2.wav", 0.5f);
                    backFunc.run();
                    break;
            }
        }

        if (textListener != null && textListener.isDone()) {
            Game.client = new GameClient(textListener.getText());
            Game.client.sendMessage("Testy!");
            textListener = null;
        }
    }

    @Override
    protected void updateCurrentBlock(GameButton btn) {
        currBlock = buttonType.get(btn);
    }

    public void addPlayer(UUID uuid) {  //TODO: Make some actually working function here
        GameButton btn = new GameButton(uuid.toString(), V_WIDTH / 6f, V_HEIGHT / 1.2f - 120 - playerNameButtons.size() * 40);
        btn.setAcceptHover(false);
        playerNameButtons.put(uuid, btn);
        buttons.add(btn);
    }

    private enum block {
        START,
        CONNECT,
        NEWSERVER,
        IPADDRESS,
        BACK
    }
}
