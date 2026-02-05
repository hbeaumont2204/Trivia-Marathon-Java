package io.github.some_example_name;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    // World rendering
    public FitViewport viewport;

    // UI rendering
    public OrthographicCamera uiCamera;
    public ScreenViewport uiViewport;

    @Override
    public void create() {
        // UI and world rendering set up
        viewport = new FitViewport(16, 9);
        uiCamera = new OrthographicCamera();
        uiViewport = new ScreenViewport(uiCamera);


        setScreen(new MainMenu(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        Gdx.app.exit();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        uiViewport.update(width, height, true);

        if (getScreen() != null) {
            getScreen().resize(width, height);
        }
    }
}
