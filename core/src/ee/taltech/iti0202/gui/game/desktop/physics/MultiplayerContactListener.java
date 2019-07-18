package ee.taltech.iti0202.gui.game.desktop.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import java.util.HashMap;
import java.util.Map;

public class MultiplayerContactListener implements ContactListener {

    public Map<Integer, PlayerBody.PlayerBodyData> players;
    private HashMap<Body, Body> collidedBullets = new HashMap<>();

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

        weaponPickup(oa, ob);

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
            players.get(((PlayerBody.PlayerFoot) oa).id).onGround = true;
        }
        if (ob instanceof PlayerBody.PlayerFoot) {
            players.get(((PlayerBody.PlayerFoot) ob).id).onGround = true;
        }
    }

    private void weaponPickup(Object oa, Object ob) {
        if (oa instanceof  PlayerBody.PlayerBodyData && ob instanceof WeaponBody.WeaponBodyData) {
            PlayerBody.PlayerBodyData data = players.get(((PlayerBody.PlayerBodyData) oa).id);
            for (int i = 0; i < data.weapons.length; i++) {
                if (data.weapons[i] == null) {
                    WeaponBody.WeaponBodyData weaponBodyData = (WeaponBody.WeaponBodyData) ob;
                    data.weapons[i] = weaponBodyData;
                    weaponBodyData.dropped = false;
                }
            }
        }

        if (ob instanceof  PlayerBody.PlayerBodyData && oa instanceof WeaponBody.WeaponBodyData) {
            PlayerBody.PlayerBodyData data = players.get(((PlayerBody.PlayerBodyData) ob).id);
            for (int i = 0; i < data.weapons.length; i++) {
                if (data.weapons[i] == null) {
                    WeaponBody.WeaponBodyData weaponBodyData = (WeaponBody.WeaponBodyData) oa;
                    data.weapons[i] = weaponBodyData;
                    weaponBodyData.dropped = false;
                }
            }
        }
    }

    private void bulletDetection(Fixture fa, Fixture fb) {
        /*if (fa.getUserData() == null && fb.getUserData() == null) {
            return;
        }
        if (fa.getUserData().toString().endsWith("bullet")) {
            collidedBullets.put(fa.getBody(), fb.getBody());
        } else if (fb.getUserData().toString().endsWith("bullet")) {
            collidedBullets.put(fb.getBody(), fa.getBody());
        }*/
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
