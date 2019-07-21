package ee.taltech.iti0202.gui.game.desktop.render;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.HashMap;
import java.util.Map;

import ee.taltech.iti0202.gui.game.desktop.entities.Handler;
import ee.taltech.iti0202.gui.game.desktop.entities.player.Player2;
import ee.taltech.iti0202.gui.game.desktop.entities.weapons2.WeaponBuilder;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.animations.Animation;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.desktop.physics.GameWorld;
import ee.taltech.iti0202.gui.game.networking.server.entity.Weapon;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.PPM;

public class WorldRenderer implements Handler {

    private GameWorld gameWorld;
    private OrthographicCamera cam;

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer;
    private Map<TiledMapTileLayer.Cell, Animation> animatedCells = new HashMap<>();

    private TiledMapTileLayer background;
    private TiledMapTileLayer foreground;
    private TiledMapTileLayer dimension_2;
    private TiledMapTileLayer dimension_1;

    private Vector2 camSpeed = new Vector2(0, 0);

    private boolean dimensionFadeDone = false;
    private boolean dimension = true;
    private float currentDimensionFade = B2DVars.DIMENSION_FADE_AMOUNT;

    private Map<Integer, Player2> players = new HashMap<>();
    private Map<Integer, ee.taltech.iti0202.gui.game.desktop.entities.weapons2.Weapon> weapons = new HashMap<>();
    private ee.taltech.iti0202.gui.game.networking.server.entity.Player playerToFollow;

    public WorldRenderer(GameWorld gameWorld, OrthographicCamera cam) {
        this.gameWorld = gameWorld;
        this.tiledMap = gameWorld.getTiledMap();
        this.cam = cam;
        renderer = new OrthogonalTiledMapRenderer(tiledMap);
        renderer.setView(cam);

        for (MapLayer layer : tiledMap.getLayers()) {
            // Only visuals
            if (layer instanceof  TiledMapTileLayer) {
                readVertices((TiledMapTileLayer) layer);
            }
        }
    }

    @Override
    public void update(float dt) {

        for (Player2 player : players.values()) {
            player.update(dt);
        }

        for (ee.taltech.iti0202.gui.game.desktop.entities.weapons2.Weapon weapon : weapons.values()) {
            weapon.update(dt);
        }

        // Set camera
        if (playerToFollow != null) {
            Body playerLocation = gameWorld.getPlayerBodies().get(playerToFollow.bodyId);
            camSpeed =
                    new Vector2(
                            (playerLocation.getPosition().x - cam.position.x / PPM) * 2 * PPM,
                            (playerLocation.getPosition().y - cam.position.y / PPM) * 4 * PPM);

            cam.position.x += camSpeed.x * dt;
            cam.position.y += camSpeed.y * dt;

            cam.position.x = Math.round(cam.position.x);
            cam.position.y = Math.round(cam.position.y);

            cam.update();
        }

        if (playerToFollow != null && dimension != playerToFollow.dimension) {
            dimensionFadeDone = false;
            dimension = playerToFollow.dimension;
        }

        if (playerToFollow != null && !dimensionFadeDone) {
            if (playerToFollow.dimension) {
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
        sb.setProjectionMatrix(cam.combined);
        renderer.setView(cam);
        renderer.render();

        for (Map.Entry<Integer, Body> playerEntry : gameWorld.getPlayerBodies().entrySet()) {
            //Add missing players -> move to update?
            if (!players.containsKey(playerEntry.getKey())) {
                players.put(playerEntry.getKey(), new Player2(playerEntry.getValue(), sb));
            }
            float opacity = playerEntry.getKey() == playerToFollow.bodyId ? 1 : 0.5f;
            Player2 player =  players.get(playerEntry.getKey());
            player.setOpacity(opacity);
            player.render(sb);
        }

        for (Map.Entry<Integer, Body> weaponEntry : gameWorld.getWeaponBodies().entrySet()) {
            if (!weapons.containsKey(weaponEntry.getKey())) {
                weapons.put(weaponEntry.getKey(), new WeaponBuilder()
                        .setBody(weaponEntry.getValue())
                        .setSpriteBatch(sb)
                        .setType(gameWorld.getWeapons().get(weaponEntry.getKey()).type)
                        .create());
            }
            weapons.get(weaponEntry.getKey()).render(sb);
        }
        //TODO: Render weapons, bullets, etc.
    }

    public void updatePlayerAnimation(ee.taltech.iti0202.gui.game.networking.server.entity.Player player) {
        if (player.animation != null && players.containsKey(player.bodyId)) {
            Player2 p = players.get(player.bodyId);
            p.setAnimation(player.animation);
            p.setFlipX(player.flippedAnimation);
            ee.taltech.iti0202.gui.game.desktop.entities.weapons2.Weapon[] weapons = new ee.taltech.iti0202.gui.game.desktop.entities.weapons2.Weapon[3];
            for (int i = 0; i < weapons.length; i++) {
                weapons[i] = this.weapons.get(player.weapons[i]);
            }
            p.setWeapons(weapons);
            p.setCurrentWeapon(player.currentWeaponIndex);

            p.setAiming(player.isAiming, player.aimAngle);
        }
    }

    public void updateWeaponAnimation(Weapon weapon) {
        if (weapon.animation != null && weapons.containsKey(weapon.bodyId)) {
            ee.taltech.iti0202.gui.game.desktop.entities.weapons2.Weapon w = weapons.get(weapon.bodyId);
            w.setAnimation(weapon.animation);
            w.isDropped = weapon.dropped;
            if (weapon.dropped) w.setFlipX(weapon.flippedAnimation);
        }
    }

    public void setPlayerToFollow(ee.taltech.iti0202.gui.game.networking.server.entity.Player player) {
        playerToFollow = player;
    }

    private void readVertices(TiledMapTileLayer layer) {
        String type = layer.getName();

        switch (type) {
            case "dimension_1":
                layer.setVisible(true);
                dimension_1 = layer;
                break;

            case "dimension_2":
                layer.setVisible(true);
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

    public Vector2 getCamSpeed() {
        return camSpeed;
    }

    public void dispose() {
        renderer.dispose();
    }
}
