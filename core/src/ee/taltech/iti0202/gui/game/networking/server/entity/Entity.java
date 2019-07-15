package ee.taltech.iti0202.gui.game.networking.server.entity;

import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;

public class Entity implements Serializable {
    public int bodyId = -1;
    public Vector2 position;
    public Vector2 velocity;
}
