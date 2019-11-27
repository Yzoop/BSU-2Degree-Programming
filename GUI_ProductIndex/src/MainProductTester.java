import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class MainProductTester {

    //-------------------------------------------------------------------------------#
    static final String FILE_NAME = "records.dat";
    static final String FILE_NAME_BACK = "records.~dat";
    static final String IDX_NAME = "records.idx";
    static final String IDX_NAME_BACK = "records.~idx";


    // input file encoding:
    private static String ENCODING = "Cp866";
    private static PrintStream STREAM_OUT = System.out;
    public static ArrayList<ProductRecord> sortedProductRecords = new ArrayList<>();
    //-------------------------------------------------------------------------------#


    public static ProductRecord readProductRecord( Scanner fin ) throws IOException {
        return ProductRecord.nextRead( fin, STREAM_OUT)
                ? ProductRecord.read( fin, STREAM_OUT) : null;
    }

    public static void deleteBackup() {
        new File(FILE_NAME_BACK).delete();
        new File(IDX_NAME_BACK).delete();
    }

    static void deleteFile() {
        deleteBackup();
        new File(FILE_NAME).delete();
        new File(IDX_NAME).delete();
        sortedProductRecords.clear();
    }

    public static void backup() {
        deleteBackup();
        new File(FILE_NAME).renameTo( new File(FILE_NAME_BACK));
        new File(IDX_NAME).renameTo( new File(IDX_NAME_BACK));
    }

    public static boolean deleteFile(String index, String key)
            throws ClassNotFoundException, IOException, KeyNotUniqueException {
        long[] poss = null;
        try ( Index idx = Index.load(IDX_NAME)) {
            IndexBase pidx = indexByArg( index, idx );
            if ( pidx == null ) {
                return false;
            }
            if ( pidx.contains(key)== false ) {
                //System.err.println( "Key not found: " + key);
                return false;
            }
            poss = pidx.get(key);
        }
        backup();
        Arrays.sort( poss );
        try (Index idx = Index.load(IDX_NAME);
             RandomAccessFile fileBak= new RandomAccessFile(FILE_NAME_BACK, "rw");
             RandomAccessFile file = new RandomAccessFile(FILE_NAME, "rw")) {
            boolean[] wasZipped = new boolean[] {false};
            long pos;
            while (( pos = fileBak.getFilePointer()) < fileBak.length() ) {
                ProductRecord record = (ProductRecord)
                        Buffer.readObject( fileBak, pos, wasZipped );
                if ( Arrays.binarySearch(poss, pos) < 0 ) { // if not found in deleted
                    long ptr = Buffer.writeObject( file, record, wasZipped[0] );
                    idx.put( record, ptr );
                }
            }
        }
        return true;
    }

    public static void appendFile( String[] args, Boolean zipped )
            throws FileNotFoundException, IOException, ClassNotFoundException,
            KeyNotUniqueException {
        if ( args.length >= 2 ) {
            FileInputStream stdin = new FileInputStream( args[1] );
            System.setIn( stdin );
            if (args.length == 3) {
                ENCODING = args[2];
            }
            // hide output:
            STREAM_OUT = new PrintStream("nul");
        }
        //appendFile( zipped );
    }

    public static void appendFile( Boolean zipped, File fileWithDataToAppend)
            throws FileNotFoundException, IOException, ClassNotFoundException,
            KeyNotUniqueException {
        Scanner fin = new Scanner( fileWithDataToAppend, ENCODING);
        STREAM_OUT.println( "Enter record data: " );
        try (Index idx = Index.load(IDX_NAME);
             RandomAccessFile raf = new RandomAccessFile(FILE_NAME, "rw" )) {
            for(;;) {
                ProductRecord record = readProductRecord( fin );
                if ( record == null )
                    break;
                idx.test( record );
                long pos = Buffer.writeObject( raf, record, zipped );
                idx.put( record, pos );
            }
        }
    }

    public static ProductRecord getRecord(RandomAccessFile raf, long pos )
            throws ClassNotFoundException, IOException {
        boolean[] wasZipped = new boolean[] {false};
        ProductRecord record = (ProductRecord) Buffer.readObject( raf, pos, wasZipped );
        if (wasZipped[0]) {
            //System.out.print( " compressed" );
        }
        return record;
        //System.out.println( " record at position "+ pos + ": \n" + record );
    }

    public static void getRecord(RandomAccessFile raf, String key, IndexBase pidx ) throws ClassNotFoundException, IOException {
        long[] poss = pidx.get( key );
        for ( long pos : poss ) {
            //System.out.print( "*** Key: " +  key + " points to" );
            sortedProductRecords.add(getRecord( raf, pos ));
        }
    }

    static ArrayList<ProductRecord> getDataToPrint()
            throws FileNotFoundException, IOException, ClassNotFoundException {
        long pos;
        ArrayList<ProductRecord> recordsToPrint = new ArrayList<>();
        int rec = 0;
        try ( RandomAccessFile raf = new RandomAccessFile(FILE_NAME, "rw" )) {
            while (( pos = raf.getFilePointer()) < raf.length() ) {
                //records.add( "#" + (++rec ));
                recordsToPrint.add(getRecord( raf, pos ));
            }
            System.out.flush();
            return recordsToPrint;
        }
    }

    public static IndexBase indexByArg( String arg, Index idx ) {
        IndexBase pidx = null;
        if ( arg.equals(Index.CODE_ARG_STORAGE_ID)) {
            pidx = idx.storageIds;
        }
        else if ( arg.equals(Index.CODE_ARG_PRODUCT_ID)) {
            pidx = idx.productIds;
        }
        else if ( arg.equals(Index.CODE_ARG_ARRIVAL_TIME)) {
            pidx = idx.arrivalTimes;
        }
        else if ( arg.equals(Index.CODE_ARG_SHELFTIME)) {
            pidx = idx.shelfTimes;
        }
        else {
            System.err.println( "Invalid index specified: " + arg );
        }
        return pidx;
    }

    public static boolean getDataToPrint(String index, boolean reverse )
            throws ClassNotFoundException, IOException {
        //correct
        sortedProductRecords.clear();
        try (Index idx = Index.load(IDX_NAME);
             RandomAccessFile raf = new RandomAccessFile(FILE_NAME, "rw" )) {
            IndexBase pidx = indexByArg( index, idx );
            if ( pidx == null ) {
                return false;
            }
            String[] keys =
                    pidx.getKeys( reverse ? new KeyCompReverse() : new KeyComp() );
            for ( String key : keys ) {
                getRecord( raf, key, pidx );
            }
        }
        return true;
    }

    public static ArrayList<ProductRecord> findByKey(String index, String key)
            throws ClassNotFoundException, IOException {
        sortedProductRecords.clear();
        try (Index idx = Index.load(IDX_NAME);
             RandomAccessFile raf = new RandomAccessFile(FILE_NAME, "rw" )) {
            IndexBase pidx = indexByArg( index, idx );
            if (!pidx.contains(key))
            {
                System.err.println( "Key not found: " + key );
                return null;
            }
            else{
                getRecord( raf, key, pidx );
                return sortedProductRecords;
            }
        }
    }

    public static ArrayList<ProductRecord> findByKey(String index, String bykey, Comparator<String> comp )
            throws ClassNotFoundException, IOException {
        sortedProductRecords.clear();
        try (Index idx = Index.load(IDX_NAME);
             RandomAccessFile raf = new RandomAccessFile(FILE_NAME, "rw" )) {
            IndexBase pidx = indexByArg( index, idx );

            String[] keys = pidx.getKeys( comp );
            for ( int i = 0; i < keys.length; i++ ) {
                String key = keys[i];
                if ( key.equals( bykey )) {
                    break;
                }
                getRecord( raf, key, pidx );
            }
        }
        return sortedProductRecords;
    }
}