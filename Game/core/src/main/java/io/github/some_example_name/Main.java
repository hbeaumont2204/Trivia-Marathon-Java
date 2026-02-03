package io.github.some_example_name;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    @Override
    public void create() {
        setScreen(new MainMenu(this));
    }

    @Override
    public void render() {
        super.render(); // important!
    }

    @Override
    public void dispose() {
        Gdx.app.exit();
    }
}
