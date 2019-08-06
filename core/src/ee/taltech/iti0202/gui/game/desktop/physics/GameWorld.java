package ee.taltech.iti0202.gui.game.desktop.physics;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ee.taltech.iti0202.gui.game.desktop.entities.projectile2.WeaponProjectile;
import ee.taltech.iti0202.gui.game.desktop.entities.weapons2.Weapon;
import ee.taltech.iti0202.gui.game.desktop.states.shapes.ShapesGreator;
import ee.taltech.iti0202.gui.game.networking.server.entity.Bullet;
import ee.taltech.iti0202.gui.game.networking.server.entity.Player;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BACKGROUND;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BIT_BOSSES;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BIT_BULLET;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BIT_WEAPON;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.DIMENSION_1;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.DIMENSION_2;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.GRAVITY;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.PATH;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.PPM;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.TERRA_DIMENSION_1;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.TERRA_DIMENSION_2;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.TERRA_SQUARES;

public class GameWorld implements Disposable {

    private World world = new World(new Vector2(0, GRAVITY), true);

    private Map<Integer, Body> playerBodies = new HashMap<>();
    private Map<Integer, PlayerBody.PlayerBodyData> players = new HashMap<>();

    private Map<Integer, Body> weaponBodies = new HashMap<>();
    private Map<Integer, WeaponBody.WeaponBodyData> weapons = new HashMap<>(); // Merge into one map?

    private Map<Integer, Body> bulletBodies = new HashMap<>();
    private Map<Integer, BulletBody.BulletBodyData> bullets = new HashMap<>();

    private MultiplayerContactListener contactListener;
    private List<RectangleMapObject> spawns = new ArrayList<>();

    private TiledMap tiledMap;

    private int newPlayerIdentifier;
    private int newWeaponIdentifier;
    private int newBulletIdentifier;

    private Set<Integer> weaponsRemoved = new HashSet<>();
    private Set<Integer> bulletsRemoved = new HashSet<>();

    public GameWorld(String act, String map) {
        contactListener = new MultiplayerContactListener(players);
        world.setContactListener(contactListener);

        String path = PATH + "maps/levels/" + act + "/" + map;
        tiledMap = new TmxMapLoader().load(path);  //TODO: @Enrico Selle mapi laadimise peaks saama nii, et see toimiks ka mitte mainthreadil. Siis saab serverit headlessina ka runnida, etm jama
        createHitboxes(tiledMap);
    }

    public void update(float dt) {
        world.step(dt, 10, 2);
        for (int id : contactListener.getWeaponsToRemove()) {
            removeWeapon(id);
            weaponsRemoved.add(id);
        }
        for (int id : contactListener.getBulletsToRemove()) {
            removeBullet(id);
            bulletsRemoved.add(id);
        }
        contactListener.getWeaponsToRemove().clear();
        contactListener.getBulletsToRemove().clear();
    }

    public int addPlayer() {
        newPlayerIdentifier++;

        //Random spawn
        Vector2 spawnCoordinates = generateRandomSpawnCoordinates();

        Body player = PlayerBody.createPlayer(world, spawnCoordinates, newPlayerIdentifier);
        playerBodies.put(newPlayerIdentifier, player);
        players.put(newPlayerIdentifier, (PlayerBody.PlayerBodyData) player.getUserData());
        return newPlayerIdentifier;
    }

    public int addWeapon(Weapon.Type type) {
        newWeaponIdentifier++;

        //Random spawn, same as player
        Vector2 spawnCoordinates = generateRandomSpawnCoordinates();

        Body weapon = WeaponBody.createWeapon(world, spawnCoordinates, newWeaponIdentifier, type);
        weaponBodies.put(newWeaponIdentifier, weapon);
        weapons.put(newWeaponIdentifier, (WeaponBody.WeaponBodyData) weapon.getUserData());
        return newWeaponIdentifier;
    }

    public int addBullet(Vector2 pos, Vector2 velocity, float angle,  WeaponProjectile.Type type) {
        newBulletIdentifier++;

        Body bullet = BulletBody.createBullet(world, pos, velocity, angle, newBulletIdentifier, type);
        bulletBodies.put(newBulletIdentifier, bullet);
        bullets.put(newBulletIdentifier, (BulletBody.BulletBodyData) bullet.getUserData());
        return newBulletIdentifier;
    }

    public boolean removePlayer(int id) {
        if (playerBodies.containsKey(id)) {
            for (WeaponBody.WeaponBodyData weapon : players.get(id).weapons) {
                if (weapon != null)
                weapon.dropped = true;
            }
            players.get(id).weapons = new WeaponBody.WeaponBodyData[3];
            world.destroyBody(playerBodies.get(id));
            playerBodies.remove(id);
            players.remove(id);
            return true;
        }
        return false;
    }

    public boolean removeWeapon(int id) {
        if (weaponBodies.containsKey(id)) {
            world.destroyBody(weaponBodies.get(id));
            weaponBodies.remove(id);
            weapons.remove(id);
            return true;
        }
        return false;
    }

    public boolean removeBullet(int id) {
        if (bulletBodies.containsKey(id)) {
            world.destroyBody(bulletBodies.get(id));
            bulletBodies.remove(id);
            bullets.remove(id);
            return true;
        }
        return false;
    }

    public void updatePlayer(Player player) {
        if (world.isLocked()) return;
        if (!playerBodies.containsKey(player.bodyId)) {
            Body playerBody = PlayerBody.createPlayer(world, player.position, player.bodyId);
            playerBodies.put(player.bodyId, playerBody);
            players.put(player.bodyId, (PlayerBody.PlayerBodyData) playerBody.getUserData());
            if (player.bodyId < newPlayerIdentifier) newPlayerIdentifier = player.bodyId;
        }
        Body body = playerBodies.get(player.bodyId);
        body.setTransform(player.position, 0);
        body.setLinearVelocity(player.velocity);

        PlayerBody.PlayerBodyData playerBodyData = players.get(player.bodyId);
        playerBodyData.currentWeaponIndex = player.currentWeaponIndex;
    }

    public void updateWeapon(ee.taltech.iti0202.gui.game.networking.server.entity.Weapon weapon) {
        if (world.isLocked()) return;
        if (!weaponBodies.containsKey(weapon.bodyId)) {
            Body weaponBody = WeaponBody.createWeapon(world, weapon.position, weapon.bodyId, weapon.type);
            weaponBodies.put(weapon.bodyId, weaponBody);
            weapons.put(weapon.bodyId, (WeaponBody.WeaponBodyData) weaponBody.getUserData());
            if (weapon.bodyId < newPlayerIdentifier) newPlayerIdentifier = weapon.bodyId;
        }
        Body body = weaponBodies.get(weapon.bodyId);
        body.setTransform(weapon.position, weapon.angle);
        body.setLinearVelocity(weapon.velocity);
    }

    public void updateBullet(Bullet bullet) {
        if (world.isLocked()) return;
        if (!bulletBodies.containsKey(bullet.bodyId)) {
            Body bulletBody = BulletBody.createBullet(world, bullet.position, bullet.velocity, bullet.angle, bullet.bodyId, bullet.type);
            bulletBodies.put(bullet.bodyId, bulletBody);
            bullets.put(bullet.bodyId, (BulletBody.BulletBodyData) bulletBody.getUserData());
            if (bullet.bodyId < newBulletIdentifier) newBulletIdentifier = bullet.bodyId;
        }
        Body body = bulletBodies.get(bullet.bodyId);
        body.setTransform(bullet.position, bullet.angle);
        body.setLinearVelocity(bullet.velocity);
    }

    private Vector2 generateRandomSpawnCoordinates() {
        Vector2 pos = new Vector2();
        RectangleMapObject rect = spawns.get((int) Math.floor(Math.random() * spawns.size()));
        pos.x = (rect.getRectangle().x + (float) Math.random() * rect.getRectangle().width) / PPM;
        pos.y = (rect.getRectangle().y + (float) Math.random() * rect.getRectangle().height) / PPM;
        return pos;
    }

    private void createHitboxes(TiledMap tiledMap) {
        for (MapLayer layer : tiledMap.getLayers()) {
            FixtureDef fixtureDef = new FixtureDef();
            switch (layer.getName()) {
                case "barrier":
                    fixtureDef.filter.categoryBits = BACKGROUND;
                    fixtureDef.filter.maskBits =
                            BIT_BOSSES | DIMENSION_1 | DIMENSION_2 | BIT_WEAPON | BIT_BULLET;
                    determineMapObject(layer, fixtureDef);
                    break;
                case "hitboxes_1":
                    fixtureDef.filter.categoryBits = TERRA_DIMENSION_1;
                    fixtureDef.filter.maskBits = BIT_BOSSES | DIMENSION_1 | BIT_WEAPON;
                    determineMapObject(layer, fixtureDef);
                    break;
                case "hitboxes_2":
                    fixtureDef.filter.categoryBits = TERRA_DIMENSION_2;
                    fixtureDef.filter.maskBits = BIT_BOSSES | DIMENSION_2 | BIT_WEAPON;
                    determineMapObject(layer, fixtureDef);
                    break;
                case "hitboxes":
                    fixtureDef.filter.categoryBits = TERRA_SQUARES;
                    fixtureDef.filter.maskBits =
                            BIT_BOSSES | DIMENSION_1 | DIMENSION_2 | BIT_WEAPON | BIT_BULLET;
                    determineMapObject(layer, fixtureDef);
                    break;
                case "spawns":
                    for (MapObject object : layer.getObjects()) {
                        if (object instanceof RectangleMapObject) {
                            RectangleMapObject rect = (RectangleMapObject) object;
                            if (layer.getName().equals("spawns")) {  //TODO: make spawns layers for other maps...
                                spawns.add(rect);
                            }
                        }
                    }
            }
        }
    }

    private void determineMapObject(MapLayer layer, FixtureDef fixtureDef) {
        BodyDef bodyDef = new BodyDef();
        Shape shape;
        for (MapObject object : layer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                shape = ShapesGreator.getRectangle((RectangleMapObject) object);
            } else if (object instanceof PolygonMapObject) {
                shape = ShapesGreator.getPolygon((PolygonMapObject) object);
            } else if (object instanceof PolylineMapObject) {
                shape = ShapesGreator.getPolyline((PolylineMapObject) object);
            } else if (object instanceof EllipseMapObject) {
                shape = ShapesGreator.getCircle((EllipseMapObject) object);
            } else {
                continue;
            }

            bodyDef.type = BodyDef.BodyType.StaticBody;
            fixtureDef.isSensor = false;
            fixtureDef.shape = shape;
            world.createBody(bodyDef).createFixture(fixtureDef).setUserData(layer.getName());
            shape.dispose();
        }
    }

    public Set<Integer> getWeaponsRemoved() {
        return weaponsRemoved;
    }

    public Set<Integer> getBulletsRemoved() {
        return bulletsRemoved;
    }

    public Map<Integer, Body> getBulletBodies() {
        return bulletBodies;
    }

    public Map<Integer, BulletBody.BulletBodyData> getBullets() {
        return bullets;
    }

    public Map<Integer, Body> getWeaponBodies() {
        return weaponBodies;
    }

    public Map<Integer, WeaponBody.WeaponBodyData> getWeapons() {
        return weapons;
    }

    public Map<Integer, Body> getPlayerBodies() {
        return playerBodies;
    }

    public Map<Integer, PlayerBody.PlayerBodyData> getPlayers() {
        return players;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    @Override
    public void dispose() {
        world.dispose();
    }
}
