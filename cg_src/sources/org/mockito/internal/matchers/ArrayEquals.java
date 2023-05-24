package org.mockito.internal.matchers;

import java.lang.reflect.Array;
import java.util.Arrays;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/matchers/ArrayEquals.class */
public class ArrayEquals extends Equals {
    public ArrayEquals(Object wanted) {
        super(wanted);
    }

    @Override // org.mockito.internal.matchers.Equals, org.mockito.ArgumentMatcher
    public boolean matches(Object actual) {
        Object wanted = getWanted();
        if (wanted == null || actual == null) {
            return super.matches(actual);
        }
        if ((wanted instanceof boolean[]) && (actual instanceof boolean[])) {
            return Arrays.equals((boolean[]) wanted, (boolean[]) actual);
        }
        if ((wanted instanceof byte[]) && (actual instanceof byte[])) {
            return Arrays.equals((byte[]) wanted, (byte[]) actual);
        }
        if ((wanted instanceof char[]) && (actual instanceof char[])) {
            return Arrays.equals((char[]) wanted, (char[]) actual);
        }
        if ((wanted instanceof double[]) && (actual instanceof double[])) {
            return Arrays.equals((double[]) wanted, (double[]) actual);
        }
        if ((wanted instanceof float[]) && (actual instanceof float[])) {
            return Arrays.equals((float[]) wanted, (float[]) actual);
        }
        if ((wanted instanceof int[]) && (actual instanceof int[])) {
            return Arrays.equals((int[]) wanted, (int[]) actual);
        }
        if ((wanted instanceof long[]) && (actual instanceof long[])) {
            return Arrays.equals((long[]) wanted, (long[]) actual);
        }
        if ((wanted instanceof short[]) && (actual instanceof short[])) {
            return Arrays.equals((short[]) wanted, (short[]) actual);
        }
        if ((wanted instanceof Object[]) && (actual instanceof Object[])) {
            return Arrays.equals((Object[]) wanted, (Object[]) actual);
        }
        return false;
    }

    @Override // org.mockito.internal.matchers.Equals
    public String toString() {
        if (getWanted() != null && getWanted().getClass().isArray()) {
            return appendArray(createObjectArray(getWanted()));
        }
        return super.toString();
    }

    private String appendArray(Object[] array) {
        StringBuilder out = new StringBuilder("[");
        for (int i = 0; i < array.length; i++) {
            out.append(new Equals(array[i]).toString());
            if (i != array.length - 1) {
                out.append(", ");
            }
        }
        out.append("]");
        return out.toString();
    }

    public static Object[] createObjectArray(Object array) {
        if (array instanceof Object[]) {
            return (Object[]) array;
        }
        Object[] result = new Object[Array.getLength(array)];
        for (int i = 0; i < Array.getLength(array); i++) {
            result[i] = Array.get(array, i);
        }
        return result;
    }
}
