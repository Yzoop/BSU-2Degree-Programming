package Lab6;

import java.util.Scanner;

public class mainTransferTester {
    //GLOBALS----------------------------------------
    private static Localizator MyLocalizator;

    ///Static strings
    private static final String ERROR_MENU_CAN_NOT_READ_LOCALE_RU = "Произошла ошибка при чтении релазиации";
    private static final String ERROR_MENU_WAIT_WHILE_FIX_RU = "Если желаете повторить попытку - введите 1, другое - иначе";
    private static final String AGREE_CONTUNIE_CHR = "1";
    ///finished Static strings
    //-----------------------------------------------

    private static void Start_Up_Menu()
    {
        Scanner in = new Scanner(System.in);

        int IdEng = 1, IdBy = 2, IdRus = 3, UserId;

        System.out.printf("%d. %s\n", 1,MyLocalizator.Get_From_Dictionary(iLanguage.enLanguage.eENG, Localizator.DICTIONARY_LANG_NAME));
        System.out.printf("%d. %s\n", 2,MyLocalizator.Get_From_Dictionary(iLanguage.enLanguage.eBEL, Localizator.DICTIONARY_LANG_NAME));
        System.out.printf("%d. %s\n", 3,MyLocalizator.Get_From_Dictionary(iLanguage.enLanguage.eRUS, Localizator.DICTIONARY_LANG_NAME));
        System.out.print(":");

        UserId = in.nextInt();
        switch (UserId)
        {
            case 1:
                MyLocalizator.Set_Current_Language(iLanguage.enLanguage.eENG);
            case 2:
                MyLocalizator.Set_Current_Language(iLanguage.enLanguage.eBEL);
            case 3:
                MyLocalizator.Set_Current_Language(iLanguage.enLanguage.eRUS);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + UserId);
        }
    }


    private static boolean Success_Read_Localization()
    {
        try {
            MyLocalizator = new Localizator();
            MyLocalizator.Read_Locale();
            return true;
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }

        return false;
    }


    private static boolean Start_Error_Menu()
    {
        java.util.Scanner in = new java.util.Scanner(System.in);
        System.out.println(ERROR_MENU_CAN_NOT_READ_LOCALE_RU);
        System.out.println("-------------------------------");
        System.out.printf("%s: ", ERROR_MENU_WAIT_WHILE_FIX_RU);

        return (in.next().equals(AGREE_CONTUNIE_CHR));
    }


    private static void Start_Localization_Process() {
        boolean AgreeRepeat = false;
        do {
            if (Success_Read_Localization()) {
                Start_Up_Menu();
            } else {
                AgreeRepeat = Start_Error_Menu();
            }
        } while(AgreeRepeat);
    }

    public static void main(String[] args) {
        try {
            Start_Localization_Process();
        }
        catch (Exception ex)
        {
            System.out.printf("Ошибка :%s\n", ex);
        }
    }
}
