package ee.taltech.iti0202.gui.game.desktop.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.desktop.states.Play;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PPM;

public class MagmaWorm extends Boss {
    private float max_speed = 5;
    private Body body;
    private Play play;

    public MagmaWorm(Body body, String type, Play play) {
        super(body, type, play);
        this.body = body;
        this.play = play;
        Texture tex = Game.res.getTexture("magmawormbody");
        TextureRegion[] sprites = TextureRegion.split(tex, 64, 64)[0];
        setAnimation(sprites, 1 / 12f);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        Player player = play.getPlayer();
        Vector2 cur_vel = body.getLinearVelocity();
        body.setLinearVelocity(
                minmax(cur_vel.x),
                minmax(cur_vel.y)
        );

        body.setTransform(
                body.getPosition(),
                MathUtils.radiansToDegrees * MathUtils.atan2(
                        (B2DVars.V_HEIGHT * PPM - body.getPosition().y) - player.getPosition().y,
                        body.getPosition().x - player.getPosition().x
                ));
    }

    private float minmax(float f) {
        return Math.min(Math.max(f, -max_speed), max_speed);
    }
}

