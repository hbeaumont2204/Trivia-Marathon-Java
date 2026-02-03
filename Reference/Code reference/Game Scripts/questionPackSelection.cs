using UnityEngine;

/**
 * A class for randomly selecting a question pack based on the game mode selected
 * @author - Harry Beaumont
 * 
 */
public class QuestionPackSelection {
    public static string[,] questionPacks = { 
        { "Questions.txt", "Choices.txt", "Answers.txt" },
        { "Questions2.txt", "Choices2.txt", "Answers2.txt" }
    };

    // { "Questions2.txt", "Choices2.txt", "Answers2.txt" }

    public static string[,] TFQuestionPacks = {
        { "TFQuestions.txt", "TFAnswers.txt" },
        { "TFQuestions2.txt", "TFAnswers2.txt" }
    };

    int max = 2;
    int randomInt;
    /**
     * @param gameMode - Value based on game mode
     * 0 - Default Game
     * 1 - True or False
     * Randomly selects a question pack. 
     */
    public string[] GetQuestionPack(int gameMode) {
        if (gameMode == 0)
        {
            max = questionPacks.GetLength(0);
            randomInt = Random.Range(0, max);
            string[] questionPack = new string[3];
            for (int i = 0; i < 3; i++)
            {
                questionPack[i] = questionPacks[randomInt, i];
            }
            return questionPack;
        }
        else if (gameMode == 1)
        {
            max = TFQuestionPacks.GetLength(0);
            randomInt = Random.Range(0, max);
            string[] questionPack = new string[2];
            questionPack[0] = TFQuestionPacks[randomInt, 0];
            questionPack[1] = TFQuestionPacks[randomInt, 1];
            return questionPack;
        }
        else
        {
            throw new InvalidArgumentException("Invalid Gamemode");
        }
    }
}