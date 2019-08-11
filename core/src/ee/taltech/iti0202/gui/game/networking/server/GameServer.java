package ee.taltech.iti0202.gui.game.networking.server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ee.taltech.iti0202.gui.game.desktop.entities.player2.Player;
import ee.taltech.iti0202.gui.game.desktop.entities.projectile2.WeaponProjectile;
import ee.taltech.iti0202.gui.game.desktop.entities.weapons2.Weapon;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.networking.serializable.Handshake;
import ee.taltech.iti0202.gui.game.networking.serializable.Lobby;
import ee.taltech.iti0202.gui.game.networking.serializable.Play;
import ee.taltech.iti0202.gui.game.networking.server.entity.BulletEntity;
import ee.taltech.iti0202.gui.game.networking.server.entity.Entity;
import ee.taltech.iti0202.gui.game.networking.server.entity.PlayerEntity;
import ee.taltech.iti0202.gui.game.networking.server.entity.WeaponEntity;
import ee.taltech.iti0202.gui.game.networking.server.listeners.ServerListener;
import ee.taltech.iti0202.gui.game.networking.server.entity.PlayerControls;

public class GameServer implements Disposable {
    private int tcpPort = 55000;
    private int udpPort = 55001;
    private Server server;
    private String connect = "";

    private String act;
    private String map;
    private B2DVars.GameDifficulty difficulty;

    private Map<Integer, PlayerEntity> players = new HashMap<>();
    private ServerLogic serverLogic = new ServerLogic();

    public GameServer() {
        server = new Server();
        server.start();

        Kryo kryo = server.getKryo();
        kryo.register(Handshake.Request.class);
        kryo.register(Handshake.Response.class);
        kryo.register(Lobby.ActMapDifficulty.class);
        kryo.register(Lobby.Kick.class);
        kryo.register(Lobby.NameChange.class);
        kryo.register(Lobby.Details.class);
        kryo.register(HashSet.class);
        kryo.register(B2DVars.GameDifficulty.class);
        kryo.register(PlayerEntity.class);
        kryo.register(Vector2.class);
        kryo.register(Play.Players.class);
        kryo.register(Play.Weapons.class);
        kryo.register(Weapon.Type.class);
        kryo.register(Lobby.StartGame.class);
        kryo.register(PlayerControls.class);
        kryo.register(Entity.class);
        kryo.register(Player.PlayerAnimation.class);
        kryo.register(Weapon.Animation.class);
        kryo.register(WeaponEntity.class);
        kryo.register(int[].class);
        kryo.register(Play.Bullets.class);
        kryo.register(BulletEntity.class);
        kryo.register(WeaponProjectile.Animation.class);
        kryo.register(WeaponProjectile.Type.class);
        kryo.register(Play.EntitiesToBeRemoved.class);

        try {
            URL url_name = new URL("http://bot.whatismyipaddress.com");

            BufferedReader sc =
                    new BufferedReader(new InputStreamReader(url_name.openStream()));
            String address = sc.readLine().trim();  //
            address =  InetAddress.getLocalHost().getHostAddress();
            server.bind(tcpPort, udpPort);
            //server.setPortAndIp(port, "192.168.0.254"); //address);  //TODO: Fix connecting issues via public ip
            connect = String.format("%s:%s|%s", address, tcpPort, udpPort);
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
            //TODO: Some actual error handling
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.addListener(new ServerListener(this));
    }

    public String getConnect() {
        return connect;
    }

    public String getLocalConnect() {
        return String.format("127.0.0.1:%s|%s", tcpPort, udpPort);
    }

    public Set<PlayerEntity> getPlayers() {
        return new HashSet<>(players.values());
    }

    public void updateWorld(Play.EntitiesToBeRemoved entitiesRemoved) {
        Play.Players players = new Play.Players();
        players.players = getPlayers();

        server.sendToAllUDP(players);

        Play.Weapons weapons = new Play.Weapons();
        int i = 0;
        for (WeaponEntity weapon : serverLogic.getWeapons().values()) {
            weapons.weapons.add(weapon);
            i++;
            if (i >= 10) {
                server.sendToAllUDP(weapons);
                i = 0;
                weapons.weapons.clear();
            }
        }
        server.sendToAllUDP(weapons);

        Play.Bullets bullets = new Play.Bullets();
        i = 0;
        for (BulletEntity bullet : serverLogic.getBullets().values()) {
            bullets.bullets.add(bullet);
            i++;
            if (i >= 15) {
                server.sendToAllUDP(bullets);
                i = 0;
                bullets.bullets.clear();
            }
        }
        server.sendToAllUDP(bullets);

        server.sendToAllUDP(entitiesRemoved);  //Use TCP instead?
    }

    public void updatePlayerName(int id, Lobby.NameChange nameChange) {
        PlayerEntity player = players.get(id);
        player.name = nameChange.newName;
        players.remove(id);
        players.put(id, player);
        updateLobbyDetails();
    }

    public void kickPlayer(Lobby.Kick player) {
        players.remove(player.playerToBeKicked.id);
        updateLobbyDetails();
    }

    public void updateActMapDifficulty(Lobby.ActMapDifficulty obj) {
        this.act = obj.act;
        this.map = obj.map;
        this.difficulty = obj.difficulty;
        updateLobbyDetails();
    }

    public void updateConnection(int id, PlayerEntity player) {
        players.put(id, player);
        updateLobbyDetails();
    }

    public void performHandshake(int id) {
        Handshake.Request request = new Handshake.Request();
        for (PlayerEntity player : players.values()) {
            request.names.add(player.name);
        }
        request.id = id;
        server.sendToTCP(id, request);
    }

    public void onDisconnected(int id) {
        players.remove(id);
        updateLobbyDetails();
    }

    public void onStartGame() {
        Gdx.app.postRunnable(() -> {
            serverLogic.loadWorld(act, map);
        });
        serverLogic.setDifficulty(difficulty);

        while (!serverLogic.isLoaded()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (PlayerEntity player : getPlayers()) {
            serverLogic.addPlayer(player);
        }

        for (int i = 0; i < 5; i++) {
            serverLogic.addWeapon(ee.taltech.iti0202.gui.game.desktop.entities.weapons2.Weapon.Type.M4);
            serverLogic.addWeapon(ee.taltech.iti0202.gui.game.desktop.entities.weapons2.Weapon.Type.DEAGLE);
            serverLogic.addWeapon(ee.taltech.iti0202.gui.game.desktop.entities.weapons2.Weapon.Type.SHOTGUN);
        }
        Lobby.StartGame data = new Lobby.StartGame();
        data.details.act = act;
        data.details.map = map;
        data.details.difficulty = difficulty;
        server.sendToAllTCP(data);

        serverLogic.run(getPlayers());
        System.out.println("Server word ready!");
    }

    public void onUpdatePlayerControls(PlayerControls controls) {
        serverLogic.updatePlayerControls(controls);
    }

    private void updateLobbyDetails() {
        Lobby.Details details = new Lobby.Details();
        details.act = act;
        details.map = map;
        details.difficulty = difficulty;
        for (Connection con : server.getConnections()) {
            if (players.containsKey(con.getID())) {
                PlayerEntity player = players.get(con.getID());
                player.latency = con.getReturnTripTime();
                details.players.add(player);
            }
        }
        server.sendToAllTCP(details);
    }

    public Set<String> getNames() {
        Set<String> names = new HashSet<>();
        for (PlayerEntity player : players.values()) {
            names.add(player.name);
        }
        return names;
    }

    @Override
    public void dispose() {
        serverLogic.dispose();
        try {
            server.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
