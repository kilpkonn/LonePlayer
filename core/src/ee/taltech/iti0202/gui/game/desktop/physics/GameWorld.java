package ee.taltech.iti0202.gui.game.desktop.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.GRAVITY;

public class GameWorld implements Disposable {

    private World world = new World(new Vector2(0, GRAVITY), true);
    private MultiplayerContactListener contactListener = new MultiplayerContactListener();
    private Map<Integer, Body> players = new HashMap<>();

    private int playerIdentifier;

    public GameWorld() {
        world.setContactListener(contactListener);
        //TODO: Load map hit boxes, maybe in builder?
    }

    public void update(float dt) {
        world.step(dt, 10, 2);
    }

    public int addPlayer() {
        playerIdentifier++;
        Body player = PlayerBody.createPlayer(world);
        players.put(playerIdentifier, player);
        return playerIdentifier;
    }

    public boolean removePlayer(int id) {
        if (players.containsKey(id)) {
            world.destroyBody(players.get(id));
            players.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public void dispose() {
        world.dispose();
    }
}
