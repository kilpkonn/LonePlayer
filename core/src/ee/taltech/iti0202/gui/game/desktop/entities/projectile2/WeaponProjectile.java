package ee.taltech.iti0202.gui.game.desktop.entities.projectile2;

import java.io.Serializable;

public class WeaponProjectile {

    public enum Type implements Serializable {
        BULLET ("images/bullets/bullet_default/bullet.scml");

        private final String animationFile;

        Type(String t) {
            animationFile = t;
        }

        public String getAnimationFile() {
            return animationFile;
        }
    }
}
