package polyglot.util;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/UniqueID.class */
public class UniqueID {
    private static int count = 0;
    private static int icount = 0;

    public static String newID(String s) {
        String uid = new StringBuffer().append(s).append("$").append(count).toString();
        count++;
        return uid;
    }

    public static int newIntID() {
        int i = icount;
        icount = i + 1;
        return i;
    }
}
