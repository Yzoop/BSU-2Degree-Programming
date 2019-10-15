package Lab6;

public interface iTransfer {
    enum enWay
    {
        eCargo,
        ePassanger
    }
    public abstract classMoney Get_Price(String fromcity, String tocity);
    public abstract classTime Get_Time(String fromcity, String tocity);
}
