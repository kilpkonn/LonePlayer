package ee.taltech.iti0202.gui.game.desktop.handlers.scene.layers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

import java.util.ArrayList;
import java.util.List;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.BossLoader;
import ee.taltech.iti0202.gui.game.desktop.entities.player.PlayerLoader;
import ee.taltech.iti0202.gui.game.desktop.entities.staticobjects.Checkpoint;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.animations.Animation;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.bleeding.Bleeding;
import ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.desktop.states.Play;
import ee.taltech.iti0202.gui.game.desktop.states.shapes.ShapesGreator;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BACKGROUND;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BIT_BOSSES;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.DIMENTSION_1;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.DIMENTSION_2;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.NONE;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PPM;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.SQUARE_CORNERS;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.TERRA_DIMENTSION_1;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.TERRA_DIMENTSION_2;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.TERRA_SQUARES;

public class Draw {

    ////////////////////////////////////////////////////////////////////    Read and draw the map   ////////////////////////////////////////////////////////////////////

    private Play play;
    private SpriteBatch spriteBatch;

    public Draw(Play play, SpriteBatch sb) {
        this.play = play;
        this.spriteBatch = sb;
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
            play.getBdef().type = BodyDef.BodyType.StaticBody;
            play.getFdef().isSensor = false;
            play.getFdef().shape = shape;
            play.getWorld().createBody(play.getBdef()).createFixture(play.getFdef()).setUserData(layer.getName());
        }
    }

    private void createEndPoint(Vector2 pos) {
        System.out.println("new endpoint");
        play.setBdef(new BodyDef());
        play.getBdef().position.set(pos);
        play.getBdef().type = BodyDef.BodyType.StaticBody;
        Body body = play.getWorld().createBody(play.getBdef());
        PolygonShape polyShape = new PolygonShape();
        polyShape.setAsBox(64 / PPM, 32 / PPM, new Vector2(0, 4 / PPM), 0);
        play.getFdef().shape = polyShape;
        play.getFdef().filter.categoryBits = DIMENTSION_1 | DIMENTSION_2;
        play.getFdef().filter.maskBits = B2DVars.BIT_ALL;
        play.getFdef().isSensor = true;
        body.createFixture(play.getFdef()).setUserData("end");
        Checkpoint checkpoint = new Checkpoint(body, spriteBatch);
        play.getCheckpointList().add(checkpoint);
    }

    public void drawLayers() {
        for (MapLayer layer : play.getTiledMap().getLayers()) {
            switch (layer.getName()) {
                case "barrier":
                    play.getFdef().filter.categoryBits = BACKGROUND;
                    play.getFdef().filter.maskBits = BIT_BOSSES | DIMENTSION_1 | DIMENTSION_2;
                    determineMapObject(layer);
                    break;
                case "hitboxes_1":
                    play.getFdef().filter.categoryBits = TERRA_DIMENTSION_1;
                    play.getFdef().filter.maskBits = BIT_BOSSES | DIMENTSION_1;
                    determineMapObject(layer);
                    break;
                case "hitboxes_2":
                    play.getFdef().filter.categoryBits = TERRA_DIMENTSION_2;
                    play.getFdef().filter.maskBits = BIT_BOSSES | DIMENTSION_2;
                    determineMapObject(layer);
                    break;
                case "hitboxes":
                    play.getFdef().filter.categoryBits = TERRA_SQUARES;
                    play.getFdef().filter.maskBits = BIT_BOSSES | DIMENTSION_1 | DIMENTSION_2;
                    determineMapObject(layer);
                    break;
                default:
                    readVertices((TiledMapTileLayer) layer);
            }
        }
    }

    private void readVertices(TiledMapTileLayer layer) {
        int[] corner_coords = SQUARE_CORNERS;
        String type = layer.getName();
        boolean isSensor = false;
        float tileSize = layer.getTileWidth();

        switch (type) {

            case "dimension_1":
                play.setDimension_1(layer);
                isSensor = true;
                layer.setVisible(true);
                layer.setOpacity(1f);
                play.setBackground(layer);
                break;

            case "dimension_2":
                play.setDimension_2(layer);
                isSensor = true;
                layer.setVisible(true);
                layer.setOpacity(0.5f);
                play.setBackground(layer);
                break;

            case "background":
                isSensor = true;
                layer.setVisible(true);
                play.setBackground(layer);
                break;

            case "foreground":
                isSensor = true;
                layer.setVisible(true);
                play.setForeground(layer);
                break;

            default:
                layer.setVisible(false);
                break;
        }

        play.getBdef().type = BodyDef.BodyType.StaticBody;

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
                    Texture tex = Game.res.getTexture("Player");
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
                play.getFdef().filter.categoryBits = NONE;
                play.getFdef().filter.maskBits = NONE;
                play.getFdef().isSensor = isSensor;
                BossLoader bossLoader = new BossLoader(play, spriteBatch);
                switch (layer.getName()) {
                    case "checkpoints":
                        if ((polygon[0].x - polygon[3].x) / (polygon[0].y - polygon[1].y) > 1.8) {
                            createEndPoint(new Vector2(polygon[1].x + tileSize / PPM, polygon[0].y));
                        } else {
                            if (play.isCheckpoints()) {
                                createCheckpoints(new Vector2(polygon[1].x + (polygon[3].x - polygon[1].x) / 2, polygon[0].y));
                            }

                        }
                        break;

                    case "bosses_small":
                        if (play.isBosses()) {
                            bossLoader.createBosses(new Vector2(polygon[2].x - (tileSize / 2) / PPM, polygon[2].y), layer.getProperties().get("type").toString(), false, (Integer) layer.getProperties().get("size"));
                        }
                        break;

                    case "bosses_big":
                        if (play.isBosses()) {
                            bossLoader.createBosses(new Vector2(polygon[2].x - (tileSize / 2) / PPM, polygon[2].y), layer.getProperties().get("type").toString(), true, (Integer) layer.getProperties().get("size"));
                        }
                        break;
                    case "bosses":
                        if (play.isBosses()) {
                            bossLoader.createBosses(new Vector2(polygon[2].x - (tileSize / 2) / PPM, polygon[2].y), layer.getProperties().get("type").toString(), true, (Integer) layer.getProperties().get("size"));
                        }
                        break;

                    case "player":
                        if (play.getInitPlayerLocation() == null) {
                            play.setInitPlayerLocation(new Vector2(polygon[2].x + (tileSize / 2) / PPM, polygon[2].y));
                            PlayerLoader playerLoader = new PlayerLoader(play, spriteBatch);
                            playerLoader.initPlayer();
                        }
                        break;

                    default:
                        play.getWorld().createBody(play.getBdef()).createFixture(play.getFdef()).setUserData(layer.getName());
                        break;
                }
            }
        }
    }

    private void createCheckpoints(Vector2 pos) {
        System.out.println("new checkpoint");
        play.setBdef(new BodyDef());
        play.setFdef(new FixtureDef());
        play.getBdef().position.set(pos);
        play.getBdef().type = BodyDef.BodyType.StaticBody;
        Body body = play.getWorld().createBody(play.getBdef());
        PolygonShape polyShape = new PolygonShape();
        polyShape.setAsBox(4 / PPM, 32 / PPM, new Vector2(0, 4 / PPM), 0);
        play.getFdef().shape = polyShape;
        play.getFdef().filter.categoryBits = DIMENTSION_1 | DIMENTSION_2;
        play.getFdef().filter.maskBits = B2DVars.BIT_ALL;
        play.getFdef().isSensor = true;
        body.createFixture(play.getFdef()).setUserData("checkpoint");
        Checkpoint checkpoint = new Checkpoint(body, spriteBatch);
        play.getCheckpointList().add(checkpoint);
    }

}
