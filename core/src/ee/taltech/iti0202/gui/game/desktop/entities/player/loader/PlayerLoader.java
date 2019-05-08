package ee.taltech.iti0202.gui.game.desktop.entities.player.loader;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import ee.taltech.iti0202.gui.game.desktop.entities.player.Player;
import ee.taltech.iti0202.gui.game.desktop.entities.player.handler.PlayerHandler;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.MyContactListener;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.canvas.Draw;
import ee.taltech.iti0202.gui.game.desktop.states.gameprogress.GameProgress;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BACKGROUND;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BIT_BOSSES;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.BIT_WORM;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.DIMENTSION_1;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.DIMENTSION_2;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.FRICTION;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.PPM;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.TERRA_DIMENTSION_1;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.TERRA_DIMENTSION_2;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.TERRA_SQUARES;

public class PlayerLoader {

    private static Player buildPlayer(BodyDef bdef, SpriteBatch spriteBatch, Draw draw, World world) {
        short mask;
        if (draw.isDimension()) {
            mask = BIT_BOSSES | BIT_WORM | DIMENTSION_1 | DIMENTSION_2 | TERRA_SQUARES | BACKGROUND | TERRA_DIMENTSION_1;
        } else {
            mask = BIT_BOSSES | BIT_WORM | DIMENTSION_1 | DIMENTSION_2 | TERRA_SQUARES | BACKGROUND | TERRA_DIMENTSION_2;
        }
        bdef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bdef);

        PolygonShape polyShape = new PolygonShape();
        polyShape.setAsBox(2 / PPM, 8 / PPM, new Vector2(-20 / PPM, 20 / PPM), 0);
        FixtureDef fdef = new FixtureDef();
        fdef.shape = polyShape;
        fdef.filter.categoryBits = DIMENTSION_1 | DIMENTSION_2;
        fdef.filter.maskBits = mask;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("side_l");

        polyShape.setAsBox(2 / PPM, 8 / PPM, new Vector2(20 / PPM, 20 / PPM), 0);
        fdef.shape = polyShape;
        fdef.filter.categoryBits = DIMENTSION_1 | DIMENTSION_2;
        fdef.filter.maskBits = mask;
        body.createFixture(fdef).setUserData("side_r");

        CircleShape circle = new CircleShape();

        fdef.isSensor = false;
        circle.setRadius(9 / PPM);
        fdef.shape = circle;
        fdef.filter.categoryBits = DIMENTSION_1 | DIMENTSION_2;
        fdef.filter.maskBits = mask;
        body.createFixture(fdef).setFriction(FRICTION);
        body.setUserData("playerBody");

        polyShape.setAsBox(8 / PPM, 18 / PPM, new Vector2(0, 12 / PPM), 0);
        fdef.shape = polyShape;
        fdef.filter.categoryBits = DIMENTSION_1 | DIMENTSION_2;
        fdef.filter.maskBits = mask;
        body.createFixture(fdef).setUserData("playerBody");

        polyShape.setAsBox(4 / PPM, 1 / PPM, new Vector2(0, -15 / PPM), 0);
        fdef.shape = polyShape;
        fdef.filter.categoryBits = DIMENTSION_1 | DIMENTSION_2;
        fdef.filter.maskBits = mask;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("foot");
        return new Player(body, spriteBatch);
    }

    public static Player initPlayer(GameProgress progress, SpriteBatch spriteBatch, PlayerHandler playerHandler, Draw draw, World world) {
        if (playerHandler.getPlayer() != null)
            world.destroyBody(playerHandler.getPlayer().getBody());

        BodyDef bdef = new BodyDef();
        bdef.position.set(progress.checkpointX, progress.checkpointY);
        //bdef.linearVelocity.set(progress.playerVelocityX, progress.playerLocationY);

        return buildPlayer(bdef, spriteBatch, draw, world);
    }

    public static Player initPlayer(SpriteBatch spriteBatch, PlayerHandler playerHandler, Draw draw, World world, MyContactListener cl) {
        if (playerHandler.getPlayer() != null)
            world.destroyBody(playerHandler.getPlayer().getBody());
        BodyDef bdef = new BodyDef();

        if (playerHandler.getPlayer() != null && playerHandler.getCheckpoint() == null) {
            if (playerHandler.getInitPlayerLocation() == null) {
                bdef.position.set(0, 0); // hopefully never get here
            } else {
                bdef.position.set(playerHandler.getInitPlayerLocation());
            }
        } else if (cl.isInitSpawn()) {
            bdef.position.set(playerHandler.getInitPlayerLocation());
        } else {
            bdef.position.set(new Vector2(playerHandler.getCheckpoint().getPosition()));
        }
        return buildPlayer(bdef, spriteBatch, draw, world);
    }
}
