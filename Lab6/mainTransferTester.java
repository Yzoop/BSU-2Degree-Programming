package Lab6;

import java.util.HashMap;
import java.util.Scanner;

public class mainTransferTester {
    //GLOBALS------------------------------------------------------------------------------------#
    private static Localizator MyLocalizator;

    ///Static strings
    private static final String ERROR_MENU_CAN_NOT_READ_LOCALE_RU = "Произошла ошибка при чтении релазиации";
    private static final String SUCCESS_MENU_SAVE_LOCALE_RU = "Настройки успешно сериализованы в файл";
    private static final String ERROR_MENU_WAIT_WHILE_FIX_RU = "Если желаете повторить попытку - введите 1, другое - иначе";
    private static final String AGREE_CONTUNIE_CHR = "1";
    private static final HashMap<Integer, String> HM_IdCity = new HashMap<>();

    ///finished Static strings
    ////////////////////////////////////////////////////
    private static final int ID_CHANGE_LANG = 1,
                             ID_COUNT_PASSANGER = 3,
                             ID_COUNT_CARGO = 2,
                             ID_SERIALIZE = 4,
                             ID_EXIT = 5;
    ////////////////////////////////////////////////////////
    private static final int ID_MINSK = 1,
                             ID_NO_CITY = -1,
                             ID_CHICAGO = 2,
                             ID_MUNICH = 3,
                             ID_KIEV = 4,
                             ID_SAINTPETERSBURG = 5,
                             ID_NEWYOURK = 6,
                             ID_WARSAW = 7;

    //-------------------------------------------------------------------------------------------#


    private static void Choose_Language_Submenu()
    {
        Scanner in = new Scanner(System.in);
        int IdEng = 1, IdBy = 2, IdRus = 3, UserId;

        System.out.printf("%d. %s\n", 1,MyLocalizator.Get_From_Dictionary(iLanguage.enLanguage.eENG, Localizator.DICTIONARY_LANG_NAME));
        System.out.printf("%d. %s\n", 2,MyLocalizator.Get_From_Dictionary(iLanguage.enLanguage.eBEL, Localizator.DICTIONARY_LANG_NAME));
        System.out.printf("%d. %s\n", 3,MyLocalizator.Get_From_Dictionary(iLanguage.enLanguage.eRUS, Localizator.DICTIONARY_LANG_NAME));
        System.out.print(":");

        UserId = in.nextInt();
        switch (UserId) {
            case 1: {
                MyLocalizator.Ok_Set_Language(iLanguage.enLanguage.eENG);
                break;
            }
            case 2: {
                MyLocalizator.Ok_Set_Language(iLanguage.enLanguage.eBEL);
                break;
            }
            case 3: {
                MyLocalizator.Ok_Set_Language(iLanguage.enLanguage.eRUS);
                break;
            }
            default: {
                throw new IllegalStateException("Unexpected value: " + UserId);
            }
        }

        System.out.println(MyLocalizator.Get_From_Dictionary(Localizator.DICTIONARY_LABEL_LANGUAGE_CHOSEN));
        System.out.println("------------------------\n");
    }


    private static void Set_Up_Menu()
    {
        Choose_Language_Submenu();
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
            System.out.println();
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


    private static boolean User_Wants_To_Continue()
    {
        Scanner scan = new Scanner(System.in);
        System.out.println(MyLocalizator.Get_From_Dictionary(Localizator.DICTIONARY_LABEL_WANT_TO_CONTINUE));
        System.out.printf("%s - 1, %s - 2:", MyLocalizator.Get_From_Dictionary(Localizator.DICTIONARY_LABEL_YES),
                                            MyLocalizator.Get_From_Dictionary(Localizator.DICTIONARY_LABEL_ELSE));

        return (scan.nextInt() == 1);
    }


    private static void Print_Main_Menu()
    {
        System.out.printf("%d. %s\n", ID_CHANGE_LANG,
                MyLocalizator.Get_From_Dictionary(Localizator.DICTIONARY_LABEL_CHANGE_LANGUAGE));
        System.out.printf("%d. %s\n", ID_COUNT_CARGO,
                MyLocalizator.Get_From_Dictionary(Localizator.DICTIONARY_LABEL_COUNT_CARGO));
        System.out.printf("%d. %s\n", ID_COUNT_PASSANGER,
                MyLocalizator.Get_From_Dictionary(Localizator.DICTIONARY_LABEL_COUNT_PASSANGER));
        System.out.printf("%d. %s\n", ID_SERIALIZE,
                MyLocalizator.Get_From_Dictionary(Localizator.DICTIONARY_LABEL_SERIALIZE));
        System.out.printf("%d. %s\n", ID_EXIT,
                MyLocalizator.Get_From_Dictionary(Localizator.DICTIONARY_LABEL_EXIT));
    }


    private static int Get_Task_ID()
    {
        Scanner scan = new Scanner(System.in);

        System.out.print(":");

        return scan.nextInt();
    }


    private static void Print_Cities(int CityIdTaken)
    {
        for(Integer cityId : HM_IdCity.keySet())
        {
            if (cityId != CityIdTaken)
            {
                System.out.printf("%d. %s\n", cityId, MyLocalizator.Get_From_Dictionary(HM_IdCity.get(cityId)));
            }
        }
    }



    private static String Get_City_StrCode(int CityICode)
    {
        if (HM_IdCity.containsKey(CityICode))
        {
            return HM_IdCity.get(CityICode);
        }
        else
            return Localizator.DICTIONARY_LABEL_EMPTYcity;
    }


    private static void Marshrute_Menu(iTransfer.enWay way)
    {
        int StartCityID, FinishCityID;
        System.out.println(MyLocalizator.Get_From_Dictionary(Localizator.DICTIONARY_LABEL_FROM));
        Print_Cities(ID_NO_CITY);
        StartCityID = Get_City();
        System.out.println(MyLocalizator.Get_From_Dictionary(Localizator.DICTIONARY_LABEL_TO));
        Print_Cities(StartCityID);
        FinishCityID = Get_City();
        Print_Prices(way, Get_City_StrCode(StartCityID), Get_City_StrCode(FinishCityID));
    }


    private static void Set_Serialize_Settings()
    {
        try {
            if (classSerializer.Ok_Serialize(new classSettings(MyLocalizator.Get_Current_Language())))
                System.out.printf("%s '%s'\n", SUCCESS_MENU_SAVE_LOCALE_RU, classSerializer.FILE_SAVE_NAME);
        }
        catch (Exception ex)
        {
            System.out.printf("%s: %s", ERROR_MENU_CAN_NOT_READ_LOCALE_RU, ex);
        }
    }


    private static void Do_Task(final int TaskID)
    {
        if(TaskID == ID_CHANGE_LANG)
        {
            Choose_Language_Submenu();
        }
        else if (TaskID == ID_COUNT_CARGO)
        {
            Marshrute_Menu(iTransfer.enWay.eCargo);
        }
        else if (TaskID == ID_COUNT_PASSANGER)
        {
            Marshrute_Menu(iTransfer.enWay.ePassanger);
        }
        else if (TaskID == ID_SERIALIZE)
        {
            Set_Serialize_Settings();
        }
        else if (TaskID == ID_EXIT)
        {
            return;
        }
    }

    private static void Print_Prices(iTransfer.enWay way, String fromCity, String toCity) {
        switch (way)
        {
            case eCargo:
            {
                cargoAirplane cair = new cargoAirplane();
                cargoCar ccar = new cargoCar();
                cargoTrain ctrain = new cargoTrain();
                Print_Shir(cair, ccar, ctrain, fromCity, toCity);
                break;
            }
            case ePassanger:
            {
                passAirplane pair = new passAirplane();
                passCar pcar = new passCar();
                passTrain ptrain = new passTrain();
                Print_Shir(pair, pcar, ptrain, fromCity, toCity);
                break;
            }
        }


        System.out.println();
    }


    private static String Grammer_Time(classTime.enTimeParts timePart, int value)
    {
        boolean isUsePlural = classTime.Grammer_Use_Plural(Integer.toString(value));
        switch (timePart)
        {
            case eDay:
                return isUsePlural ? Localizator.DICTIONARY_CODE_DAY_S : Localizator.DICTIONARY_CODE_DAY;
            case eHour:
                return isUsePlural ? Localizator.DICTIONARY_CODE_HOUR_S : Localizator.DICTIONARY_CODE_HOUR;
            case eMinute:
                return isUsePlural ? Localizator.DICTIONARY_CODE_MINUTE_S : Localizator.DICTIONARY_CODE_MINUTE;
            default:
                return "";

        }
    }


    private static void Print_Price_Time(final String strTransfer, iTransfer transfer,final String fromCity, final String toCity)
    {
        int DaysQt = transfer.Get_Time(fromCity, toCity).DD,
            HoursQt = transfer.Get_Time(fromCity, toCity).HH,
            MinutesQt = transfer.Get_Time(fromCity, toCity).MM;

        System.out.printf("--%s--\n", MyLocalizator.Get_From_Dictionary(strTransfer));
        System.out.printf("%s: ", MyLocalizator.Get_From_Dictionary(Localizator.DICTIONARY_LABEL_COSTS));

        System.out.printf("%d %s\n",
                transfer.Get_Price(fromCity, toCity).val,
                MyLocalizator.Get_From_Dictionary(Localizator.DICTIONARY_CODE_MONEY));

        System.out.printf("%d %s; %d %s; %d %s\n",
                DaysQt,
                MyLocalizator.Get_From_Dictionary(Grammer_Time(classTime.enTimeParts.eDay, DaysQt)),
                HoursQt,
                MyLocalizator.Get_From_Dictionary(Grammer_Time(classTime.enTimeParts.eHour, HoursQt)),
                MinutesQt,
                MyLocalizator.Get_From_Dictionary(Grammer_Time(classTime.enTimeParts.eMinute, MinutesQt)));

        System.out.println("###########################");
    }


    private static void Print_Shir(iTransfer airTransfer, iTransfer carTransfer, iTransfer trainTransfer, String fromCity, String toCity) {
        Print_Price_Time(Localizator.DICTIONARY_LABEL_TRAIN, trainTransfer, fromCity, toCity);
        Print_Price_Time(Localizator.DICTIONARY_LABEL_AIRPLANE, airTransfer, fromCity, toCity);
        Print_Price_Time(Localizator.DICTIONARY_LABEL_CAR, carTransfer, fromCity, toCity);
    }


    private static int Get_City() {
        Scanner scan = new Scanner(System.in);
        return scan.nextInt();
    }


    private static void Main_Menu()
    {
        int NewTaskID;
        do {
            Print_Main_Menu();
            NewTaskID = Get_Task_ID();
            Do_Task(NewTaskID);
        } while(User_Wants_To_Continue());
    }


    private static void Set_Defaults()
    {
        HM_IdCity.put(ID_MINSK, Localizator.DICTIONARY_LABEL_MINSKCITY);
        HM_IdCity.put(ID_CHICAGO, Localizator.DICTIONARY_LABEL_CHICAGOCITY);
        HM_IdCity.put(ID_MUNICH, Localizator.DICTIONARY_LABEL_MUNICHCITY);
        HM_IdCity.put(ID_KIEV, Localizator.DICTIONARY_LABEL_KIEVCITY);
        HM_IdCity.put(ID_SAINTPETERSBURG, Localizator.DICTIONARY_LABEL_SAINTPETERSBURGCITY);
        HM_IdCity.put(ID_NEWYOURK, Localizator.DICTIONARY_LABEL_NEWYORKCITY);
        HM_IdCity.put(ID_WARSAW, Localizator.DICTIONARY_LABEL_WARSAWCITY);
    }


    private static boolean No_Saved_Settings(){
        boolean local_OK_SAVED_SETTINGS = false,
                local_BAD_SAVE_SETTINGS = true;
        try
        {
            classSettings SavedSettings = classSerializer.Deserialize();
            MyLocalizator.Ok_Set_Language(SavedSettings.Get_Chosen_Language());
            return local_OK_SAVED_SETTINGS;
        }
        catch (Exception ex)
        {

            return local_BAD_SAVE_SETTINGS;
        }
    }


    private static void Start_Localization_Process() {
        boolean AgreeRepeat = false;

        do {
            if (Success_Read_Localization()) {
                Set_Defaults();
                if (No_Saved_Settings()) {
                    Set_Up_Menu();
                }
                Main_Menu();
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
