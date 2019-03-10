package ee.taltech.iti0202.gui.game.desktop.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars;
import ee.taltech.iti0202.gui.game.desktop.handlers.GameStateManager;
import ee.taltech.iti0202.gui.game.desktop.handlers.MyContactListener;
import ee.taltech.iti0202.gui.game.desktop.handlers.MyInput;
import ee.taltech.iti0202.gui.game.desktop.handlers.ParallaxBackground;
import ee.taltech.iti0202.gui.game.desktop.handlers.PauseMenu;
import ee.taltech.iti0202.gui.game.entities.Boss;
import ee.taltech.iti0202.gui.game.entities.Checkpoint;
import ee.taltech.iti0202.gui.game.entities.Player;

import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.BOSS;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.CORNER_LOCATION;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.FRICTION;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.GRAVITY;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.MAX_SPEED;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.PATH;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.PLAYER_DASH_FORCE_SIDE;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.PLAYER_DASH_FORCE_UP;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.PLAYER_SPEED;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.PPM;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.SCALE;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.SQUARE_CORNERS;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.STEP;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.TRIANGLE_BOTTOM_LEFT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.TRIANGLE_BOTTOM_RIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.TRIANGLE_TOP_LEFT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.TRIANGLE_TOP_RIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.TYPE;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.V_WIDTH;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.pauseState.PAUSE;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.pauseState.RUN;

public class Play extends GameState {

    private World world;
    //private Box2DDebugRenderer b2dr; // for showing hitboxes
    private OrthographicCamera b2dcam;
    private OrthographicCamera hudCam;
    private MyContactListener cl;
    private TiledMap tiledMap;
    private float tileSize;
    private OrthoCachedTiledMapRenderer tmr;
    private Player player;
    private Array<Boss> bossArray;
    private Boss boss;
    private Checkpoint checkpoint;
    private Vector2 initPlayerLocation;
    private BodyDef bdef;
    private PolygonShape shape;
    private CircleShape circle;
    private FixtureDef fdef;
    private PauseMenu pauseMenu;
    private ShapeRenderer shapeRenderer;
    private Stage stage;
    private ParallaxBackground parallaxBackground;
    private Vector2 current_force = new Vector2(0, 0);

    ////////////////////////////////////////////////////////////////////         Set up game        ////////////////////////////////////////////////////////////////////

    public Play(GameStateManager gsm, int act, int map) {

        super(gsm);

        System.out.println("Tere");
        // sey up box2d stuff
        world = new World(new Vector2(0, GRAVITY), true);
        cl = new MyContactListener();
        world.setContactListener(cl);
        //b2dr = new Box2DDebugRenderer();


        // create shapes
        bdef = new BodyDef();
        shape = new PolygonShape();
        circle = new CircleShape();
        fdef = new FixtureDef();

        // create array for bosses
        initPlayer();
        bossArray = new Array<>();

        //set up box2d cam
        b2dcam = new OrthographicCamera();
        b2dcam.setToOrtho(false, V_WIDTH / PPM, V_HEIGHT / PPM);

        hudCam = new OrthographicCamera();
        hudCam.setToOrtho(false, V_WIDTH / PPM, V_HEIGHT / PPM);

        pauseMenu = new PauseMenu(act, map, hudCam);
        shapeRenderer = new ShapeRenderer();

        // set up background
        Texture texture = Game.res.getTexture("starBackground");
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        TextureRegion textureRegion = new TextureRegion(Game.res.getTexture("starBackground"), 0, 0, V_WIDTH, V_HEIGHT);
        Array<Texture> textures = new Array<>();
        textures.add(texture);

        parallaxBackground = new ParallaxBackground(textures);
        stage = new Stage();

        // draw background
        parallaxBackground.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        stage.addActor(parallaxBackground);

        ////////////////////////////////    Tiled stuff here    ///////////////////////


        // load tiled map
        String path = PATH + "maps/level_" + act + "_" + map + ".tmx";
        tiledMap = new TmxMapLoader().load(path);
        tmr = new OrthoCachedTiledMapRenderer(tiledMap);

        drawLayers();

    }


    ////////////////////////////////////////////////////////////////////   Create Animated bodies   ////////////////////////////////////////////////////////////////////

    private Player initPlayer() {

        circle = new CircleShape();
        if (checkpoint == null) {
            if (initPlayerLocation == null)
                bdef.position.set(0, 0); //incase you forgot to include player in the first place
            else bdef.position.set(initPlayerLocation);
        } else {
            if (cl.isInitSpawn()) bdef.position.set(initPlayerLocation);
            else
                bdef.position.set(new Vector2(checkpoint.getPosition().x, checkpoint.getPosition().y));
        }
        bdef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bdef);
        fdef.isSensor = false;
        circle.setRadius(6 / PPM);
        fdef.shape = circle;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_PLAYER;
        body.createFixture(fdef).setFriction(FRICTION);
        body.setFixedRotation(false);
        body.setUserData("playerBall");

        shape = new PolygonShape();
        shape.setAsBox(4 / PPM, 8 / PPM, new Vector2(0, 8 / PPM), 0);
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

    private void createBosses(Vector2 position, String type) {

        /*
         *
         * TYPE 1: MAGMA WORM, can flu through walls n shit
         * TYPE 2: COLOSSEOS, net.dermetfan.gdx.physics.box2d.Breakable
         * TYPE 3: idk
         *
         * */

        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(position);
        shape.setAsBox(10 / PPM, 10 / PPM);

        fdef.shape = shape;
        fdef.friction = 0.75f;
        fdef.restitution = 0.1f;
        fdef.density = 5;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_PLAYER;

        Body body = world.createBody(bdef);
        body.setUserData("boss");
        body.createFixture(fdef);
        boss = new Boss(body);
        bossArray.add(boss);

    }


    ////////////////////////////////////////////////////////////////////    Read and draw the map   ////////////////////////////////////////////////////////////////////

    private void drawLayers() {
        TiledMapTileLayer layer;


        layer = (TiledMapTileLayer) tiledMap.getLayers().get("terra_squares");
        draw_solid(layer, B2DVars.TERRA_SQUARES, false);

        layer = (TiledMapTileLayer) tiledMap.getLayers().get("border");
        if (layer != null) {
            tileSize = layer.getTileWidth();
            ReadPolygonVertices(layer, B2DVars.BIT_ALL, true);
            layer.setVisible(false); // disable visibility here <--------------------------------------------
        }

        layer = (TiledMapTileLayer) tiledMap.getLayers().get("bosses");
        if (layer != null) {
            tileSize = layer.getTileWidth();
            ReadPolygonVertices(layer, B2DVars.NONE, false);
            layer.setVisible(false);
        }

        layer = (TiledMapTileLayer) tiledMap.getLayers().get("checkpoints");
        draw_solid(layer, B2DVars.BACKGROUND, true);

        layer = (TiledMapTileLayer) tiledMap.getLayers().get("player");
        if (layer != null) {
            tileSize = layer.getTileWidth();
            ReadPolygonVertices(layer, B2DVars.NONE, false);
            layer.setVisible(false);
        }


        layer = (TiledMapTileLayer) tiledMap.getLayers().get("background");
        draw_solid(layer, B2DVars.NONE, false);


        layer = (TiledMapTileLayer) tiledMap.getLayers().get("terra_triangle_bottom_right");
        draw_solid(layer, B2DVars.TERRA_SQUARES, false);


        layer = (TiledMapTileLayer) tiledMap.getLayers().get("terra_triangle_bottom_left");
        draw_solid(layer, B2DVars.TERRA_SQUARES, false);


        layer = (TiledMapTileLayer) tiledMap.getLayers().get("terra_triangle_top_left");
        draw_solid(layer, B2DVars.TERRA_SQUARES, false);


        layer = (TiledMapTileLayer) tiledMap.getLayers().get("terra_triangle_top_right");
        draw_solid(layer, B2DVars.TERRA_SQUARES, false);

    }

    private void draw_solid(TiledMapTileLayer layer, short terraSquares, boolean b) {
        if (layer != null) {
            tileSize = layer.getTileWidth();
            ReadPolygonVertices(layer, terraSquares, b);
        }
    }

    private void ReadPolygonVertices(TiledMapTileLayer layer, short bits, boolean isSensor) {

        int[] corner_coords;
        String type = layer.getProperties().get(TYPE).toString();

        switch (type) {
            default:
                corner_coords = SQUARE_CORNERS;
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
                        corner_coords = SQUARE_CORNERS;
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

                shape.set(polygon);
                fdef.filter.categoryBits = bits;
                fdef.filter.maskBits = B2DVars.BIT_PLAYER;
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
                        world.destroyBody(player.getBody());
                        bdef = new BodyDef();
                        fdef = new FixtureDef();
                        player = initPlayer();
                        break;

                    default:
                        world.createBody(bdef).createFixture(fdef).setUserData(layer.getName());
                        break;
                }
            }
        }
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


    ////////////////////////////////////////////////////////////////////      Handle I/O devices    ////////////////////////////////////////////////////////////////////

    public void handleInput() {

        current_force = player.getBody().getLinearVelocity();

        //pause screen
        if (MyInput.isPressed(MyInput.ESC)) {
            if (pauseMenu.getPauseState() == RUN && Math.abs(current_force.x) < 1 && Math.abs(current_force.y) < .5f)
                pauseMenu.setGameState(PAUSE);
            else pauseMenu.setGameState(RUN);
        }

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
                if (cl.isPlayerOnGround()) {
                    player.getBody().applyForceToCenter(-PLAYER_SPEED, 0, true);
                } else {
                    player.getBody().applyForceToCenter(-PLAYER_SPEED * 1.25f, 0, true);
                }

            }
        }

        //player dash left
        if (MyInput.isPressed(MyInput.MOVE_LEFT)) {
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
        if (MyInput.isDown(MyInput.MOVE_RIGHT)) {
            if (current_force.x < MAX_SPEED) {
                if (cl.isPlayerOnGround()) {
                    player.getBody().applyForceToCenter(PLAYER_SPEED, 0, true);
                } else {
                    player.getBody().applyForceToCenter(PLAYER_SPEED * 1.25f, 0, true);
                }
            }
        }

        //player dash right
        if (MyInput.isPressed(MyInput.MOVE_RIGHT)) {
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

        switch (pauseMenu.getPauseState()) {
            case RUN:

                UpdateProps(dt);

            case PAUSE:
                pauseMenu.update(dt);

            case RESUME:
                break;

            default:
                break;
        }

    }

    private void UpdateProps(float dt) {
        //call update animation
        if (!cl.IsPlayerDead()) player.update(dt);
        else {
            world.destroyBody(player.getBody());
            bdef = new BodyDef();
            fdef = new FixtureDef();
            player = initPlayer();
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

        if (bossArray != null) for (Boss boss : bossArray) boss.update(dt);


        // dispose of old bodies
        Body temp = cl.removeOldCheckpoint();
        if (temp != null) {
            world.destroyBody(temp);
            cl.resetOldCheckpoint();
            System.out.println("Number of bodies:" + world.getBodyCount());
        }

    }

    public void render() {

        switch (pauseMenu.getPauseState()) {
            case RUN:
                drawAndSetCamera();
                break;

            case PAUSE:
                handlePauseInput();
                drawPauseScreen();
                break;

            case RESUME:
                break;

            default:
                break;

        }
    }

    private void handlePauseInput() {
        if (MyInput.isPressed(MyInput.SHOOT) && pauseMenu.getCur_block() == PauseMenu.block.RESUME)
            pauseMenu.setGameState(B2DVars.pauseState.RUN);
        if (MyInput.isPressed(MyInput.SHOOT) && pauseMenu.getCur_block() == PauseMenu.block.EXIT)
            gsm.pushState(GameStateManager.State.MENU);
    }

    private void drawPauseScreen() {
        //clear screen
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0, 0, 0, 0.05f));
        shapeRenderer.rect(0, 0, V_WIDTH * SCALE, V_HEIGHT * SCALE);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        //render pauseMenu

        hudCam.update();

        pauseMenu.render(sb);
    }

    private void drawAndSetCamera() {

        //clear screen
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        parallaxBackground.setSpeed(current_force.x);
        stage.act();
        stage.draw();

        //set camera to follow player
        cam.position.set(
                player.getPosition().x * PPM,
                player.getPosition().y * PPM,
                0);

        //b2dr.render(world, b2dcam.combined);

        //b2dcam.update();
        cam.update();

        //draw tilemap
        tmr.setView(cam);
        tmr.render();

        //draw player
        sb.setProjectionMatrix(cam.combined);
        if (player != null) {
            player.render(sb);
        }

        if (bossArray != null) {
            for (Boss boss : bossArray) boss.render(sb);
        }

        // draw checkpoint
        if (checkpoint != null) {
            checkpoint.render(sb);
        }
    }

    public void dispose() {
    }
}
