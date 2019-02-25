package ee.taltech.iti0202.gui.game.desktop.states;

import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import ee.taltech.iti0202.gui.game.desktop.handlers.GameStateManager;
import ee.taltech.iti0202.gui.game.desktop.handlers.MyInput;

public class Menu extends GameState {


    private World world;
    private Box2DDebugRenderer b2dRenderer;

    public Menu(GameStateManager gsm) {

        super(gsm);
    }

    @Override
    public void handleInput() {   // TODO: MAKE A MAIN SCREEN THATS VISUALLY ENJOYABLE AND A ACT SELECTOR
        if(MyInput.isPressed(MyInput.JUMP)) {
            gsm.pushState(GameStateManager.State.PLAY, 1, 2);
        }

    }

    @Override
    public void update(float dt) {
        handleInput();
//        world.step(dt / 5, 8, 3);

    }

    @Override
    public void render() {

    }

    @Override
    public void dispose() {

    }
}
