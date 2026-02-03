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

/* A class containing functions that read/ write to text files.
This is used in saving and updating scores */
public class FileManagement
{
    // Reads data from a text file
    public string[] ReadFile(string path)
    {
        string[] data = { };
        if (File.Exists(path))
        {
            using (StreamReader sr = new StreamReader(path))
            {
                string line;
                while ((line = sr.ReadLine()) != null)
                {
                    Array.Resize(ref data, data.Length + 1);
                    data[data.Length - 1] = line;
                }
            }
        }
        else
        {
            throw new InvalidArgumentException("Invalid file path");
        }
        return data;
    }
    // Used to read the choices from a text file
    public string[][] GetChoices(string path)
    {
        string[][] data = { };
        if (File.Exists(path))
        {
            using (StreamReader sr = new StreamReader(path))
            {
                string line;
                while ((line = sr.ReadLine()) != null)
                {
                    string[] options = line.Split(',');
                    Array.Resize(ref data, data.Length + 1);
                    data[data.Length - 1] = options;
                }
            }
        }
        else
        {
            throw new InvalidArgumentException("Invalid file path");
        }
        return data;
    }
    public string getScore(string path)
    {
        StreamReader sr = new StreamReader(path);
        string line = sr.ReadLine();
        sr.Close();
        return line;
    }
    public void updateScore(string text, string path)
    {
        int score = Convert.ToInt32(text);
        if (score >= 0)
        {
            using (StreamWriter sw = new StreamWriter(path))
            {
                sw.WriteLine(text);
                sw.Close();
            }          
        }
    }
}
