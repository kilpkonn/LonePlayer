package ee.taltech.iti0202.gui.game.desktop.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import java.util.Map;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BACKGROUND;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BIT_BULLET;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.TERRA_DIMENSION_1;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.TERRA_DIMENSION_2;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.TERRA_SQUARES;

public class MultiplayerContactListener implements ContactListener {

    public Map<Integer, PlayerBody.PlayerBodyData> players;

    public MultiplayerContactListener(Map<Integer, PlayerBody.PlayerBodyData> players) {
        this.players = players;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        Object oa = fa.getUserData();
        Object ob = fb.getUserData();

        //if (oa != null && ob != null) {

        // detect bullet collision
        bulletDetection(fa, fb);

        // set wall jump
        setWallJump(oa, ob, 1);

        groundDetection(oa, ob);

        weaponPickup(oa, ob, fa, fa);

        // detection happens when player goes outside of initial game border
        dmgDetection(oa, ob);
        //}
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        Object oa = fa.getUserData();
        Object ob = fb.getUserData();

        //if (oa != null && ob != null) {

        setWallJump(oa, ob, 0);

        PlayerBody.PlayerBodyData player;
        if (oa instanceof PlayerBody.PlayerFoot) {
            player = players.get(((PlayerBody.PlayerFoot) oa).id);

            player.onGround = false;
            player.doubleJump = true;
            // wallJump = 0;
            player.dash = true;
        }
        if (ob instanceof PlayerBody.PlayerFoot) {
            player = players.get(((PlayerBody.PlayerFoot) ob).id);

            player.onGround = false;
            player.doubleJump = true;
            // wallJump = 0;
            player.dash = true;
        }
        //}
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    private void groundDetection(Object oa, Object ob) {
        if (oa instanceof PlayerBody.PlayerFoot) {
            players.get(((BodyData) oa).id).onGround = true;
        }
        if (ob instanceof PlayerBody.PlayerFoot) {
            players.get(((BodyData) ob).id).onGround = true;
        }
    }

    private void weaponPickup(Object oa, Object ob, Fixture fa, Fixture fb) {
        // TODO: Seems to fail often when 1 weapon is already picked up
        short mask = TERRA_SQUARES | BACKGROUND | TERRA_DIMENSION_1 | TERRA_DIMENSION_2 | BIT_BULLET;
        Filter filter = new Filter();
        if (oa != null && PlayerBody.class.equals(oa.getClass().getEnclosingClass()) && ob instanceof WeaponBody.WeaponBodyData) {
            PlayerBody.PlayerBodyData data = players.get(((BodyData) oa).id);
            WeaponBody.WeaponBodyData weaponBodyData = (WeaponBody.WeaponBodyData) ob;
            if (weaponBodyData.dropped) {
                for (int i = 0; i < data.weapons.length; i++) {
                    if (data.weapons[i] == null) {
                        data.weapons[i] = weaponBodyData;
                        weaponBodyData.dropped = false;

                        filter.groupIndex = fb.getFilterData().groupIndex;
                        filter.categoryBits = fb.getFilterData().categoryBits;
                        filter.maskBits = mask;
                        fb.setFilterData(filter);
                        break;
                    }
                }
            }
        }

        if (ob != null && PlayerBody.class.equals(ob.getClass().getEnclosingClass()) && oa instanceof WeaponBody.WeaponBodyData) {
            PlayerBody.PlayerBodyData data = players.get(((BodyData) ob).id);
            WeaponBody.WeaponBodyData weaponBodyData = (WeaponBody.WeaponBodyData) oa;
            if (weaponBodyData.dropped) {
                for (int i = 0; i < data.weapons.length; i++) {
                    if (data.weapons[i] == null) {
                        data.weapons[i] = weaponBodyData;
                        weaponBodyData.dropped = false;
                        filter.groupIndex = fa.getFilterData().groupIndex;
                        filter.categoryBits = fa.getFilterData().categoryBits;
                        filter.maskBits = mask;
                        fa.setFilterData(filter);
                        break;
                    }
                }
            }
        }
    }

    private void bulletDetection(Object oa, Object ob) {
        if (oa instanceof BulletBody.BulletBodyData) {
            ((BulletBody.BulletBodyData) oa).isHit = true;
        }
        if (ob instanceof BulletBody.BulletBodyData) {
            ((BulletBody.BulletBodyData) ob).isHit = true;
        }
    }

    private void dmgDetection(Object oa, Object ob) {
        if (oa == null || ob == null) {
            return;
        }

        if (oa instanceof PlayerBody.PlayerBodyData) {
            PlayerBody.PlayerBodyData player = players.get(((PlayerBody.PlayerBodyData) oa).id);
            if (ob.equals("barrier")) {
                player.health = 0;
            }
        }

        if (ob instanceof PlayerBody.PlayerBodyData) {
            PlayerBody.PlayerBodyData player = players.get(((PlayerBody.PlayerBodyData) ob).id);
            if (oa.equals("barrier")) {
                player.health = 0;
            }
        }
    }

    private void setWallJump(Object oa, Object ob, int i) {
        PlayerBody.PlayerBodyData playerA;
        PlayerBody.PlayerBodyData playerB;

        if (oa instanceof PlayerBody.PlayerRightSide) {
            playerA = players.get(((PlayerBody.PlayerRightSide) oa).id);
            playerA.wallJump = -1 * i;
        } else if (oa instanceof PlayerBody.PlayerLeftSide) {
            playerA = players.get(((PlayerBody.PlayerLeftSide) oa).id);
            playerA.wallJump = i;
        }

        if (ob instanceof PlayerBody.PlayerRightSide) {
            playerB = players.get(((PlayerBody.PlayerRightSide) ob).id);
            playerB.wallJump = -1 * i;
        } else if (ob instanceof PlayerBody.PlayerLeftSide) {
            playerB = players.get(((PlayerBody.PlayerLeftSide) ob).id);
            playerB.wallJump = i;
        }
    }

}
