package io.github.wickeddroid.plugin.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SaveLoad {
    public static <T extends Object> void save(T object, String path) throws Exception{
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
        oos.writeObject(object);
        oos.flush();
        oos.close();
    }
    public static <T extends Object> T load(String path) throws Exception
    {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
        T result = (T)ois.readObject();
        ois.close();
        return result;
    }
}
