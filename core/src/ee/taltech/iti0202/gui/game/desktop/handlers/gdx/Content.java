package ee.taltech.iti0202.gui.game.desktop.handlers.gdx;

import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

public class Content {

    private HashMap<String, Texture> textures;

    public Content() {
        textures = new HashMap<>();
    }

    public void loadTexture(String path, String key) {
        Texture tex = new Texture(path);
        textures.put(key, tex);
    }

    public Texture getTexture(String key) {
        return textures.get(key);
    }

    public void disposeTexture(String key) {
        Texture tex = textures.get(key);
        if (tex != null) tex.dispose();
    }
}

