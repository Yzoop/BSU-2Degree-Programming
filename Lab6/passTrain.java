package Lab6;

public class passTrain extends passangerTransfer {
    @Override
    public classMoney Get_Price(String fromcity, String tocity) {
        return new classMoney(MyRandom.nextInt());
    }

    @Override
    public classTime Get_Time(String fromcity, String tocity) {
        return new classTime(MyRandom.nextInt(), MyRandom.nextInt(), MyRandom.nextInt());
    }

}
