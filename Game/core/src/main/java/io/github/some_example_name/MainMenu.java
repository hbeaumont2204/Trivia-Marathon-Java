package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class MainMenu implements Screen {

    private final Main game;
    private Stage stage;

    public MainMenu(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        // Use the UI viewport from Main
        stage = new Stage(game.uiViewport);

        // IMPORTANT: let Stage handle input
        Gdx.input.setInputProcessor(stage);

        // Basic skin (comes with LibGDX)
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        // UI elements
        Label title = new Label("Trivia Marathon LibGDX Edition", skin);
        TextButton startButton = new TextButton("Start Game", skin);
        TextButton quitButton = new TextButton("Quit Game", skin);

        // Button actions
        startButton.addListener(e -> {
            if (startButton.isPressed()) {
                game.setScreen(new Game(game));
                return true;
            }
            return false;
        });

        quitButton.addListener(e -> {
            if (quitButton.isPressed()) {
                Gdx.app.exit();
                return true;
            }
            return false;
        });

        // Layout
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        table.add(title).padBottom(40);
        table.row();
        table.add(startButton).width(200).height(50).padBottom(20);
        table.row();
        table.add(quitButton).width(200).height(50);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        // Optional ESC quit
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new Game(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        //game.uiViewport.update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}
