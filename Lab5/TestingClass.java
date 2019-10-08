package Lab5;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;

public class TestingClass {

    private static Random rand = new Random();
    private static int QtTrapeziums;
    private static Trapezium[] MyTrapeziums;
    ///GLOBALS-------------------------
    static final String PLEASE_ENTER_QT_TRAPEZIUMS = "Пожалуйста, введите целое количество трапеций >1:";
    //Asks for CUI
    static final int ANS_YES = 1;
    static final String ANS_NO = "другое";
    static final String DO_YOU_WANT_TO_SORT_AGAIN = "Желаете повторить? " + ANS_YES + " - да, " + ANS_NO + " - нет.";
    //Asks for points
    static final String ENTER_lEFT_DOWN_X = "Пожалуйста, введите целую координату Х ЛЕВОГО НИЖНЕГО угла:";
    static final String ENTER_lEFT_DOWN_Y = "Пожалуйста, введите целую координату Y ЛЕВОГО НИЖНЕГО угла:";
    static final String ENTER_lEFT_UP_X = "Пожалуйста, введите целую координату Х ЛЕВОГО ВЕРХНЕГО угла:";
    static final String ENTER_lEFT_UP_Y = "Пожалуйста, введите целую координату Y ЛЕВОГО ВЕРХНЕГО угла:";
    static final String ENTER_RIGHT_UP_X = "Пожалуйста, введите целую координату Х ПРАВОГО ВЕРХНЕГО угла:";
    static final String ENTER_RIGHT_UP_Y = "Пожалуйста, введите целую координату Y ПРАВОГО ВЕРХНЕГО угла:";
    static final String ENTER_RIGHT_DOWN_X = "Пожалуйста, введите целую координату Х ПРАВОГО НИЖНЕГО угла:";
    static final String ENTER_RIGHT_DOWN_Y = "Пожалуйста, введите целую координату Y ПРАВОГО НИЖНЕГО угла:";
    static final int MinimumQtTrapeziums = 2;
    ///--------------------------------


    private static boolean Check_Borders(final int _currentVal, final boolean _isBorderMinimum, final int _border) {
        if (_isBorderMinimum)
            return (_currentVal < _border);
        else
            return (_currentVal > _border);
    }


    private static int Ask_For_Int(final String _strAsk, boolean _isBorderMinimum, final int _border) {
        Scanner in = new Scanner(System.in);
        int _ReadValue = 0;
        do {
            System.out.print(_strAsk);
            _ReadValue = in.nextInt();

        } while (Check_Borders(_ReadValue, _isBorderMinimum, _border));

        return _ReadValue;
    }

    private static int Simple_Ask(final String _strAsk) {
        Scanner in = new Scanner(System.in);
        System.out.print(_strAsk);

        return in.nextInt();
    }

    private static boolean Continue_Ask() {
        Scanner in = new Scanner(System.in);
        System.out.println(DO_YOU_WANT_TO_SORT_AGAIN);

        return (in.nextInt() == 1);
    }


    private static void Ask_For_Qt_Trapeziums() {
        QtTrapeziums = Ask_For_Int(PLEASE_ENTER_QT_TRAPEZIUMS, true, MinimumQtTrapeziums);
        MyTrapeziums = new Trapezium[QtTrapeziums];
        System.out.println("\n-----Успешно введено-----");
    }

    private static void Set_Default_Trapeziums()
    {
        final int _Border = 40;

        if (QtTrapeziums < MinimumQtTrapeziums)
        {
            QtTrapeziums = MinimumQtTrapeziums;
        }
        for (int i = 0; i < QtTrapeziums; i++)
        {
            double LD_X = rand.nextInt(_Border),
                   LD_Y = rand.nextInt(_Border),
                   LU_X = LD_X + rand.nextInt(_Border),
                   LU_Y = LD_Y + rand.nextInt(_Border),
                   RU_X = LD_X + rand.nextInt(_Border),
                   RD_X = RU_X + rand.nextInt(_Border);
            MyTrapeziums[i] = new Trapezium(new Point(LD_X, LD_Y), new Point(LU_X, LU_Y),
                              new Point(RU_X, LU_Y), new Point(RD_X, LD_Y));
        }
    }


    private static void Create_Trapeziums() {
        Scanner in = new Scanner(System.in);
        System.out.println("------------Генерация трапеций------------\n");
        System.out.println("Желаете ли вы использовать уже созданные трапеции? 1 - да, иначе - нет");

        if (in.nextInt() != ANS_YES) {
            for (int index = 0; index < QtTrapeziums; index++) {
                System.out.printf("Ввод данных для %d из %d трапеции\n", index + 1, QtTrapeziums);
                Point LD = new Point(Simple_Ask(ENTER_lEFT_DOWN_X), Simple_Ask(ENTER_lEFT_DOWN_Y)),
                        LU = new Point(Simple_Ask(ENTER_lEFT_UP_X), Simple_Ask(ENTER_lEFT_UP_Y)),
                        RU = new Point(Simple_Ask(ENTER_RIGHT_UP_X), Simple_Ask(ENTER_RIGHT_UP_Y)),
                        RD = new Point(Simple_Ask(ENTER_RIGHT_DOWN_X), Simple_Ask(ENTER_RIGHT_DOWN_Y));
                try {
                    MyTrapeziums[index] = new Trapezium(LD, LU, RU, RD);
                } catch (Exception ex) {
                    System.out.printf("Ошибка ввода: %s\n", ex);
                }
                System.out.printf("Трапеция № %d успешно создана.\n", index + 1);
            }
        } else {
            Set_Default_Trapeziums();
        }
        System.out.printf("----------------------------------\nВсе трапеции успешно созданы.\n");
    }


    public static Figure.eSortedBy Get_Sort_Way()
    {
        int IdSortParameter = 1, IdSortArea = 2;
        String By_Perimeter = "По периметру", By_Area = "По площади";
        System.out.println("Выбор сортировки.");
        System.out.printf("%d. %s\n", IdSortParameter, By_Perimeter);
        System.out.printf("%d. %s\n", IdSortArea, By_Area);
        return (Simple_Ask(":") == 1 ? Figure.eSortedBy.Perimeter : Figure.eSortedBy.Area);
    }


    public static void Print_Trapeziums()
    {
        for(int i = 0; i < QtTrapeziums; i++)
        {
            System.out.printf("Points: [%s]. perimeter = %.2f, area = %.2f\n",
                    MyTrapeziums[i].toString(),
                    MyTrapeziums[i].Get_Perimeter(),
                    MyTrapeziums[i].Get_Area());
        }
        System.out.println("--------------------");
    }


    public static void Start_Sorting()
    {
        System.out.println("Трапеции до сортировки:");
        Print_Trapeziums();
        Arrays.sort(MyTrapeziums);
        System.out.println("Трапеции после сортировки:");
        Print_Trapeziums();
    }


    public static void Foreach_Print() {
        System.out.println("Тестирование foreach (iterators)\n");
        for (Trapezium trapezium : MyTrapeziums) {
            System.out.println(trapezium.toString());
        }
        System.out.println("------------Конец foreach.------------");
    }


    public static void Repeat_Menu()
    {
        boolean _WantToContinue = false;
        do {
            Trapezium.Set_Sort_By(Get_Sort_Way());
            Start_Sorting();
            Foreach_Print();
            _WantToContinue = Continue_Ask();
        } while(_WantToContinue);

        System.out.printf("\n\n\nFinished...");
    }


    public static void main(String[] args)
    {
        Ask_For_Qt_Trapeziums();
        Create_Trapeziums();
        Repeat_Menu();
    }
}
