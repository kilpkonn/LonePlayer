package ee.taltech.iti0202.gui.game.desktop.game_handlers.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.components.ButtonGroup;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.components.GameButton;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.components.TextField;
import ee.taltech.iti0202.gui.game.networking.client.GameClient;
import ee.taltech.iti0202.gui.game.networking.serializable.Lobby;
import ee.taltech.iti0202.gui.game.networking.server.GameServer;
import ee.taltech.iti0202.gui.game.networking.server.player.Player;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.V_WIDTH;

public class MatchmakingMenu extends Scene {

    private Runnable backFunc;

    private GameButton backButton;
    private GameButton startButton;
    private GameButton connectButton;
    private GameButton newServerButton;
    private GameButton mapSelectionButton;
    private GameButton playersCountLabel;
    private GameButton nameLabel;
    private TextField nameTextField;
    private TextField connectTextField;
    private Map<UUID, ButtonGroup> playerNameButtons = new HashMap<>();

    private float timePassed = 0;

    public MatchmakingMenu(OrthographicCamera cam, Runnable backFunc) {
        super(cam);

        this.backFunc = backFunc;

        backButton = new GameButton("Back", V_WIDTH / 6f, V_HEIGHT / 1.2f);
        startButton = new GameButton("Start", V_WIDTH * 5 / 6f, V_HEIGHT / 1.2f);
        connectButton = new GameButton("Connect", V_WIDTH * 2 / 6f, V_HEIGHT / 1.2f);
        newServerButton = new GameButton("Start Server", V_WIDTH * 4 / 6f, V_HEIGHT / 1.2f);
        mapSelectionButton = new GameButton("Map: Not Selected", V_WIDTH * 4 / 6f, V_HEIGHT / 6f);
        playersCountLabel = new GameButton("Players: 0", V_WIDTH / 6f, V_HEIGHT / 1.2f - 80);
        nameLabel = new GameButton("Name:", V_WIDTH * 4 / 6f, V_HEIGHT / 1.2f + 60);
        nameTextField = new TextField(Game.settings.NAME, V_WIDTH * 4 / 6f + nameLabel.width + 10, V_HEIGHT / 1.2f + 60, V_WIDTH / 6f, 40f);
        connectTextField = new TextField("", V_WIDTH * 3 / 6f - 50, V_HEIGHT / 1.2f, V_WIDTH / 6f - 30, 40f);

        playersCountLabel.setAcceptHover(false);
        nameLabel.setAcceptHover(false);

        nameTextField.setOnInputCompleted(() -> {
            Game.settings.NAME = nameTextField.getText();
            // Game.settings.save(B2DVars.PATH + "settings/settings.json");
            Game.client.updateName();
        });

        startButton.setOnAction(() -> System.out.println("Start game"));
        connectButton.setOnAction(() -> {
            if (Game.client == null) {
                Game.client = new GameClient(connectTextField.getText(), this);
                connectButton.setText("Disconnect");
            } else {
                Game.client.disconnect();
                Game.client = null;
                connectButton.setText("Connect");
            }
        });
        newServerButton.setOnAction(() -> {
            Game.server = new GameServer();
            Game.client = new GameClient(Game.server.getConnect(), this);  // Auto connect
            connectButton.setText("Disconnect");
            connectTextField.setText(Game.server.getConnect());
        });
        connectTextField.setOnAction(() -> {
            if (Game.server != null) {
                Gdx.app.getClipboard().setContents(Game.server.getConnect());
            }
        });
        mapSelectionButton.setOnAction(() -> System.out.println("Map")); //TODO: Select map
        backButton.setOnAction(backFunc);

        buttons = new HashSet<>(Arrays.asList(backButton,
                startButton,
                connectButton,
                newServerButton,
                mapSelectionButton,
                playersCountLabel,
                nameLabel,
                nameTextField,
                connectTextField));

        for (GameButton button : buttons) played.put(button, false);
        for (GameButton button : buttons) button.setLineLengthMultiplier(0.6f);
        mapSelectionButton.setLineLengthMultiplier(1);

        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
    }

    @Override
    public void update(float dt) {
        timePassed += dt;
        super.update(dt);
    }

    @Override
    public void handleInput() { }

    @Override
    protected void updateCurrentBlock(GameButton btn) { }

    public void updateLobbyDetails(Lobby.Details details) {
        for (ButtonGroup group : playerNameButtons.values()) {
            buttons.removeAll(group.getButtons());
            group.dispose();
        }
        playerNameButtons.clear();

        if (details != null) {
            for (Player player : details.players) {
                addPlayer(player);
            }
        }
        playersCountLabel.setText("Players: " + playerNameButtons.size());
    }

    public void addPlayer(Player player) {
        ButtonGroup group = new ButtonGroup();
        GameButton btn = new GameButton(player.name, V_WIDTH / 6f, V_HEIGHT / 1.2f - 120 - playerNameButtons.size() * 40);
        GameButton lblPing = new GameButton(player.latency + "ms", V_WIDTH * 2 / 6f, V_HEIGHT / 1.2f - 120 - playerNameButtons.size() * 40);
        GameButton btnKick = new GameButton("Kick", V_WIDTH * 2 / 6f + 100, V_HEIGHT / 1.2f - 120 - playerNameButtons.size() * 40);
        btnKick.setOnAction(() -> Game.client.kickPlayer(player));
        group.addButton(btn);
        group.addButton(lblPing);
        group.addButton(btnKick);

        group.setAcceptHover(false);

        playerNameButtons.put(player.uuid, group);
        buttons.addAll(group.getButtons());
        played.put(btn, false);
    }
}
