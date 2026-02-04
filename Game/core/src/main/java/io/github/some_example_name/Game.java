package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Objects;

public class Game implements Screen {

    private final Main game;
    private SpriteBatch batch;
    private BitmapFont font;

    private String[] questionPack;

    private FileManager fileManager = new FileManager();
    private QuestionPackManager packManager = new QuestionPackManager();

    private boolean inProgress = true;
    private boolean frozen = false;

    private ArrayList<String> questions;
    private ArrayList<String> choices;
    private ArrayList<String> answers;

    private double timer;
    private double freezeTimer;
    private int playerScore;
    private int questionCount;

    private String currentQuestion;
    private String currentAnswer;
    private String[] currentChoices;

    private String message;

    public Game(Main game) {
        this.game = game;
        this.timer = 30.0f;
        this.freezeTimer = 5.0f;
        this.playerScore = 0;
        this.questionCount = 0;
        this.message = "";
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();

        fileManager = new FileManager();
        packManager = new QuestionPackManager();

        // Load files from assets
        FileHandle questionsFile = Gdx.files.internal("Questions.txt");
        FileHandle choicesFile   = Gdx.files.internal("Choices.txt");
        FileHandle answersFile   = Gdx.files.internal("Answers.txt");

        questions = fileManager.readTXTFile(questionsFile);
        choices = fileManager.readTXTFile(choicesFile);
        answers = fileManager.readTXTFile(answersFile);

        setCurrentQuestion();

        // Example usage
        System.out.println("Loaded " + questions.size() + " questions");
    }

    public void setCurrentQuestion() {
        currentQuestion = questions.get(questionCount);
        currentAnswer = answers.get(questionCount);
        currentChoices = (choices.get(questionCount)).split(",");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0.6f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw menu
        batch.begin();
        font.draw(batch, Double.toString(timer), 100, 300);
        font.draw(batch, "Press 1,2,3 or 4 to answer", 50, 250);
        font.draw(batch, "Press ESC to Quit", 50, 220);
        font.draw(batch, "Press Space to skip question",50,200);
        batch.end();

        // Quit
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        if (inProgress) {

            batch.begin();
            font.draw(batch, currentQuestion,200,200);
            font.draw(batch, "1. " + currentChoices[0],200,180);
            font.draw(batch,"2. " + currentChoices[1],200,160);
            font.draw(batch,"3. " + currentChoices[2],200,140);
            font.draw(batch,"4. " + currentChoices[3],200,120);
            batch.end();
            if (!frozen) {
                timer -= delta;
                if (timer <= 0) {
                    checkAnswer(0);
                }

                if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                    checkAnswer(1);
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                    checkAnswer(2);
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
                    checkAnswer(3);
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
                    checkAnswer(4);
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    checkAnswer(5);
                }
            }
            else {
                freezeTimer -= delta;
                batch.begin();
                font.draw(batch,message,250,150);
                batch.end();
                if (freezeTimer <= 0) {
                    reset();
                }
            }
        }
    }

    public void reset() {
        questionCount++;
        timer = 30.0f;
        freezeTimer = 5f;
        frozen = false;

        setCurrentQuestion();
    }

    public void checkAnswer(int choice) {
        batch.begin();
        if (choice < 1) { // No answer given
            playerScore -= 10;
            message = "No answer provided";
        }
        else if (choice > 4) { // Skipped question, no score change
            message = "Question skipped";
        }
        else if (Objects.equals(currentChoices[choice - 1], currentAnswer)) { // Correct answer
            playerScore += 10;
            message = "Correct answer";
        }
        else if (!(Objects.equals(currentChoices[choice - 1], currentAnswer))){ // Wrong answer
            playerScore -= 5;
            message = "Incorrect answer";
        }
        batch.end();
        frozen = true;
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
