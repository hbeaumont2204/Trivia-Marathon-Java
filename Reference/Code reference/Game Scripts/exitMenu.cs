using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class exitMenu : MonoBehaviour
{
    public GameObject menu;
    private bool isActive = false;
    void Update() {
        if (Input.GetKeyDown("escape") && !isActive) {
            menu.SetActive(true);
            isActive = true;
        }
        else if (Input.GetKeyDown("escape") && isActive) {
            menu.SetActive(false);
            isActive = false;
        }
    }
}