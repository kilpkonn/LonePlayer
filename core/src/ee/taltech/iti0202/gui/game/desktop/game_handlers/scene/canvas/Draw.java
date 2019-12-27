package ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.canvas;

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
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.entities.Handler;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.BossLoader;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.handler.BossHander;
import ee.taltech.iti0202.gui.game.desktop.entities.checkpoints.Checkpoint;
import ee.taltech.iti0202.gui.game.desktop.entities.checkpoints.handler.CheckpointHandler;
import ee.taltech.iti0202.gui.game.desktop.entities.checkpoints.loader.CheckpointLoader;
import ee.taltech.iti0202.gui.game.desktop.entities.player.handler.PlayerHandler;
import ee.taltech.iti0202.gui.game.desktop.entities.projectile.bullet.Bullet;
import ee.taltech.iti0202.gui.game.desktop.entities.projectile.bullet.handler.BulletHandler;
import ee.taltech.iti0202.gui.game.desktop.entities.weapons.handler.WeaponHandler;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.animations.Animation;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.bleeding.Bleeding;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.desktop.states.gameprogress.BossData;
import ee.taltech.iti0202.gui.game.desktop.states.shapes.ShapesGreator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.*;

public class Draw implements Handler {

    ////////////////////////////////////////////////////////////////////    Read and draw the map
    //////////////////////////////////////////////////////////////////////

    private PlayerHandler playerHandler;
    private BossHander bossHander;
    private CheckpointHandler checkpointHandler;
    private BulletHandler bulletHandler;
    private WeaponHandler weaponHandler;
    private World world;
    private SpriteBatch spriteBatch;
    private BodyDef bdef;
    private FixtureDef fdef;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer;
    private Map<TiledMapTileLayer.Cell, Animation> animatedCells;
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

    public Draw(SpriteBatch sb, World world, String act, String map) {
        this.spriteBatch = sb;
        this.bdef = new BodyDef();
        this.fdef = new FixtureDef();
        this.dimension = true;
        this.gameFadeOut = false;
        this.gameFadeDone = false;
        this.world = world;
        this.currentMenuFade = 1;

        // load tiled map
        String path = PATH + "maps/levels/" + act + "/" + map;
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
            else
                continue;

            this.bdef.type = BodyDef.BodyType.StaticBody;
            this.fdef.isSensor = false;
            this.fdef.shape = shape;
            world.createBody(this.bdef).createFixture(this.fdef).setUserData(layer.getName());
        }
    }

    public void drawLayers(boolean defaultSpawn, List<BossData> bosses) {
        for (MapLayer layer : tiledMap.getLayers()) {
            switch (layer.getName()) {
                case "barrier":
                    this.fdef.filter.categoryBits = BACKGROUND;
                    this.fdef.filter.maskBits =
                            BIT_BOSSES | DIMENSION_1 | DIMENSION_2 | BIT_WEAPON | BIT_BULLET;
                    determineMapObject(layer);
                    break;
                case "hitboxes_1":
                    this.fdef.filter.categoryBits = TERRA_DIMENSION_1;
                    this.fdef.filter.maskBits = BIT_BOSSES | DIMENSION_1 | BIT_WEAPON;
                    determineMapObject(layer);
                    break;
                case "hitboxes_2":
                    this.fdef.filter.categoryBits = TERRA_DIMENSION_2;
                    this.fdef.filter.maskBits = BIT_BOSSES | DIMENSION_2 | BIT_WEAPON;
                    determineMapObject(layer);
                    break;
                case "hitboxes":
                    this.fdef.filter.categoryBits = TERRA_SQUARES;
                    this.fdef.filter.maskBits =
                            BIT_BOSSES | DIMENSION_1 | DIMENSION_2 | BIT_WEAPON;
                    determineMapObject(layer);
                    break;
                default:
                    if (layer instanceof TiledMapTileLayer)
                        readVertices((TiledMapTileLayer) layer, defaultSpawn);
            }
        }
        if (!defaultSpawn) {
            new BossLoader(world, spriteBatch, fdef, bdef, bossHander, playerHandler)
                    .createAllBosses(bosses);
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
                    Texture tex =
                            Game.res.getTexture(
                                    "PlayerEntity"); // TODO: <- Wut is dis? misleading names or obsolete?
                    // // this is to
                    // make animated cells, like grass in the wind or so on
                    TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[0];
                    animatedCells.put(cell, new Animation(sprites, 1 / 12f));
                }

                float corner = tileSize / 2 / PPM;
                float mapPosCol = (col + 0.5f) * tileSize / PPM;
                float mapPosRow = (row + 0.5f) * tileSize / PPM;

                // writing vertices for hit boxes
                if (lastWasThere) {

                    v[2] =
                            new Vector2(
                                    mapPosCol + corner_coords[4] * corner,
                                    mapPosRow + corner_coords[5] * corner);
                    v[3] =
                            new Vector2(
                                    mapPosCol + corner_coords[6] * corner,
                                    mapPosRow + corner_coords[7] * corner);

                } else {
                    v[0] =
                            new Vector2(
                                    mapPosCol + corner_coords[0] * corner,
                                    mapPosRow + corner_coords[1] * corner);
                    v[1] =
                            new Vector2(
                                    mapPosCol + corner_coords[2] * corner,
                                    mapPosRow + corner_coords[3] * corner);
                    v[2] =
                            new Vector2(
                                    mapPosCol + corner_coords[4] * corner,
                                    mapPosRow + corner_coords[5] * corner);
                    v[3] =
                            new Vector2(
                                    mapPosCol + corner_coords[6] * corner,
                                    mapPosRow + corner_coords[7] * corner);
                }
                lastWasThere = true;
            }

            for (Vector2[] polygon : polygonVertices) {

                PolygonShape polyShape = new PolygonShape();
                polyShape.set(polygon);
                this.fdef.filter.categoryBits = NONE;
                this.fdef.filter.maskBits = NONE;
                this.fdef.isSensor = isSensor;
                BossLoader bossLoader =
                        new BossLoader(world, spriteBatch, fdef, bdef, bossHander, playerHandler);
                switch (layer.getName()) {
                    case "checkpoints":
                        if ((polygon[0].x - polygon[3].x) / (polygon[0].y - polygon[1].y) > 1.8) {
                            Checkpoint checkpoint =
                                    CheckpointLoader.createEndPoint(
                                            new Vector2(
                                                    polygon[1].x + tileSize / PPM, polygon[0].y),
                                            world,
                                            spriteBatch);
                            checkpointHandler.getCheckpointList().add(checkpoint);
                        } else {
                            if (B2DVars.CHECKPOINTS) {
                                Checkpoint checkpoint =
                                        CheckpointLoader.createCheckpoints(
                                                new Vector2(
                                                        polygon[1].x
                                                                + (polygon[3].x - polygon[1].x) / 2,
                                                        polygon[0].y),
                                                world,
                                                spriteBatch);
                                checkpointHandler.getCheckpointList().add(checkpoint);
                            }
                        }
                        break;

                    case "bosses_small":
                        if (B2DVars.BOSSES && defaultSpawn) {
                            bossLoader.createBosses(
                                    new Vector2(polygon[2].x - (tileSize / 2) / PPM, polygon[2].y),
                                    layer.getProperties().get("type").toString(),
                                    false,
                                    (Integer) layer.getProperties().get("size"));
                        }
                        break;

                    case "bosses_big":
                        if (B2DVars.BOSSES && defaultSpawn) {
                            bossLoader.createBosses(
                                    new Vector2(polygon[2].x - (tileSize / 2) / PPM, polygon[2].y),
                                    layer.getProperties().get("type").toString(),
                                    true,
                                    (Integer) layer.getProperties().get("size"));
                        }
                        break;
                    case "bosses":
                        if (B2DVars.BOSSES && defaultSpawn) {
                            bossLoader.createBosses(
                                    new Vector2(polygon[2].x - (tileSize / 2) / PPM, polygon[2].y),
                                    layer.getProperties().get("type").toString(),
                                    true,
                                    (Integer) layer.getProperties().get("size"));
                        }
                        break;

                    case "player":
                        if (playerHandler.getInitPlayerLocation() == null) {
                            playerHandler.setInitPlayerLocation(
                                    new Vector2(polygon[2].x + (tileSize / 2) / PPM, polygon[2].y));
                        }
                        break;

                    default:
                        world.createBody(this.bdef)
                                .createFixture(this.fdef)
                                .setUserData(layer.getName());
                        break;
                }
            }
        }
    }

    public void update(float dt) {
        // draw tilemap animations
        if (animatedCells != null) {
            for (Animation animation : animatedCells.values()) {
                animation.update(dt);
            }
        }
        // update player
        playerHandler.update(dt);

        // update weapons
        weaponHandler.update(dt);

        // update checkpoints
        checkpointHandler.update(dt);

        // update bosses
        bossHander.update(dt);

        // update bullets
        bulletHandler.update(dt);

        dispose();
    }

    private void dispose() {
        for (Bullet bullet : bulletHandler.getBulletArray()) {
            if (bullet.toBeRemoved()) {
                bossHander.getCl().getCollidedBullets().remove(bullet);
                world.destroyBody(bullet.getBody());
            }
        }
    }

    public void render(OrthographicCamera cam) {

        // render animations
        if (animatedCells != null)
            for (TiledMapTileLayer.Cell cell : animatedCells.keySet())
                cell.setTile(new StaticTiledMapTile(animatedCells.get(cell).getFrame()));

        // draw tilemap
        renderer.setView(cam);
        renderer.getBatch().begin();
        if (background != null) renderer.renderTileLayer(background);
        if (foreground != null) renderer.renderTileLayer(foreground);
        if (dimension_1 != null) renderer.renderTileLayer(dimension_1);
        if (dimension_2 != null) renderer.renderTileLayer(dimension_2);
        renderer.getBatch().end();

        // if (DEBUG) play.getB2dr().render(world, play.getB2dcam().combined);

        spriteBatch.setProjectionMatrix(cam.combined);
    }

    public void render(SpriteBatch sb) {
        // draw bosses
        bossHander.render(sb);

        // draw player and bullets
        playerHandler.render(sb);

        // draw weapon
        weaponHandler.render(sb);

        // draw bullets
        bulletHandler.render(sb);

        // draw checkpoints
        checkpointHandler.render(sb);
    }

    public void updateDimensionFade(float dt) {
        if (!dimensionFadeDone) {
            if (dimension) {
                if (currentDimensionFade > 0) {
                    currentDimensionFade -=
                            (B2DVars.DIMENSION_FADE_AMOUNT / B2DVars.DIMENSION_FADE_TIME) * dt;
                } else {
                    currentDimensionFade = 0;
                    dimensionFadeDone = true;
                }
            } else {
                if (currentDimensionFade < B2DVars.DIMENSION_FADE_AMOUNT) {
                    currentDimensionFade +=
                            (B2DVars.DIMENSION_FADE_AMOUNT / B2DVars.DIMENSION_FADE_TIME) * dt;
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
        }
    }

    public void renderFade() {
        if (currentMenuFade > 0) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0, 0, 0, currentMenuFade);
            shapeRenderer.rect(0, 0, B2DVars.V_WIDTH, B2DVars.V_HEIGHT);
            shapeRenderer.end();
        }
    }

    public void setDimensionFadeDone(boolean dimensionFadeDone) {
        this.dimensionFadeDone = dimensionFadeDone;
    }

    public void setDimension(boolean dimension) {
        this.dimension = dimension;
    }

    public void setGameFadeDone(boolean gameFadeDone) {
        this.gameFadeDone = gameFadeDone;
    }

    public void setGameFadeOut(boolean gameFadeOut) {
        this.gameFadeOut = gameFadeOut;
    }

    public boolean isDimension() {
        return dimension;
    }

    public WeaponHandler getWeaponHandler() {
        return weaponHandler;
    }

    public void setPlayerHandler(PlayerHandler playerHandler) {
        this.playerHandler = playerHandler;
    }

    public void setBossHander(BossHander bossHander) {
        this.bossHander = bossHander;
    }

    public void setCheckpointHandler(CheckpointHandler checkpointHandler) {
        this.checkpointHandler = checkpointHandler;
    }

    public void setBulletHandler(BulletHandler bulletHandler) {
        this.bulletHandler = bulletHandler;
    }

    public void setWeaponHandler(WeaponHandler weaponHandler) {
        this.weaponHandler = weaponHandler;
    }

    public PlayerHandler getPlayerHandler() {
        return playerHandler;
    }

    public BulletHandler getBulletHandler() {
        return bulletHandler;
    }
}
