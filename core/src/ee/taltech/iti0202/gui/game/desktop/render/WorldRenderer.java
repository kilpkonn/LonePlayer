package ee.taltech.iti0202.gui.game.desktop.render;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

import java.util.HashMap;
import java.util.Map;

import ee.taltech.iti0202.gui.game.desktop.entities.Handler;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.animations.Animation;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.desktop.physics.GameWorld;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.DIMENSION_FADE_AMOUNT;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.SQUARE_CORNERS;

public class WorldRenderer implements Handler {

    private GameWorld gameWorld;

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer;
    private Map<TiledMapTileLayer.Cell, Animation> animatedCells = new HashMap<>();

    private TiledMapTileLayer background;
    private TiledMapTileLayer foreground;
    private TiledMapTileLayer dimension_2;
    private TiledMapTileLayer dimension_1;

    private boolean dimensionFadeDone = false;
    private boolean dimension = true;
    private float currentDimensionFade = B2DVars.DIMENSION_FADE_AMOUNT;

    public WorldRenderer(GameWorld gameWorld, OrthographicCamera cam) {
        this.gameWorld = gameWorld;
        this.tiledMap = gameWorld.getTiledMap();
        renderer = new OrthogonalTiledMapRenderer(tiledMap);
        renderer.setView(cam);
    }

    @Override
    public void update(float dt) {
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

    @Override
    public void render(SpriteBatch sb) {
        // render animations
        if (animatedCells != null)
            for (TiledMapTileLayer.Cell cell : animatedCells.keySet())
                cell.setTile(new StaticTiledMapTile(animatedCells.get(cell).getFrame()));

        if (background != null) renderer.renderTileLayer(background);
        if (foreground != null) renderer.renderTileLayer(foreground);
        if (dimension_1 != null) renderer.renderTileLayer(dimension_1);
        if (dimension_2 != null) renderer.renderTileLayer(dimension_2);

        //TODO: Render player, bullets, etc.
    }

    private void readVertices(TiledMapTileLayer layer) {
        int[] corner_coords = SQUARE_CORNERS;
        String type = layer.getName();
        float tileSize = layer.getTileWidth();

        switch (type) {
            case "dimension_1":
                layer.setVisible(true);
                layer.setOpacity(dimension ? 1f : 1 - DIMENSION_FADE_AMOUNT);
                dimension_1 = layer;
                break;

            case "dimension_2":
                layer.setVisible(true);
                layer.setOpacity(dimension ? 1 - DIMENSION_FADE_AMOUNT : 1f);
                dimension_2 = layer;
                break;

            case "background":
                layer.setVisible(true);
                background = layer;
                break;

            case "foreground":
                layer.setVisible(true);
                foreground = layer;
                break;

            default:
                layer.setVisible(false);
                break;
        }
    }

    public void dispose() {
        renderer.dispose();
    }
}
