package org.powermock.tests.utils.impl;

import java.lang.reflect.Array;
import org.powermock.tests.utils.ArrayMerger;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/tests/utils/impl/ArrayMergerImpl.class */
public class ArrayMergerImpl implements ArrayMerger {
    @Override // org.powermock.tests.utils.ArrayMerger
    public <T> T[] mergeArrays(Class<T> type, T[]... arraysToMerge) {
        if (arraysToMerge == null || arraysToMerge.length == 0) {
            return (T[]) ((Object[]) Array.newInstance((Class<?>) type, 0));
        }
        int size = 0;
        for (T[] array : arraysToMerge) {
            if (array != null) {
                size += array.length;
            }
        }
        T[] finalArray = (T[]) ((Object[]) Array.newInstance((Class<?>) type, size));
        int lastIndex = 0;
        for (T[] currentArray : arraysToMerge) {
            if (currentArray != null) {
                int currentArrayLength = currentArray.length;
                System.arraycopy(currentArray, 0, finalArray, lastIndex, currentArrayLength);
                lastIndex += currentArrayLength;
            }
        }
        return finalArray;
    }
}
