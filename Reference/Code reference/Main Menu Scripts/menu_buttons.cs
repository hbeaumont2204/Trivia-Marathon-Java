using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using System.Threading.Tasks;
using System.IO;
using System;
using TMPro;
using UnityEngine.SceneManagement;
using Unity.VisualScripting;

public class menu_buttons : MonoBehaviour
{
    public GameObject rulesScreen;
    public GameObject choiceScreen;
    FileManagement fileManager = new FileManagement();
    string hScorePath = Path.Combine(Application.streamingAssetsPath, "High Score.txt");
    string pScorepath = Path.Combine(Application.streamingAssetsPath, "Previous Score.txt");
    public TextMeshProUGUI previousScore;
    public TextMeshProUGUI highScore;

    // Main Menu buttons
    public void play_game()
    {
        choiceScreen.SetActive(true);
    }
    public void quit_game()
    {
        Application.Quit();
    }
    // Pause Menu buttons
    public void main_menu()
    {
        SceneManager.LoadSceneAsync("Main Menu");
    }

    public void reset()
    {
        fileManager.updateScore("0", pScorepath);
        fileManager.updateScore("0", hScorePath);
        previousScore.text = "Previous Score: 0";
        highScore.text = "Previous Score: 0";
    }

    // Game mode buttons
    public void baseGame() 
    {
        SceneManager.LoadSceneAsync("Game");
    }

    public void TOF()
    {
        SceneManager.LoadSceneAsync("T or F");
    }
}