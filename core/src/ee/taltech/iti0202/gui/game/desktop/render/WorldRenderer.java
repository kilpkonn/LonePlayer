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
import ee.taltech.iti0202.gui.game.desktop.entities.player2.Player;
import ee.taltech.iti0202.gui.game.desktop.entities.projectile2.WeaponProjectile;
import ee.taltech.iti0202.gui.game.desktop.entities.projectile2.WeaponProjectileBuilder;
import ee.taltech.iti0202.gui.game.desktop.entities.weapons2.Weapon;
import ee.taltech.iti0202.gui.game.desktop.entities.weapons2.WeaponBuilder;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.animations.Animation;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.desktop.physics.GameWorld;
import ee.taltech.iti0202.gui.game.networking.server.entity.BulletEntity;
import ee.taltech.iti0202.gui.game.networking.server.entity.PlayerEntity;
import ee.taltech.iti0202.gui.game.networking.server.entity.WeaponEntity;

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

    private Map<Integer, Player> players = new HashMap<>();
    private Map<Integer, ee.taltech.iti0202.gui.game.desktop.entities.weapons2.Weapon> weapons = new HashMap<>();
    private Map<Integer, WeaponProjectile> bullets = new HashMap<>();
    private PlayerEntity playerToFollow;

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

        for (Player player : players.values()) {
            player.update(dt);
        }

        for (Weapon weapon : weapons.values()) {
            weapon.update(dt);
        }

        for (WeaponProjectile projectile : bullets.values()) {
            projectile.update(dt);
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

        for (Player player : players.values()) {
            player.render(sb);
        }

        for (Weapon weapon : weapons.values()) {
            weapon.render(sb);
        }

        for (WeaponProjectile bullet : bullets.values()) {
            bullet.render(sb);
        }
    }

    public void removePlayerAnimation(int id) {
        players.remove(id);
    }

    public void removeWeaponAnimation(int id) {
        weapons.remove(id);
    }

    public void removeBulletanimation(int id) {
        bullets.remove(id);
    }

    public void updatePlayerAnimation(PlayerEntity player) {
        if (player.animation != null && players.containsKey(player.bodyId)) {
            Player p = players.get(player.bodyId);
            p.setAnimation(player.animation);
            p.setFlipX(player.flippedAnimation);
            Weapon[] weapons = new Weapon[3];
            for (int i = 0; i < weapons.length; i++) {
                weapons[i] = this.weapons.get(player.weapons[i]);
            }
            p.setWeapons(weapons);
            p.setCurrentWeapon(player.currentWeaponIndex);

            p.setAiming(player.isAiming, player.aimAngle);
        } else if (!players.containsKey(player.bodyId)) {
            Player p = new Player(gameWorld.getPlayerBodies().get(player.bodyId));
            float opacity = player.bodyId == playerToFollow.bodyId ? 1 : 0.5f;
            p.setOpacity(opacity);

            players.put(player.bodyId, p);
        }
    }

    public void updateWeaponAnimation(WeaponEntity weapon) {
        if (weapon.animation != null && weapons.containsKey(weapon.bodyId)) {
            Weapon w = weapons.get(weapon.bodyId);
            w.setAnimation(weapon.animation);
            w.isDropped = weapon.dropped;
            if (weapon.dropped) w.setFlipX(weapon.flippedAnimation);
        } else if (!weapons.containsKey(weapon.bodyId)) {
            Weapon w = new WeaponBuilder()
                    .setBody(gameWorld.getWeaponBodies().get(weapon.bodyId))
                    .setType(weapon.type)
                    .create();
            w.isDropped = weapon.dropped;

            weapons.put(weapon.bodyId, w);
        }
    }

    public void updateBulletAnimation(BulletEntity bullet) {
        if (bullet.animation != null && bullets.containsKey(bullet.bodyId)) {
            WeaponProjectile b = bullets.get(bullet.bodyId);
            b.setAnimation(bullet.animation);
        } else if (!bullets.containsKey(bullet.bodyId)) {
            bullets.put(bullet.bodyId, new WeaponProjectileBuilder()
                    .setBody(gameWorld.getBulletBodies().get(bullet.bodyId))
                    .setType(bullet.type)
                    .createWeaponProjectile());
        }
    }

    public void setPlayerToFollow(PlayerEntity player) {
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
