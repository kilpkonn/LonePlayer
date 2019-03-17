package ee.taltech.iti0202.gui.game.desktop.handlers.gdx;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;


public class MyContactListener implements ContactListener {

    private boolean playerOnGround;
    private boolean doubleJump;
    private boolean dash;
    private boolean newCheckpoint = false;
    private Body curCheckpoint;
    private Body toBeDeleted;
    private boolean isPlayerDead = false;
    private boolean initSpawn = true;

    // called when 2 fixtures start to collide
    public void beginContact(Contact c) {
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();
        System.out.println(fa.getUserData());
        System.out.println(fb.getUserData());
        //System.out.println(fa.getUserData() + ", " + fb.getUserData());
        if (fa.getUserData() != null && fb.getUserData() != null) {

            // set up a new checkpoint
            if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
                playerOnGround = true;
                if (fb.getUserData().equals("checkpoint")) {
                    if (curCheckpoint == null || curCheckpoint.getPosition().x != fb.getBody().getPosition().x || curCheckpoint.getPosition().y != fb.getBody().getPosition().y) {
                        setCurCheckpoint(fb.getBody());
                    }
                }
            }
            if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
                playerOnGround = true;
                if (fa.getUserData().equals("checkpoint")) {
                    if (curCheckpoint == null || curCheckpoint.getPosition().x != fa.getBody().getPosition().x || curCheckpoint.getPosition().y != fa.getBody().getPosition().y) {
                        setCurCheckpoint(fa.getBody());
                    }
                }
            }


            // detection happens when player goes outside of initial game border
            if (fa.getUserData() != null && (fa.getUserData().equals("playerBody") || fa.getUserData().equals("foot"))) {
                if (fb.getUserData().equals("border")) {
                    setPlayerDead(true);
                }
            }
            if (fb.getUserData() != null && (fb.getUserData().equals("playerBody") || fb.getUserData().equals("foot"))) {
                if (fa.getUserData().equals("border")) {
                    setPlayerDead(true);
                }
            }

        }
    }

    // called when two fixtures no longer collide
    public void endContact(Contact c) {
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        if (fa.getUserData() != null && fa.getUserData().equals("foot") || fb.getUserData() != null && fb.getUserData().equals("foot")) {
            playerOnGround = false;
            doubleJump = true;
            dash = true;
        }

    }

    public boolean isNewCheckpoint() {
        return newCheckpoint;
    }

    public void setNewCheckpoint(boolean state) {
        newCheckpoint = state;
    }

    public boolean IsPlayerDead() {
        return isPlayerDead;
    }

    public void setPlayerDead(boolean state) {
        isPlayerDead = state;
    }

    public boolean isPlayerOnGround() {
        return playerOnGround;
    }

    public boolean hasDoubleJump() {
        return doubleJump;
    }

    public void setDoubleJump(boolean a) {
        doubleJump = a;
    }

    public boolean hasDash() {
        return dash;
    }

    public void setDash(boolean a) {
        dash = a;
    }

    public Body removeOldCheckpoint() {
        return toBeDeleted;
    }

    public void resetOldCheckpoint() {
        toBeDeleted = null;
    }

    public boolean isInitSpawn() {
        return initSpawn;
    }

    // detection
    //presolve
    public void preSolve(Contact c, Manifold m) {
    }

    // handling
    public void postSolve(Contact c, ContactImpulse ci) {
    }

    private void setCurCheckpoint(Body new_vec) {
        initSpawn = false;
        newCheckpoint = true;
        toBeDeleted = curCheckpoint;
        curCheckpoint = new_vec;
    }

    public Vector2 getCurCheckpoint() {
        return curCheckpoint.getPosition();
    }

}
