package ee.taltech.iti0202.gui.game.desktop.physics;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ee.taltech.iti0202.gui.game.desktop.states.shapes.ShapesGreator;
import ee.taltech.iti0202.gui.game.networking.server.entity.Player;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BACKGROUND;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BIT_BOSSES;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BIT_BULLET;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BIT_WEAPON;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.DIMENSION_1;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.DIMENSION_2;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.GRAVITY;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.PATH;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.PPM;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.TERRA_DIMENSION_1;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.TERRA_DIMENSION_2;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.TERRA_SQUARES;

public class GameWorld implements Disposable {

    private World world = new World(new Vector2(0, GRAVITY), true);
    private Map<Integer, Body> playerBodies = new HashMap<>();
    private Map<Integer, PlayerBody.PlayerBodyData> players = new HashMap<>();
    private MultiplayerContactListener contactListener;
    private List<Vector2> spawns = new ArrayList<>();

    private TiledMap tiledMap;

    private int newPlayerIdentifier;

    public GameWorld(String act, String map) {
        contactListener = new MultiplayerContactListener(players);
        world.setContactListener(contactListener);

        String path = PATH + "maps/levels/" + act + "/" + map;
        tiledMap = new TmxMapLoader().load(path);  //TODO: @Enrico Selle mapi laadimise peaks saama nii, et see toimiks ka mitte mainthreadil. Siis saab serverit headlessina ka runnida, etm jama
        createHitboxes(tiledMap);
    }

    public void update(float dt) {
        world.step(dt, 10, 2);
    }

    public int addPlayer() {
        newPlayerIdentifier++;

        //Random spawn
        Vector2 spawnCoordinates = spawns.get((int) Math.floor(Math.random() * spawns.size()));

        Body player = PlayerBody.createPlayer(world, spawnCoordinates, newPlayerIdentifier);
        playerBodies.put(newPlayerIdentifier, player);
        players.put(newPlayerIdentifier, (PlayerBody.PlayerBodyData) player.getUserData());
        return newPlayerIdentifier;
    }

    public void addWeapon() {

    }

    public boolean removePlayer(int id) {
        if (playerBodies.containsKey(id)) {
            world.destroyBody(playerBodies.get(id));
            playerBodies.remove(id);
            players.remove(id);
            return true;
        }
        return false;
    }

    public void updatePlayer(Player player) {
        if (!playerBodies.containsKey(player.bodyId)) {
            Body playerBody = PlayerBody.createPlayer(world, player.position, player.bodyId);
            playerBodies.put(player.bodyId, playerBody);
            players.put(player.bodyId, (PlayerBody.PlayerBodyData) playerBody.getUserData());
            if (player.bodyId < newPlayerIdentifier) newPlayerIdentifier = player.bodyId + 1;
        }
        Body body = playerBodies.get(player.bodyId);
        body.setTransform(player.position, 0);
        body.setLinearVelocity(player.velocity);
    }

    private void createHitboxes(TiledMap tiledMap) {
        for (MapLayer layer : tiledMap.getLayers()) {
            FixtureDef fixtureDef = new FixtureDef();
            switch (layer.getName()) {
                case "barrier":
                    fixtureDef.filter.categoryBits = BACKGROUND;
                    fixtureDef.filter.maskBits =
                            BIT_BOSSES | DIMENSION_1 | DIMENSION_2 | BIT_WEAPON | BIT_BULLET;
                    determineMapObject(layer, fixtureDef);
                    break;
                case "hitboxes_1":
                    fixtureDef.filter.categoryBits = TERRA_DIMENSION_1;
                    fixtureDef.filter.maskBits = BIT_BOSSES | DIMENSION_1 | BIT_WEAPON;
                    determineMapObject(layer, fixtureDef);
                    break;
                case "hitboxes_2":
                    fixtureDef.filter.categoryBits = TERRA_DIMENSION_2;
                    fixtureDef.filter.maskBits = BIT_BOSSES | DIMENSION_2 | BIT_WEAPON;
                    determineMapObject(layer, fixtureDef);
                    break;
                case "hitboxes":
                    fixtureDef.filter.categoryBits = TERRA_SQUARES;
                    fixtureDef.filter.maskBits =
                            BIT_BOSSES | DIMENSION_1 | DIMENSION_2 | BIT_WEAPON;
                    determineMapObject(layer, fixtureDef);
                    break;
            }
        }
    }

    private void determineMapObject(MapLayer layer, FixtureDef fixtureDef) {
        BodyDef bodyDef = new BodyDef();
        Shape shape;
        Vector2 pos = new Vector2();
        for (MapObject object : layer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rect = (RectangleMapObject) object;
                shape = ShapesGreator.getRectangle(rect);
                if (layer.getName().equals("hitboxes")) {
                    pos.x = (rect.getRectangle().x + rect.getRectangle().width / 2) / PPM;
                    pos.y = (rect.getRectangle().y + rect.getRectangle().height + 200) / PPM;
                    spawns.add(pos.cpy());
                }
            } else if (object instanceof PolygonMapObject) {
                shape = ShapesGreator.getPolygon((PolygonMapObject) object);
            } else if (object instanceof PolylineMapObject) {
                shape = ShapesGreator.getPolyline((PolylineMapObject) object);
            } else if (object instanceof EllipseMapObject) {
                shape = ShapesGreator.getCircle((EllipseMapObject) object);
            } else {
                continue;
            }

            bodyDef.type = BodyDef.BodyType.StaticBody;
            fixtureDef.isSensor = false;
            fixtureDef.shape = shape;
            world.createBody(bodyDef).createFixture(fixtureDef).setUserData(layer.getName());
            shape.dispose();
        }
    }

    public Map<Integer, Body> getPlayerBodies() {
        return playerBodies;
    }

    public Map<Integer, PlayerBody.PlayerBodyData> getPlayers() {
        return players;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    @Override
    public void dispose() {
        world.dispose();
    }
}
