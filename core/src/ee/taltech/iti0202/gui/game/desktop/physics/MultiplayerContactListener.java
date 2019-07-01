package ee.taltech.iti0202.gui.game.desktop.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import java.util.HashMap;
import java.util.Map;

import ee.taltech.iti0202.gui.game.networking.server.player.Player;

public class MultiplayerContactListener implements ContactListener {

    public Map<Integer, Player> players;
    private HashMap<Body, Body> collidedBullets = new HashMap<>();

    public MultiplayerContactListener(Map<Integer, Player> players) {
        this.players = players;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa.getUserData() != null && fb.getUserData() != null) {

            // detect bullet collision
            bulletDetection(fa, fb);

            // set wall jump
            setWallJump(fa, fb, (short) 1);

            // detection happens when player goes outside of initial game border
            dmgDetection(fa, fb);
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    private void bulletDetection(Fixture fa, Fixture fb) {
        if (fa.getUserData().toString().endsWith("bullet")) {
            collidedBullets.put(fa.getBody(), fb.getBody());
        } else if (fb.getUserData().toString().endsWith("bullet")) {
            collidedBullets.put(fb.getBody(), fa.getBody());
        }
    }

    private void dmgDetection(Fixture fa, Fixture fb) {
        Object oa = fa.getBody().getUserData();
        Object ob = fb.getBody().getUserData();

        if (oa instanceof PlayerBody.PlayerBodyData) {
            Player player = players.get(((PlayerBody.PlayerBodyData) oa).id);
            if (ob.equals("barrier")) {
                player.health = 0;
            }
        }

        if (ob instanceof PlayerBody.PlayerBodyData) {
            Player player = players.get(((PlayerBody.PlayerBodyData) ob).id);
            if (oa.equals("barrier")) {
                player.health = 0;
            }
        }
    }

    private void setWallJump(Fixture fa, Fixture fb, short i) {
        Object oa = fa.getBody().getUserData();
        Object ob = fb.getBody().getUserData();

        Player playerA = players.get(((PlayerBody.PlayerBodyData) oa).id);
        Player playerB = players.get(((PlayerBody.PlayerBodyData) ob).id);

        if (fa.getUserData().equals("side_r")) {
            playerA.wallJump = (short) (-1 * i);
        }
        if (fb.getUserData().equals("side_r")) {
            playerB.wallJump = (short) (-1 * i);
        }

        if (fa.getUserData().equals("side_l")) {
            playerA.wallJump = i;
        }
        if (fb.getUserData().equals("side_l")) {
            playerB.wallJump = i;
        }
    }

}
