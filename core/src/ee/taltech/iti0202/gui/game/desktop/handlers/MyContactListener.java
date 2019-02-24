package ee.taltech.iti0202.gui.game.desktop.handlers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MyContactListener implements ContactListener {

    private boolean playerOnGround;
    private boolean doubleJump;
    private boolean dash;

    // called when 2 fixtures start to collide
    public void beginContact(Contact c) {
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
            playerOnGround = true;

        }

        if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
            playerOnGround = true;

        }
        System.out.println(fa.getUserData() + ", " + fb.getUserData());
    }

    // called when two fixtures no longer collide
    public void endContact(Contact c) {
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
            playerOnGround = false;
            doubleJump = true;
            dash = true;
        }

        if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
            playerOnGround = false;
            doubleJump = true;
            dash = true;
        }
        System.out.println(fa.getUserData() + ", " + fb.getUserData());

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


}
