package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

import java.util.ArrayList;
import java.util.Objects;

public class Game implements Screen {

    private final Main game;

    // Scene2D
    private Stage stage;
    private Skin skin;

    // UI elements
    private Label timerLabel;
    private Label scoreLabel;
    private Label questionLabel;
    private Label messageLabel;
    private TextButton[] choiceButtons;
    private TextButton skipButton;

    // Managers
    private FileManager fileManager = new FileManager();
    private QuestionPackManager packManager = new QuestionPackManager(); // Needed later

    // Game state
    private boolean inProgress = true;
    private boolean frozen = false;

    private ArrayList<String> questions;
    private ArrayList<String> choices;
    private ArrayList<String> answers;

    private double timer = 30;
    private double freezeTimer = 5;
    private int playerScore = 10;
    private int questionCount = 0;

    private String currentQuestion;
    private String currentAnswer;
    private String[] currentChoices;

    private String message = "";

    public Game(Main game) {
        this.game = game;
    }

    // -------------------------------------------------
    // SETUP
    // -------------------------------------------------

    @Override
    public void show() {
        stage = new Stage(game.uiViewport);
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        timerLabel = new Label("", skin);
        scoreLabel = new Label("", skin);
        questionLabel = new Label("", skin);
        messageLabel = new Label("", skin);

        choiceButtons = new TextButton[4];

        for (int i = 0; i < 4; i++) {
            final int choice = i + 1;
            choiceButtons[i] = new TextButton("", skin);
            choiceButtons[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (!frozen) {
                        checkAnswer(choice);
                    }
                }
            });
        }

        skipButton = new TextButton("Skip Question", skin);
        skipButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!frozen) {
                    checkAnswer(5);
                }
            }
        });

        // -------- UI Layout --------
        Table table = new Table();
        table.setFillParent(true);
        table.top().pad(30);

        table.add(timerLabel).left();
        table.add().expandX();
        table.add(scoreLabel).right();
        table.row().padTop(40);

        table.add(questionLabel).colspan(3).left().padBottom(30);
        table.row();

        for (TextButton btn : choiceButtons) {
            table.add(btn)
                .colspan(3)
                .width(600)
                .height(60)
                .padBottom(15);

            table.row();
        }

        table.add(skipButton).colspan(3).padTop(20);
        table.row();

        table.add(messageLabel).colspan(3).padTop(30);

        stage.addActor(table);

        // -------- Load data --------
        FileHandle questionsFile = Gdx.files.internal("Questions.txt");
        FileHandle choicesFile = Gdx.files.internal("Choices.txt");
        FileHandle answersFile = Gdx.files.internal("Answers.txt");

        questions = fileManager.readTXTFile(questionsFile);
        choices = fileManager.readTXTFile(choicesFile);
        answers = fileManager.readTXTFile(answersFile);

        setCurrentQuestion();
        updateUI();
    }

    // -------------------------------------------------
    // GAME LOOP
    // -------------------------------------------------

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0.6f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        if (inProgress) {
            if (!frozen) {
                timer -= delta;
                if (timer <= 0) {
                    checkAnswer(0);
                }
            } else {
                freezeTimer -= delta;
                if (freezeTimer <= 0) {
                    reset();
                }
            }
        }

        updateUI();
        stage.act(delta);
        stage.draw();
    }

    // -------------------------------------------------
    // GAME LOGIC
    // -------------------------------------------------

    private void setCurrentQuestion() {
        if (questionCount >= questions.size()) {
            inProgress = false;
            message = "Game Over!";
            game.setScreen(new GameOver(game,playerScore));
        }

        currentQuestion = questions.get(questionCount);
        currentAnswer = answers.get(questionCount);
        currentChoices = choices.get(questionCount).split(",");
    }

    private void reset() {
        questionCount++;
        timer = 30;
        freezeTimer = 5;
        frozen = false;
        message = "";
        setCurrentQuestion();
    }

    private void checkAnswer(int choice) {
        if (choice < 1) {
            playerScore -= 10;
            message = "No answer given";
        } else if (choice > 4) {
            message = "Question skipped";
        } else if (Objects.equals(currentChoices[choice - 1], currentAnswer)) {
            playerScore += 10;
            message = "Correct!";
        } else {
            playerScore -= 5;
            message = "Incorrect!";
        }

        frozen = true;
    }

    private void updateUI() {
        timerLabel.setText("Time: " + (int) timer);
        scoreLabel.setText("Points: " + playerScore);
        questionLabel.setText(currentQuestion != null ? currentQuestion : "");

        for (int i = 0; i < 4; i++) {
            choiceButtons[i].setText((i + 1) + ". " + currentChoices[i]);
            choiceButtons[i].setDisabled(frozen);
        }

        skipButton.setDisabled(frozen);
        messageLabel.setText(message);
    }

    // -------------------------------------------------
    // REQUIRED OVERRIDES
    // -------------------------------------------------

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
        skin.dispose();
    }
}
