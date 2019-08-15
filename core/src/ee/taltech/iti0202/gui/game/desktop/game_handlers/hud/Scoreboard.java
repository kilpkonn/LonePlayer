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
    private GameButton background;

    private float timeSinceUpdate = 0;
    private float updateInterval = 1f; // sec

    public Scoreboard(OrthographicCamera cam) {
        this.cam = cam;

        background = new GameButton("", V_WIDTH / 6f - 10, V_HEIGHT / 1.4f + 50);
        background.setAutoScale(false);
        background.setAcceptHover(false);
        background.setSize(V_WIDTH / 1.6f, 40f);

        header = new ButtonGroup();
        GameButton btn = new GameButton("Name", V_WIDTH / 6f, V_HEIGHT / 1.4f + 40);
        GameButton lblPing = new GameButton("Ping", V_WIDTH * 2 / 6f, V_HEIGHT / 1.4f + 40);
        GameButton lblKills = new GameButton("Kills", V_WIDTH * 2.5f / 6f, V_HEIGHT / 1.4f + 40);
        GameButton lblDeaths = new GameButton("Deaths", V_WIDTH * 3f / 6f, V_HEIGHT / 1.4f + 40);
        GameButton lblDamage = new GameButton("Damage", V_WIDTH * 3.5f / 6f, V_HEIGHT / 1.4f + 40);

        header.addButton(btn);
        header.addButton(lblPing);
        header.addButton(lblKills);
        header.addButton(lblDeaths);
        header.addButton(lblDamage);
        header.setAcceptHover(false);
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
                info.lblDamage.setText(Integer.toString(player.damage));
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
            GameButton btn = new GameButton(player.name, V_WIDTH / 6f, V_HEIGHT / 1.4f - i * 40);
            GameButton lblPing = new GameButton(player.latency + "ms", V_WIDTH * 2 / 6f, V_HEIGHT / 1.4f - i * 40);
            GameButton lblKills = new GameButton(Integer.toString(player.kills), V_WIDTH * 2.5f / 6f, V_HEIGHT / 1.4f - i * 40);
            GameButton lblDeaths = new GameButton(Integer.toString(player.kills), V_WIDTH * 3f / 6f, V_HEIGHT / 1.4f - i * 40);
            GameButton lblDamage = new GameButton(Integer.toString(player.damage), V_WIDTH * 3.5f /6f, V_HEIGHT / 1.4f - i * 40);

            group.addButton(btn);
            group.addButton(lblPing);
            group.addButton(lblKills);
            group.addButton(lblDeaths);
            group.addButton(lblDamage);

            group.setAcceptHover(false);

            group.update(new Vector2());

            playerData.put(player.id, new PlayerInfo(btn, lblPing, lblKills, lblDeaths, lblDamage));

            playerButtons.add(group);

            i++;
        }
        background.setSize(V_WIDTH / 1.6f, 40 * (i + 2));
    }

    public void render(SpriteBatch sb) {
        cam.update();
        sb.setProjectionMatrix(cam.combined);
        background.renderBackground();
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
        header.dispose();
        background.dispose();
    }

    private class PlayerInfo {
        private GameButton lblName;
        private GameButton lblPing;
        private GameButton lblKills;
        private GameButton lblDeaths;
        private GameButton lblDamage;

        PlayerInfo(GameButton lblName, GameButton lblPing, GameButton lblKills, GameButton lblDeaths, GameButton lblDamage) {
            this.lblName = lblName;
            this.lblPing = lblPing;
            this.lblKills = lblKills;
            this.lblDeaths = lblDeaths;
            this.lblDamage = lblDamage;
        }
    }
}
