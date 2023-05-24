package org.powermock.utils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/utils/ArrayUtil.class */
public class ArrayUtil {
    public static <T> T[] addAll(T[] array1, T[] array2) {
        if (isEmpty(array1)) {
            return (T[]) clone(array2);
        }
        if (isEmpty(array2)) {
            return (T[]) clone(array1);
        }
        int newLength = array1.length + array2.length;
        T[] joinedArray = (T[]) createNewArrayWithSameType(array1, newLength);
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }

    private static <T> boolean isEmpty(T[] a) {
        return a == null || a.length == 0;
    }

    public static <T> T[] clone(T[] array) {
        if (array == null) {
            return null;
        }
        return (T[]) ((Object[]) array.clone());
    }

    private static <T> T[] createNewArrayWithSameType(T[] arrayPrototype, int newLength) {
        return (T[]) ((Object[]) Array.newInstance(arrayPrototype[0].getClass(), newLength));
    }

    public static String[] mergeArrays(String[] firstArray, String[] secondArray) {
        if (firstArray == null && secondArray == null) {
            return null;
        }
        if (firstArray == null) {
            return secondArray;
        }
        if (secondArray == null) {
            return firstArray;
        }
        Set<String> globalIgnore = new HashSet<>();
        globalIgnore.addAll(Arrays.asList(firstArray));
        globalIgnore.addAll(Arrays.asList(secondArray));
        return (String[]) globalIgnore.toArray(new String[globalIgnore.size()]);
    }
}
