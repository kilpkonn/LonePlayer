package ee.taltech.iti0202.gui.game.desktop.entities.player.handler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.entities.player.Player;
import ee.taltech.iti0202.gui.game.desktop.entities.player.loader.PlayerLoader;
import ee.taltech.iti0202.gui.game.desktop.entities.player.weapons.Weapon;
import ee.taltech.iti0202.gui.game.desktop.entities.player.weapons.handler.WeaponHandler;
import ee.taltech.iti0202.gui.game.desktop.entities.player.weapons.loader.WeaponLoader;
import ee.taltech.iti0202.gui.game.desktop.entities.projectile.bullet.Bullet;
import ee.taltech.iti0202.gui.game.desktop.entities.projectile.bullet.BulletLoader;
import ee.taltech.iti0202.gui.game.desktop.entities.staticobjects.Checkpoint;
import ee.taltech.iti0202.gui.game.desktop.handlers.gdx.MyContactListener;
import ee.taltech.iti0202.gui.game.desktop.handlers.gdx.input.MyInput;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.layers.Draw;
import ee.taltech.iti0202.gui.game.desktop.states.Play;
import ee.taltech.iti0202.gui.game.desktop.states.gameprogress.GameProgress;
import lombok.Data;
import lombok.ToString;

import static ee.taltech.iti0202.gui.game.desktop.handlers.sound.Sound.playSoundOnce;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BACKGROUND;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BIT_BOSSES;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BIT_WORM;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.DIMENTSION_1;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.DIMENTSION_2;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.MAX_SPEED;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PLAYER_DASH_FORCE_SIDE;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PLAYER_DASH_FORCE_UP;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PLAYER_SPEED;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.TERRA_DIMENTSION_1;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.TERRA_DIMENTSION_2;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.TERRA_SQUARES;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_WIDTH;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.gotHitBySnek;

@Data
public class PlayerHandler {

    private Player player;
    private GameProgress progress;
    private SpriteBatch spriteBatch;

    private Vector2 tempPlayerLocation = new Vector2();
    private Vector2 initPlayerLocation;
    private Vector2 current_force = new Vector2(0, 0);
    private boolean newPlayer = true;
    private int gracePeriod = 60;
    private GameProgress gameProgress;
    @ToString.Exclude
    private Play play;
    private Draw draw;
    private MyContactListener cl;

    //Checkpoints
    private Array<Checkpoint> checkpointList = new Array<>();
    private Checkpoint activeCheckpoint;

    //Bullets
    private Array<Bullet> bulletArray = new Array<>();
    private int bulletHeat = 10;

    //weapon
    private WeaponHandler weaponHandling;

    public PlayerHandler(Play play, SpriteBatch sb, GameProgress gameProgress, MyContactListener cl, Draw draw) {
        this.play = play;
        this.spriteBatch = sb;
        this.progress = gameProgress;
        this.cl = cl;
        this.draw = draw;
    }

    public void handlePlayerInput(Play.pauseState playState) {
        current_force = player.getBody().getLinearVelocity();

        //player jump / double jump / dash
        if (MyInput.isPressed(Game.settings.JUMP)) {
            if (cl.isPlayerOnGround()) {
                player.getBody().applyLinearImpulse(new Vector2(0, PLAYER_DASH_FORCE_UP), tempPlayerLocation, true);//.applyForceToCenter(0, PLAYER_DASH_FORCE_UP, true);
                player.setAnimation(Player.PlayerAnimation.JUMP);
            } else if (cl.getWallJump() != 0) {
                System.out.println("Walljump");
                player.getBody().applyLinearImpulse(new Vector2(cl.getWallJump() * PLAYER_DASH_FORCE_UP, PLAYER_DASH_FORCE_UP), tempPlayerLocation, true);
                cl.setWallJump(0);
            } else if (cl.isDoubleJump()) {
                System.out.println("Double jump");
                player.getBody().applyLinearImpulse(new Vector2(0, PLAYER_DASH_FORCE_UP), tempPlayerLocation, true);
                cl.setDoubleJump(false);
                player.setAnimation(Player.PlayerAnimation.ROLL, true);
            }
        }

        //player move left
        if (MyInput.isDown(Game.settings.MOVE_LEFT)) {
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

        //player dash left
        if (MyInput.isPressed(Game.settings.MOVE_LEFT)) {
            if (!cl.isPlayerOnGround() && cl.isDash()) {
                current_force = player.getBody().getLinearVelocity();
                if (current_force.x > 0) {
                    player.getBody().applyLinearImpulse(new Vector2(-current_force.x, 0), tempPlayerLocation, true);
                } else {
                    player.getBody().applyLinearImpulse(new Vector2(-PLAYER_DASH_FORCE_SIDE, 0), tempPlayerLocation, true);
                }
                cl.setDash(false);
                player.setAnimation(Player.PlayerAnimation.DASH);
            }
            player.setFlipX(true);
        }

        //player move right
        if (MyInput.isDown(Game.settings.MOVE_RIGHT)) {
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

        //player dash right
        if (MyInput.isPressed(Game.settings.MOVE_RIGHT)) {
            if (!cl.isPlayerOnGround() && cl.isDash()) {
                if (current_force.x < 0) {
                    player.getBody().applyLinearImpulse(new Vector2(-current_force.x, 0), tempPlayerLocation, true);
                } else {
                    player.getBody().applyLinearImpulse(new Vector2(PLAYER_DASH_FORCE_SIDE, 0), tempPlayerLocation, true);
                }
                cl.setDash(false);
                player.setAnimation(Player.PlayerAnimation.DASH);
            }
            player.setFlipX(false);
        }

        if (!MyInput.isDown(-1) && cl.isPlayerOnGround()) {
            player.setAnimation(Player.PlayerAnimation.IDLE);
        }

        if (playState == Play.pauseState.RUN) {
            //change dimension
            if (MyInput.isPressed(Game.settings.CHANGE_DIMENSION)) {
                System.out.println("changed dimension");
                draw.setDimensionFadeDone(false);
                draw.setDimension(!draw.isDimension());

                short mask;
                if (draw.isDimension()) {
                    mask = BIT_BOSSES | BIT_WORM | DIMENTSION_1 | DIMENTSION_2 | TERRA_SQUARES | BACKGROUND | TERRA_DIMENTSION_1;
                } else {
                    mask = BIT_BOSSES | BIT_WORM | DIMENTSION_1 | DIMENTSION_2 | TERRA_SQUARES | BACKGROUND | TERRA_DIMENTSION_2;
                }

                Filter filter = new Filter();
                for (Fixture playerFixture : player.getBody().getFixtureList()) {
                    filter.groupIndex = playerFixture.getFilterData().groupIndex;
                    filter.categoryBits = playerFixture.getFilterData().categoryBits;
                    filter.maskBits = mask;
                    playerFixture.setFilterData(filter);
                }

            }


            if (MyInput.isMouseDown(Game.settings.SHOOT) && bulletHeat == 0) {
                bulletHeat = 10;
                BulletLoader bulletLoader = new BulletLoader();
                Bullet bullet = bulletLoader.bulletLoader(play, spriteBatch, play.getWorld(), new Vector2(V_WIDTH >> 1, V_HEIGHT >> 1), new Vector2(Gdx.input.getX(), V_HEIGHT - Gdx.input.getY()), player.getPosition());
                bulletArray.add(bullet);
            } else {
                if (bulletHeat > 0) {
                    bulletHeat--;
                }
            }

        }
    }

    public void updatePlayer(float dt) {
        //update weapon
        if (weaponHandling != null) {
            weaponHandling.update(dt);
        }
        /*if (weaponHandling != null) {
            weaponHandling.update(dt);
        }*/

        //call update animation
        if (player.getHealth() == 0) {
            cl.setDeathState((short) 3);
        }

        if (cl.getDeathState() == 0) {
            player.update(dt);

        } else if (getGracePeriod() == 0) {
            if (cl.getDeathState() == 2) {
                player.setHealth(player.getHealth() - gotHitBySnek * 10);
                playSoundOnce("sounds/sfx_deathscream_alien1.wav");
            }
            if (cl.getDeathState() == 3) {
                if (!isNewPlayer())
                    player.setHealth(0); //TODO: Fix instant death on load game
            } else {
                playSoundOnce("sounds/sfx_damage_hit2.wav", 0.1f);
                player.setHealth(player.getHealth() - gotHitBySnek);
            }
            if (player.getHealth() <= 0) {
                playSoundOnce("sounds/sfx_sound_shutdown1.wav");

                this.player = PlayerLoader.initPlayer(spriteBatch, this, draw, play.getWorld(), cl);
            }
            cl.setDeathState((short) 0);
        }
        player.update(dt);

        //update bullets
        if (bulletArray != null) {
            for (Bullet bullet : bulletArray) {
                bullet.update(dt);
            }
        }

        if (gracePeriod > 0)
            gracePeriod -= 1;

        // create a new checkpoint if needed
        if (checkpointList != null) {
            if (cl.isNewCheckpoint()) {
                Checkpoint curTemp = checkpointList.get(0); // just in case
                for (Checkpoint checkpoint : checkpointList) {
                    if (Math.pow(checkpoint.getPosition().x - player.getBody().getPosition().x, 2) + Math.pow(checkpoint.getPosition().y - player.getBody().getPosition().y, 2) <=
                            Math.pow(curTemp.getPosition().x - player.getBody().getPosition().x, 2) + Math.pow(curTemp.getPosition().y - player.getBody().getPosition().y, 2)) {
                        curTemp = checkpoint;
                    }
                }
                cl.setCurCheckpoint(curTemp.getBody());
                curTemp.onReached();
                if (activeCheckpoint != null)
                    activeCheckpoint.lostItsPurposeAsANewBetterCheckpointWasFoundAndTheOldOneWasTossedAwayLikeAnOldCondom();
                activeCheckpoint = curTemp;

                playSoundOnce("sounds/checkpoint.ogg");
            }
            for (Checkpoint checkpoint : checkpointList)
                checkpoint.update(dt);
        }
    }

    public void renderPlayer(SpriteBatch sb) {
        // draw weapon
        if (weaponHandling != null) {
            weaponHandling.render(spriteBatch);
        }

        // draw checkpoint
        if (checkpointList.size != 0)
            for (Checkpoint checkpoint : checkpointList) {
                checkpoint.render(sb);
            }

        // draw player
        if (player != null) player.render(sb);


        // draw bullets
        if (bulletArray != null) {
            for (Bullet bullet : bulletArray) {
                bullet.render(sb);
            }
        }
    }

    public Weapon initWeapon(String type) {
        Weapon weapon = WeaponLoader.buildWeapon(type, spriteBatch, weaponHandling);
        player.setWeapon(weapon); //TODO: Not auto add to player
        return weapon;
    }

    public void initWeaponHandling() {
        this.weaponHandling = new WeaponHandler(play.getWorld());
    }
}
