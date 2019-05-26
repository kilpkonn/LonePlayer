package ee.taltech.iti0202.gui.game.desktop.entities.player.handler;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.entities.Handler;
import ee.taltech.iti0202.gui.game.desktop.entities.checkpoints.Checkpoint;
import ee.taltech.iti0202.gui.game.desktop.entities.player.Player;
import ee.taltech.iti0202.gui.game.desktop.entities.player.loader.PlayerLoader;
import ee.taltech.iti0202.gui.game.desktop.entities.projectile.bullet.Bullet;
import ee.taltech.iti0202.gui.game.desktop.entities.projectile.bullet.loader.BulletLoader;
import ee.taltech.iti0202.gui.game.desktop.entities.weapons.loader.WeaponLoader;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.MyContactListener;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.input.MyInput;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.canvas.Draw;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.sound.Sound;
import ee.taltech.iti0202.gui.game.desktop.states.Play;
import ee.taltech.iti0202.gui.game.desktop.states.gameprogress.GameProgress;
import lombok.Data;
import lombok.ToString;

import java.util.Random;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.sound.Sound.playSoundOnce;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.*;

@Data
public class PlayerHandler implements Handler {

    @ToString.Exclude private Play play;
    private Player player;
    private GameProgress progress;
    private SpriteBatch spriteBatch;
    private Vector2 tempPlayerLocation = new Vector2();
    private Vector2 initPlayerLocation;
    private Checkpoint checkpoint;
    private Vector2 current_force = new Vector2(0, 0);
    private boolean newPlayer = true;
    private int gracePeriod = 60;
    private int stepTime = 0;
    private int selectedWeapon = 0;
    private GameProgress gameProgress;
    private Draw draw;
    private MyContactListener cl;
    private static final Random RANDOM = new Random();

    public PlayerHandler(
            Play play, SpriteBatch sb, GameProgress gameProgress, MyContactListener cl, Draw draw) {
        this.play = play;
        this.spriteBatch = sb;
        this.progress = gameProgress;
        this.cl = cl;
        this.draw = draw;
    }

    public void handlePlayerInput(Play.pauseState playState) {
        current_force = player.getBody().getLinearVelocity();

        if (!MyInput.isDown(-1) && cl.isPlayerOnGround()) {
            player.setAnimation(Player.PlayerAnimation.IDLE);
        }
        if (playState == Play.pauseState.RUN) {
            // player jump / double jump / dash
            if (MyInput.isPressed(Game.settings.JUMP)) {
                if (cl.isPlayerOnGround()) {
                    Sound.playSoundOnce("sounds/jump/flaunch.ogg", 0.07f);
                    player.getBody()
                            .applyLinearImpulse(
                                    new Vector2(0, PLAYER_DASH_FORCE_UP),
                                    tempPlayerLocation,
                                    true); // .applyForceToCenter(0, PLAYER_DASH_FORCE_UP, true);
                    player.setAnimation(Player.PlayerAnimation.JUMP);
                } else if (cl.getWallJump() != 0) {
                    Sound.playSoundOnce("sounds/jump/flaunch.ogg", 0.07f);
                    System.out.println("Walljump");
                    player.getBody()
                            .applyLinearImpulse(
                                    new Vector2(
                                            cl.getWallJump() * PLAYER_DASH_FORCE_UP,
                                            PLAYER_DASH_FORCE_UP),
                                    tempPlayerLocation,
                                    true);
                    cl.setWallJump(0);
                } else if (cl.isDoubleJump()) {
                    Sound.playSoundOnce("sounds/jump/flaunch.ogg", 0.07f);
                    System.out.println("Double jump");
                    player.getBody()
                            .applyLinearImpulse(
                                    new Vector2(0, PLAYER_DASH_FORCE_UP), tempPlayerLocation, true);
                    cl.setDoubleJump(false);
                    player.setAnimation(Player.PlayerAnimation.ROLL, true);
                }
            }

            // player move left
            if (MyInput.isDown(Game.settings.MOVE_LEFT)) {
                if (cl.isPlayerOnGround()) {
                    if (stepTime == 0) {
                        playSoundOnce(
                                "sounds/footsteps/Footstep_Dirt_0" + RANDOM.nextInt(9) + ".ogg", 0.1f);
                        stepTime = 10;
                    } else {
                        stepTime--;
                    }
                }
                if (current_force.x > -MAX_SPEED) {
                    if (cl.isPlayerOnGround()) {
                        player.getBody().applyForceToCenter(-PLAYER_SPEED, 0, true);
                        player.setAnimation(Player.PlayerAnimation.RUN);
                    } else {
                        player.getBody().applyForceToCenter(-PLAYER_SPEED * 1.25f, 0, true);
                    }
                }
                player.setFlipX(true);
            }

            // player dash left
            if (MyInput.isPressed(Game.settings.MOVE_LEFT)) {
                if (!cl.isPlayerOnGround() && cl.isDash()) {
                    Sound.playSoundOnce("sounds/jump/slimeball.ogg", 0.07f);
                    current_force = player.getBody().getLinearVelocity();
                    if (current_force.x > 0) {
                        player.getBody()
                                .applyLinearImpulse(
                                        new Vector2(-current_force.x, 0), tempPlayerLocation, true);
                    } else {
                        player.getBody()
                                .applyLinearImpulse(
                                        new Vector2(-PLAYER_DASH_FORCE_SIDE, 0),
                                        tempPlayerLocation,
                                        true);
                    }
                    cl.setDash(false);
                    player.setAnimation(Player.PlayerAnimation.DASH);
                }
                player.setFlipX(true);
            }

            // player move right
            if (MyInput.isDown(Game.settings.MOVE_RIGHT)) {
                if (cl.isPlayerOnGround()) {
                    if (stepTime == 0) {
                        playSoundOnce(
                                "sounds/footsteps/Footstep_Dirt_0" + RANDOM.nextInt(9) + ".ogg", 0.1f);
                        stepTime = 10;
                    } else {
                        stepTime--;
                    }
                }
                if (current_force.x < MAX_SPEED) {
                    if (cl.isPlayerOnGround()) {
                        player.setAnimation(Player.PlayerAnimation.RUN);
                        player.getBody().applyForceToCenter(PLAYER_SPEED, 0, true);
                    } else {
                        player.getBody().applyForceToCenter(PLAYER_SPEED * 1.25f, 0, true);
                    }
                }
                player.setFlipX(false);
            }

            // player dash right
            if (MyInput.isPressed(Game.settings.MOVE_RIGHT)) {
                if (!cl.isPlayerOnGround() && cl.isDash()) {
                    Sound.playSoundOnce("sounds/jump/slimeball.ogg", 0.07f);
                    if (current_force.x < 0) {
                        player.getBody()
                                .applyLinearImpulse(
                                        new Vector2(-current_force.x, 0), tempPlayerLocation, true);
                    } else {
                        player.getBody()
                                .applyLinearImpulse(
                                        new Vector2(PLAYER_DASH_FORCE_SIDE, 0),
                                        tempPlayerLocation,
                                        true);
                    }
                    cl.setDash(false);
                    player.setAnimation(Player.PlayerAnimation.DASH);
                }
                player.setFlipX(false);
            }

            // change dimension
            if (MyInput.isPressed(Game.settings.CHANGE_DIMENSION)) {
                System.out.println("changed dimension");
                draw.setDimensionFadeDone(false);
                draw.setDimension(!draw.isDimension());

                short mask;
                if (draw.isDimension()) {
                    mask =
                            BIT_BOSSES
                                    | BIT_WORM
                                    | DIMENTSION_1
                                    | DIMENTSION_2
                                    | TERRA_SQUARES
                                    | BACKGROUND
                                    | TERRA_DIMENTSION_1;
                } else {
                    mask =
                            BIT_BOSSES
                                    | BIT_WORM
                                    | DIMENTSION_1
                                    | DIMENTSION_2
                                    | TERRA_SQUARES
                                    | BACKGROUND
                                    | TERRA_DIMENTSION_2;
                }

                Filter filter = new Filter();
                for (Fixture playerFixture : player.getBody().getFixtureList()) {
                    filter.groupIndex = playerFixture.getFilterData().groupIndex;
                    filter.categoryBits = playerFixture.getFilterData().categoryBits;
                    filter.maskBits = mask;
                    playerFixture.setFilterData(filter);
                }
            }

            if (MyInput.isPressed(Game.settings.NEXT_WEAPON)) {
                if (player.getWeapons().size() > 0) {
                    selectedWeapon++;
                    selectedWeapon %= player.getWeapons().size();
                    player.setCurrentWeapon(player.getWeapons().get(selectedWeapon));
                }
            }

            if (MyInput.isPressed(Game.settings.PREVIOUS_WEAPON)) {
                if (player.getWeapons().size() > 0) {
                    selectedWeapon--;
                    if (selectedWeapon < 0) selectedWeapon += player.getWeapons().size();
                    selectedWeapon %= player.getWeapons().size();
                    player.setCurrentWeapon(player.getWeapons().get(selectedWeapon));
                }
            }

            if (MyInput.isMouseDown(Game.settings.SHOOT)
                    && player.getCurrentWeapon() != null
                    && player.getCurrentWeapon().canFire()) {
                playSoundOnce("sounds/jumpland.wav");
                player.getCurrentWeapon().fire(new Vector2(MyInput.getMouseLocation().x,
                        V_HEIGHT - MyInput.getMouseLocation().y));
            }
        }
    }

    @Override
    public void update(float dt) {

        // call update animation
        if (player.getHealth() == 0) {
            cl.setDeathState((short) 3);
        }

        if (cl.getDeathState() == 0) {
            player.update(dt);

        } else if (getGracePeriod() == 0) {
            if (cl.getDeathState() == 2) {
                player.setHealth(player.getHealth() - gotHitBySnek * 10);
                playSoundOnce("sounds/aww/aw0" + new Random().nextInt(6) + ".ogg");
            }
            if (cl.getDeathState() == 3) {
                if (!isNewPlayer()) player.setHealth(0); // TODO: Fix instant death on load game
            } else {
                String a = new Random().nextBoolean() ? "a" : "b";
                playSoundOnce("sounds/hit/sword-1" + a + ".ogg", 0.05f);
                player.setHealth(player.getHealth() - gotHitBySnek);
            }
            if (player.getHealth() <= 0) {
                playSoundOnce("sounds/sfx_sound_shutdown1.wav");

                draw.getWeaponHandler().deadPlayerWeaponTransfer(player);
                this.player = PlayerLoader.initPlayer(spriteBatch, this, draw, play.getWorld(), cl);
                this.player.addWeapon(
                        WeaponLoader.buildWeapon(
                                "M4",
                                spriteBatch,
                                draw.getWeaponHandler(),
                                draw)); // after death gets deagle and m4,
                // why not
                this.player.addWeapon(
                        WeaponLoader.buildWeapon("Deagle", spriteBatch, draw.getWeaponHandler(), draw));
                this.player.setCurrentWeapon(player.getWeapons().get(0));
            }
            cl.setDeathState((short) 0);
        }
        player.update(dt);

        if (gracePeriod > 0) gracePeriod -= 1;
    }

    @Override
    public void render(SpriteBatch sb) {

        // draw player
        if (player != null) player.render(sb);
    }
}
