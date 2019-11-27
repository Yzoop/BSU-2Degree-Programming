
import java.io.PrintStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Baggage implements Serializable  {
    public String flight_number;
    public static final String P_flightNumber = "Flight number";
    public Date date;
    public static final String P_date = "Date";
    public String destination;
    public static final String P_destination = "Destination";
    public String full_name;
    public static final String P_fullName = "Full name";
    public int pieces;
    public static final String P_pieces = "Pieces";
    public double weight;
    public static final String P_weight = "Weight";

    public static Boolean nextRead( Scanner fin, PrintStream out ) {
        return nextRead( P_flightNumber, fin, out );
    }
    static Boolean nextRead( final String prompt, Scanner fin, PrintStream out ) {
        return fin.hasNextLine();
    }

    public static Baggage read( Scanner fin, PrintStream out ){
        Baggage baggage = new Baggage();
        DateFormat dateFormat = new SimpleDateFormat("d.mm.yyyy, HH:mm");

        baggage.flight_number = fin.nextLine();

        if (! nextRead( P_date, fin, out )) return null;
        try {
            baggage.date = dateFormat.parse(fin.nextLine());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        if (! nextRead( P_destination, fin, out )) return null;
        baggage.destination = fin.nextLine();

        if (! nextRead( P_fullName, fin, out )) return null;
        baggage.full_name = fin.nextLine();

        if (! nextRead( P_pieces, fin, out )) return null;
        baggage.pieces = Integer.parseInt(fin.nextLine());

        if (! nextRead( P_weight, fin, out )) return null;
        baggage.weight = Integer.parseInt(fin.nextLine());
        return baggage;
    }

    public Baggage() {
    }

    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d.mm.yyyy, HH:mm");
        return String.format("%s|%s|%s|%d|%a|%s",
                full_name, flight_number, destination, pieces, weight, date);
    }






}
