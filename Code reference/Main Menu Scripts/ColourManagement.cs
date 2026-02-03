using UnityEngine;
using System;
using System.IO;
using Unity.VisualScripting;
using UnityEngine.UI;
using TMPro;
using UnityEngine.UIElements;

public class ColourManagement : MonoBehaviour
{
    public UnityEngine.UI.Image background;
    string redPath = Path.Combine(Application.streamingAssetsPath, "R.txt");
    string greenPath = Path.Combine(Application.streamingAssetsPath, "G.txt");
    string bluePath = Path.Combine(Application.streamingAssetsPath, "B.txt");
    // Start is called once before the first execution of Update after the MonoBehaviour is created
    void Start()
    {
        float r = float.Parse(read(redPath));
        float g = float.Parse(read(greenPath));
        float b = float.Parse(read(bluePath));
        background.color = new Color(r, g, b, 1f);
    }

    void write(string value, string path)
    {
        using (StreamWriter sw = new StreamWriter(path))
        {
            sw.WriteLine(value);
            sw.Close();
        }
    }

    public string read(string path)
    {
        StreamReader sr = new StreamReader(path);
        string line = sr.ReadLine();
        sr.Close();
        return line;
    }

    public void red()
    {
        write("1", redPath);
        write("0", greenPath);
        write("0", bluePath);
        background.color = new Color(1f, 0f, 0f, 1f);
    }

    public void green()
    {
        write("0", redPath);
        write("1", greenPath);
        write("0", bluePath);
        background.color = new Color(0f, 1f, 0f, 1f);
    }
    public void blue()
    {
        write("0", redPath);
        write("0", greenPath);
        write("1", bluePath);
        background.color = new Color(0f, 0f, 1f, 1f);
    }

    public void white()
    {
        write("0", redPath);
        write("1", greenPath);
        write("0", bluePath);
        background.color = new Color(1f, 1f, 1f, 1f);
    }

    /*public void black()
    {
        write("0", redPath);
        write("0", greenPath);
        write("0", bluePath);
        background.color = new Color(0f, 0f, 0f, 1f);
    }*/

    public void cyan()
    {
        write("0", redPath);
        write("1", greenPath);
        write("1", bluePath);
        background.color = new Color(0f, 1f, 1f, 1f);
    }

    public void purple()
    {
        write("1", redPath);
        write("0", greenPath);
        write("1", bluePath);
        background.color = new Color(1f, 0f, 1f, 1f);
    }

    public void yellow()
    {
        write("1", redPath);
        write("1", greenPath);
        write("0", bluePath);
        background.color = new Color(1f, 1f, 0f, 1f);
    }
}
