import java.io.Serializable;
import java.security.InvalidParameterException;

public class Time implements Comparable<Time>, Serializable {
    public static final int MAX_MONTH = 12;
    public static final int[] MAX_DAYS = {
                                            -1, 31, 30, 31, 30, 31, 30, 31, 30, 31, 30, 31, 31
                                         };

    public static final int DAYS_IN_YEAR = 365;
    private static final String WRONG_TIME = "Ошибка: Неверно указана дата";

    private int curday;
    private int curmonth;
    private int curyear;
    private  static final String TIME_DIVIDER = "#";



    public int getDay()
    {
        return curday;
    }


    public int getMonth()
    {
        return curmonth;
    }

    public int getYear()
    {
        return curyear;
    }

    public Time timeTo(Time totime)
    {
        return new Time(Math.abs(curday - totime.getDay()),
                        Math.abs(curmonth - totime.getMonth()),
                        Math.abs(curyear - totime.getYear()));
    }


    public String toString()
    {
        return Integer.toString(curyear) + TIME_DIVIDER + Integer.toString(curmonth) + TIME_DIVIDER + Integer.toString(curday);
    }


    public int getInDays()
    {
        return curyear * DAYS_IN_YEAR + curmonth * MAX_DAYS[curmonth] + curday;
    }



    public static Time strToTime(String strTime)
    {
        String strParts[] = strTime.split(TIME_DIVIDER);
        if (strParts.length == 3)
            return new Time(Integer.parseInt(strParts[2]), Integer.parseInt(strParts[1]), Integer.parseInt(strParts[0]));
        else
            throw new IllegalArgumentException();
    }


    public Time(int _day, int _month, int _year) throws InvalidParameterException
    {
        if (_day <= MAX_DAYS[_month] && _month <= MAX_MONTH && _day > 0 && _month > 0)
        {
            curday = _day;
            curmonth = _month;
            curyear = _year;
        }
        else
        {
            throw new InvalidParameterException(WRONG_TIME);
        }
    }

    @Override
    public int compareTo(Time time) {
        if ((curyear > time.getYear()) ||
            (curyear == time.getYear() && curmonth > time.getMonth()) ||
            (curyear == time.getYear() && curmonth == time.getMonth() && curday > time.getDay()))
            return 1;
        else if (curyear == time.getYear() && curmonth == time.getMonth() && curday == time.getDay())
            return 0;
        else
            return -1;
    }
}
