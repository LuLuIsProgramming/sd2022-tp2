package util;

public class Flag {
    private static boolean flag;

    public static void set(boolean b) {
        flag = b;
    }

    public static boolean get() {
        return flag;
    }

    public static boolean matches(boolean b) {
        return flag == b;
    }

}
