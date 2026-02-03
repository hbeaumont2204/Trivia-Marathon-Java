using UnityEngine;

public class TrueOrFalseQuestion
{
    public string questionText; // The question
    public int correctChoice; // 1 - True or 0 - False

    // Constructor
    public TrueOrFalseQuestion(string questionText, int correctChoice)
    {
        this.questionText = questionText;
        this.correctChoice = correctChoice;
    }
}
