
import java.io.*;
import java.util.*;

public class  Main {
    public static ArrayList<Baggage> sortedBaggage = new ArrayList<>();


    public static void main(String[] args) {
        try {
            if ( args.length >= 1 ) {
                switch (args[0]) {
                    case "-?":
                    case "-h":
                        System.out.println(
                                "Syntax:\n" +
                                        "\t-a  [file [encoding]]    - append data\n" +
                                        "\t-az [file [encoding]]    - append data, compress every record\n" +
                                        "\t-d                       - clear all data\n" +
                                        "\t-dk  {fn|dat|dest|w} key - clear data by key\n" +
                                        "\t-p                       - print data unsorted\n" +
                                        "\t-ps  {fn|dat|dest|w}     - print data sorted by isbn/author/name\n" +
                                        "\t-psr {fn|dat|dest|w}     - print data reverse sorted by isbn/author/name\n" +
                                        "\t-f   {fn|dat|dest|w} key - find record by key\n" +
                                        "\t-fr  {fn|dat|dest|w} key - find records > key\n" +
                                        "\t-fl  {fn|dat|dest|w} key - find records < key\n" +
                                        "\t-?, -h                   - command line syntax\n"
                        );
                        break;
                    case "-a":
                        // Append file with new object from System.in
                        // -a [file [encoding]]
                        //appendFile(args, false);
                        break;
                    case "-az":
                        // Append file with compressed new object from System.in
                        // -az [file [encoding]]
                        //appendFile(args, true);
                        break;
                    case "-p":
                        // Prints data file
                        printFile();
                        break;
                    case "-ps":
                        // Prints data file sorted by key
//                        if (!printFile(args, false)) {
//                            System.exit(1);
//                        }
                        break;
                    case "-psr":
                        // Prints data file reverse-sorted by key
//                        if (!printFile(args, true)) {
//                            System.exit(1);
//                        }
                        break;
                    case "-d":
                        // delete files
                        if (args.length != 1) {
                            System.err.println("Invalid number of arguments");
                            System.exit(1);
                        }
                        deleteFile();
                        break;
                    case "-dk":
                        // Delete records by key
//                        if (!deleteFile(args)) {
//                            System.exit(1);
//                        }
                        break;
                    case "-f":
                        // Find record(s) by key
//                        if (!findByKey(args)) {
//                            System.exit(1);
//                        }
                        break;
                    case "-fr":
                        // Find record(s) by key large then key
//                        if (!findByKey(args, new KeyCompReverse())) {
//                            System.exit(1);
//                        }
                        break;
                    case "-fl":
                        // Find record(s) by key less then key
                       // if (!findByKey(args, new KeyComp())) {
                            System.exit(1);
                        //}
                        break;
                    default:
                        //System.err.println("Option is not realised: " + args[0]);
                        System.exit(1);
                }
            }
            else {
                //System.err.println( "Books: Nothing to do! Enter -? for options" );
            }
        }
        catch ( Exception e ) {
            //System.err.println( "Run/time error: " + e );
            System.exit(1);
        }
        //System.err.println( "Finished..." );
        System.exit(0);
    }

    private static final String filename    = "Books.dat";
    private static final String filenameBak = "Books.~dat";
    private static final String idxname     = "Books.idx";
    private static final String idxnameBak  = "Books.~idx";

    // input file encoding:
    private static String encoding = "Cp866";
    private static PrintStream bookOut = System.out;

    public static Baggage readBaggage(Scanner fin){
        return Baggage.nextRead( fin, bookOut )
                ? Baggage.read( fin, bookOut ) : null;
    }

    private static void deleteBackup() {
        new File( filenameBak ).delete();
        new File( idxnameBak ).delete();
    }

    public static void deleteFile() {
        deleteBackup();
        new File( filename ).delete();
        new File( idxname ).delete();
    }

    private static void backup() {
        deleteBackup();
        new File( filename ).renameTo( new File( filenameBak ));
        new File( idxname ).renameTo( new File( idxnameBak ));
    }

    public static boolean deleteFile(String index, String strkey)
            throws ClassNotFoundException, IOException, KeyNotUniqueException {
        //-dk  {i|a|n} key      - clear data by key
        long[] poss;
        try ( Index idx = Index.load( idxname )) {
            IndexBase pidx = indexByArg( index, idx );
            if ( pidx == null ) {
                return false;
            }
            if (!pidx.contains(strkey)) {
                //System.err.println( "Key not found: " + args[2] );
                return false;
            }
            poss = pidx.get(strkey);
        }
        backup();
        Arrays.sort( poss );
        try ( Index idx = Index.load( idxname );
              RandomAccessFile fileBak= new RandomAccessFile(filenameBak, "rw");
              RandomAccessFile file = new RandomAccessFile( filename, "rw")) {
            boolean[] wasZipped = new boolean[] {false};
            long pos;
            while (( pos = fileBak.getFilePointer()) < fileBak.length() ) {
                Baggage baggage = (Baggage)
                        Buffer.readObject( fileBak, pos, wasZipped );
                if ( Arrays.binarySearch(poss, pos) < 0 ) { // if not found in deleted
                    long ptr = Buffer.writeObject( file, baggage, wasZipped[0] );
                    idx.put( baggage, ptr );
                }
            }
        }
        return true;
    }

    public static void appendFile(Boolean zipped, File file)
            throws IOException, ClassNotFoundException,
            KeyNotUniqueException {
        Scanner fin = new Scanner( file, encoding );
        //bookOut.println( "Ent book data: " );
        try ( Index idx = Index.load( idxname );
              RandomAccessFile raf = new RandomAccessFile( filename, "rw" )) {
            for(;;) {
                Baggage baggage = readBaggage( fin );
                if ( baggage == null )
                    break;
                long pos = Buffer.writeObject( raf, baggage, zipped );
                idx.put( baggage, pos );
            }
        }
    }

    public static Baggage getRecord( RandomAccessFile raf, long pos )
            throws ClassNotFoundException, IOException {
        boolean[] wasZipped = new boolean[] {false};
        Baggage baggage = (Baggage) Buffer.readObject( raf, pos, wasZipped );
        if (wasZipped[0]) {
            //System.out.print( " compressed" );
        }
        return baggage;
        //System.out.println( " record at position "+ pos + ": \n" + baggage );
    }

    private static void getRecord( RandomAccessFile raf, String key,
                                     IndexBase pidx ) throws ClassNotFoundException, IOException {
        long[] poss = pidx.get( key );
        for ( long pos : poss ) {

            //System.out.print( "*** Key: " +  key + " points to" );
            sortedBaggage.add(getRecord( raf, pos ));
        }
    }

    public static ArrayList<Baggage> printFile()
            throws IOException, ClassNotFoundException {
        long pos;
        ArrayList<Baggage> recordsToPrint = new ArrayList<>();
        try ( RandomAccessFile raf = new RandomAccessFile( filename, "rw" )) {
            while (( pos = raf.getFilePointer()) < raf.length() ) {
                //System.out.print( "#" + (++rec ));
                recordsToPrint.add( getRecord(raf, pos) );
            }
            System.out.flush();
            return recordsToPrint;
        }
    }

    public static IndexBase indexByArg( String arg, Index idx ) {
        IndexBase pidx = null;
        switch (arg) {
            case "fn":
                pidx = idx.flightNumbers;
                break;
            case "dest":
                pidx = idx.destinations;
                break;
            case "dat":
                pidx = idx.dates;
                break;
            case "w":
                pidx = idx.weights;
                break;
            default:
                System.err.println("Invalid index specified: " + arg);
                break;
        }
        return pidx;
    }

    public static ArrayList<Baggage> printFile(String index, boolean reverse )
            throws ClassNotFoundException, IOException {
        sortedBaggage.clear();
        try ( Index idx = Index.load( idxname );
              RandomAccessFile raf = new RandomAccessFile( filename, "rw" )) {
            IndexBase pidx = indexByArg( index, idx );
            if ( pidx == null ) {
                return null;
            }
            String[] keys =
                    pidx.getKeys( reverse ? new KeyCompReverse() : new KeyComp() );
            for ( String key : keys ) {
                getRecord( raf, key, pidx );
            }
        }
        return sortedBaggage;
    }

    public static ArrayList findByKey(String index, String strkey)
            throws ClassNotFoundException, IOException {
        sortedBaggage.clear();
        try ( Index idx = Index.load( idxname );
              RandomAccessFile raf = new RandomAccessFile( filename, "rw" )) {
            IndexBase pidx = indexByArg( index, idx );
            if (!pidx.contains(strkey)) {
                System.err.println( "Key not found: " + strkey );
                return null;
            }

            getRecord( raf, strkey, pidx );
            return sortedBaggage;
        }

    }

    public static ArrayList findByKey(String index, String strkey, Comparator<String> comp )
            throws ClassNotFoundException, IOException {
        sortedBaggage.clear();
        try ( Index idx = Index.load( idxname );
              RandomAccessFile raf = new RandomAccessFile( filename, "rw" )) {
            IndexBase pidx = indexByArg( index, idx );
            String[] keys = pidx.getKeys( comp );
            int type = keys[0].compareTo(strkey);
            boolean isKeyLess = keys[0].compareTo(strkey) >= 0;
            for (String key : keys) {
                if ((key.compareTo(strkey) >= 0) != isKeyLess) {
                    break;
                }
                getRecord(raf, key, pidx);
            }
        }
        return sortedBaggage;
    }
}