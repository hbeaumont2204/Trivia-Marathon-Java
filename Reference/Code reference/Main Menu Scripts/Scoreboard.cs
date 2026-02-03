using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using UnityEngine.UI;
using System.Threading.Tasks;
using System.IO;
using System;
using TMPro;
using UnityEngine.SceneManagement;

public class Scoreboard : MonoBehaviour
{
    public TextMeshProUGUI previousScore;
    public TextMeshProUGUI highScore;

    public GameObject TFButton;
    public GameObject multipleChoiceButton;
    FileManagement fileManager = new FileManagement();
    string pScore;
    string hScore;
    string pScoreTF;
    string hScoreTF;
    string hScorePath = Path.Combine(Application.streamingAssetsPath, "High Score.txt");
    string pScorePath = Path.Combine(Application.streamingAssetsPath, "Previous Score.txt");

    string hScoreTFPath = Path.Combine(Application.streamingAssetsPath, "High Score TF.txt");
    string pScoreTFPath = Path.Combine(Application.streamingAssetsPath, "Previous Score TF.txt");
    void Start()
    {
        pScore = fileManager.getScore(pScorePath);
        hScore = fileManager.getScore(hScorePath);
        pScoreTF = fileManager.getScore(pScoreTFPath);
        hScoreTF = fileManager.getScore(hScoreTFPath);
        previousScore.text = "Previous Score: " + pScore.ToString();
        highScore.text = "High Score: " + hScore.ToString();
    }

    public void TF()
    {
        TFButton.SetActive(false);
        multipleChoiceButton.SetActive(true);
        previousScore.text = "Previous Score: " + pScoreTF.ToString();
        highScore.text = "High Score: " + hScoreTF.ToString();
    }

    public void multipleChoice()
    {
        multipleChoiceButton.SetActive(false);
        TFButton.SetActive(true);
        previousScore.text = "Previous Score: " + pScore.ToString();
        highScore.text = "High Score: " + hScore.ToString();
    }
}
