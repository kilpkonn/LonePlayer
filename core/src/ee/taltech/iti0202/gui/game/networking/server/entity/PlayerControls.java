package ee.taltech.iti0202.gui.game.networking.server.entity;

import java.io.Serializable;

public class PlayerControls implements Serializable {
    public int id;
    public boolean dimension = true;  // Represents dimension, rather than change of it
    public boolean moveRight = false;
    public boolean moveLeft = false;
    public boolean dashRight = false;
    public boolean dashLeft = false;
    public boolean jump = false;
    public boolean idle = true;
    public short currentWeapon;

    public boolean isShooting;
    public float shootingAngle;
}
