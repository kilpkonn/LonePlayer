package ee.taltech.iti0202.gui.game.desktop.entities.bosses.plantworm;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import ee.taltech.iti0202.gui.game.desktop.entities.bosses.Boss;
import ee.taltech.iti0202.gui.game.desktop.entities.player.Player;
import ee.taltech.iti0202.gui.game.desktop.entities.player.handler.PlayerHandler;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
public class PlantWorm extends Boss {

    private final float max_speed = 3;
    private PlayerHandler playerHandler;
    private float time;
    private Body body;
    private SpriteBatch spriteBatch;
    private String type;
    private PlantWorm.Part part;
    private float size;
    private float xOffset;
    private float yOffset;

    public PlantWorm(PlayerHandler playerHandler, Body body, SpriteBatch sb, String type, PlantWorm.Part part, float size) {
        this(playerHandler, 0, body, sb, type, part, size, 0, 0);
    }

    public PlantWorm(PlayerHandler playerHandler, float time, Body body, SpriteBatch sb, String type, Part part, float size, float x, float y) {
        super(playerHandler, body, sb, "images/bosses/plantworm/plantworm.scml", part.name, size, x, y);
        this.body = body;
        this.spriteBatch = sb;
        this.type = type;
        this.playerHandler = playerHandler;
        this.part = part;
        this.size = size;
        this.xOffset = x;
        this.yOffset = y;
        this.time = time;

        body.setUserData(type);
        setAnimation(PlantWormAnimation.IDLE.toString(), false);
        setAnimationSpeed(50);
        setScale(2); // support for only big plants
    }

    public enum PlantWormAnimation {
        IDLE ("idle"),
        ATTACK ("attack");

        private final String name;

        PlantWormAnimation(String s) {
            name = s;
        }

        public String toString() {
            return this.name;
        }
    }

    public enum Part {
        FLOWER_HEAD ("plantwormflowerhead"),
        CLAW_HEAD ("plantwormclawhead"),
        BODY ("plantwormbody"),
        TAIL("plantwormbody");

        private final String name;

        Part(String s) {
            name = s;
        }

        public String toString() {
            return this.name;
        }
    }

    @Override
    public void updateHeadBig(float dt, float max_speed) {
        super.update(dt);
        time += dt;

        Vector2 playerLoc = playerHandler.getPlayer().getPosition();
        Vector2 bossLoc = this.body.getPosition();
        float distance = (float) Math.sqrt(Math.pow(playerLoc.x - bossLoc.x, 2) + Math.pow(playerLoc.y - bossLoc.x, 2));

        float sinOffset = (float) Math.sin(time) * 10;

        float velocity = (float) Math.sqrt(Math.pow(body.linVelLoc.x, 2) + Math.pow(body.linVelLoc.y, 2)); // Your desired velocity
        float angle = body.getAngle(); // Body angle in radians.

        float velX = MathUtils.cos(angle) * velocity; // X-component.
        float velY = MathUtils.sin(angle) * velocity; // Y-component.

        Player player = playerHandler.getPlayer();

        body.setLinearVelocity(
                velX + (player.getPosition().x - body.getPosition().x),
                velY + (player.getPosition().y - body.getPosition().y) + sinOffset);


        Vector2 cur_vel = body.getLinearVelocity();
        body.setLinearVelocity(
                minmax(cur_vel.x, max_speed),
                minmax(cur_vel.y, max_speed)
        );

        //System.out.println("Distance: " + distance);
        if (distance < max_speed) {
            setAnimation(PlantWormAnimation.ATTACK.name, true);
        }
    }
}
