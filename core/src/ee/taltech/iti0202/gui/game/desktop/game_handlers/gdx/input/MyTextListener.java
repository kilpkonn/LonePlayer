package ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.input;

import com.badlogic.gdx.Input;

public class MyTextListener implements Input.TextInputListener {
    private String text = "";
    private boolean done = false;

    @Override
    public void input(String text) {
        this.text = text;
        done = true;
    }

    @Override
    public void canceled() {
        done = true;
    }

    public String getText() {
        return text;
    }

    public boolean isDone() {
        return done;
    }
}
