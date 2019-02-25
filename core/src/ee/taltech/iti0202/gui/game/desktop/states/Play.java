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
import ee.taltech.iti0202.gui.game.entities.Player;

import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.FRICTION;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.GRAVITY;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.MAX_SPEED;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.PLAYER_DASH_FORCE_SIDE;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.PLAYER_DASH_FORCE_UP;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.PLAYER_SPEED;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.PPM;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.SQUARE_CORNERS;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.TRIANGLE_BOTTOM_LEFT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.TRIANGLE_BOTTOM_RIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.TRIANGLE_TOP_LEFT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.TRIANGLE_TOP_RIGHT;
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
    private Vector2 current_force;
    private BodyDef bdef;
    private PolygonShape shape;
    private CircleShape circle;
    private FixtureDef fdef;

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
        InitPlayer(bdef, fdef);

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

    private void drawLayers() {
        TiledMapTileLayer layer;

        layer = (TiledMapTileLayer) tiledMap.getLayers().get("terra_squares");
        tileSize = layer.getTileWidth();
        ReadPolygonVertices(layer, B2DVars.TERRA_SQUARES);

        layer = (TiledMapTileLayer) tiledMap.getLayers().get("background");
        tileSize = layer.getTileWidth();
        ReadPolygonVertices(layer, B2DVars.BACKGROUND);

        layer = (TiledMapTileLayer) tiledMap.getLayers().get("terra_triangle_bottom_right");
        tileSize = layer.getTileWidth();
        ReadPolygonVertices(layer, B2DVars.TERRA_SQUARES);

        layer = (TiledMapTileLayer) tiledMap.getLayers().get("terra_triangle_bottom_left");
        tileSize = layer.getTileWidth();
        ReadPolygonVertices(layer, B2DVars.TERRA_SQUARES);

        layer = (TiledMapTileLayer) tiledMap.getLayers().get("terra_triangle_top_left");
        tileSize = layer.getTileWidth();
        ReadPolygonVertices(layer, B2DVars.TERRA_SQUARES);

        layer = (TiledMapTileLayer) tiledMap.getLayers().get("terra_triangle_top_right");
        tileSize = layer.getTileWidth();
        ReadPolygonVertices(layer, B2DVars.TERRA_SQUARES);




    }

    private void ReadPolygonVertices(TiledMapTileLayer layer, short bits) {

        int[] corner_coords = new int[8];
        System.out.println(layer.getProperties().get("1"));
        String type = layer.getProperties().get("1").toString();

        switch (type) {
            case "square":
                corner_coords = SQUARE_CORNERS;
                break;
            case "background":
                corner_coords = SQUARE_CORNERS;
                break;
            case "triangle":
                String triangle = layer.getProperties().get("2").toString();
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
                fdef.isSensor = false;
                world.createBody(bdef).createFixture(fdef).setUserData(layer.getName());

            }
        }
    }


    private void InitPlayer(BodyDef bdef, FixtureDef fdef) {
        circle = new CircleShape();
        bdef.position.set(160 / PPM, 210 / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bdef);
        circle.setRadius(5 / PPM);
        //shape.setAsBox(8 / PPM, 8 / PPM);
        fdef.shape = circle;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_PLAYER;
        body.createFixture(fdef).setFriction(FRICTION);
        body.setFixedRotation(false);
        body.setUserData("playerBottom");

        shape = new PolygonShape();
        shape.setAsBox(4 / PPM, 6 / PPM, new Vector2(0, 4 / PPM), 0);
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_PLAYER;
        fdef.isSensor = false;
        body.createFixture(fdef).setUserData("playerBottom");

        shape.setAsBox(2 / PPM, 2 / PPM, new Vector2(0, -13 / PPM), 0);
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_PLAYER;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("foot");

        player = new Player(body);
    }

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

        player.update(dt);
    }

    public void render() {

        //clear screen
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //set camera to follow player
        cam.position.set(
                player.getPosition().x * PPM,
                player.getPosition().y * PPM,
                0);
        cam.update();

        //draw tilemap
        tmr.setView(cam);
        tmr.render();

        //draw player
        sb.setProjectionMatrix(cam.combined);
        player.render(sb);

        //draw boxes around stuff
        b2dr.render(world, b2dcam.combined);

    }

    public void dispose() {
    }
}
