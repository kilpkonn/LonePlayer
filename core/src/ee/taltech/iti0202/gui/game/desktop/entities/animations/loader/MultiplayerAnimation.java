package ee.taltech.iti0202.gui.game.desktop.entities.animations.loader;

public interface MultiplayerAnimation {

    float getScale();
    int getSpeed();
    boolean isToPlayOnce();
    boolean isHardSet();
    String getName();
}
