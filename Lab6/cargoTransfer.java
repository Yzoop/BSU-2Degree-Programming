package Lab6;

import java.util.Random;

public abstract class cargoTransfer implements iTransfer {
    protected Random MyRandom = new Random();
    protected static int RandBand = 1500;
    protected String MyStartCity;
    protected String MyFinishCity;
}
