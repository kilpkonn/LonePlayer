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
        result.add("playerLocationX", new JsonPrimitive(progress.playerLocationX));
        result.add("playerLocationY", new JsonPrimitive(progress.playerLocationY));
        result.add("act", new JsonPrimitive(progress.act));
        result.add("map", new JsonPrimitive(progress.map));
        result.add("difficulty", new JsonPrimitive(progress.difficulty.getValue()));
        result.add("dimension", new JsonPrimitive(progress.dimension));
        result.add("playerVelocityX", new JsonPrimitive(progress.playerVelocityX));
        result.add("playerVelocityY", new JsonPrimitive(progress.playerVelocityY));
        return result;
    }
}