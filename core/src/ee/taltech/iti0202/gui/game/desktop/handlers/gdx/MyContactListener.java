package ee.taltech.iti0202.gui.game.desktop.handlers.gdx;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import java.util.HashMap;

import lombok.Data;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BOSS;

@Data
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
    private HashMap<Body, String> collidedBullets = new HashMap<>();

    // called when 2 fixtures start to collide
    @Override
    public void beginContact(Contact c) {
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        if (fa.getUserData() != null && fb.getUserData() != null) {

            // detect bullet collision
            bulletDetection(fa, fb);

            // set up a new checkpoint and double jump
            checkpointDetection(fa, fb);

            //set wall jump
            setWallJump(fa, fb, 1);

            // detection happens when player goes outside of initial game border
            dmgDetection(fa, fb);
        }

    }

    private void bulletDetection(Fixture fa, Fixture fb) {
        if (fa.getUserData().toString().endsWith("bullet")) {
            collidedBullets.put(fa.getBody(), fb.getUserData().toString());
        } else if (fb.getUserData().toString().endsWith("bullet")) {
            collidedBullets.put(fb.getBody(), fa.getUserData().toString());
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

    private void setWallJump(Fixture fa, Fixture fb, int i) {
        if (!fb.getUserData().equals("checkpoint") && fa.getUserData().equals("side_r")) {
            wallJump = -1 * i;
        }
        if (!fa.getUserData().equals("checkpoint") && fb.getUserData().equals("side_r")) {
            wallJump = -1 * i;
        }

        if (!fb.getUserData().equals("checkpoint") && fa.getUserData().equals("side_l")) {
            wallJump = i;
        }
        if (!fa.getUserData().equals("checkpoint") && fb.getUserData().equals("side_l")) {
            wallJump = i;
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
        if (fa.getUserData() != null && fb.getUserData() != null) {

            setWallJump(fa, fb, 0);

            if (fa.getUserData().equals("foot")) {
                if (fb.getUserData().equals("checkpoint")) return;
                playerOnGround = false;
                doubleJump = true;
                //wallJump = 0;
                dash = true;
            } else if (fb.getUserData().equals("foot")) {
                if (fa.getUserData().equals("checkpoint")) return;
                playerOnGround = false;
                doubleJump = true;
                //wallJump = 0;
                dash = true;
            }

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

    public void setCurCheckpoint(Body new_vec) {
        initSpawn = false;
        newCheckpoint = false;
        curCheckpoint = new_vec;
    }

}
