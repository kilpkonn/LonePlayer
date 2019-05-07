package ee.taltech.iti0202.gui.game.desktop.states.gameprogress;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;


public class GameProgressSerializer implements JsonSerializer<GameProgress> {
    public JsonElement serialize(final GameProgress progress, final Type type, final JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        /*result.add("playerLocationX", new JsonPrimitive(progress.playerLocationX));
        result.add("playerLocationY", new JsonPrimitive(progress.playerLocationY));*/
        result.add("checkpointX", new JsonPrimitive(progress.checkpointX));
        result.add("checkpointY", new JsonPrimitive(progress.checkpointY));
        result.add("act", new JsonPrimitive(progress.act));
        result.add("map", new JsonPrimitive(progress.map));
        result.add("time", new JsonPrimitive(progress.time));
        result.add("difficulty", new JsonPrimitive(progress.difficulty.getValue()));
        result.add("dimension", new JsonPrimitive(progress.dimension));
        /*result.add("playerVelocityX", new JsonPrimitive(progress.playerVelocityX));
        result.add("playerVelocityY", new JsonPrimitive(progress.playerVelocityY));*/

        JsonArray bossesArray = new JsonArray();
        for (BossData b : progress.bosses) {
            JsonObject obj = new JsonObject();
            obj.add("type", new JsonPrimitive(b.type));
            obj.add("size", new JsonPrimitive(b.size));
            obj.add("location_x", new JsonPrimitive(b.locationX));
            obj.add("location_y", new JsonPrimitive(b.locationY));
            obj.add("decider", new JsonPrimitive(b.decider));
            bossesArray.add(obj);
        }
        result.add("bosses", bossesArray);

        return result;
    }
}