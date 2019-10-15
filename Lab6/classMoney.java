package Lab6;

public class classMoney {
    public int val;
    private final int TOO_MUCH_MONEY = 5000;

    public classMoney(final int _val)
    {
        val = Math.abs(_val % TOO_MUCH_MONEY);
    }
}
