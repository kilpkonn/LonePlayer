package ee.taltech.iti0202.gui.game.networking.shared;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;

import java.util.HashSet;

import ee.taltech.iti0202.gui.game.desktop.entities.player2.Player;
import ee.taltech.iti0202.gui.game.desktop.entities.projectile2.WeaponProjectile;
import ee.taltech.iti0202.gui.game.desktop.entities.weapons2.Weapon;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.networking.serializable.Handshake;
import ee.taltech.iti0202.gui.game.networking.serializable.Lobby;
import ee.taltech.iti0202.gui.game.networking.serializable.Play;
import ee.taltech.iti0202.gui.game.networking.server.entity.BulletEntity;
import ee.taltech.iti0202.gui.game.networking.server.entity.Entity;
import ee.taltech.iti0202.gui.game.networking.server.entity.PlayerControls;
import ee.taltech.iti0202.gui.game.networking.server.entity.PlayerEntity;
import ee.taltech.iti0202.gui.game.networking.server.entity.WeaponEntity;

public final class SerializableClassesHandler {

    public static void registerClasses(Kryo kryo) {
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
    }
}
