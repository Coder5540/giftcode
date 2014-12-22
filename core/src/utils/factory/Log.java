package utils.factory;

public class Log {

    public final static boolean DEBUG_ENABLE = true;


    public static void s(String message) {
        if (DEBUG_ENABLE) {
            System.out.println("SEND : " + message);
        }
    }

    public static void r(String message) {
        if (DEBUG_ENABLE) {
            System.out.println("RECEIVED : " + message);            
        }
    }

    public static void r(String title, String message) {
        if (DEBUG_ENABLE) {
            System.out.println("RECEIVED " + title + " : " + message);
        }
    }

    public static void d(String message) {
        if (DEBUG_ENABLE) {
            System.out.println("DEBUG : " + message);
        }
    }

    public static void d(String title, String message) {
        if (DEBUG_ENABLE) {
            System.out.println("DEBUG : " + title + " : " + message);
        }
    }

    public static void d(String title, int value) {
        d(title, value + "");
    }

    public static void l(String message) {
        if (DEBUG_ENABLE) {
            System.out.println("LOG : " + message);
        }
    }
}
