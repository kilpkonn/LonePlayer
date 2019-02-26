package ee.taltech.iti0202.gui.game.desktop.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

import ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars;
import ee.taltech.iti0202.gui.game.desktop.handlers.GameStateManager;
import ee.taltech.iti0202.gui.game.desktop.handlers.MyContactListener;
import ee.taltech.iti0202.gui.game.desktop.handlers.MyInput;
import ee.taltech.iti0202.gui.game.entities.Checkpoint;
import ee.taltech.iti0202.gui.game.entities.Player;

import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.CORNER_LOCATION;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.FRICTION;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.GRAVITY;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.MAX_SPEED;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.PLAYER_DASH_FORCE_SIDE;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.PLAYER_DASH_FORCE_UP;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.PLAYER_SPEED;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.PPM;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.SQUARE_CORNERS;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.ShiftX;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.ShiftY;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.TRIANGLE_BOTTOM_LEFT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.TRIANGLE_BOTTOM_RIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.TRIANGLE_TOP_LEFT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.TRIANGLE_TOP_RIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.TYPE;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.V_WIDTH;

public class Play extends GameState {

    private World world;
    private Box2DDebugRenderer b2dr; // for showing hitboxes
    private OrthographicCamera b2dcam;
    private MyContactListener cl;
    private TiledMap tiledMap;
    private float tileSize;
    private OrthoCachedTiledMapRenderer tmr;
    private Player player;
    private Body body;
    private Checkpoint checkpoint;
    private Vector2 current_force;
    private BodyDef bdef;
    private PolygonShape shape;
    private CircleShape circle;
    private FixtureDef fdef;

    ////////////////////////////////////////////////////////////////////         Set up game        ////////////////////////////////////////////////////////////////////

    public Play(GameStateManager gsm, int act, int map) {

        super(gsm);

        // sey up box2d stuff
        world = new World(new Vector2(0, GRAVITY), true);
        cl = new MyContactListener();
        world.setContactListener(cl);
        b2dr = new Box2DDebugRenderer();

        // create shapes
        bdef = new BodyDef();
        shape = new PolygonShape();
        circle = new CircleShape();
        fdef = new FixtureDef();

        // create player dynamic always gets affected
        player = InitPlayer();

        //set up box2d cam
        b2dcam = new OrthographicCamera();
        b2dcam.setToOrtho(false, V_WIDTH / PPM, V_HEIGHT / PPM);


        ////////////////////////////////    Tiled stuff here    ///////////////////////


        // load tiled map
        String path = "android/res/maps/level_" + act + "_" + map + ".tmx";
        tiledMap = new TmxMapLoader().load(path);
        tmr = new OrthoCachedTiledMapRenderer(tiledMap);

        drawLayers();

    }

    ////////////////////////////////////////////////////////////////////   Create Animated bodies   ////////////////////////////////////////////////////////////////////

    private Player InitPlayer() {

        circle = new CircleShape();
        if (checkpoint == null) {
            bdef.position.set(160 / PPM, 210 / PPM);
        } else {
            bdef.position.set(new Vector2(checkpoint.getPosition().x, checkpoint.getPosition().y + 60 / PPM));
        }
        bdef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bdef);
        fdef.isSensor = false;
        circle.setRadius(5 / PPM);
        fdef.shape = circle;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_PLAYER;
        body.createFixture(fdef).setFriction(FRICTION);
        body.setFixedRotation(false);
        body.setUserData("playerBall");

        shape = new PolygonShape();
        shape.setAsBox(4 / PPM, 6 / PPM, new Vector2(0, 4 / PPM), 0);
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_PLAYER;
        body.createFixture(fdef).setUserData("playerBody");

        shape.setAsBox(4 / PPM, 2 / PPM, new Vector2(0, -13 / PPM), 0);
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_PLAYER;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("foot");

        player = new Player(body);

        return player;
    }

    private void createCheckpoints(Vector2 pos) {
        bdef = new BodyDef();
        bdef.position.set(pos);
        bdef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bdef);
        shape = new PolygonShape();
        shape.setAsBox(4 / PPM, 32 / PPM, new Vector2(0, 4 / PPM), 0);
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_ALL;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("checkpoint");
        checkpoint = new Checkpoint(body);
    }

    ////////////////////////////////////////////////////////////////////    Read and draw the map   ////////////////////////////////////////////////////////////////////

    private void drawLayers() {
        TiledMapTileLayer layer;

        layer = (TiledMapTileLayer) tiledMap.getLayers().get("terra_squares");
        if (layer != null) {
            tileSize = layer.getTileWidth();
            ReadPolygonVertices(layer, B2DVars.TERRA_SQUARES, false);
        }

        layer = (TiledMapTileLayer) tiledMap.getLayers().get("border");
        if (layer != null) {
            tileSize = layer.getTileWidth();
            ReadPolygonVertices(layer, B2DVars.BIT_ALL, true);
            layer.setVisible(true); // disable visibility here <--------------------------------------------
        }

        layer = (TiledMapTileLayer) tiledMap.getLayers().get("checkpoints");
        if (layer != null) {
            tileSize = layer.getTileWidth();
            ReadPolygonVertices(layer, B2DVars.BACKGROUND, true);
        }


        layer = (TiledMapTileLayer) tiledMap.getLayers().get("background");
        if (layer != null) {
            tileSize = layer.getTileWidth();
            ReadPolygonVertices(layer, B2DVars.BACKGROUND, false);
        }


        layer = (TiledMapTileLayer) tiledMap.getLayers().get("terra_triangle_bottom_right");
        if (layer != null) {
            tileSize = layer.getTileWidth();
            ReadPolygonVertices(layer, B2DVars.TERRA_SQUARES, false);
        }


        layer = (TiledMapTileLayer) tiledMap.getLayers().get("terra_triangle_bottom_left");
        if (layer != null) {
            tileSize = layer.getTileWidth();
            ReadPolygonVertices(layer, B2DVars.TERRA_SQUARES, false);
        }


        layer = (TiledMapTileLayer) tiledMap.getLayers().get("terra_triangle_top_left");
        if (layer != null) {
            tileSize = layer.getTileWidth();
            ReadPolygonVertices(layer, B2DVars.TERRA_SQUARES, false);
        }


        layer = (TiledMapTileLayer) tiledMap.getLayers().get("terra_triangle_top_right");
        if (layer != null) {
            tileSize = layer.getTileWidth();
            ReadPolygonVertices(layer, B2DVars.TERRA_SQUARES, false);
        }

    }

    private void ReadPolygonVertices(TiledMapTileLayer layer, short bits, boolean isSensor) {

        int[] corner_coords = new int[8];
        String type = layer.getProperties().get(TYPE).toString();

        switch (type) {
            default:
                System.out.println("failed to determine layer");
                break;
            case "square":
                corner_coords = SQUARE_CORNERS;
                break;
            case "background":
                corner_coords = SQUARE_CORNERS;
                break;
            case "checkpoint":
                corner_coords = SQUARE_CORNERS;
                break;
            case "border":
                corner_coords = SQUARE_CORNERS;
                break;
            case "triangle":
                String triangle = layer.getProperties().get(CORNER_LOCATION).toString();
                switch (triangle) {
                    case "bottom_left":
                        corner_coords = TRIANGLE_BOTTOM_LEFT;
                        break;

                    case "bottom_right":
                        corner_coords = TRIANGLE_BOTTOM_RIGHT;
                        break;

                    case "top_left":
                        corner_coords = TRIANGLE_TOP_LEFT;
                        break;

                    case "top_right":
                        corner_coords = TRIANGLE_TOP_RIGHT;
                        break;

                    default:
                        System.out.println("failed to determine layer");
                        break;

                }
        }

        createMap(layer, bits, isSensor, corner_coords);
    }

    private void createMap(TiledMapTileLayer layer, short bits, boolean isSensor, int[] corner_coords) {
        for (int row = 0; row < layer.getHeight(); row++) {

            List<Vector2[]> polygonVertices = new ArrayList<Vector2[]>();
            Vector2[] v = new Vector2[4];
            boolean lastWasThere = false;

            for (int col = 0; col < layer.getWidth(); col++) {

                // get cell
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);

                if (cell == null) {
                    lastWasThere = false;
                    if (v[0] != null) {
                        polygonVertices.add(v);
                    }
                    v = new Vector2[4];
                    continue;
                }

                bdef.type = BodyDef.BodyType.StaticBody;
                float corner = tileSize / 2 / PPM;
                float mapPosCol = (col + 0.5f) * tileSize / PPM - B2DVars.ShiftX;
                float mapPosRow = (row + 0.5f) * tileSize / PPM - B2DVars.ShiftY;

                if (lastWasThere) {

                    v[2] = new Vector2(
                            mapPosCol + corner_coords[4] * corner,
                            mapPosRow + corner_coords[5] * corner
                    );

                    v[3] = new Vector2(
                            mapPosCol + corner_coords[6] * corner,
                            mapPosRow + corner_coords[7] * corner
                    );

                } else {
                    v[0] = new Vector2(
                            mapPosCol + corner_coords[0] * corner,
                            mapPosRow + corner_coords[1] * corner
                    );

                    v[1] = new Vector2(
                            mapPosCol + corner_coords[2] * corner,
                            mapPosRow + corner_coords[3] * corner
                    );

                    v[2] = new Vector2(
                            mapPosCol + corner_coords[4] * corner,
                            mapPosRow + corner_coords[5] * corner
                    );

                    v[3] = new Vector2(
                            mapPosCol + corner_coords[6] * corner,
                            mapPosRow + corner_coords[7] * corner
                    );

                }
                lastWasThere = true;

            }

            for (Vector2[] polygon : polygonVertices) {

                shape.set(polygon);
                fdef.filter.categoryBits = bits;
                fdef.filter.maskBits = B2DVars.BIT_PLAYER;
                fdef.isSensor = isSensor;
                if (layer.getName().equals("checkpoints")) {
                    createCheckpoints(new Vector2(polygon[2].x - (tileSize / 2) / PPM + ShiftX, polygon[2].y + ShiftY));

                } else {
                    world.createBody(bdef).createFixture(fdef).setUserData(layer.getName());
                }

            }
        }
    }

    ////////////////////////////////////////////////////////////////////      Handle I/O devices    ////////////////////////////////////////////////////////////////////

    public void handleInput() {

        current_force = player.getBody().getLinearVelocity();

        //player jump / double jump
        if (MyInput.isPressed(MyInput.JUMP)) {
            if (cl.isPlayerOnGround()) {
                player.getBody().applyForceToCenter(0, PLAYER_DASH_FORCE_UP, true);
            } else if (cl.hasDoubleJump()) {
                player.getBody().applyForceToCenter(0, PLAYER_DASH_FORCE_UP, true);
                cl.setDoubleJump(false);
            }
        }

        //player move left
        if (MyInput.isDown(MyInput.MOVE_LEFT)) {
            if (current_force.x > -MAX_SPEED) {
                player.getBody().applyForceToCenter(-PLAYER_SPEED, 0, true);
            }
        }

        //player dash left
        if (MyInput.isPressed(MyInput.MOVE_LEFT)) {
            if (!cl.isPlayerOnGround() && cl.hasDash()) {
                player.getBody().applyForceToCenter(-PLAYER_DASH_FORCE_SIDE, 0, true);
                cl.setDash(false);
            }
        }

        //player move right
        if (MyInput.isDown(MyInput.MOVE_RIGHT)) {
            if (current_force.x < MAX_SPEED) {
                player.getBody().applyForceToCenter(PLAYER_SPEED, 0, true);
            }
        }

        //player dash right
        if (MyInput.isPressed(MyInput.MOVE_RIGHT)) {
            if (!cl.isPlayerOnGround() && cl.hasDash()) {
                player.getBody().applyForceToCenter(PLAYER_DASH_FORCE_SIDE, 0, true);
                cl.setDash(false);
            }
        }
    }

    public void update(float dt) {

        handleInput();

        world.step(dt, 10, 2); // recommended values
        UpdateProps(dt);

    }

    private void UpdateProps(float dt) {
        //call update animation
        if (!cl.IsPlayerDead()) {
            player.update(dt);
        } else {
            world.destroyBody(player.getBody());
            bdef = new BodyDef();
            fdef = new FixtureDef();
            player = InitPlayer();
            cl.setPlayerDead(false);
        }
        // create a new checkpoint if needed
        if (checkpoint != null) {
            if (cl.isNewCheckpoint()) {
                cl.setNewCheckpoint(false);
                createCheckpoints(cl.getCurCheckpoint());
            }
            checkpoint.update(dt);
        }

        // dispose of old bodies
        Body temp = cl.removeOldCheckpoint();
        if (temp != null) {
            world.destroyBody(temp);
            cl.resetOldCheckpoint();
            System.out.println("Number of bodeies:" + world.getBodyCount());
        }
    }

    public void render() {

        //clear screen
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //set camera to follow player
        //if (!cl.IsPlayerDead()) {
        cam.position.set(
                player.getPosition().x * PPM,
                player.getPosition().y * PPM,
                0);
        //}

        cam.update();

        //draw tilemap
        tmr.setView(cam);
        tmr.render();

        //draw player
        sb.setProjectionMatrix(cam.combined);
        if (player != null) {
            player.render(sb);
        }

        // draw checkpoint
        if (checkpoint != null) {
            checkpoint.render(sb);
        }

        //draw boxes around stuff
        //b2dr.render(world, b2dcam.combined);

    }

    public void dispose() {
    }
}
