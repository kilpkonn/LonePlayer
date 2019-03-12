package ee.taltech.iti0202.gui.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import ee.taltech.iti0202.gui.game.Game;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.SCALE;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_WIDTH;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new Game(), config);

		config.title = Game.TITLE;
        config.width = V_WIDTH * SCALE;
        config.forceExit = true;
        config.fullscreen = false;
        config.height = V_HEIGHT * SCALE;
        config.foregroundFPS = 60; // <- limit when focused
        config.backgroundFPS = 60; // <- limit when minimized

	}
}
