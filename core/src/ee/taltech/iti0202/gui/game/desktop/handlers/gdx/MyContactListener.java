package ee.taltech.iti0202.gui.game.desktop.handlers.gdx;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BOSS;


public class MyContactListener implements ContactListener {

    private boolean playerOnGround = false;
    private boolean doubleJump = false;
    private int wallJump = 0;
    private boolean dash = false;
    private boolean newCheckpoint = false;
    private Body curCheckpoint;
    private short deathState = 0;
    private boolean initSpawn = true;
    private boolean end = false;

    // called when 2 fixtures start to collide
    @Override
    public void beginContact(Contact c) {
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        if (fa.getUserData() != null && fb.getUserData() != null) {

            // set up a new checkpoint and double jump
            checkpointDetection(fa, fb);

            //set wall jump
            setWallJump(fa, fb);

            // detection happens when player goes outside of initial game border
            dmgDetection(fa, fb);
        }

    }

    private void dmgDetection(Fixture fa, Fixture fb) {
        if (fa.getBody().getUserData() != null && fa.getBody().getUserData().equals("playerBody") || fa.getUserData().equals("foot")) {
            if (fb.getUserData().equals("barrier")) {
                deathState = 3;
            }
            if (fb.getUserData().equals(BOSS)) {
                deathState = 1;
            }
            if (fb.getUserData().equals(BOSS + BOSS)) {
                deathState = 2;
            }
        }
        if (fb.getBody().getUserData() != null && fb.getBody().getUserData().equals("playerBody") || fb.getUserData().equals("foot")) {
            if (fa.getUserData().equals("barrier")) {
                deathState = 3;
            }
            if (fa.getUserData().equals(BOSS)) {
                deathState = 1;
            }
            if (fa.getUserData().equals(BOSS + BOSS)) {
                deathState = 2;
            }
        }
    }

    private void setWallJump(Fixture fa, Fixture fb) {
        if (!fb.getUserData().equals("checkpoint") && fa.getUserData().equals("side_r")) {
            wallJump = -1;
        }
        if (!fa.getUserData().equals("checkpoint") && fb.getUserData().equals("side_r")) {
            wallJump = -1;
        }

        if (!fb.getUserData().equals("checkpoint") && fa.getUserData().equals("side_l")) {
            wallJump = 1;
        }
        if (!fa.getUserData().equals("checkpoint") && fb.getUserData().equals("side_l")) {
            wallJump = 1;
        }
    }

    private void checkpointDetection(Fixture fa, Fixture fb) {
        if (fa.getUserData().equals("foot")) {
            playerOnGround = true;
            if (fb.getUserData().equals("checkpoint")) {
                if (curCheckpoint == null || curCheckpoint.getPosition().x != fb.getBody().getPosition().x || curCheckpoint.getPosition().y != fb.getBody().getPosition().y) {
                    newCheckpoint = true;
                }
            }
            if (fb.getUserData().equals("end")) {
                if (curCheckpoint == null || curCheckpoint.getPosition().x != fb.getBody().getPosition().x || curCheckpoint.getPosition().y != fb.getBody().getPosition().y) {
                    end = true;
                }
            }
        }

        if (fb.getUserData().equals("foot")) {
            playerOnGround = true;
            if (fa.getUserData().equals("checkpoint")) {
                if (curCheckpoint == null || curCheckpoint.getPosition().x != fa.getBody().getPosition().x || curCheckpoint.getPosition().y != fa.getBody().getPosition().y) {
                    newCheckpoint = true;
                }
            }

            if (fa.getUserData().equals("end")) {
                if (curCheckpoint == null || curCheckpoint.getPosition().x != fa.getBody().getPosition().x || curCheckpoint.getPosition().y != fa.getBody().getPosition().y) {
                    end = true;
                }
            }
        }
    }


    // called when two fixtures no longer collide
    @Override
    public void endContact(Contact c) {
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        if (fa.getUserData() != null && (fa.getUserData().equals("foot") || fa.getUserData().equals("side"))) {
            if (fb.getUserData() != null && fb.getUserData().equals("checkpoint")) return;
            playerOnGround = false;
            doubleJump = true;
            //wallJump = 0;
            dash = true;
        } else if (fb.getUserData() != null && (fb.getUserData().equals("foot") || fb.getUserData().equals("side"))) {
            if (fa.getUserData() != null && fa.getUserData().equals("checkpoint")) return;
            playerOnGround = false;
            doubleJump = true;
            //wallJump = 0;
            dash = true;
        }

    }

    //presolve
    @Override
    public void preSolve(Contact c, Manifold m) {
        // Fixture fa = c.getFixtureA();
        // Fixture fb = c.getFixtureB();

        // if (fa.getUserData() != null && (fa.getBody().getUserData() != null && fa.getBody().getUserData().equals("playerBody") || fa.getUserData().equals("foot"))) {
        //     impact = fa.getBody().getLinearVelocity();
        // }
        // if (fb.getUserData() != null && (fb.getBody().getUserData() != null && fb.getBody().getUserData().equals("playerBody") || fb.getUserData().equals("foot"))) {
        //     impact = fb.getBody().getLinearVelocity();
        // }
    }

    // handling
    @Override
    public void postSolve(Contact c, ContactImpulse ci) {
    }

    public boolean isNewCheckpoint() {
        return newCheckpoint;
    }

    public void setNewCheckpoint(boolean state) {
        newCheckpoint = state;
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

    public boolean isInitSpawn() {
        return initSpawn;
    }

    public void setCurCheckpoint(Body new_vec) {
        initSpawn = false;
        newCheckpoint = false;
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

    public short getDeathState() {
        return deathState;
    }

    public void setDeathState(short deathState) {
        this.deathState = deathState;
    }

    // public Vector2 isImpact() {
    //     return impact;
    // }

    // public void setImpact(Vector2 impact) {
    //     this.impact = impact;
    // }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public boolean isEnd() {
        return end;
    }

}
