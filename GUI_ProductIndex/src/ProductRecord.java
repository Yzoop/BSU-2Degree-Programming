import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.Scanner;

public class ProductRecord implements Record, Serializable {

    private static final long serialVersionUID = 1L;

    //VARIABLES FOR STR KEYS----------------------------------#
    static final String P_ArrivalTime = "ArrivalTime";
    static final String P_SinglePrice = "SinglePrice";
    static final String P_ProductQuantity = "ProductQiantity";
    static final String P_ShelfTimeInDays = "ShelfTimeInDays";
    static final String P_ProductName = "ProductName";
    static final String P_ProductId = "ProductId";
    static final String P_StorageId = "StorageId";
    //--------------------------------------------------------#

    private Time myArrivalTIme;
    private float mySinglePrice;
    private static final char DIVIDER = '|';
    private int myProductQuantity, myShelfTimeInDays;
    private String myProductName, myProductId, myStorageId;


    @Override
    public String getStorageId() {
        return myStorageId;
    }

    @Override
    public String getProductId() {
        return myProductId;
    }

    @Override
    public String getProductName() {
        return myProductName;
    }

    @Override
    public Time getArrivalTime() {
        return myArrivalTIme;
    }

    @Override
    public int getQuantity() {
        return myProductQuantity;
    }

    @Override
    public int getShelfInDays() {
        return myShelfTimeInDays;
    }


    @Override
    public float getSinglePrice() {
        return mySinglePrice;
    }


    public static Boolean nextRead(Scanner fin, PrintStream out)
    {
        return nextRead(P_StorageId, fin, out);
    }


    public static Boolean nextRead(final String prompt, Scanner fin, PrintStream out)
    {
        //out.print(prompt);
        //out.print(": ");
        return fin.hasNextLine();
    }


    public static ProductRecord read(Scanner fin, PrintStream out) throws IOException
    {
        String _storageId, _productId, _productName;
        Time _arrivalT = null;
        int _daysBest, _productQuantity;
        float _singlePrice;

        _storageId = fin.nextLine();

        if (nextRead(P_ProductId, fin, out))
        {
            _productId = fin.nextLine();
        } else{ return null; }

        if (nextRead(P_ProductName, fin, out))
        {
            _productName = fin.nextLine();
        } else{ return null; }

        if (nextRead(P_ArrivalTime, fin, out))
        {
            String strTime = fin.nextLine();
            try {
                _arrivalT = Time.strToTime(strTime);
            }
            catch (IllegalArgumentException iae)
            {
                throw new IOException("Ошибка: невозможно перевести строку '" + strTime + "'в класс Time") ;
            }
        }else{ return null; }

        if (nextRead(P_ProductQuantity, fin, out))
        {
            _productQuantity = Integer.parseInt(fin.nextLine());
        } else{ return null; }

        if (nextRead(P_ShelfTimeInDays, fin, out))
        {
            _daysBest = Integer.parseInt(fin.nextLine());
        } else{ return null; }

        if (nextRead(P_SinglePrice, fin, out))
        {
            _singlePrice = Float.parseFloat(fin.nextLine());
        } else{ return null; }

        //System.out.println("----OK----");

        return new ProductRecord(_storageId, _productId, _productName, _arrivalT, _daysBest, _productQuantity, _singlePrice);
    }


    public ProductRecord(String _storageId, String _productId, String _productName, Time _arrivalT, int _daysBest,
                         int _quantity, float _singlePrice) throws InvalidParameterException
    {
        myStorageId = _storageId;
        myProductId = _productId;
        myProductName = _productName;
        myArrivalTIme = _arrivalT;
        if (_daysBest >= 0)
        {
            myShelfTimeInDays = _daysBest;
        }
        else
            throw new InvalidParameterException("Ошибка: неверно указана дата окончания хранения");

        if (_quantity >= 0)
            myProductQuantity = _quantity;
        else
            throw new InvalidParameterException("Ошибка: неверно указано количество товаров");

        if (_singlePrice >= 0)
            mySinglePrice = _singlePrice;
        else
            throw new InvalidParameterException("Ошибка: неверно указана цена одного товара");
    }


    public ProductRecord(String _storageId, String _productId, String _productName, Time _arrivalT, Time _bestBefore,
                         int _quantity, float _singlePrice) throws InvalidParameterException
    {
        myStorageId = _storageId;
        myProductId = _productId;
        myProductName = _productName;
        myArrivalTIme = _arrivalT;
        if (myArrivalTIme.compareTo(_bestBefore) <= 0)
        {
            myShelfTimeInDays = myArrivalTIme.timeTo(_bestBefore).getInDays();
        }
        else
            throw new InvalidParameterException("Ошибка: неверно указана дата окончания хранения");

        if (_quantity >= 0)
            myProductQuantity = _quantity;
        else
            throw new InvalidParameterException("Ошибка: неверно указано количество товаров");

        if (_singlePrice >= 0)
            mySinglePrice = _singlePrice;
        else
            throw new InvalidParameterException("Ошибка: неверно указана цена одного товара");
    }

    public String toString()
    {
        return  myStorageId                        + DIVIDER +
                myProductId                        + DIVIDER +
                myProductName                      + DIVIDER +
                myArrivalTIme.toString()           + DIVIDER +
               Integer.toString(myProductQuantity) + DIVIDER +
               Integer.toString(myShelfTimeInDays) + DIVIDER +
               Float.toString(mySinglePrice);
    }
}
