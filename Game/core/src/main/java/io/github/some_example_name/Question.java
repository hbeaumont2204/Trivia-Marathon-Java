package io.github.some_example_name;

public class Question {
    public String questionText;
    public String[] questionChoices;
    public int correctChoice;

    public Question(String questionText, String[] questionChoices, int correctChoice) {
        this.questionText = questionText;
        this.questionChoices = questionChoices;
        this.correctChoice = correctChoice;
    }
}
