package ee.taltech.iti0202.gui.game.desktop.states.gameprogress;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;


public class GameProgressSerializer implements JsonSerializer<GameProgress> {
    public JsonElement serialize(final GameProgress progress, final Type type, final JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("player_x", new JsonPrimitive(progress.playerLocationX));
        result.add("player_y", new JsonPrimitive(progress.playerLocationY));
        return result;
    }
}