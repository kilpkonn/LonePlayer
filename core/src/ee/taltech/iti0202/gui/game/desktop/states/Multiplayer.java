package ee.taltech.iti0202.gui.game.desktop.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.controllers.WeaponController;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.GameStateManager;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.input.MyInput;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.hud.Hud;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.hud.Scoreboard;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.MultiplayerPauseMenu;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.SettingsMenu;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.animations.ParallaxBackground;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.desktop.physics.GameWorld;
import ee.taltech.iti0202.gui.game.desktop.controllers.PlayerController;
import ee.taltech.iti0202.gui.game.desktop.render.WorldRenderer;
import ee.taltech.iti0202.gui.game.networking.serializable.Play;
import ee.taltech.iti0202.gui.game.networking.server.entity.BulletEntity;
import ee.taltech.iti0202.gui.game.networking.server.entity.PlayerEntity;
import ee.taltech.iti0202.gui.game.networking.server.entity.PlayerControls;
import ee.taltech.iti0202.gui.game.networking.server.entity.WeaponEntity;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BACKGROUND_SCREENS;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BACKGROUND_SPEEDS;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BOSSES;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BOSS_BASE_HP;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.CHECKPOINTS;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.DMG_MULTIPLIER;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.DMG_ON_LANDING_SPEED;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.MAIN_SCREENS;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.PATH;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.V_WIDTH;

public class Multiplayer extends GameState {

    private GameWorld gameWorld;
    private WorldRenderer worldRenderer;
    private PlayerController playerController;
    private WeaponController weaponController;
    private Hud hud;
    private Scoreboard scoreboard;

    private MultiplayerPauseMenu pauseMenu;
    private SettingsMenu settingsMenu;

    private Stage stage = new Stage(new ScreenViewport());
    private String act;
    private String map;
    private B2DVars.GameDifficulty difficulty;

    // Background based variables
    private Texture backgroundTexture;
    private ParallaxBackground parallaxBackground;
    private float backgroundSpeed;

    private float playTime = 0;

    private State state = State.RUN;

    private PlayerControls controls;
    private PlayerEntity playerToFollow;
    private boolean dimension = true;

    public Multiplayer(String act, String map, B2DVars.GameDifficulty difficulty) {
        this.act = act;
        this.map = map;
        this.difficulty = difficulty;

        System.out.println(String.format("Multiplayer game: %s - %s - %s", act, map, difficulty));

        switch (difficulty) {
            case EASY:
                DMG_MULTIPLIER = 1;
                DMG_ON_LANDING_SPEED = 6;
                CHECKPOINTS = true;
                BOSSES = false;
                break;

            case HARD:
                DMG_MULTIPLIER = 1.5f;
                DMG_ON_LANDING_SPEED = 5;
                CHECKPOINTS = true;
                BOSSES = true;
                break;

            case BRUTAL:
                DMG_MULTIPLIER = 2;
                DMG_ON_LANDING_SPEED = 4;
                CHECKPOINTS = true;
                BOSS_BASE_HP *= 2;
                BOSSES = true;
                break;
        }

        game.getSound().stop();
        List<String> songs =
                new ArrayList<>(
                        Arrays.asList(
                                "ObservingTheStar.ogg",
                                "Soliloquy.ogg",
                                "Rise of spirit.ogg",
                                "Cyberpunk Moonlight Sonata.ogg",
                                "Hero Immortal.ogg", // Please credit me "Trevor Lentz" if you choose to use this. :)
                                "Vilified (2012).ogg",
                                "xeon6.ogg"));

        String song = songs.get(new Random().nextInt(songs.size()));

        game.setSound(Gdx.audio.newMusic(Gdx.files.internal(PATH + "sounds/" + song)));
        game.getSound().setLooping(true);
        game.getSound().play();
        game.getSound().setVolume(0.12f);

        // set up background
        String backgroundPath = MAIN_SCREENS[BACKGROUND_SCREENS.get(act)];
        backgroundTexture =
                new Texture(Gdx.files.local(PATH + backgroundPath + "backgroundLayer.png"));

        backgroundSpeed = BACKGROUND_SPEEDS.get(act);
        Array<Texture> textures = new Array<>();
        int layersCount = Gdx.files.local(PATH + backgroundPath).list().length;
        for (int i = 1; i < layersCount; i++) {
            textures.add(
                    new Texture(
                            Gdx.files.local(
                                    PATH + backgroundPath + "backgroundLayer" + i + ".png")));
            textures.get(textures.size - 1)
                    .setWrap(
                            Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }

        parallaxBackground = new ParallaxBackground(textures);
        parallaxBackground.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        parallaxBackground.setSpeed(0f);
        stage.addActor(parallaxBackground);

        gameWorld = new GameWorld(act, map);
        worldRenderer = new WorldRenderer(gameWorld, cam);
        weaponController = new WeaponController(gameWorld.getWeaponBodies(), gameWorld.getWeapons(), null);
        playerController = new PlayerController(gameWorld.getPlayerBodies(), gameWorld.getPlayers(), weaponController);

        hud = new Hud(hudCam);
        scoreboard = new Scoreboard(hudCam);

        pauseMenu = new MultiplayerPauseMenu(
                act,
                map,
                hudCam,
                () -> {
                    state = State.RUN;
                    hud.setGameFade(false);
                },
                () -> state = State.SETTINGS,
                () -> GameStateManager.pushState(GameStateManager.State.MENU));
        settingsMenu = new SettingsMenu(hudCam, () -> state = State.PAUSE);

        Game.setCursor(true);
    }

    public void updatePlayers(Set<PlayerEntity> players) {
        for (PlayerEntity player : players) {
            if (player == null) continue;

            gameWorld.updatePlayer(player);
            if (player.id == Game.client.id) {
                setPlayerToFollow(player);
            }
            worldRenderer.updatePlayerAnimation(player);
        }
        scoreboard.setPlayers(players);
    }

    public void updateWeapons(Set<WeaponEntity> weapons) {
        for (WeaponEntity weapon : weapons) {
            if (weapon == null) continue;

            gameWorld.updateWeapon(weapon);
            worldRenderer.updateWeaponAnimation(weapon);
        }
    }

    public void updateBullets(Set<BulletEntity> bullets) {
        for (BulletEntity bullet : bullets) {
            if (bullet == null) continue;

            gameWorld.updateBullet(bullet);
            worldRenderer.updateBulletAnimation(bullet);
        }
    }

    public void removeEntities(Play.EntitiesToBeRemoved entities) {
        for (int id : entities.players) {
            gameWorld.removePlayer(id);
            worldRenderer.removePlayerAnimation(id);
        }
        for (int id : entities.weapons) {
            gameWorld.removeWeapon(id);
            worldRenderer.removeWeaponAnimation(id);
        }
        for (int id : entities.bullets) {
            gameWorld.removeBullet(id);
            worldRenderer.removeBulletAnimation(id);
        }
    }

    @Override
    public void handleInput() {
        switch (state) {
            case RUN:
                handleRunInput();
                break;
            case PAUSE:
                pauseMenu.handleInput();
                break;
            case SETTINGS:
                settingsMenu.handleInput();
                break;
        }
    }

    @Override
    public void update(float dt) {
        playTime += dt;

        handleInput();
        gameWorld.update(dt);
        worldRenderer.update(dt);

        if (playerToFollow != null) {
            controls.bodyId = playerToFollow.bodyId;
            controls.id = playerToFollow.id;
            controls.dimension = dimension;
            controls.idle = !(controls.jump || controls.dashLeft || controls.dashRight || controls.moveLeft || controls.moveRight);
            Game.client.updatePlayerControls(controls);

            // Update locally to be ready for next frame. Waiting for server reply is too slow.
            playerToFollow.currentWeaponIndex = controls.currentWeapon;
            while (playerToFollow.currentWeaponIndex < 0 || playerToFollow.currentWeaponIndex > playerToFollow.weapons.length) {
                playerToFollow.currentWeaponIndex += playerToFollow.weapons.length * ((playerToFollow.currentWeaponIndex < 0) ? 1 : -1);
            }
        }

        hud.setPlayTime(playTime);
        if (playerToFollow != null) hud.setHp(playerToFollow.health);
        hud.update(dt);
        scoreboard.update(dt);

        switch (state) {
            case PAUSE:
                pauseMenu.update(dt);
                break;
            case SETTINGS:
                settingsMenu.update(dt);
                break;
        }

        if (state == State.RUN) {
            Game.setCursor(true);
        } else {
            Game.setCursor(false);
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        switch (state) {
            case RUN:
                renderWorld();
                break;
            case PAUSE:
                renderWorld();
                pauseMenu.render(sb);
                break;
            case SETTINGS:
                renderWorld();
                settingsMenu.render(sb);
                break;
        }
    }

    @Override
    public void dispose() {
        worldRenderer.dispose();
        pauseMenu.dispose();
        settingsMenu.dispose();
    }

    private void handleRunInput() {
        controls = new PlayerControls();
        if (playerToFollow != null) controls.currentWeapon = playerToFollow.currentWeaponIndex;

        if (MyInput.isPressed(Game.settings.JUMP)) {
            controls.jump = true;
            //playerController.tryJump(playerToFollow.id);
        }
        if (MyInput.isDown(Game.settings.MOVE_LEFT)) {
            controls.moveLeft = true;
            //playerController.tryMoveLeft(playerToFollow.id);
        }
        if (MyInput.isDown(Game.settings.MOVE_RIGHT)) {
            controls.moveRight = true;
            //playerController.tryDashRight(playerToFollow.id);
        }
        if (MyInput.isPressed(Game.settings.MOVE_LEFT)) {
            controls.dashLeft = true;
            //playerController.tryDashLeft(playerToFollow.id);
        }
        if (MyInput.isPressed(Game.settings.MOVE_RIGHT)) {
            controls.dashRight = true;
            //playerController.tryDashRight(playerToFollow.id);
        }
        if (MyInput.isPressed(Game.settings.CHANGE_DIMENSION)) {
            dimension = !dimension;
        }
        if (MyInput.isPressed(Game.settings.NEXT_WEAPON)) {
            controls.currentWeapon++;
            //playerController.trySetCurrentWeapon(playerToFollow.id, controls.currentWeapon);
        }
        if (MyInput.isPressed(Game.settings.PREVIOUS_WEAPON)) {
            controls.currentWeapon--;
            //playerController.trySetCurrentWeapon(playerToFollow.id, controls.currentWeapon);
        }
        if (MyInput.isMouseDown(Game.settings.SHOOT)) {
            controls.isAiming = true;
            controls.aimingAngle = (float) Math.atan2(
                    -MyInput.getMouseLocation().y + (double) V_HEIGHT / 2,
                    MyInput.getMouseLocation().x - (double) V_WIDTH / 2);
        }
        if (MyInput.isPressed(Game.settings.ESC)) {
            state = State.PAUSE;
            hud.setGameFade(true);
        }
    }

    private void renderWorld() {

        // clear screen
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.begin();
        sb.draw(backgroundTexture, 0, 0);
        sb.end();
        parallaxBackground.setSpeed(backgroundSpeed + worldRenderer.getCamSpeed().x / 10);
        stage.act();
        stage.draw();

        // draw tilemap

        worldRenderer.render(sb);

        hud.render(sb);

        if (MyInput.isDown(Game.settings.TAB)) {
            scoreboard.render(sb);
        }
    }

    public void setPlayerToFollow(PlayerEntity playerToFollow) {
        this.playerToFollow = playerToFollow;
        playerController.addAnimation(playerToFollow.bodyId);
        worldRenderer.setPlayerToFollow(playerToFollow);
    }

    private enum State {
        PAUSE,
        RUN,
        SETTINGS,
        END,
    }
}
