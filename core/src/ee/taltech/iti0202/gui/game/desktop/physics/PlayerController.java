package ee.taltech.iti0202.gui.game.desktop.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.Map;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.PLAYER_DASH_FORCE_UP;

public class PlayerController {
    private Map<Integer, Body> playerBodies;
    private Map<Integer, PlayerBody.PlayerBodyData> players;

    public PlayerController(Map<Integer, Body> playerBodies, Map<Integer, PlayerBody.PlayerBodyData> players) {
        this.playerBodies = playerBodies;
        this.players = players;
    }

    public boolean tryJump(int id) {
        PlayerBody.PlayerBodyData data = players.get(id);
        Body body = playerBodies.get(id);

        if (data.onGround) {
            body.applyLinearImpulse(new Vector2(0, PLAYER_DASH_FORCE_UP), body.getPosition(), true);
        } else if (data.wallJump != 0) {
            body.applyLinearImpulse(new Vector2(data.wallJump * PLAYER_DASH_FORCE_UP, PLAYER_DASH_FORCE_UP),
                    body.getPosition(), true);
            data.wallJump = 0;
        } else if (data.doubleJump) {
            body.applyLinearImpulse(new Vector2(0, PLAYER_DASH_FORCE_UP), body.getPosition(), true);
            data.doubleJump = false;
        }

        return false;
    }
}
