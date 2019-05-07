package ee.taltech.iti0202.gui.game.desktop.game_handlers.sound;

import com.badlogic.gdx.Gdx;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.PATH;

public class Sound {

    public static void playSoundOnce(String source) {
        playSoundOnce(source, 1.0f);
    }

    public static void playSoundOnce(String source, float db) {
        try {
            com.badlogic.gdx.audio.Sound sound = Gdx.audio.newSound(Gdx.files.internal(PATH + source));
            sound.play(db);
        } catch (Exception e) {
            System.out.println(e.getCause().getCause().toString());
        }
    }
}
