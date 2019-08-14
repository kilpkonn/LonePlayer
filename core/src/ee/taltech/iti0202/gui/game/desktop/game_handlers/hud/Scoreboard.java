package ee.taltech.iti0202.gui.game.desktop.game_handlers.hud;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
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

    private float timeSinceUpdate = 0;
    private float updateInterval = 1f; // sec

    public Scoreboard(OrthographicCamera cam) {
        this.cam = cam;
    }

    public void update(float dt) {
        timeSinceUpdate += dt;
        if (timeSinceUpdate > updateInterval) {
            for (ButtonGroup group : playerButtons) {
                //TODO: Set kills...
            }
            timeSinceUpdate = 0;
        }
    }

    public void buildPlayers() {
        for (ButtonGroup group : playerButtons) {
            group.dispose();
        }
        playerButtons.clear();
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

            playerButtons.add(group);

            i++;
        }
    }

    public void render(SpriteBatch sb) {
        cam.update();
        sb.setProjectionMatrix(cam.combined);
        for (ButtonGroup group : playerButtons) {
            System.out.println("Render");
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
}
