package ee.taltech.iti0202.gui.game.desktop.handlers.scene.layers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.BossLoader;
import ee.taltech.iti0202.gui.game.desktop.entities.staticobjects.Checkpoint;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.animations.Animation;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.bleeding.Bleeding;
import ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.desktop.states.Play;
import ee.taltech.iti0202.gui.game.desktop.states.gameprogress.BossData;
import ee.taltech.iti0202.gui.game.desktop.states.shapes.ShapesGreator;
import lombok.Data;
import lombok.ToString;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BACKGROUND;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BIT_BOSSES;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.DEBUG;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.DIMENTSION_1;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.DIMENTSION_2;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.NONE;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PATH;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PPM;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.SQUARE_CORNERS;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.TERRA_DIMENTSION_1;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.TERRA_DIMENTSION_2;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.TERRA_SQUARES;

@Data
public class Draw {

    ////////////////////////////////////////////////////////////////////    Read and draw the map   ////////////////////////////////////////////////////////////////////

    @ToString.Exclude
    private Play play;
    private SpriteBatch spriteBatch;
    private BodyDef bdef;
    private FixtureDef fdef;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer;
    private HashMap animatedCells;
    private TiledMapTileLayer background;
    private TiledMapTileLayer foreground;
    private TiledMapTileLayer dimension_2;
    private TiledMapTileLayer dimension_1;
    private boolean dimensionFadeDone = false;
    private boolean dimension;
    private float currentDimensionFade = B2DVars.DIMENSION_FADE_AMOUNT;
    private boolean gameFadeDone;
    private boolean gameFadeOut;
    private float currentMenuFade;

    public Draw(Play play, SpriteBatch sb) {
        this.play = play;
        this.spriteBatch = sb;
        this.bdef = new BodyDef();
        this.fdef = new FixtureDef();
        this.dimension = true;
        this.gameFadeOut = false;
        this.gameFadeDone = false;
        this.currentMenuFade = 1;

        // load tiled map
        String path = PATH + "maps/levels/" + play.getAct() + "/" + play.getMap();
        tiledMap = new TmxMapLoader().load(path);
        renderer = new OrthogonalTiledMapRenderer(tiledMap);
        animatedCells = new HashMap<>();
    }

    private void determineMapObject(MapLayer layer) {
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
            else continue;
            this.bdef.type = BodyDef.BodyType.StaticBody;
            this.fdef.isSensor = false;
            this.fdef.shape = shape;
            play.getWorld().createBody(this.bdef).createFixture(this.fdef).setUserData(layer.getName());
        }
    }

    private Checkpoint createEndPoint(Vector2 pos) {
        System.out.println("new endpoint");
        this.bdef = new BodyDef();
        this.bdef.position.set(pos);
        this.bdef.type = BodyDef.BodyType.StaticBody;
        Body body = play.getWorld().createBody(this.bdef);
        PolygonShape polyShape = new PolygonShape();
        polyShape.setAsBox(64 / PPM, 32 / PPM, new Vector2(0, 4 / PPM), 0);
        this.fdef.shape = polyShape;
        this.fdef.filter.categoryBits = DIMENTSION_1 | DIMENTSION_2;
        this.fdef.filter.maskBits = B2DVars.BIT_ALL;
        this.fdef.isSensor = true;
        body.createFixture(this.fdef).setUserData("end");
        return new Checkpoint(body, spriteBatch);
    }

    public void drawLayers(boolean defaultSpawn, List<BossData> bosses) {
        for (MapLayer layer : tiledMap.getLayers()) {
            switch (layer.getName()) {
                case "barrier":
                    this.fdef.filter.categoryBits = BACKGROUND;
                    this.fdef.filter.maskBits = BIT_BOSSES | DIMENTSION_1 | DIMENTSION_2;
                    determineMapObject(layer);
                    break;
                case "hitboxes_1":
                    this.fdef.filter.categoryBits = TERRA_DIMENTSION_1;
                    this.fdef.filter.maskBits = BIT_BOSSES | DIMENTSION_1;
                    determineMapObject(layer);
                    break;
                case "hitboxes_2":
                    this.fdef.filter.categoryBits = TERRA_DIMENTSION_2;
                    this.fdef.filter.maskBits = BIT_BOSSES | DIMENTSION_2;
                    determineMapObject(layer);
                    break;
                case "hitboxes":
                    this.fdef.filter.categoryBits = TERRA_SQUARES;
                    this.fdef.filter.maskBits = BIT_BOSSES | DIMENTSION_1 | DIMENTSION_2;
                    determineMapObject(layer);
                    break;
                default:
                    readVertices((TiledMapTileLayer) layer, defaultSpawn);
            }
        }

        if (!defaultSpawn) {
            BossLoader bossLoader = new BossLoader(play, spriteBatch, fdef, bdef);
            bossLoader.createAllBosses(bosses);
        }
    }

    private void readVertices(TiledMapTileLayer layer, boolean defaultSpawn) {
        int[] corner_coords = SQUARE_CORNERS;
        String type = layer.getName();
        boolean isSensor = false;
        float tileSize = layer.getTileWidth();

        switch (type) {

            case "dimension_1":
                dimension_1 = layer;
                isSensor = true;
                layer.setVisible(true);
                layer.setOpacity(1f);
                background = layer;
                break;

            case "dimension_2":
                dimension_2 = layer;
                isSensor = true;
                layer.setVisible(true);
                layer.setOpacity(0.5f);
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

        this.bdef.type = BodyDef.BodyType.StaticBody;

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

                Bleeding.fixBleeding(cell.getTile().getTextureRegion()); // fix bleeding hopefully

                if (cell.getTile().getProperties().containsKey("animation")) {
                    Texture tex = Game.res.getTexture("Player"); // TODO: <- Wut is dis? misleading names or obsolete?
                    TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[0];
                    play.getAnimatedCells().put(cell, new Animation(sprites, 1 / 12f));
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

                PolygonShape polyShape = new PolygonShape();
                polyShape.set(polygon);
                this.fdef.filter.categoryBits = NONE;
                this.fdef.filter.maskBits = NONE;
                this.fdef.isSensor = isSensor;
                BossLoader bossLoader = new BossLoader(play, spriteBatch, fdef, bdef);
                switch (layer.getName()) {
                    case "checkpoints":
                        if ((polygon[0].x - polygon[3].x) / (polygon[0].y - polygon[1].y) > 1.8) {
                            Checkpoint checkpoint = createEndPoint(new Vector2(polygon[1].x + tileSize / PPM, polygon[0].y));
                            play.getPlayerHandler().getCheckpointList().add(checkpoint);
                        } else {
                            if (B2DVars.CHECKPOINTS) {
                                Checkpoint checkpoint = createCheckpoints(new Vector2(polygon[1].x + (polygon[3].x - polygon[1].x) / 2, polygon[0].y));
                                play.getPlayerHandler().getCheckpointList().add(checkpoint);
                            }

                        }
                        break;

                    case "bosses_small":
                        if (B2DVars.BOSSES && defaultSpawn) {
                            bossLoader.createBosses(new Vector2(polygon[2].x - (tileSize / 2) / PPM, polygon[2].y), layer.getProperties().get("type").toString(), false, (Integer) layer.getProperties().get("size"));
                        }
                        break;

                    case "bosses_big":
                        if (B2DVars.BOSSES && defaultSpawn) {
                            bossLoader.createBosses(new Vector2(polygon[2].x - (tileSize / 2) / PPM, polygon[2].y), layer.getProperties().get("type").toString(), true, (Integer) layer.getProperties().get("size"));
                        }
                        break;
                    case "bosses":
                        if (B2DVars.BOSSES && defaultSpawn) {
                            bossLoader.createBosses(new Vector2(polygon[2].x - (tileSize / 2) / PPM, polygon[2].y), layer.getProperties().get("type").toString(), true, (Integer) layer.getProperties().get("size"));
                        }
                        break;

                    case "player":
                        if (play.getPlayerHandler().getInitPlayerLocation() == null) {
                            play.getPlayerHandler().setInitPlayerLocation(new Vector2(polygon[2].x + (tileSize / 2) / PPM, polygon[2].y));
                        }
                        break;

                    default:
                        play.getWorld().createBody(this.bdef).createFixture(this.fdef).setUserData(layer.getName());
                        break;
                }
            }
        }
    }

    private Checkpoint createCheckpoints(Vector2 pos) {
        System.out.println("new checkpoint");
        this.bdef = new BodyDef();
        this.fdef = new FixtureDef();
        this.bdef.position.set(pos);
        this.bdef.type = BodyDef.BodyType.StaticBody;
        Body body = play.getWorld().createBody(this.bdef);
        PolygonShape polyShape = new PolygonShape();
        polyShape.setAsBox(4 / PPM, 32 / PPM, new Vector2(0, 4 / PPM), 0);
        this.fdef.shape = polyShape;
        this.fdef.filter.categoryBits = DIMENTSION_1 | DIMENTSION_2;
        this.fdef.filter.maskBits = B2DVars.BIT_ALL;
        this.fdef.isSensor = true;
        body.createFixture(this.fdef).setUserData("checkpoint");
        return new Checkpoint(body, spriteBatch);
    }

    public void render(OrthographicCamera cam) {
        //draw tilemap
        renderer.setView(cam);
        renderer.getBatch().begin();
        if (background != null) renderer.renderTileLayer(background);
        if (foreground != null) renderer.renderTileLayer(foreground);
        if (dimension_1 != null) renderer.renderTileLayer(dimension_1);
        if (dimension_2 != null) renderer.renderTileLayer(dimension_2);
        renderer.getBatch().end();

        if (DEBUG) play.getB2dr().render(play.getWorld(), play.getB2dcam().combined);

        spriteBatch.setProjectionMatrix(cam.combined);
    }

    public void updateDimensionFade(float dt) {
        if (!dimensionFadeDone) {
            if (dimension) {
                if (currentDimensionFade > 0) {
                    currentDimensionFade -= (B2DVars.DIMENSION_FADE_AMOUNT / B2DVars.DIMENSION_FADE_TIME) * dt;
                } else {
                    currentDimensionFade = 0;
                    dimensionFadeDone = true;
                }
            } else {
                if (currentDimensionFade < B2DVars.DIMENSION_FADE_AMOUNT) {
                    currentDimensionFade += (B2DVars.DIMENSION_FADE_AMOUNT / B2DVars.DIMENSION_FADE_TIME) * dt;
                } else {
                    currentDimensionFade = B2DVars.DIMENSION_FADE_AMOUNT;
                    dimensionFadeDone = true;
                }
            }
            if (dimension_1 != null) dimension_1.setOpacity(1 - currentDimensionFade);
            if (dimension_2 != null)
                dimension_2.setOpacity((1 - B2DVars.DIMENSION_FADE_AMOUNT) + currentDimensionFade);
        }
    }

    public void updateGameFade(float dt) {
        if (!gameFadeDone) {
            if (gameFadeOut) {
                if (currentMenuFade < B2DVars.MENU_FADE_AMOUNT) {
                    currentMenuFade += (B2DVars.MENU_FADE_AMOUNT / B2DVars.MENU_FADE_TIME) * dt;
                } else {
                    currentMenuFade = B2DVars.MENU_FADE_AMOUNT;
                    gameFadeDone = true;
                }
            } else {
                if (currentMenuFade > 0) {
                    currentMenuFade -= (B2DVars.MENU_FADE_AMOUNT / B2DVars.MENU_FADE_TIME) * dt;
                } else {
                    currentMenuFade = 0;
                    gameFadeDone = true;
                }
            }

            /*parallaxBackground.setColor(1, 1, 1, 1 - currentMenuFade);
            player.setOpacity(1 - currentMenuFade);
            if (checkpoint != null) checkpoint.setOpacity(1 - currentMenuFade);*/
        }
    }

    public void renderFade() {
        if (currentMenuFade > 0) {
            /*renderer.getBatch().setColor(0, 0, 0, 1);
            if (background != null) renderer.renderTileLayer(background);
            if (foreground != null) renderer.renderTileLayer(foreground);
            if (dimension_1 != null) renderer.renderTileLayer(dimension_1);
            if (dimension_2 != null) renderer.renderTileLayer(dimension_2);
            renderer.getBatch().setColor(1, 1, 1, 1 - currentMenuFade);*/
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0, 0, 0, currentMenuFade);
            shapeRenderer.rect(0, 0, B2DVars.V_WIDTH, B2DVars.V_HEIGHT);
            shapeRenderer.end();
        }
    }
}
