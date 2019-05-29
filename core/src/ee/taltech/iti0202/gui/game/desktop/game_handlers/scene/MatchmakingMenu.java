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
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.components.GameButton;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.components.TextField;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.networking.client.GameClient;
import ee.taltech.iti0202.gui.game.networking.serializable.Lobby;
import ee.taltech.iti0202.gui.game.networking.server.GameServer;
import ee.taltech.iti0202.gui.game.networking.server.player.Player;

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
    private GameButton nameLabel;
    private TextField nameTextField;
    private TextField connectTextField;
    private Map<UUID, GameButton> playerNameButtons = new HashMap<>();
    private Map<GameButton, block> buttonType;

    private float timePassed = 0;

    public MatchmakingMenu(OrthographicCamera cam, Runnable backFunc) {
        super(cam);

        this.backFunc = backFunc;

        backButton = new GameButton("Back", V_WIDTH / 6f, V_HEIGHT / 1.2f);
        startButton = new GameButton("Start", V_WIDTH * 5 / 6f, V_HEIGHT / 1.2f);
        connectButton = new GameButton("Connect", V_WIDTH * 2 / 6f, V_HEIGHT / 1.2f);
        newServerButton = new GameButton("Start Server", V_WIDTH * 4 / 6f, V_HEIGHT / 1.2f);
        playersCountLabel = new GameButton("Players: 0", V_WIDTH / 6f, V_HEIGHT / 1.2f - 80);
        nameLabel = new GameButton("Name:", V_WIDTH * 4 / 6f, V_HEIGHT / 1.2f + 60);
        nameTextField = new TextField(Game.settings.NAME, V_WIDTH * 4 / 6f + nameLabel.width + 10, V_HEIGHT / 1.2f + 60, V_WIDTH / 6f, 40f);
        connectTextField = new TextField("", V_WIDTH * 3 / 6f - 50, V_HEIGHT / 1.2f, V_WIDTH / 6f - 30, 40f);

        playersCountLabel.setAcceptHover(false);
        nameLabel.setAcceptHover(false);

        nameTextField.setOnInputCompleted(() -> {
            Game.settings.NAME = nameTextField.getText();
            // Game.settings.save(B2DVars.PATH + "settings/settings.json");
            //TODO: Change name on server
        });

        buttons = new HashSet<>(Arrays.asList(backButton,
                startButton,
                connectButton,
                newServerButton,
                playersCountLabel,
                nameLabel,
                nameTextField,
                connectTextField));

        buttonType =
                new HashMap<GameButton, block>() {
                    {
                        put(backButton, block.BACK);
                        put(startButton, block.START);
                        put(connectButton, block.CONNECT);
                        put(newServerButton, block.NEWSERVER);
                        put(connectTextField, block.IPADDRESS);
                    }
                };

        for (GameButton button : buttons) played.put(button, false);
        for (GameButton button : buttons) button.setLineLengthMultiplier(0.6f);
        //connectTextField.setLineLengthMultiplier(0.8f);

        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
    }

    @Override
    public void update(float dt) {
        timePassed += dt;
        super.update(dt);
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
                    if (Game.client == null) {
                        Game.client = new GameClient(connectTextField.getText(), this);
                        connectButton.setText("Disconnect");
                    } else {
                        Game.client.disconnect();
                        Game.client = null;
                        connectButton.setText("Connect");
                    }
                    break;
                case NEWSERVER:
                    Game.server = new GameServer();
                    Game.client = new GameClient(Game.server.getConnect(), this);  // Auto connect
                    connectButton.setText("Disconnect");
                    connectTextField.setText(Game.server.getConnect());
                    break;
                case IPADDRESS:
                    if (Game.server != null) {
                        Gdx.app.getClipboard().setContents(Game.server.getConnect());
                    }
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

    public void updateLobbyDetails(Lobby.Details details) {
        buttons.removeAll(playerNameButtons.values());
        playerNameButtons.clear();

        if (details != null) {
            for (Player player : details.players) {
                addPlayer(player);
            }
        }
        playersCountLabel.setText("Players: " + playerNameButtons.size());
    }

    public void addPlayer(Player player) {  //TODO: Make some actually working function here
        GameButton btn = new GameButton(player.name, V_WIDTH / 6f, V_HEIGHT / 1.2f - 120 - playerNameButtons.size() * 40);
        btn.setAcceptHover(false);
        playerNameButtons.put(player.uuid, btn);
        buttons.add(btn);
        played.put(btn, false);
    }

    private enum block {
        START,
        CONNECT,
        NEWSERVER,
        IPADDRESS,
        BACK
    }
}
