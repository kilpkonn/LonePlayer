package ee.taltech.iti0202.gui.game.desktop.handlers;

import com.badlogic.gdx.math.Vector2;
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
    private Vector2 curCheckpoint;
    private boolean isPlayerDead = false;

    // called when 2 fixtures start to collide
    public void beginContact(Contact c) {
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();
        //System.out.println(fa.getUserData() + ", " + fb.getUserData());

        if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
            playerOnGround = true;

            if (fb.getUserData().equals("checkpoint")) {
                setCurCheckpoint(fb.getBody().getPosition());
            }
        }
        if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
            if (fa.getUserData().equals("checkpoint")) {
                setCurCheckpoint(fa.getBody().getPosition());
            }
        }


        // detection happens when player goes outside of initial game border
        if (fa.getUserData() != null && fa.getUserData().equals("playerBody")) {
            if (fb.getUserData().equals("border")) {
                setPlayerDead(true);
            }
        }
        if (fb.getUserData() != null && fb.getUserData().equals("playerBody")) {
            if (fa.getUserData().equals("border")) {
                setPlayerDead(true);
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

    // detection
    //presolve
    public void preSolve(Contact c, Manifold m) {
    }

    // handling
    public void postSolve(Contact c, ContactImpulse ci) {
    }

    public void setCurCheckpoint(Vector2 new_vec) {
        newCheckpoint = true;
        curCheckpoint = new_vec;
        System.out.println(curCheckpoint);
    }

    public Vector2 getCurCheckpoint() {
        return curCheckpoint;
    }


}
