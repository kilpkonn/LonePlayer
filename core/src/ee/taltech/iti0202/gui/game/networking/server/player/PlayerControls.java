package ee.taltech.iti0202.gui.game.networking.server.player;

import java.io.Serializable;

public class PlayerControls implements Serializable {
    public int id;
    public boolean moveRight = false;
    public boolean moveLeft = false;
    public boolean dashRight = false;
    public boolean dashLeft = false;
    public boolean jump = false;
}
