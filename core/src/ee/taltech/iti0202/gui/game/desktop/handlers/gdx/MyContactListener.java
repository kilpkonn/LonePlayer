package ee.taltech.iti0202.gui.game.desktop.handlers.gdx;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.MAGMAWORM;


public class MyContactListener implements ContactListener {

    private boolean playerOnGround = false;
    private boolean doubleJump = false;
    private int wallJump = 0;
    private boolean dash = false;
    private boolean newCheckpoint = false;
    private Body curCheckpoint;
    private Body toBeDeleted;
    private boolean isPlayerDead = false;
    private boolean initSpawn = true;

    // called when 2 fixtures start to collide
    public void beginContact(Contact c) {
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();
        if (fa.getUserData() != null && fb.getUserData() != null) {

            // set up a new checkpoint and double jump
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

            //set wall jump
            if (fa.getUserData() != null && fa.getUserData().equals("side_r")) {
                wallJump = -1;
            }
            if (fb.getUserData() != null && fb.getUserData().equals("side_r")) {
                wallJump = -1;
            }

            if (fa.getUserData() != null && fa.getUserData().equals("side_l")) {
                wallJump = 1;
            }
            if (fb.getUserData() != null && fb.getUserData().equals("side_l")) {
                wallJump = 1;
            }
        }


        // detection happens when player goes outside of initial game border
        if (fa.getUserData() != null && (fa.getUserData().equals("playerBody") || fa.getUserData().equals("foot"))) {
            if (fb.getUserData() != null && fb.getUserData().equals("barrier")) {
                setPlayerDead(true); //TODO: Fix random deaths (wrong coords?)
            }
            if (fb.getBody().getUserData() != null && fb.getBody().getUserData().equals(MAGMAWORM)) {
                setPlayerDead(true); //TODO: Fix random deaths (wrong coords?)
            }
        }
        if (fb.getUserData() != null && (fb.getUserData().equals("playerBody") || fb.getUserData().equals("foot"))) {
            if (fa.getUserData() != null && fa.getUserData().equals("barrier")) {
                setPlayerDead(true);
            }
            if (fa.getBody().getUserData() != null && fa.getBody().getUserData().equals(MAGMAWORM)) {
                setPlayerDead(true);
            }
        }
    }


    // called when two fixtures no longer collide
    public void endContact(Contact c) {
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        if (fa.getUserData() != null && (fa.getUserData().equals("foot") || fa.getUserData().equals("side")) || fb.getUserData() != null && (fb.getUserData().equals("foot") || fb.getUserData().equals("side"))) {
            playerOnGround = false;
            doubleJump = true;
            wallJump = 0;
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

    public int isWallJump() {
        return wallJump;
    }

    public void setWallJump(int wallJump) {
        this.wallJump = wallJump;
    }
}
