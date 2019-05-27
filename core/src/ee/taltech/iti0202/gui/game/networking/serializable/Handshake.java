package ee.taltech.iti0202.gui.game.networking.serializable;

import java.io.Serializable;

public class Handshake implements Serializable {
    static public class Request implements Serializable {
    }

    static public class Response implements Serializable {
        public String name;
    }
}
