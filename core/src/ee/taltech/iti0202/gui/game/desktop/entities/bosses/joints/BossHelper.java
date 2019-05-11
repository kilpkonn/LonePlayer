package ee.taltech.iti0202.gui.game.desktop.entities.bosses.joints;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.utils.Array;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.Boss;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.*;

public class BossHelper {

    public static void createDistanceJointBetweenLinks(
            Array<Boss> tempArray, float lock, float scale, World world) {
        // create joint between bodies
        DistanceJointDef distanceJointDef = new DistanceJointDef();
        distanceJointDef.bodyA = tempArray.get(tempArray.size - 1).getBody();
        distanceJointDef.bodyB = tempArray.get(tempArray.size - 2).getBody();
        distanceJointDef.length = 20 * scale / PPM;
        distanceJointDef.collideConnected = true;
        distanceJointDef.localAnchorA.set(lock * scale, 0.95f * scale);
        distanceJointDef.localAnchorB.set(lock * scale, 0.05f * scale);
        distanceJointDef.length = 0.05f * scale;
        world.createJoint(distanceJointDef);
    }

    public static void createRopeJointGEOCordBetweenLinksPlantWorm(
            Array<Boss> tempArray, int p, World world) {
        // create joint between bodies
        float split = p == 0 ? 1f : 0f;
        float delta = p == 0 ? 0f : 0.4f;
        for (int i = 0; i < 3; i++) {
            DistanceJointDef distanceJointDef = new DistanceJointDef();
            distanceJointDef.bodyA = tempArray.get(tempArray.size - 1).getBody();
            distanceJointDef.bodyB = tempArray.get(tempArray.size - 2).getBody();
            distanceJointDef.collideConnected = false;
            distanceJointDef.localAnchorA.set(0.5f + i * 0.1f + delta, 0.95f); // done
            distanceJointDef.localAnchorB.set((95 + i * 5) / PPM, 0.05f + split);
            distanceJointDef.length = 0.05f;
            world.createJoint(distanceJointDef);
        }
    }

    public static void createRopeJointBetweenLinks(
            Array<Boss> tempArray, float lock, float scale, World world) {
        // create joint between bodies
        RopeJointDef ropeJointDef = new RopeJointDef();
        ropeJointDef.bodyA = tempArray.get(tempArray.size - 1).getBody();
        ropeJointDef.bodyB = tempArray.get(tempArray.size - 2).getBody();
        // ropeJointDef.length = MagmabossArray.size == 2 ? 40 * scale / PPM : 20 * scale / PPM;
        ropeJointDef.collideConnected = true;
        ropeJointDef.maxLength = 0.02f * scale;
        ropeJointDef.localAnchorA.set(lock * scale, 1f * scale);
        ropeJointDef.localAnchorB.set(lock * scale, 0f * scale);
        world.createJoint(ropeJointDef);
    }

    public static void filterTextures(Array<Boss> tempArray) {
        Fixture brokenFixture =
                tempArray.get(0).getBody().getFixtureList().removeIndex(0); // .get(0);
        brokenFixture.setSensor(true);
        brokenFixture.setUserData(BOSS + BOSS + BOSS);
        brokenFixture.getBody().setUserData(BOSS + BOSS + BOSS);
        brokenFixture.getFilterData().maskBits = NONE;
        brokenFixture.refilter();
        for (Fixture fixture : tempArray.get(0).getBody().getFixtureList()) {
            fixture.setUserData(BOSS + BOSS);
        }
        tempArray.reverse();
    }
}
