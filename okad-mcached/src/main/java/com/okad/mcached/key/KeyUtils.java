package com.okad.mcached.key;

public class KeyUtils {

    public static String keyToString(Object key) {
        if (key == null) {
            return null;
        } else if (key instanceof String) {
            return (String) key;
        } else {
            // returned Object Reference
            if ((key.toString().contains("java.") && key.toString().contains("@"))
                    || (key.toString().contains("epermits.") && key.toString().contains("@"))) {
                return null;
            }
            return key.toString();
        }
    }

}
