package Lab5;

import java.util.Iterator;

public abstract class Figure implements Iterator<Double>, Comparable<Figure> {

    public enum eSortedBy
    {
        Perimeter,
        Area
    };

    protected final Double NOT_IMPLEMENTED = (double) -1;

    protected static eSortedBy CurrentSorter = eSortedBy.Area;
    protected Double MyArea = NOT_IMPLEMENTED, MyPerimeter = NOT_IMPLEMENTED;
    protected Point[] MyPoints;
    protected int MyQtPoints;

    public abstract double Get_Perimeter();
    public abstract double Get_Area();
    static public void Set_Sort_By(eSortedBy sortby)
    {
        CurrentSorter = sortby;
    }

    public Figure(int qtPoints, Point[] newPoints)
    {
        MyPoints = newPoints;
        MyQtPoints = qtPoints;
    }

}
