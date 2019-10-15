package Lab6;

public class cargoTrain extends cargoTransfer {
    @Override
    public classMoney Get_Price(String fromcity, String tocity)
    {
        return new classMoney(MyRandom.nextInt(RandBand));
    }

    @Override
    public classTime Get_Time(String fromcity, String tocity) {
        return new classTime(MyRandom.nextInt(), MyRandom.nextInt(), MyRandom.nextInt());
    }

}
