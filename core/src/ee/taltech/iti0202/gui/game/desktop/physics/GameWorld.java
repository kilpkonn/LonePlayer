package ee.taltech.iti0202.gui.game.desktop.physics;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

import ee.taltech.iti0202.gui.game.desktop.states.shapes.ShapesGreator;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BACKGROUND;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BIT_BOSSES;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BIT_BULLET;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BIT_WEAPON;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.DIMENSION_1;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.DIMENSION_2;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.GRAVITY;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.PATH;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.TERRA_DIMENSION_1;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.TERRA_DIMENSION_2;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.TERRA_SQUARES;

public class GameWorld implements Disposable {

    private World world = new World(new Vector2(0, GRAVITY), true);
    private MultiplayerContactListener contactListener = new MultiplayerContactListener();
    private Map<Integer, Body> players = new HashMap<>();

    private TiledMap tiledMap;
    private TiledMapTileLayer foreground;
    private TiledMapTileLayer dimension_2;
    private TiledMapTileLayer dimension_1;

    private int newPlayerIdentifier;

    public GameWorld(String act, String map) {
        world.setContactListener(contactListener);

        String path = PATH + "maps/levels/" + act + "/" + map;
        tiledMap = new TmxMapLoader().load(path);
        createHitboxes(tiledMap);

        //TODO: Spawn players
    }

    public void update(float dt) {
        world.step(dt, 10, 2);
    }

    public int addPlayer() {
        newPlayerIdentifier++;
        Body player = PlayerBody.createPlayer(world);
        players.put(newPlayerIdentifier, player);
        return newPlayerIdentifier;
    }

    public boolean removePlayer(int id) {
        if (players.containsKey(id)) {
            world.destroyBody(players.get(id));
            players.remove(id);
            return true;
        }
        return false;
    }

    private void createHitboxes(TiledMap tiledMap) {
        for (MapLayer layer : tiledMap.getLayers()) {
            FixtureDef fdef = new FixtureDef();
            switch (layer.getName()) {
                case "barrier":
                    fdef.filter.categoryBits = BACKGROUND;
                    fdef.filter.maskBits = BIT_BOSSES | DIMENSION_1 | DIMENSION_2 | BIT_WEAPON | BIT_BULLET;
                    determineMapObject(layer, fdef);
                    break;
                case "hitboxes_1":
                    fdef.filter.categoryBits = TERRA_DIMENSION_1;
                    fdef.filter.maskBits = BIT_BOSSES | DIMENSION_1 | BIT_WEAPON;
                    determineMapObject(layer, fdef);
                    break;
                case "hitboxes_2":
                    fdef.filter.categoryBits = TERRA_DIMENSION_2;
                    fdef.filter.maskBits = BIT_BOSSES | DIMENSION_2 | BIT_WEAPON;
                    determineMapObject(layer, fdef);
                    break;
                case "hitboxes":
                    fdef.filter.categoryBits = TERRA_SQUARES;
                    fdef.filter.maskBits = BIT_BOSSES | DIMENSION_1 | DIMENSION_2 | BIT_WEAPON;
                    determineMapObject(layer, fdef);
                    break;
            }
        }
    }

    private void determineMapObject(MapLayer layer, FixtureDef fixtureDef) {
        BodyDef bodyDef = new BodyDef();
        Shape shape;
        for (MapObject object : layer.getObjects()) {
            if (object instanceof RectangleMapObject)
                shape = ShapesGreator.getRectangle((RectangleMapObject) object);
            else if (object instanceof PolygonMapObject)
                shape = ShapesGreator.getPolygon((PolygonMapObject) object);
            else if (object instanceof PolylineMapObject)
                shape = ShapesGreator.getPolyline((PolylineMapObject) object);
            else if (object instanceof EllipseMapObject)
                shape = ShapesGreator.getCircle((EllipseMapObject) object);
            else
                continue;

            bodyDef.type = BodyDef.BodyType.StaticBody;
            fixtureDef.isSensor = false;
            fixtureDef.shape = shape;
            world.createBody(bodyDef).createFixture(fixtureDef).setUserData(layer.getName());
        }
    }

    @Override
    public void dispose() {
        world.dispose();
    }
}
