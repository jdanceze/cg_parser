package soot.toDex;

import soot.options.Options;
/* loaded from: gencallgraphv3.jar:soot/toDex/Debug.class */
public class Debug {
    public static boolean TODEX_DEBUG;

    public static void printDbg(String s, Object... objects) {
        TODEX_DEBUG = Options.v().verbose();
        if (TODEX_DEBUG) {
            for (Object o : objects) {
                s = String.valueOf(s) + o.toString();
            }
            System.out.println(s);
        }
    }

    public static void printDbg(boolean c, String s, Object... objects) {
        if (c) {
            printDbg(s, objects);
        }
    }
}
