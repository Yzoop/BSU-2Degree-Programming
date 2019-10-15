package Lab6;

public class classTime {
    public static enum enTimeParts
    {
        eDay,
        eMinute,
        eHour
    }

    public int DD, HH, MM;


    private static final int MAX_HH = 24, MAX_MM = 60, MAX_DD = 365;

    public static boolean Grammer_Use_Plural(String strTime)
    {
        String LastTwoChars = "";
        if (strTime.charAt(strTime.length() - 1) != '1')
        {
            if (strTime.length() > 1)
            {
                char lastChar = strTime.charAt(strTime.length() - 2),
                     penultimateChar  = strTime.charAt(strTime.length() - 1);
                return (lastChar != '1' || penultimateChar != '1');
            }
            else
                return true;
        }
        else
            return false;
    }

    public classTime(int _dd,int _hh, int _mm)
    {
        DD = Math.abs(_dd % MAX_DD);
        MM = Math.abs(_mm % MAX_MM);
        HH = Math.abs(_hh % MAX_HH);
    }
}
