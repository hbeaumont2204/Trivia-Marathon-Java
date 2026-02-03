using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class addCursor : MonoBehaviour
{
    void Start()
    {
        /* The cursor doesn't appear on the game scene at the start so it
        must be added with this code so the menus (pause, death and victory menu)
        can be interacted with */
        Cursor.visible = true;
        Cursor.lockState = CursorLockMode.None;
    }
}