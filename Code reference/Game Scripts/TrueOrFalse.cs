using UnityEngine;
using UnityEngine.UI;
using System.IO;
using System;
using TMPro;

/**
 * Class for the True of False Quiz game
 * Selects a question pack and selects each question in order
 * Checks all answers given and returns results accordingly 
**/
public class TrueOrFalse  : MonoBehaviour
{
    FileManagement fileManager = new FileManagement();
    QuestionPackSelection packSelection = new QuestionPackSelection(); 
    public TrueOrFalseQuestion[] questions = { }; // Array containing questions
    public int currentQuestionNumber = 0; // Index of the current question
    TrueOrFalseQuestion currentQuestion; // The current question
    public int score = 10; // Player Score
    public bool questionActive;
    float timer = 30; // 30 seconds to answer every question
    // UI Display
    public TextMeshProUGUI questionDisplay;
    public TextMeshProUGUI scoreDisplay;
    public TextMeshProUGUI resultDisplay;
    public TextMeshProUGUI timerDisplay;
    public TextMeshProUGUI previousScore;
    public TextMeshProUGUI highScore;
    // Game screens
    public GameObject questionScreen;
    public GameObject endScreen;

    [SerializeField] private Slider timerSlider;

    string hScore;

    // Called at the start
    private void Start()
    {
        setup();
        //Debug.Log(questions.Length);
    }

    // Called every frame
    void Update()
    {
        if (timer > 0 && questionActive)
        {
            timer = timer - Time.deltaTime; // Timer 
            timerDisplay.text = (Convert.ToInt16(timer)).ToString();
            timerSlider.value = timer / 30;
            if (timer <= 15 && timer > 5)
            {
                timerDisplay.color = Color.yellow;
            }
            if (timer <= 5)
            {
                timerDisplay.color = Color.red;
            }
            
        }
        else if (timer <= 0 && questionActive)
        {
            Skip();
        }

    }

    // Takes questions, choices and the correct answer index from text files
    void setup()
    {
        string[] questionPack = packSelection.GetQuestionPack(1);
        string questionsPath = Path.Combine(Application.streamingAssetsPath, questionPack[0]);
        string answersPath = Path.Combine(Application.streamingAssetsPath, questionPack[1]);
        string[] allQuestions = fileManager.ReadFile(questionsPath);
        string[] allAnswers =  fileManager.ReadFile(answersPath);
        // Adds every question to the array questions.
        for (int i = 0; i < allAnswers.Length; i++)
        {
            int answer = Convert.ToInt32(allAnswers[i]);
            TrueOrFalseQuestion newQuestion = new TrueOrFalseQuestion(allQuestions[i], answer);
            Array.Resize(ref questions, questions.Length + 1);
            questions[questions.Length - 1] = newQuestion;
        }
        displayQuestion();
    }

    // Displays the current question
    void displayQuestion()
    {
        if (currentQuestionNumber < questions.Length)
        {
            timer = 30;
            timerDisplay.color = Color.green;
            questionActive = true;
            currentQuestion = questions[currentQuestionNumber];
            questionDisplay.text = currentQuestion.questionText;
            resultDisplay.text = "";
        }
        else
        {
            endQuiz();
        }
    }

    void checkAnswer(int choice, TrueOrFalseQuestion currentQuestion)
    {
        if (choice == currentQuestion.correctChoice)
        {
            correctAnswer();
        }
        else if (choice == -1)
        {
            skipQuestion();
        }
        else
        {
            incorrectAnswer();
        }
    }

    void correctAnswer()
    {
        score = score + 10; // 10 points added for every correct answer
        scoreDisplay.text = score.ToString();
        if (score > 0)
        {
            scoreDisplay.color = Color.green;
        }
        resultDisplay.text = "Correct answer";
        resultDisplay.color = Color.green;
        currentQuestionNumber++;
        Invoke("displayQuestion", 5.0f); // Waits for 5 seconds before the next question
    }

    void incorrectAnswer()
    {
        score = score - 5; // 5 points lost for a correct answer
        scoreDisplay.text = score.ToString();
        resultDisplay.text = "Incorrect answer"; // Displays message
        resultDisplay.color = Color.red;
        currentQuestionNumber++; // Increments to next question number
        /* Checks score, if above zero, the next question is displayed after a delay
        If the score is zero, the colour of the score turns yellow as another wrong answer
        will cause them to lose.
        If the score is under zero then the game ends. */
        if (score == 0)
        {
            scoreDisplay.color = Color.yellow;
            Invoke("displayQuestion", 5.0f); // 5 second delay
        }
        else if (score < 0)
        {
            scoreDisplay.color = Color.red;
            Invoke("endQuiz", 5.0f); // 5 second delay
        }
        else
        {
            Invoke("displayQuestion", 5.0f); // 5 second delay
        }
    }

    void skipQuestion()
    {
        /* The player can choose to skip a question. If they do, the score
        doesn't change and the next question is displayed */
        resultDisplay.text = "No answer given";
        resultDisplay.color = Color.yellow;
        currentQuestionNumber++;
        Invoke("displayQuestion", 5.0f); // 5 second delay
    }
    
    void endQuiz()
    {
        /* Once the quiz is completed, this function is called. It checks the scores
        and displays them accordingly. The score in the game is always dislpayed 
        (if it is not negative.) If the score is higher than the current score,
        it is also displayed as a high score. */
        string hScorePath = Path.Combine(Application.streamingAssetsPath, "High Score.txt");
        hScore = fileManager.getScore(hScorePath);
        if (score >= 0) 
        {
            previousScore.text = "Score: " + score.ToString();
        }
        if (score > Convert.ToInt32(hScore))
        {
            fileManager.updateScore(score.ToString(), hScorePath);
            highScore.text = "High Score: " + score.ToString();
        }
        else
        {
            highScore.text = "High Score: " + hScore.ToString();
        }
        string pScorepath = Path.Combine(Application.streamingAssetsPath, "Previous Score.txt");
        fileManager.updateScore(score.ToString(), pScorepath);
        questionScreen.SetActive(false);
        endScreen.SetActive(true);
    }

    // Buttons
    public void False()
    {
        if (questionActive)
        {
            questionActive = false;
            checkAnswer(0, currentQuestion);
        }
    }

    public void True()
    {
        if (questionActive)
        {
            questionActive = false;
            checkAnswer(1, currentQuestion);
        }
        
    }

    public void Skip()
    {
        if (questionActive)
        {
            questionActive = false;
            checkAnswer(-1, currentQuestion);
        }
    }

    public void end()
    {
        if (questionActive)
        {
            questionActive = false;
            endQuiz();
        }
    }
}
