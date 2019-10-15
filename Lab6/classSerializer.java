package Lab6;

import java.io.*;

public class classSerializer {

    private static classSerializer MyInstance = null;
    public static final String FILE_SAVE_NAME = "settings.dat";


    public static classSettings Deserialize() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_SAVE_NAME));

        return (classSettings)ois.readObject();
    }


    public static boolean Ok_Serialize(classSettings currentSettings){
        ObjectOutputStream oos = null;
        try
        {
            oos = new ObjectOutputStream(new FileOutputStream(FILE_SAVE_NAME));
            oos.writeObject(currentSettings);
            return true;
        }
        catch(Exception ex)
        {
            return false;
        }
        finally
        {
            if (oos != null)
            {
                try
                {
                    oos.close();
                }
                catch (Exception ex)
                {
                    System.out.println(ex);
                }
            }
        }
    }
}
