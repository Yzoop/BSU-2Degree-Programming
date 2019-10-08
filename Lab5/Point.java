package Lab5;

public class Point {
    private Double MyX, MyY;
    private static final String pointDivider = ",";

    public double Get_X()
    {
        return MyX;
    }

    public double Dist_To(final Point p)
    {
        return Math.sqrt(Math.pow(MyX - p.Get_X(), 2) + Math.pow(MyY - p.Get_Y(),2));
    }


    public double Get_Y()
    {
        return MyY;
    }

    public Point(double newX,double newY)
    {
        MyX = newX;
        MyY = newY;
    }

    public String toString()
    {
        return Double.toString(MyX) + pointDivider + Double.toString(MyY);
    }

    //format: X,Y
    public Point(String strPoint)
    {
        String[] Parts = strPoint.split(pointDivider);
        MyX = Double.parseDouble(Parts[0]);
        MyY = Double.parseDouble(Parts[1]);
    }
}
