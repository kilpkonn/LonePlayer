package ee.taltech.iti0202.gui.game.desktop.game_handlers.hud;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.components.ButtonGroup;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.components.GameButton;
import ee.taltech.iti0202.gui.game.networking.server.entity.PlayerEntity;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.V_WIDTH;

public class Scoreboard implements Disposable {

    protected OrthographicCamera cam;
    protected Set<PlayerEntity> players = new HashSet<>();

    protected Set<ButtonGroup> playerButtons = new HashSet<>();
    protected Map<Integer, PlayerInfo> playerData = new HashMap<>();

    private ButtonGroup header;

    private float timeSinceUpdate = 0;
    private float updateInterval = 1f; // sec

    public Scoreboard(OrthographicCamera cam) {
        this.cam = cam;

        header = new ButtonGroup();
        GameButton btn = new GameButton("Name", V_WIDTH / 6f, V_HEIGHT / 1.2f + 40);
        GameButton lblPing = new GameButton("Ping", V_WIDTH * 2 / 6f, V_HEIGHT / 1.2f + 40);
        GameButton lblKills = new GameButton("Kills", V_WIDTH * 2.5f / 6f, V_HEIGHT / 1.2f + 40);
        GameButton lblDeaths = new GameButton("Deaths", V_WIDTH * 3f / 6f, V_HEIGHT / 1.2f + 40);

        header.addButton(btn);
        header.addButton(lblPing);
        header.addButton(lblKills);
        header.addButton(lblDeaths);
    }

    public void update(float dt) {
        timeSinceUpdate += dt;
        header.update(new Vector2());
        if (timeSinceUpdate > updateInterval) {
            for (PlayerEntity player : players) {
                PlayerInfo info = playerData.get(player.id);
                info.lblKills.setText(Integer.toString(player.kills));
                info.lblDeaths.setText(Integer.toString(player.deaths));
                info.lblPing.setText(Long.toString(player.latency));
            }
            timeSinceUpdate = 0;
        }
    }

    public void buildPlayers() {
        for (ButtonGroup group : playerButtons) {
            group.dispose();
        }
        playerButtons.clear();
        playerData.clear();
        ArrayList<PlayerEntity> sortedPlayers = new ArrayList<>(players);
        Collections.sort(sortedPlayers, (playerEntity, t1) -> {
            if (playerEntity.kills == t1.kills) return 0;
            return playerEntity.kills > t1.kills ? 1 : -1;
        });

        int i = 0;
        for (PlayerEntity player : sortedPlayers) {
            ButtonGroup group = new ButtonGroup();
            GameButton btn = new GameButton(player.name, V_WIDTH / 6f, V_HEIGHT / 1.2f - i * 40);
            GameButton lblPing = new GameButton(player.latency + "ms", V_WIDTH * 2 / 6f, V_HEIGHT / 1.2f - i * 40);
            GameButton lblKills = new GameButton(Integer.toString(player.kills), V_WIDTH * 2.5f / 6f, V_HEIGHT / 1.2f - i * 40);
            GameButton lblDeaths = new GameButton(Integer.toString(player.kills), V_WIDTH * 3f / 6f, V_HEIGHT / 1.2f - i * 40);

            group.addButton(btn);
            group.addButton(lblPing);
            group.addButton(lblKills);
            group.addButton(lblDeaths);

            group.setAcceptHover(false);

            group.update(new Vector2());

            playerData.put(player.id, new PlayerInfo(btn, lblPing, lblKills, lblDeaths));

            playerButtons.add(group);

            i++;
        }
    }

    public void render(SpriteBatch sb) {
        cam.update();
        sb.setProjectionMatrix(cam.combined);
        header.render(sb);
        for (ButtonGroup group : playerButtons) {
            group.render(sb);
        }
    }

    public void setPlayers(Set<PlayerEntity> players) {
        if (this.players.size() != players.size()) {
            this.players = players;
            buildPlayers();
        } else {
            this.players = players;
        }
    }

    @Override
    public void dispose() {
        for (ButtonGroup group : playerButtons) {
            group.dispose();
        }
    }

    private class PlayerInfo {
        private GameButton lblName;
        private GameButton lblPing;
        private GameButton lblKills;
        private GameButton lblDeaths;

        public PlayerInfo(GameButton lblName, GameButton lblPing, GameButton lblKills, GameButton lblDeaths) {
            this.lblName = lblName;
            this.lblPing = lblPing;
            this.lblKills = lblKills;
            this.lblDeaths = lblDeaths;
        }
    }
}
