package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Align;

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

        messageLabel = new Label("", skin);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Font1.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 32;
        parameter.color = Color.BLACK;

        BitmapFont questionFont = generator.generateFont(parameter);
        generator.dispose();

        Label.LabelStyle questionStyle = new Label.LabelStyle(questionFont, Color.BLACK);

        questionLabel = new Label("", questionStyle);
        questionLabel.setWrap(true);
        questionLabel.setAlignment(Align.center);

        timerLabel = new Label("", questionStyle);
        timerLabel.setAlignment(Align.left);

        scoreLabel = new Label("",questionStyle);
        scoreLabel.setAlignment(Align.right);

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
        Table questionTable = new Table();
        table.setFillParent(true);
        questionTable.top().pad(30);

        table.add(timerLabel).colspan(1).left().padRight(50);
        table.add().expandX();
        table.add(scoreLabel).colspan(1).width(1000).right().padLeft(50);
        table.row().padTop(40);

        table.add(questionLabel).colspan(3).width(1000).center().padBottom(80);
        table.row();

        for (TextButton btn : choiceButtons) {
            questionTable.add(btn)
                .colspan(3)
                .width(600)
                .height(60)
                .padBottom(20);

            questionTable.row();
        }

        questionTable.add(skipButton).colspan(3).width(100).center().padTop(20);

        table.add(questionTable).colspan(3);
        table.row();

        table.add(messageLabel).colspan(3).padTop(30);

        stage.addActor(table);

        // -------- Load data --------
        FileHandle questionsFile = Gdx.files.internal("Questions2.txt");
        FileHandle choicesFile = Gdx.files.internal("Choices2.txt");
        FileHandle answersFile = Gdx.files.internal("Answers2.txt");

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
        // Button input
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            checkAnswer(1);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            checkAnswer(2);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            checkAnswer(3);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            checkAnswer(4);
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
        else {
            currentQuestion = questions.get(questionCount);
            currentAnswer = answers.get(questionCount);
            currentChoices = choices.get(questionCount).split(",");
        }
    }

    private void reset() {
        questionCount++;
        //System.out.println(questions.size());
        if (questionCount >= questions.size()) {
            inProgress = false;
        }
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
        } else if (Objects.equals(currentChoices[choice - 1].trim(), currentAnswer.trim())) {
            System.out.println(currentChoices[choice - 1]);
            System.out.println(currentAnswer);
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

        if (inProgress) {
            for (int i = 0; i < 4; i++) {
                choiceButtons[i].setText((i + 1) + ". " + currentChoices[i]);
                choiceButtons[i].setDisabled(frozen);
            }
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
