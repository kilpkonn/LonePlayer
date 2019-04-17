package ee.taltech.iti0202.gui.game.desktop.entities.animated.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.brashmonkey.spriter.Data;
import com.brashmonkey.spriter.LibGdx.LibGdxLoader;
import com.brashmonkey.spriter.SCMLReader;

import java.util.HashMap;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PATH;

public class AnimationLoader {
    private static HashMap<String, LibGdxLoader> loaders = new HashMap<>();
    private static HashMap<String, Data> datas = new HashMap<>();

    public static LibGdxLoader getLoader(String path) {
        if (loaders.containsKey(path)) {
            return loaders.get(path);
        }

        FileHandle handle = Gdx.files.internal(PATH + path);
        Data data = getData(path);
        LibGdxLoader loader = new LibGdxLoader(data);
        loader.load(handle.file());

        loaders.put(path, loader);
        return loader;
    }

    public static Data getData(String path) {
        if (datas.containsKey(path)) {
            return datas.get(path);
        }

        FileHandle handle = Gdx.files.internal(PATH + path);
        Data data = new SCMLReader(handle.read()).getData();

        datas.put(path, data);
        return data;
    }
}
