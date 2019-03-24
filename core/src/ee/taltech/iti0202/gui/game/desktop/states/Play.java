package ee.taltech.iti0202.gui.game.desktop.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.entities.Boss;
import ee.taltech.iti0202.gui.game.desktop.entities.Checkpoint;
import ee.taltech.iti0202.gui.game.desktop.entities.MagmaWorm;
import ee.taltech.iti0202.gui.game.desktop.entities.MagmaWormProperties;
import ee.taltech.iti0202.gui.game.desktop.entities.Player;
import ee.taltech.iti0202.gui.game.desktop.handlers.gdx.GameStateManager;
import ee.taltech.iti0202.gui.game.desktop.handlers.gdx.MyContactListener;
import ee.taltech.iti0202.gui.game.desktop.handlers.gdx.input.MyInput;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.PauseMenu;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.SettingsMenu;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.animations.Animation;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.animations.ParallaxBackground;
import ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.desktop.states.gameprogress.GameProgress;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BACKGROUND;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BACKGROUND_SCREENS;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BIT_ALL;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BIT_BOSSES;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BIT_WORM;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BOSS;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.COLOSSEOS;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.DEBUG;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.DIMENTSION_1;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.DIMENTSION_2;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.FRICTION;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.GRAVITY;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.MAGMAWORM;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.MAX_SPEED;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.NONE;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PATH;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PLAYER_DASH_FORCE_SIDE;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PLAYER_DASH_FORCE_UP;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PLAYER_SPEED;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PPM;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.SCALE;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.SQUARE_CORNERS;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.TERRA_DIMENTSION_1;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.TERRA_DIMENTSION_2;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.TERRA_SQUARES;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_WIDTH;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.pauseState.PAUSE;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.pauseState.RUN;

public class Play extends GameState {

    private World world;
    private Box2DDebugRenderer b2dr;
    private OrthographicCamera b2dcam;
    private OrthographicCamera hudCam;
    private MyContactListener cl;
    private TiledMap tiledMap;
    private Map<TiledMapTileLayer.Cell, Animation> animatedCells;
    private OrthogonalTiledMapRenderer renderer;
    private Player player;
    private boolean dimension;
    private boolean dimensionJump;
    private Array<Boss> bossArray;
    private Checkpoint checkpoint;
    private Vector2 tempPlayerLocation;
    private Vector2 tempPlayerVelocity;
    private Vector2 initPlayerLocation;
    private BodyDef bdef;
    private PolygonShape polyShape;
    private CircleShape circle;
    private FixtureDef fdef;
    private PauseMenu pauseMenu;
    private SettingsMenu settingsMenu;
    private ShapeRenderer shapeRenderer;
    private Stage stage;
    private ParallaxBackground parallaxBackground;
    private Vector2 current_force;
    private TiledMapTileLayer background;
    private TiledMapTileLayer foreground;
    private TiledMapTileLayer dimension_2;
    private TiledMapTileLayer dimension_1;
    private B2DVars.pauseState playState;


    ////////////////////////////////////////////////////////////////////         Set up game        ////////////////////////////////////////////////////////////////////

    public Play(GameStateManager gsm, int act, String map) {

        super(gsm);
        // sey up world
        world = new World(new Vector2(0, GRAVITY), true);
        cl = new MyContactListener();
        world.setContactListener(cl);
        if (DEBUG) b2dr = new Box2DDebugRenderer();

        // create shapes
        bdef = new BodyDef();
        polyShape = new PolygonShape();
        circle = new CircleShape();
        fdef = new FixtureDef();

        // create array for bosses
        dimensionJump = false;
        dimension = true;
        tempPlayerLocation = new Vector2();
        tempPlayerVelocity = new Vector2();
        bossArray = new Array<>();
        initPlayer();

        //set up cameras
        b2dcam = new OrthographicCamera();
        b2dcam.setToOrtho(false, V_WIDTH / PPM, V_HEIGHT / PPM);
        hudCam = new OrthographicCamera();
        hudCam.setToOrtho(false, V_WIDTH / PPM, V_HEIGHT / PPM);

        // create pause state
        pauseMenu = new PauseMenu(act, map, hudCam);
        settingsMenu = new SettingsMenu(hudCam);
        shapeRenderer = new ShapeRenderer();
        playState = B2DVars.pauseState.RUN;

        // set up background
        current_force = new Vector2(0, 0);
        Texture texture = Game.res.getTexture(BACKGROUND_SCREENS.get(act));
        texture.setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.ClampToEdge);
        Array<Texture> textures = new Array<>();
        textures.add(texture);
        parallaxBackground = new ParallaxBackground(textures);
        stage = new Stage();

        // draw background
        parallaxBackground.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(parallaxBackground);

        ////////////////////////////////    Tiled stuff here    ///////////////////////


        // load tiled map
        String path = PATH + "maps/levels/Act_" + act + "/" + map;
        tiledMap = new TmxMapLoader().load(path);
        renderer = new OrthogonalTiledMapRenderer(tiledMap);
        animatedCells = new HashMap<>();

        drawLayers();
    }


    ////////////////////////////////////////////////////////////////////   Create Animated bodies   ////////////////////////////////////////////////////////////////////

    private void initPlayer() {
        if (player != null) world.destroyBody(player.getBody());
        bdef = new BodyDef();
        fdef = new FixtureDef();
        circle = new CircleShape();
        polyShape = new PolygonShape();


        if (dimensionJump) {
            dimensionJump = false;
            bdef.position.set(new Vector2(tempPlayerLocation.x, tempPlayerLocation.y + 1 / PPM));
            bdef.linearVelocity.set(new Vector2(tempPlayerVelocity));
        } else if (checkpoint == null) if (initPlayerLocation == null) bdef.position.set(0, 0);
        else bdef.position.set(initPlayerLocation);
        else if (cl.isInitSpawn()) bdef.position.set(initPlayerLocation);
        else bdef.position.set(new Vector2(checkpoint.getPosition()));

        short mask;
        if (dimension)
            mask = BIT_BOSSES | BIT_WORM | DIMENTSION_1 | DIMENTSION_2 | TERRA_SQUARES | BACKGROUND | TERRA_DIMENTSION_1;
        else
            mask = BIT_BOSSES | BIT_WORM | DIMENTSION_1 | DIMENTSION_2 | TERRA_SQUARES | BACKGROUND | TERRA_DIMENTSION_2;
        bdef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bdef);

        polyShape.setAsBox(2 / PPM, 8 / PPM, new Vector2(-20 / PPM, 20 / PPM), 0);
        fdef.shape = polyShape;
        fdef.filter.categoryBits = DIMENTSION_1 | DIMENTSION_2;
        fdef.filter.maskBits = mask;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("side_l");

        polyShape.setAsBox(2 / PPM, 8 / PPM, new Vector2(20 / PPM, 20 / PPM), 0);
        fdef.shape = polyShape;
        fdef.filter.categoryBits = DIMENTSION_1 | DIMENTSION_2;
        fdef.filter.maskBits = mask;
        body.createFixture(fdef).setUserData("side_r");


        fdef.isSensor = false;
        circle.setRadius(9 / PPM);
        fdef.shape = circle;
        fdef.filter.categoryBits = DIMENTSION_1 | DIMENTSION_2;
        fdef.filter.maskBits = mask;
        body.createFixture(fdef).setFriction(FRICTION);
        body.setUserData("playerBody");

        polyShape.setAsBox(8 / PPM, 16 / PPM, new Vector2(0, 12 / PPM), 0);
        fdef.shape = polyShape;
        fdef.filter.categoryBits = DIMENTSION_1 | DIMENTSION_2;
        fdef.filter.maskBits = mask;
        body.createFixture(fdef).setUserData("playerBody");

        polyShape.setAsBox(1 / PPM, 1 / PPM, new Vector2(0, -15 / PPM), 0);
        fdef.shape = polyShape;
        fdef.filter.categoryBits = DIMENTSION_1 | DIMENTSION_2;
        fdef.filter.maskBits = mask;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("foot");

        player = new Player(body);

        cam.position.set(
                player.getPosition().x * PPM,
                player.getPosition().y * PPM,
                0);
    }

    private void createBosses(Vector2 position, String type) {

        /////////////////////////////////////////////////////////////////////////
        //                                                                     //
        //   TYPE 1: MAGMA WORM, can flu through walls n shit                  //
        //   TYPE 2: COLOSSEOS, net.dermetfan.gdx.physics.box2d.Breakable      //
        //   TYPE 3: idk                                                       //
        //                                                                     //
        /////////////////////////////////////////////////////////////////////////


        switch (type) {
            case MAGMAWORM:
                MagmaWormProperties alias = new MagmaWormProperties(bdef, fdef, position);

                Body body = world.createBody(alias.getBdef());
                body.setUserData("boss");
                body.createFixture(alias.getFdef());
                Boss boss = new MagmaWorm(body, type);
                bossArray.add(boss);
                break;

            case COLOSSEOS:
                break;

            default:
                break;

        }
    }

    private void createCheckpoints(Vector2 pos) {
        System.out.println("new checkpoint");
        bdef = new BodyDef();
        bdef.position.set(pos);
        bdef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bdef);
        polyShape = new PolygonShape();
        polyShape.setAsBox(4 / PPM, 32 / PPM, new Vector2(0, 4 / PPM), 0);
        fdef.shape = polyShape;
        fdef.filter.categoryBits = DIMENTSION_1 | DIMENTSION_2;
        fdef.filter.maskBits = B2DVars.BIT_ALL;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("checkpoint");
        checkpoint = new Checkpoint(body);
    }


    ////////////////////////////////////////////////////////////////////    Read and draw the map   ////////////////////////////////////////////////////////////////////

    private void drawLayers() {
        for (MapLayer layer : tiledMap.getLayers()) {
            switch (layer.getName()) {
                case "barrier":
                    fdef.filter.categoryBits = BIT_ALL;
                    fdef.filter.maskBits = BIT_ALL;
                    determineMapObject(layer);
                    break;
                case "hitboxes_1":
                    fdef.filter.categoryBits = TERRA_DIMENTSION_1;
                    fdef.filter.maskBits = BIT_BOSSES | DIMENTSION_1;
                    determineMapObject(layer);
                    break;
                case "hitboxes_2":
                    fdef.filter.categoryBits = TERRA_DIMENTSION_2;
                    fdef.filter.maskBits = BIT_BOSSES | DIMENTSION_2;
                    determineMapObject(layer);
                    break;
                case "hitboxes":
                    fdef.filter.categoryBits = TERRA_SQUARES;
                    fdef.filter.maskBits = BIT_BOSSES | DIMENTSION_1 | DIMENTSION_2;
                    determineMapObject(layer);
                    break;
                default:
                    ReadVertices((TiledMapTileLayer) layer);
            }
        }
    }

    private void determineMapObject(MapLayer layer) {
        Shape shape;
        for (MapObject object : layer.getObjects()) {
            if (object instanceof RectangleMapObject)
                shape = getRectangle((RectangleMapObject) object);
            else if (object instanceof PolygonMapObject)
                shape = getPolygon((PolygonMapObject) object);
            else if (object instanceof PolylineMapObject)
                shape = getPolyline((PolylineMapObject) object);
            else if (object instanceof EllipseMapObject)
                shape = getCircle((EllipseMapObject) object);
            else continue;
            bdef.type = BodyDef.BodyType.StaticBody;
            fdef.isSensor = false;
            fdef.shape = shape;
            world.createBody(bdef).createFixture(fdef).setUserData(layer.getName());
        }
    }

    private void ReadVertices(TiledMapTileLayer layer) {
        int[] corner_coords = SQUARE_CORNERS;
        String type = layer.getName();
        boolean isSensor = false;
        float tileSize = layer.getTileWidth();

        switch (type) {

            case "dimension_1":
                dimension_1 = layer;
                isSensor = true;
                layer.setVisible(true);
                background = layer;
                break;

            case "dimension_2":
                dimension_2 = layer;
                isSensor = true;
                layer.setVisible(true);
                background = layer;
                break;

            case "background":
                isSensor = true;
                layer.setVisible(true);
                background = layer;
                break;

            case "foreground":
                isSensor = true;
                layer.setVisible(true);
                foreground = layer;
                break;

            default:
                layer.setVisible(false);
                break;
        }

        bdef.type = BodyDef.BodyType.StaticBody;

        for (int row = 0; row <= layer.getHeight(); row++) {

            List<Vector2[]> polygonVertices = new ArrayList<>();
            Vector2[] v = new Vector2[4];
            boolean lastWasThere = false;

            for (int col = 0; col <= layer.getWidth(); col++) {

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
                fixBleeding(cell.getTile().getTextureRegion());
                if (cell.getTile().getProperties().containsKey("animation")) {
                    Texture tex = Game.res.getTexture("Player");
                    TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[0];
                    animatedCells.put(cell, new Animation(sprites, 1 / 12f));
                }

                float corner = tileSize / 2 / PPM;
                float mapPosCol = (col + 0.5f) * tileSize / PPM;
                float mapPosRow = (row + 0.5f) * tileSize / PPM;

                // writing vertices for hit boxes
                if (lastWasThere) {

                    v[2] = new Vector2(mapPosCol + corner_coords[4] * corner, mapPosRow + corner_coords[5] * corner);
                    v[3] = new Vector2(mapPosCol + corner_coords[6] * corner, mapPosRow + corner_coords[7] * corner);

                } else {
                    v[0] = new Vector2(mapPosCol + corner_coords[0] * corner, mapPosRow + corner_coords[1] * corner);
                    v[1] = new Vector2(mapPosCol + corner_coords[2] * corner, mapPosRow + corner_coords[3] * corner);
                    v[2] = new Vector2(mapPosCol + corner_coords[4] * corner, mapPosRow + corner_coords[5] * corner);
                    v[3] = new Vector2(mapPosCol + corner_coords[6] * corner, mapPosRow + corner_coords[7] * corner);

                }
                lastWasThere = true;

            }

            for (Vector2[] polygon : polygonVertices) {

                polyShape.set(polygon);
                fdef.filter.categoryBits = NONE;
                fdef.filter.maskBits = NONE;
                fdef.isSensor = isSensor;
                switch (layer.getName()) {
                    case "checkpoints":
                        createCheckpoints(new Vector2(polygon[1].x, polygon[0].y));
                        break;

                    case "bosses":
                        createBosses(new Vector2(polygon[2].x - (tileSize / 2) / PPM, polygon[2].y), layer.getProperties().get(BOSS).toString());
                        break;

                    case "player":
                        initPlayerLocation = new Vector2(polygon[2].x + (tileSize / 2) / PPM, polygon[2].y);
                        initPlayer();
                        break;

                    default:
                        world.createBody(bdef).createFixture(fdef).setUserData(layer.getName());
                        break;
                }
            }
        }
    }

    private static PolygonShape getRectangle(RectangleMapObject rectangleObject) {
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) / PPM,
                (rectangle.y + rectangle.height * 0.5f) / PPM);
        polygon.setAsBox(rectangle.width * 0.5f / PPM,
                rectangle.height * 0.5f / PPM,
                size,
                0.0f);
        return polygon;
    }

    private static CircleShape getCircle(EllipseMapObject circleObject) {
        Ellipse circle = circleObject.getEllipse();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(circle.width / PPM);
        circleShape.setPosition(new Vector2(circle.x / PPM, circle.y / PPM));
        return circleShape;
    }

    private static PolygonShape getPolygon(PolygonMapObject polygonObject) {
        PolygonShape polygon = new PolygonShape();
        float[] vertices = polygonObject.getPolygon().getTransformedVertices();

        float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; ++i) {
            worldVertices[i] = vertices[i] / PPM;
        }

        polygon.set(worldVertices);
        return polygon;
    }

    private static ChainShape getPolyline(PolylineMapObject polylineObject) {
        float[] vertices = polylineObject.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; ++i) {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = vertices[i * 2] / PPM;
            worldVertices[i].y = vertices[i * 2 + 1] / PPM;
        }

        ChainShape chain = new ChainShape();
        chain.createChain(worldVertices);
        return chain;
    }

    // private static void fixBleeding(TextureRegion[][] region) {
    //     for (TextureRegion[] array : region) {
    //         for (TextureRegion texture : array) {
    //             fixBleeding(texture);
    //         }
    //     }
    // }

    private static void fixBleeding(TextureRegion region) {
        float fix = 0.01f;

        float x = region.getRegionX();
        float y = region.getRegionY();
        float width = region.getRegionWidth();
        float height = region.getRegionHeight();
        float invTexWidth = 1f / region.getTexture().getWidth();
        float invTexHeight = 1f / region.getTexture().getHeight();
        region.setRegion((x + fix) * invTexWidth, (y + fix) * invTexHeight, (x + width - fix) * invTexWidth, (y + height - fix) * invTexHeight); // Trims
        // region
    }


    ////////////////////////////////////////////////////////////////////      Handle I/O devices    ////////////////////////////////////////////////////////////////////

    public void handleInput() {

        current_force = player.getBody().getLinearVelocity();

        //pause screen
        if (MyInput.isPressed(Game.settings.ESC)) {
            if (playState == RUN && Math.abs(current_force.x) < 1 && Math.abs(current_force.y) < .5f)
                playState = PAUSE;
            else
                playState = RUN;
        }

        //change dimension
        if (MyInput.isPressed(Game.settings.CHANGE_DIMENTION)) {
            System.out.println("changed dimension");
            dimensionJump = true;
            tempPlayerLocation = player.getPosition();
            tempPlayerVelocity = player.getBody().getLinearVelocity();
            dimension = !dimension;
            cl.setPlayerDead(true);
        }

        //player jump / double jump / dash
        if (MyInput.isPressed(Game.settings.JUMP)) {
            if (cl.isPlayerOnGround()) {
                player.getBody().applyForceToCenter(0, PLAYER_DASH_FORCE_UP, true);
            } else if (cl.isWallJump() != 0) {
                player.getBody().applyForceToCenter(cl.isWallJump() * PLAYER_DASH_FORCE_UP, PLAYER_DASH_FORCE_UP, true);
                cl.setWallJump(0);
            } else if (cl.hasDoubleJump()) {
                player.getBody().applyForceToCenter(0, PLAYER_DASH_FORCE_UP, true);
                cl.setDoubleJump(false);
            }
        }

        //player move left
        if (MyInput.isDown(Game.settings.MOVE_LEFT)) {
            if (current_force.x > -MAX_SPEED) {
                if (cl.isPlayerOnGround()) {
                    player.getBody().applyForceToCenter(-PLAYER_SPEED, 0, true);
                } else {
                    player.getBody().applyForceToCenter(-PLAYER_SPEED * 1.25f, 0, true);
                }

            }
        }

        //player dash left
        if (MyInput.isPressed(Game.settings.MOVE_LEFT)) {
            if (!cl.isPlayerOnGround() && cl.hasDash()) {
                current_force = player.getBody().getLinearVelocity();
                if (current_force.x > 0) {
                    player.getBody().applyForceToCenter(-current_force.x * PPM / 3, 0, true);
                } else {
                    player.getBody().applyForceToCenter(-PLAYER_DASH_FORCE_SIDE, 0, true);
                }
                cl.setDash(false);
            }
        }

        //player move right
        if (MyInput.isDown(Game.settings.MOVE_RIGHT)) {
            if (current_force.x < MAX_SPEED) {
                if (cl.isPlayerOnGround()) {
                    player.getBody().applyForceToCenter(PLAYER_SPEED, 0, true);
                } else {
                    player.getBody().applyForceToCenter(PLAYER_SPEED * 1.25f, 0, true);
                }
            }
        }

        //player dash right
        if (MyInput.isPressed(Game.settings.MOVE_RIGHT)) {
            if (!cl.isPlayerOnGround() && cl.hasDash()) {
                if (current_force.x < 0) {
                    player.getBody().applyForceToCenter(-current_force.x * PPM / 3, 0, true);
                } else {
                    player.getBody().applyForceToCenter(PLAYER_DASH_FORCE_SIDE, 0, true);
                }
                cl.setDash(false);
            }
        }
    }

    public void update(float dt) {

        handleInput();

        world.step(dt, 10, 2); // recommended values

        switch (playState) {
            case RUN:
                UpdateProps(dt);
                break;

            case PAUSE:
                pauseMenu.update(dt);
                break;

            case RESUME:
                break;

            case SETTINGS:
                settingsMenu.update(dt);
                break;

            case STOPPED:
                break;

            default:
                break;
        }
    }

    private void UpdateProps(float dt) {
        //update camera
        if (DEBUG) {

            b2dcam.position.set(
                    player.getPosition().x,
                    player.getPosition().y,
                    0);
            b2dr.render(world, b2dcam.combined);

            cam.position.set(
                    player.getPosition().x * PPM,
                    player.getPosition().y * PPM,
                    0);
            b2dcam.update();

        } else {

            cam.position.x += (player.getPosition().x - cam.position.x / PPM) * 2 * PPM * dt;
            cam.position.y += (player.getPosition().y - cam.position.y / PPM) * 2 * PPM * dt;
        }

        cam.update();

        //call update animation
        if (!cl.IsPlayerDead()) player.update(dt);
        else {
            initPlayer();
            cl.setPlayerDead(false);
        }

        //draw tilemap animations
        if (animatedCells != null) {
            for (Animation animation : animatedCells.values()) {
                animation.update(dt);
            }
        }

        // create a new checkpoint if needed
        if (checkpoint != null) {
            if (cl.isNewCheckpoint()) {
                cl.setNewCheckpoint(false);
                createCheckpoints(cl.getCurCheckpoint());
            }
            checkpoint.update(dt);
        }

        if (bossArray != null) for (Boss boss : bossArray) boss.update(dt);


        // dispose of old bodies
        Body temp = cl.removeOldCheckpoint();
        if (temp != null) {
            world.destroyBody(temp);
            cl.resetOldCheckpoint();
        }
    }

    public void render() {

        switch (playState) {
            case RUN:
                drawAndSetCamera();
                break;

            case PAUSE:
                handlePauseInput();
                drawPauseScreen();
                break;

            case RESUME:
                break;

            case SETTINGS:
                handleSettingsInput();
                drawPauseScreen();
                break;

            default:
                break;
        }
    } //TODO: render update rectangle around player and smoorther paste

    private void handlePauseInput() {
        if (MyInput.isMouseClicked(Game.settings.SHOOT)) {
            switch (pauseMenu.getCur_block()) {
                case RESUME:
                    playState = RUN;
                    break;
                case SAVE:
                    saveGame();
                    break;
                case SAVEANDEXIT:
                    saveGame();
                    gsm.pushState(GameStateManager.State.MENU);
                    break;
                case EXIT:
                    gsm.pushState(GameStateManager.State.MENU);
                    break;
                case SETTINGS:
                    playState = B2DVars.pauseState.SETTINGS;
                    break;
            }
        }
    }

    private void handleSettingsInput() {
        settingsMenu.handleKey(MyInput.getKeyDown());

        if (MyInput.isMouseClicked(Game.settings.SHOOT)) {
            switch (settingsMenu.getCur_block()) {
                case EXIT:
                    playState = PAUSE;
                    break;
                case SAVE:
                    Game.settings.save(B2DVars.PATH + "settings/settings.json");
                    break;
                case NEXT:
                    Game.settings = Game.settings.loadDefault();
                    settingsMenu.updateAllBindsDisplayed();
                    break;
                case LOAD:
                    Game.settings = Game.settings.load(B2DVars.PATH + "settings/settings.json");
                    settingsMenu.updateAllBindsDisplayed();
                    break;
                case SETTINGS:
                    settingsMenu.handleSettingsButtonClick();
                    break;
            }
        }
    }

    private void drawPauseScreen() {
        //clear screen gradually by shading it
        if (playState == B2DVars.pauseState.PAUSE) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(new Color(0, 0, 0, 0.05f));
            shapeRenderer.rect(0, 0, V_WIDTH * SCALE, V_HEIGHT * SCALE);
            shapeRenderer.end();
        } else {
            // TODO: No fade in settings.
            // Help pls!
        }

        //render pauseMenu

        hudCam.update();

        if (playState == B2DVars.pauseState.SETTINGS) settingsMenu.render(sb);
        else pauseMenu.render(sb);
    }


    private void drawAndSetCamera() {

        //clear screen
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl20.glClear(GL20.GL_ALPHA_BITS);

        //set camera to follow player
        if (current_force.x < -1) parallaxBackground.setSpeed(-1f);
        else if (current_force.x > 1) parallaxBackground.setSpeed(1f);
        else parallaxBackground.setSpeed(0);
        stage.act();
        stage.draw();

        //render animations
        if (animatedCells != null) for (TiledMapTileLayer.Cell cell : animatedCells.keySet())
            cell.setTile(new StaticTiledMapTile(animatedCells.get(cell).getFrame()));

        //draw tilemap
        renderer.setView(cam);
        renderer.getBatch().begin();
        if (background != null) renderer.renderTileLayer(background);
        if (foreground != null) renderer.renderTileLayer(foreground);
        if (dimension_1 != null) renderer.renderTileLayer(dimension_1);
        if (dimension_2 != null) renderer.renderTileLayer(dimension_2);
        renderer.getBatch().end();

        //draw player
        sb.setProjectionMatrix(cam.combined);
        if (player != null) player.render(sb);

        if (bossArray != null) for (Boss boss : bossArray) boss.render(sb);

        // draw checkpoint
        if (checkpoint != null) checkpoint.render(sb);
    }

    private void saveGame() {
        GameProgress progress = new GameProgress();
        progress.playerLocationX = player.getPosition().x;
        progress.playerLocationY = player.getPosition().y;

        progress.save(B2DVars.PATH + "gameprogress/" + new SimpleDateFormat("dd-mm-YYYY_HH,mm,ss").format(new Date()) + ".json");
    }

    public void dispose() {
        Gdx.input.setInputProcessor(null);
        stage.dispose();
        System.gc();
    }
}
