package ee.taltech.iti0202.gui.game.desktop.entities.animated;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;


public class MyPlayer extends SpriteAnimation {

    public enum PlayerAnimation {
        WALK ("walk"),
        RUN ("run"),
        JUMP ("jump"),
        IDLE ("idle"),
        DOUBLE_JUMP ("double_jump");

        private final String name;

        PlayerAnimation(String s) {
            name = s;
        }

        public String toString() {
            return this.name;
        }
    }

    private int numCrystals;
    private int totalCrystals;
    private int health;

    public MyPlayer(Body body, SpriteBatch sb) {
        super(body, sb, "images/Player/Knights.scml");
        setScale(0.16f);
        setAnimationSpeed(100);
        health = 100;
    }

    public void setAnimation(PlayerAnimation animation, boolean flipX) {
        setAnimation(animation.name, flipX);
    }

    public void collectCrystal() {
        numCrystals++;
    }

    public int getNumCrystals() {
        return numCrystals;
    }

    public void setTotalCrystals(int i) {
        totalCrystals = i;
    }

    public int getTotalCrystals() {
        return totalCrystals;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

}