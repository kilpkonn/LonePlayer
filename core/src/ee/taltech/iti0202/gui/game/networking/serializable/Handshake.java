package ee.taltech.iti0202.gui.game.networking.serializable;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Handshake implements Serializable {
    public static class Request implements Serializable {
        public Set<String> names = new HashSet<>();
    }

    public static class Response implements Serializable {
        public String name;
    }
}
