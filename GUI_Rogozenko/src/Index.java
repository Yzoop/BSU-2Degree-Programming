
import java.io.*;
import java.util.*;
import java.util.zip.*;

class KeyComp implements Comparator<String> {
    public int compare(String o1, String o2) {
        // right order:
        return o1.compareTo(o2);
    }
}

class KeyCompReverse implements Comparator<String> {
    public int compare(String o1, String o2) {
        // reverse order:
        return o2.compareTo(o1);
    }
}

interface IndexBase {
    String[] getKeys( Comparator<String> comp );
    void put(String key, long value );
    boolean contains( String key );
    long[] get( String key );
}

class IndexOne2One implements Serializable, IndexBase {
    // Unique keys
    // class release version:
    private static final long serialVersionUID = 1L;

    private TreeMap<String,Long> map;

    public IndexOne2One() {
        map = new TreeMap<String,Long> ();
    }

    public String[] getKeys( Comparator<String> comp ) {
        String[] result = map.keySet().toArray( new String[0] );
        Arrays.sort( result, comp );
        return result;
    }

    public void put(String key, long value ) {
        map.put(key, value);
    }

    public boolean contains( String key ) {
        return map.containsKey(key);
    }

    public long[] get( String key ) {
        long pos = map.get(key);
        return new long[] {pos};
    }
}

class IndexOne2N implements Serializable, IndexBase {
    // Not unique keys
    // class release version:
    private static final long serialVersionUID = 1L;

    private TreeMap<String,long[]> map;

    public IndexOne2N() {
        map = new TreeMap<String,long[]> ();
    }

    public String[] getKeys( Comparator<String> comp ) {
        String[] result = map.keySet().toArray( new String[0] );
        Arrays.sort( result, comp );
        return result;
    }

    public void put(String key, long value ) {
        long[] arr = map.get(key);
        arr = ( arr != null ) ?
                Index.InsertValue( arr, value ) :
                new long[] {value};
        map.put(key, arr);
    }

    public void put( String keys,   // few keys in one string
                     String keyDel, // key delimiter
                     long value ) {
        StringTokenizer st = new StringTokenizer( keys, keyDel );
        int num = st.countTokens();
        for ( int i= 0; i < num; i++ ) {
            String key = st.nextToken();
            key = key.trim();
            put( key, value );
        }
    }

    public boolean contains( String key ) {
        return map.containsKey(key);
    }

    public long[] get( String key ) {
        return map.get( key );
    }
}

class KeyNotUniqueException extends Exception {
    // class release version:
    private static final long serialVersionUID = 1L;

    public KeyNotUniqueException( String key ) {
        super( new String( "Key is not unique: " + key ));
    }
}

public class Index implements Serializable, Closeable {
    // class release version:
    private static final long serialVersionUID = 1L;

    public static long[] InsertValue( long[] arr, long value ) {
        int length = ( arr == null ) ? 0 : arr.length;
        long [] result = new long[length + 1];
        for( int i = 0; i < length; i++ )
            result[i] = arr[i];
        result[length] = value;
        return result;
    }

    IndexOne2N          flightNumbers;
    IndexOne2N          dates;
    IndexOne2N          destinations;
    IndexOne2N          weights;


    public void put( Baggage baggage, long value ) throws KeyNotUniqueException {
        flightNumbers.put( baggage.flight_number+"", value );
        dates.put( baggage.date.toString(), value );
        destinations.put( baggage.destination, value );
        weights.put( baggage.weight+"", value );
    }

    public Index()  {
        flightNumbers = new IndexOne2N();
        dates = new IndexOne2N();
        destinations = new IndexOne2N();
        weights = new IndexOne2N();
    }

    public static Index load( String name )
            throws IOException, ClassNotFoundException {
        Index obj = null;
        try {
            FileInputStream file = new FileInputStream( name );
            try( ZipInputStream zis = new ZipInputStream( file )) {
                ZipEntry zen = zis.getNextEntry();
                if (!zen.getName().equals(Buffer.zipEntryName)) {
                    throw new IOException("Invalid block format");
                }
                try ( ObjectInputStream ois = new ObjectInputStream( zis )) {
                    obj = (Index) ois.readObject();
                }
            }
        } catch ( FileNotFoundException e ) {
            obj = new Index();
        }
        if ( obj != null ) {
            obj.save( name );
        }
        return obj;
    }

    private transient String filename = null;

    public void save( String name ) {
        filename = name;
    }

    public void saveAs( String name ) throws IOException {
        FileOutputStream file = new FileOutputStream( name );
        try ( ZipOutputStream zos = new ZipOutputStream( file )) {
            zos.putNextEntry( new ZipEntry( Buffer.zipEntryName ));
            zos.setLevel( ZipOutputStream.DEFLATED );
            try ( ObjectOutputStream oos = new ObjectOutputStream( zos )) {
                oos.writeObject( this );
                oos.flush();
                zos.closeEntry();
                zos.flush();
            }
        }
    }

    public void close() throws IOException {
        saveAs( filename );
    }
}
