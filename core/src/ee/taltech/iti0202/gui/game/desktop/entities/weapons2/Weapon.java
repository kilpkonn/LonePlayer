package ee.taltech.iti0202.gui.game.desktop.entities.weapons2;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

import java.io.Serializable;

import ee.taltech.iti0202.gui.game.desktop.entities.animations.SpriteAnimation;
import ee.taltech.iti0202.gui.game.desktop.entities.animations.loader.MultiplayerAnimation;

public abstract class Weapon extends SpriteAnimation {

    public Weapon(Body body, SpriteBatch sb, String path) {
        super(body, sb, path);
    }

    public void setAnimation(MultiplayerAnimation animation) {
        setAnimation(animation.getName(), animation.isToPlayOnce());
    }

    public enum Type implements Serializable {
        DEAGLE ("images/bullets/deagle/deagle.scml"),
        SHOTGUN ("images/bullets/shotgun/shotgun.scml"),
        M4 ("images/bullets/m4/m4.scml");

        private final String animationFile;

        Type(String t) {
            animationFile = t;
        }

        public String getAnimationFile() {
            return animationFile;
        }
    }
}