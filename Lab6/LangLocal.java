package Lab6;

import java.security.Key;
import java.util.HashMap;

public class LangLocal implements iLanguage {

    public static final String NO_NAME = "";

    private HashMap<String, String> HM_LocaleDictionary = new HashMap<String, String>();

    private enLanguage MyName;

    @Override
    public String Get_Translation(String withKey)
    {
        return HM_LocaleDictionary.getOrDefault(withKey, NO_NAME);
    }

    @Override
    public void Set_Translation(String KeyField, String theTranslation) {
        if (!HM_LocaleDictionary.containsKey(KeyField))
        {
            HM_LocaleDictionary.put(KeyField, theTranslation);
        }
    }


    public LangLocal(enLanguage language)
    {
        MyName = language;
    }

}
