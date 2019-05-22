package com.mcspacecraft.itemmagnet.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class SLAPI {
    public static void save(Object obj, String path) throws Exception {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(obj);
            oos.flush();
        }
    }

    public static void save(Object obj, OutputStream out) throws Exception {
        try (ObjectOutputStream oos = new ObjectOutputStream(out)) {
            oos.writeObject(obj);
            oos.flush();
        }
    }

    public static Object load(String path) throws Exception {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            Object result = ois.readObject();
            return result;
        }
    }

    public static Object load(InputStream in) throws Exception {
        try (ObjectInputStream ois = new ObjectInputStream(in)) {
            Object result = ois.readObject();
            return result;
        }
    }
}
