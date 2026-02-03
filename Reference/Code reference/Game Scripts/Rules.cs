using UnityEngine;

public class Rules : MonoBehaviour
{
    public GameObject rules;
    
    public void toggleRules() {
        rules.SetActive(!rules.activeSelf);
    }
}
