package Lab6;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class Localizator {
    //statics for exceptions
    public static final String ERROR_READ_LOCALIZATION = "Error while reading localization";
    //statics for dictionary
    public static final String DICTIONARY_LANG_NAME = "LangName";
    public static final String DICTIONARY_CODE_DAY = "DayName";
    public static final String DICTIONARY_CODE_DAY_S = "DaysName";
    public static final String DICTIONARY_CODE_HOUR = "HourName";
    public static final String DICTIONARY_CODE_HOUR_S = "HoursName";
    public static final String DICTIONARY_CODE_MINUTE = "MinuteName";
    public static final String DICTIONARY_CODE_MINUTE_S = "MinutesName";
    public static final String DICTIONARY_CODE_MONEY = "MoneyName";
    public static final String DICTIONARY_CODE_BY = "@BY";
    private static final char DICTIONARY_CODE_LANGUAGE = '@';
    private static final String DICTIONARY_CODE_RU = "@RU";
    private static final String DICTIONARY_CODE_EN = "@EN";
    //finished statics for dictionary
    //statics for file
    private static final String DICTIONARY_DIVIDER = "!";
    private static final String FILE_END_OF_LOCALIZATION = "#end";
    public static final String FILE_NAME = "locale.txt";
    //finished statics for file

    private HashMap<iLanguage.enLanguage, LangLocal> HM_MyLanguages = new HashMap<iLanguage.enLanguage, LangLocal>();
    private boolean IsLocaleRead;

    private static iLanguage.enLanguage CurrentLanguage = iLanguage.enLanguage.eENG;


    public boolean Set_Current_Language(iLanguage.enLanguage eLang) {
        if (IsLocaleRead) {
            CurrentLanguage = eLang;
            return true;
        } else {
            return false;
        }
    }

    public Localizator() throws Exception
    {
        HM_MyLanguages.put(iLanguage.enLanguage.eRUS, new LangLocal(iLanguage.enLanguage.eRUS));
        HM_MyLanguages.put(iLanguage.enLanguage.eENG, new LangLocal(iLanguage.enLanguage.eENG));
        HM_MyLanguages.put(iLanguage.enLanguage.eBEL, new LangLocal(iLanguage.enLanguage.eBEL));
        IsLocaleRead = false;
    }


    private iLanguage.enLanguage Set_Line_Language(final String compareString)
    {
        if (compareString.equals(DICTIONARY_CODE_RU))
            return iLanguage.enLanguage.eRUS;
        else if (compareString.equals(DICTIONARY_CODE_EN))
            return iLanguage.enLanguage.eENG;
        else
            return iLanguage.enLanguage.eBEL;
    }


    private void Put_Into_Dictionary(iLanguage.enLanguage lang, String unparsedLine)
    {
        int DividerPos = unparsedLine.indexOf(DICTIONARY_DIVIDER);
        String DictKey = unparsedLine.substring(0, DividerPos),
               DictValue = unparsedLine.substring(DividerPos + 1);

        HM_MyLanguages.get(lang).Set_Translation(DictKey, DictValue);
    }


    public String Get_From_Dictionary(iLanguage.enLanguage ForLang, final String Key)
    {
        if (IsLocaleRead)
            return (HM_MyLanguages.get(ForLang).Get_Translation(Key));
        else
            return LangLocal.NO_NAME;
    }


    public String Get_From_Dictionary(final String Key)
    {
        if (IsLocaleRead)
            return (HM_MyLanguages.get(CurrentLanguage).Get_Translation(Key));
        else
            return LangLocal.NO_NAME;
    }


    private void Parse_Lines(File fileToRead) throws Exception
    {
        try {
            String Line;
            iLanguage.enLanguage CurrentLanguage = iLanguage.enLanguage.eENG;
            Scanner fScan = new Scanner(fileToRead);

            while(fScan.hasNext() && (Line = fScan.nextLine()) != FILE_END_OF_LOCALIZATION)
            {
                if (Line.charAt(0) == DICTIONARY_CODE_LANGUAGE) {
                    CurrentLanguage = Set_Line_Language(Line);
                }
                else if (Line.contains(DICTIONARY_DIVIDER))
                {
                    Put_Into_Dictionary(CurrentLanguage, Line);
                }

            }

            IsLocaleRead = true;
        }
        catch (Exception ex)
        {
            System.out.printf("%s: %s", ERROR_READ_LOCALIZATION, ex);
        }
    }


    public void Read_Locale() throws Exception
    {
        File fileLocal = new File(FILE_NAME);
        Parse_Lines(fileLocal);
        IsLocaleRead = true;
    }

}
