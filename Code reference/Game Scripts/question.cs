using UnityEngine;

public class question
{
    public string questionText; // The question
    public string[] choices; // A list of 4 choices
    public int correctChoice; // The index of the correct choice (1-4)

    // Constructor
    public question(string questionText, string[] choices, int correctChoice)
    {
        this.questionText = questionText;
        this.choices = choices;
        this.correctChoice = correctChoice;
    }
}
