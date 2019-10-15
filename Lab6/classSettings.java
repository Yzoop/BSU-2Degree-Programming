package Lab6;

import java.io.Serializable;

public class classSettings implements Serializable {
    private iLanguage.enLanguage MyChosenLanguage;

    public iLanguage.enLanguage Get_Chosen_Language()
    {
        return MyChosenLanguage;
    }

    public void Set_Language(iLanguage.enLanguage lang)
    {
        MyChosenLanguage = lang;
    }

    public classSettings(iLanguage.enLanguage newlang)
    {
        MyChosenLanguage = newlang;
    }
}
