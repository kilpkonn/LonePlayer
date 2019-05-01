package ee.taltech.iti0202.gui.game.desktop.entities.bosses;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import ee.taltech.iti0202.gui.game.desktop.entities.animations.SpriteAnimation;
import ee.taltech.iti0202.gui.game.desktop.entities.player.Player;
import ee.taltech.iti0202.gui.game.desktop.states.Play;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Boss extends SpriteAnimation {

    public Play getPlay() {
        return play;
    }

    protected Play play;
    protected SpriteBatch spriteBatch;
    protected String path;
    protected String entity;
    protected float xOffset;
    protected float yOffset;
    protected float size;
    private float time = 0;
    private boolean decider = true;

    protected Boss(Body body, SpriteBatch sb, Play play, String path, String entity) {
        this(body, sb, play, path, entity, 1, 0, 0);

    }

    public enum Part {
        HEAD("magmawormhead"),
        BODY("magmawormbody"),
        TAIL("magmawormtail");

        private final String name;

        Part(String s) {
            name = s;
        }

        public String toString() {
            return this.name;
        }
    }

    protected Boss(Body body, SpriteBatch sb, Play play, String path, String entity, float size, float x, float y) {
        super(body, sb, path, entity, x, y);
        this.play = play;
        this.body = body;
        this.spriteBatch = sb;
        this.path = path;
        this.entity = entity;
        this.size = size;
        this.yOffset = y;
        this.xOffset = x;

    }

    public void updateRotation(float dt) {
        super.update(dt);

        // Get the positions of both Entities
        Vector2 playerPos = play.getPlayerHandler().getPlayer().getPosition();
        Vector2 enemyPos = body.getPosition();

        float angle = body.getAngle();
        Vector2 target = new Vector2(playerPos.x - enemyPos.x, playerPos.y - enemyPos.y);
        float newAngle = (float) Math.atan2(-target.x, target.y);
        if (newAngle < 0) newAngle += 2 * Math.PI;

        body.setTransform(body.getPosition(), newAngle);


        // body.setLinearVelocity(
        //         (float) (body.linVelLoc.x - 1 + Math.random() * (2)),
        //         (float) (body.linVelLoc.y - 1 + Math.random() * (2))
        // );
    }

    public void updateCircularMotion(float dt) {
        super.update(dt);
        float x = 0.3f;

        Vector2 player = play.getPlayerHandler().getPlayer().getPosition();
        Vector2 boss = this.body.getPosition();

        float distance = (float) Math.sqrt(Math.pow(player.x - boss.x, 2) + Math.pow(player.y - boss.x, 2));

        float optimalDistanceFromPlayer = 5f;
        if (distance < optimalDistanceFromPlayer) {

            /*body.setLinearVelocity(body.getLinearVelocity().x * 0.99f + ((player.x - boss.x) * -1) * 0.005f,
                    body.getLinearVelocity().y * 0.99f + ((player.y - boss.y) * -1) * 0.005f);*/
            body.applyForceToCenter(new Vector2((player.x - boss.x) * -2 * x, (player.y - boss.y) * -2 * x), true);

        } else if (distance > optimalDistanceFromPlayer) {

            /*body.setLinearVelocity(body.getLinearVelocity().x * 0.99f + (player.x - boss.x) * 0.005f,
                    body.getLinearVelocity().y * 0.99f + ((player.y - boss.y) * 1) * 0.005f);*/
            body.applyForceToCenter(new Vector2((player.x - boss.x) * x, (player.y - boss.y) * x), true);
        }


    }

    public void updateHeadBig(float dt) {
        super.update(dt);
        time += dt;
        float max_speed = 3;

        float sinOffset = (float) Math.sin(time) * 10;

        float velocity = (float) Math.sqrt(Math.pow(body.linVelLoc.x, 2) + Math.pow(body.linVelLoc.y, 2)); // Your desired velocity
        float angle = body.getAngle(); // Body angle in radians.

        float velX = MathUtils.cos(angle) * velocity; // X-component.
        float velY = MathUtils.sin(angle) * velocity; // Y-component.

        Player player = play.getPlayerHandler().getPlayer();

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
                minmax(cur_vel.x, max_speed),
                minmax(cur_vel.y, max_speed)
        );

        // body.setTransform(
        //         body.getPosition(),
        //         MathUtils.radiansToDegrees * MathUtils.atan2(
        //                 (B2DVars.V_HEIGHT * PPM - body.getPosition().y) - player.getPosition().y,
        //                 body.getPosition().x - player.getPosition().x
        //         ));
    }

    public void updateHeadSmall(float dt) {
        super.update(dt);
        time += dt;
        float max_speed = 3f;

        float sinOffset = (float) Math.sin(time) * 2;

        float velocity = (float) Math.sqrt(Math.pow(body.linVelLoc.x, 2) + Math.pow(body.linVelLoc.y, 2)); // Your desired velocity of the car.
        float angle = body.getAngle(); // Body angle in radians.

        float velX = MathUtils.cos(angle) * velocity; // X-component.
        float velY = MathUtils.sin(angle) * velocity; // Y-component.

        Player player = play.getPlayerHandler().getPlayer();

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
                minmax(cur_vel.x, max_speed),
                minmax(cur_vel.y, max_speed)
        );

    }

    protected float minmax(float f, float max) {
        return Math.min(Math.max(f, -max), max);
    }
}
