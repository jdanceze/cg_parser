package org.apache.http.util;
/* loaded from: gencallgraphv3.jar:httpcore-4.0.1.jar:org/apache/http/util/LangUtils.class */
public final class LangUtils {
    public static final int HASH_SEED = 17;
    public static final int HASH_OFFSET = 37;

    private LangUtils() {
    }

    public static int hashCode(int seed, int hashcode) {
        return (seed * 37) + hashcode;
    }

    public static int hashCode(int seed, boolean b) {
        return hashCode(seed, b ? 1 : 0);
    }

    public static int hashCode(int seed, Object obj) {
        return hashCode(seed, obj != null ? obj.hashCode() : 0);
    }

    public static boolean equals(Object obj1, Object obj2) {
        return obj1 == null ? obj2 == null : obj1.equals(obj2);
    }

    public static boolean equals(Object[] a1, Object[] a2) {
        if (a1 == null) {
            if (a2 == null) {
                return true;
            }
            return false;
        } else if (a2 != null && a1.length == a2.length) {
            for (int i = 0; i < a1.length; i++) {
                if (!equals(a1[i], a2[i])) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
