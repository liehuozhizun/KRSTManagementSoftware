package org.krst.app.services;

public class DataPassService {
    private static Object value;
    private static Boolean ready = false;

    public static Object getValue() {
        if (!ready) return null;
        ready = false;
        return value;
    }

    public static void setValue(Object object) {
        value = object;
        ready = true;
    }

}
