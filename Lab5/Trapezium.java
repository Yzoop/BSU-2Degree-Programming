package Lab5;

public class Trapezium extends Figure {
    protected static final String EXCEPTION_INDEX = "Ошибка индекса точки";
    protected final String strDivider = ";";

    private static final int MyQtPoints = 4;
    private static final double MyQtDoubleFields = 2; //area and perimeter
    private int CurrentIterator = 0;

    public Point Get_Point(int AtIndex) throws Exception
    {
        if (AtIndex >= 0 && AtIndex < MyQtPoints)
        {
            return new Point(MyPoints[AtIndex].Get_X(), MyPoints[AtIndex].Get_Y());
        }
        else
            throw new Exception(EXCEPTION_INDEX);
    }

    @Override
    public double Get_Perimeter()
    {
        if (MyPerimeter.equals(NOT_IMPLEMENTED))
        {
            MyPerimeter = (double)0;
            for(int i = 0; i < MyQtPoints - 1; i++)
            {
                MyPerimeter += MyPoints[i].Dist_To(MyPoints[i + 1]);
            }
        }

        return MyPerimeter;
    }

    @Override
    public double Get_Area()
    {
        if (MyArea.equals(NOT_IMPLEMENTED))
        {
            Point HeightSecondPoint = new Point(MyPoints[1].Get_X(), MyPoints[0].Get_Y());
            double Height = MyPoints[1].Dist_To(HeightSecondPoint);
            MyArea = ((MyPoints[1].Dist_To(MyPoints[2]) + MyPoints[0].Dist_To(MyPoints[3])) * Height) / 2;
        }

        return MyArea;
    }

    public Trapezium Str_To_Figure(String strFigure) throws Exception {
        String[] strPoints = strFigure.split(strDivider);
        for(int i = 0; i < strPoints.length; i++)
        {
            MyPoints[i] = new Point(strPoints[i]);
        }
        return new Trapezium(MyPoints[0], MyPoints[1], MyPoints[2], MyPoints[3]);
    }


    public Trapezium(Point LeftDown, Point LeftUp, Point RightUp, Point RightDown)
    {
        super(MyQtPoints, new Point[]{LeftDown, LeftUp, RightUp, RightDown});
    }

    public String toString()
    {
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < MyQtPoints - 1; i++)
        {
            str.append(MyPoints[i].toString()).append(strDivider);
        }
        str.append(MyPoints[MyQtPoints - 1].toString());

        return str.toString();
    }


    public void reset()
    {
        CurrentIterator = 0;
    }


    @Override
    public boolean hasNext()
    {
        return CurrentIterator < MyQtDoubleFields;
    }

    @Override
    public Double next() {
        if (hasNext()) {
            switch (CurrentIterator) {
                case 0:
                    return MyPerimeter;
                case 1:
                    return MyArea;
            }

            ++CurrentIterator;
        }
        reset();
        return null;
    }


    @Override
    public int compareTo(Figure figure) {
        if (figure != null)
        {
            switch(CurrentSorter)
            {
                case Area:
                    return MyArea.compareTo(figure.Get_Area());
                case Perimeter:
                    return MyPerimeter.compareTo(figure.Get_Perimeter());
                default:
                    return 0;
            }
        }
        else
            throw new IllegalArgumentException();
    }
}
