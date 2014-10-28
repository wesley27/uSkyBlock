package us.talabrek.ultimateskyblock;

import java.io.*;

public class SLAPI {
    public static Object load(final File path) throws Exception {
        final ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
        final Object result = ois.readObject();
        ois.close();
        return result;
    }

    public static void save(final Object obj, final File path) throws Exception {
        final ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
        oos.writeObject(obj);
        oos.flush();
        oos.close();
    }
}