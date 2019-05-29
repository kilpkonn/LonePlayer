package ee.taltech.iti0202.gui.game.desktop.settings;

import com.google.gson.*;

import java.lang.reflect.Type;

public class SettingsSerializer implements JsonSerializer<Settings> {
    public JsonElement serialize(
            final Settings settings, final Type type, final JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("move_left", new JsonPrimitive(settings.MOVE_LEFT));
        result.add("move_right", new JsonPrimitive(settings.MOVE_RIGHT));
        result.add("toggle_dimension", new JsonPrimitive(settings.CHANGE_DIMENSION));
        result.add("jump", new JsonPrimitive(settings.JUMP));
        result.add("attack", new JsonPrimitive(settings.SHOOT));
        result.add("next_weapon", new JsonPrimitive(settings.NEXT_WEAPON));
        result.add("previous_weapon", new JsonPrimitive(settings.PREVIOUS_WEAPON));
        result.add("escape_button", new JsonPrimitive(settings.ESC));
        result.add("enable_dev_maps", new JsonPrimitive(settings.ENABLE_DEV_MAPS));
        result.add("show_fps", new JsonPrimitive(settings.SHOW_FPS));
        result.add("max_fps", new JsonPrimitive(settings.MAX_FPS));
        result.add("enable_vsync", new JsonPrimitive(settings.ENABLE_VSYNC));
        result.add("name", new JsonPrimitive(settings.NAME));
        return result;
    }
}
