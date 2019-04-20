package ee.taltech.iti0202.gui.game.desktop.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import ee.taltech.iti0202.gui.game.desktop.entities.animated.Player;
import ee.taltech.iti0202.gui.game.desktop.states.Play;

public class MagmaWorm extends Boss {

    public enum MagmaWormAnimation {
        DEFAULT ("default");

        private final String name;

        MagmaWormAnimation(String s) {
            name = s;
        }

        public String toString() {
            return this.name;
        }
    }

    public enum Part {
        HEAD ("magmawormhead"),
        BODY ("magmawormbody"),
        TAIL ("magmawormtail");

        private final String name;

        Part(String s) {
            name = s;
        }

        public String toString() {
            return this.name;
        }
    }

    private float max_speed = 3;
    private float time = 0;

    public MagmaWorm(Body body, SpriteBatch sb, String type, Play play, Part part, float size) {
        this(body, sb, type, play, part, size, 0, 0);
    }

    public MagmaWorm(Body body, SpriteBatch sb, String type, Play play, Part part, float size, float x, float y) {
        super(body, sb, play, "images/bosses/magmaworm/magmaworm.scml", part.name, x, y); //TODO: fix Part toString
        body.setUserData(type + type);
        setAnimation(MagmaWormAnimation.DEFAULT.name, false);
        setAnimationSpeed(50);
        setScale(size); //TODO: Bend animation / smoother bodies
    }

    @Override
    public void updateHeadBig(float dt) {
        super.update(dt);
        time += dt;
        max_speed = 3;

        float sinOffset = (float) Math.sin(time) * 10;

        float velocity = (float) Math.sqrt(Math.pow(body.linVelLoc.x, 2) + Math.pow(body.linVelLoc.y, 2)); // Your desired velocity of the car.
        float angle = body.getAngle(); // Body angle in radians.

        float velX = MathUtils.cos(angle) * velocity; // X-component.
        float velY = MathUtils.sin(angle) * velocity; // Y-component.

        Player player = play.getPlayer();

        body.setLinearVelocity(
                velX + (player.getPosition().x - body.getPosition().x),
                velY + (player.getPosition().y - body.getPosition().y) + sinOffset);

        // Vector2 v = new Vector2(play.getPlayer().getPosition().x - body.getPosition().x, play.getPlayer().getPosition().x - body.getPosition().y);
        // double len = sqrt(v.x * v.x + v.y * v.y);
        // Vector2 dir = new Vector2((float) (v.x / len), (float) (v.y / len)); //v.x / len, v.y / len
        // Vector2 movement = new Vector2(dir.x * max_speed, dir.y * max_speed);
        // body.setLinearVelocity(body.getLinearVelocity().x + movement.x, body.linVelLoc.y + movement.y);


        Vector2 cur_vel = body.getLinearVelocity();
        body.setLinearVelocity(
                minmax(cur_vel.x),
                minmax(cur_vel.y)
        );

        // body.setTransform(
        //         body.getPosition(),
        //         MathUtils.radiansToDegrees * MathUtils.atan2(
        //                 (B2DVars.V_HEIGHT * PPM - body.getPosition().y) - player.getPosition().y,
        //                 body.getPosition().x - player.getPosition().x
        //         ));
    }

    @Override
    public void updateHeadSmall(float dt) {
        super.update(dt);
        time += dt;
        max_speed = 3f;

        float sinOffset = (float) Math.sin(time) * 2;

        float velocity = (float) Math.sqrt(Math.pow(body.linVelLoc.x, 2) + Math.pow(body.linVelLoc.y, 2)); // Your desired velocity of the car.
        float angle = body.getAngle(); // Body angle in radians.

        float velX = MathUtils.cos(angle) * velocity; // X-component.
        float velY = MathUtils.sin(angle) * velocity; // Y-component.

        Player player = play.getPlayer();

        body.setLinearVelocity(
                velX + (player.getPosition().x - body.getPosition().x),
                velY + (player.getPosition().y - body.getPosition().y + sinOffset));

        // Vector2 v = new Vector2(play.getPlayer().getPosition().x - body.getPosition().x, play.getPlayer().getPosition().x - body.getPosition().y);
        // double len = sqrt(v.x * v.x + v.y * v.y);
        // Vector2 dir = new Vector2((float) (v.x / len), (float) (v.y / len)); //v.x / len, v.y / len
        // Vector2 movement = new Vector2(dir.x * max_speed, dir.y * max_speed);
        // body.setLinearVelocity(body.getLinearVelocity().x + movement.x, body.linVelLoc.y + movement.y);


        Vector2 cur_vel = body.getLinearVelocity();
        body.setLinearVelocity(
                minmax(cur_vel.x),
                minmax(cur_vel.y)
        );

    }

    private float minmax(float f) {
        return Math.min(Math.max(f, -max_speed), max_speed);
    }
}
