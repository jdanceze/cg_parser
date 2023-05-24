package com.google.common.primitives;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Converter;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
@GwtCompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/primitives/Ints.class */
public final class Ints {
    public static final int BYTES = 4;
    public static final int MAX_POWER_OF_TWO = 1073741824;

    private Ints() {
    }

    public static int hashCode(int value) {
        return value;
    }

    public static int checkedCast(long value) {
        int result = (int) value;
        Preconditions.checkArgument(((long) result) == value, "Out of range: %s", value);
        return result;
    }

    public static int saturatedCast(long value) {
        if (value > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        if (value < -2147483648L) {
            return Integer.MIN_VALUE;
        }
        return (int) value;
    }

    public static int compare(int a, int b) {
        if (a < b) {
            return -1;
        }
        return a > b ? 1 : 0;
    }

    public static boolean contains(int[] array, int target) {
        for (int value : array) {
            if (value == target) {
                return true;
            }
        }
        return false;
    }

    public static int indexOf(int[] array, int target) {
        return indexOf(array, target, 0, array.length);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int indexOf(int[] array, int target, int start, int end) {
        for (int i = start; i < end; i++) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x0040, code lost:
        r6 = r6 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int indexOf(int[] r4, int[] r5) {
        /*
            r0 = r4
            java.lang.String r1 = "array"
            java.lang.Object r0 = com.google.common.base.Preconditions.checkNotNull(r0, r1)
            r0 = r5
            java.lang.String r1 = "target"
            java.lang.Object r0 = com.google.common.base.Preconditions.checkNotNull(r0, r1)
            r0 = r5
            int r0 = r0.length
            if (r0 != 0) goto L15
            r0 = 0
            return r0
        L15:
            r0 = 0
            r6 = r0
        L17:
            r0 = r6
            r1 = r4
            int r1 = r1.length
            r2 = r5
            int r2 = r2.length
            int r1 = r1 - r2
            r2 = 1
            int r1 = r1 + r2
            if (r0 >= r1) goto L46
            r0 = 0
            r7 = r0
        L24:
            r0 = r7
            r1 = r5
            int r1 = r1.length
            if (r0 >= r1) goto L3e
            r0 = r4
            r1 = r6
            r2 = r7
            int r1 = r1 + r2
            r0 = r0[r1]
            r1 = r5
            r2 = r7
            r1 = r1[r2]
            if (r0 == r1) goto L38
            goto L40
        L38:
            int r7 = r7 + 1
            goto L24
        L3e:
            r0 = r6
            return r0
        L40:
            int r6 = r6 + 1
            goto L17
        L46:
            r0 = -1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.primitives.Ints.indexOf(int[], int[]):int");
    }

    public static int lastIndexOf(int[] array, int target) {
        return lastIndexOf(array, target, 0, array.length);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int lastIndexOf(int[] array, int target, int start, int end) {
        for (int i = end - 1; i >= start; i--) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public static int min(int... array) {
        Preconditions.checkArgument(array.length > 0);
        int min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    public static int max(int... array) {
        Preconditions.checkArgument(array.length > 0);
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    @Beta
    public static int constrainToRange(int value, int min, int max) {
        Preconditions.checkArgument(min <= max, "min (%s) must be less than or equal to max (%s)", min, max);
        return Math.min(Math.max(value, min), max);
    }

    public static int[] concat(int[]... arrays) {
        int length = 0;
        for (int[] array : arrays) {
            length += array.length;
        }
        int[] result = new int[length];
        int pos = 0;
        for (int[] array2 : arrays) {
            System.arraycopy(array2, 0, result, pos, array2.length);
            pos += array2.length;
        }
        return result;
    }

    public static byte[] toByteArray(int value) {
        return new byte[]{(byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) value};
    }

    public static int fromByteArray(byte[] bytes) {
        Preconditions.checkArgument(bytes.length >= 4, "array too small: %s < %s", bytes.length, 4);
        return fromBytes(bytes[0], bytes[1], bytes[2], bytes[3]);
    }

    public static int fromBytes(byte b1, byte b2, byte b3, byte b4) {
        return (b1 << 24) | ((b2 & 255) << 16) | ((b3 & 255) << 8) | (b4 & 255);
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/primitives/Ints$IntConverter.class */
    private static final class IntConverter extends Converter<String, Integer> implements Serializable {
        static final IntConverter INSTANCE = new IntConverter();
        private static final long serialVersionUID = 1;

        private IntConverter() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.base.Converter
        public Integer doForward(String value) {
            return Integer.decode(value);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.base.Converter
        public String doBackward(Integer value) {
            return value.toString();
        }

        public String toString() {
            return "Ints.stringConverter()";
        }

        private Object readResolve() {
            return INSTANCE;
        }
    }

    @Beta
    public static Converter<String, Integer> stringConverter() {
        return IntConverter.INSTANCE;
    }

    public static int[] ensureCapacity(int[] array, int minLength, int padding) {
        Preconditions.checkArgument(minLength >= 0, "Invalid minLength: %s", minLength);
        Preconditions.checkArgument(padding >= 0, "Invalid padding: %s", padding);
        return array.length < minLength ? Arrays.copyOf(array, minLength + padding) : array;
    }

    public static String join(String separator, int... array) {
        Preconditions.checkNotNull(separator);
        if (array.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder(array.length * 5);
        builder.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            builder.append(separator).append(array[i]);
        }
        return builder.toString();
    }

    public static Comparator<int[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/primitives/Ints$LexicographicalComparator.class */
    private enum LexicographicalComparator implements Comparator<int[]> {
        INSTANCE;

        @Override // java.util.Comparator
        public int compare(int[] left, int[] right) {
            int minLength = Math.min(left.length, right.length);
            for (int i = 0; i < minLength; i++) {
                int result = Ints.compare(left[i], right[i]);
                if (result != 0) {
                    return result;
                }
            }
            return left.length - right.length;
        }

        @Override // java.lang.Enum
        public String toString() {
            return "Ints.lexicographicalComparator()";
        }
    }

    public static void sortDescending(int[] array) {
        Preconditions.checkNotNull(array);
        sortDescending(array, 0, array.length);
    }

    public static void sortDescending(int[] array, int fromIndex, int toIndex) {
        Preconditions.checkNotNull(array);
        Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
        Arrays.sort(array, fromIndex, toIndex);
        reverse(array, fromIndex, toIndex);
    }

    public static void reverse(int[] array) {
        Preconditions.checkNotNull(array);
        reverse(array, 0, array.length);
    }

    public static void reverse(int[] array, int fromIndex, int toIndex) {
        Preconditions.checkNotNull(array);
        Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
        int i = fromIndex;
        for (int j = toIndex - 1; i < j; j--) {
            int tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
            i++;
        }
    }

    public static int[] toArray(Collection<? extends Number> collection) {
        if (collection instanceof IntArrayAsList) {
            return ((IntArrayAsList) collection).toIntArray();
        }
        Object[] boxedArray = collection.toArray();
        int len = boxedArray.length;
        int[] array = new int[len];
        for (int i = 0; i < len; i++) {
            array[i] = ((Number) Preconditions.checkNotNull(boxedArray[i])).intValue();
        }
        return array;
    }

    public static List<Integer> asList(int... backingArray) {
        if (backingArray.length == 0) {
            return Collections.emptyList();
        }
        return new IntArrayAsList(backingArray);
    }

    @GwtCompatible
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/primitives/Ints$IntArrayAsList.class */
    private static class IntArrayAsList extends AbstractList<Integer> implements RandomAccess, Serializable {
        final int[] array;
        final int start;
        final int end;
        private static final long serialVersionUID = 0;

        IntArrayAsList(int[] array) {
            this(array, 0, array.length);
        }

        IntArrayAsList(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.end - this.start;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean isEmpty() {
            return false;
        }

        @Override // java.util.AbstractList, java.util.List
        public Integer get(int index) {
            Preconditions.checkElementIndex(index, size());
            return Integer.valueOf(this.array[this.start + index]);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean contains(Object target) {
            return (target instanceof Integer) && Ints.indexOf(this.array, ((Integer) target).intValue(), this.start, this.end) != -1;
        }

        @Override // java.util.AbstractList, java.util.List
        public int indexOf(Object target) {
            int i;
            if ((target instanceof Integer) && (i = Ints.indexOf(this.array, ((Integer) target).intValue(), this.start, this.end)) >= 0) {
                return i - this.start;
            }
            return -1;
        }

        @Override // java.util.AbstractList, java.util.List
        public int lastIndexOf(Object target) {
            int i;
            if ((target instanceof Integer) && (i = Ints.lastIndexOf(this.array, ((Integer) target).intValue(), this.start, this.end)) >= 0) {
                return i - this.start;
            }
            return -1;
        }

        @Override // java.util.AbstractList, java.util.List
        public Integer set(int index, Integer element) {
            Preconditions.checkElementIndex(index, size());
            int oldValue = this.array[this.start + index];
            this.array[this.start + index] = ((Integer) Preconditions.checkNotNull(element)).intValue();
            return Integer.valueOf(oldValue);
        }

        @Override // java.util.AbstractList, java.util.List
        public List<Integer> subList(int fromIndex, int toIndex) {
            int size = size();
            Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
            if (fromIndex == toIndex) {
                return Collections.emptyList();
            }
            return new IntArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
        }

        @Override // java.util.AbstractList, java.util.Collection, java.util.List
        public boolean equals(@NullableDecl Object object) {
            if (object == this) {
                return true;
            }
            if (object instanceof IntArrayAsList) {
                IntArrayAsList that = (IntArrayAsList) object;
                int size = size();
                if (that.size() != size) {
                    return false;
                }
                for (int i = 0; i < size; i++) {
                    if (this.array[this.start + i] != that.array[that.start + i]) {
                        return false;
                    }
                }
                return true;
            }
            return super.equals(object);
        }

        @Override // java.util.AbstractList, java.util.Collection, java.util.List
        public int hashCode() {
            int result = 1;
            for (int i = this.start; i < this.end; i++) {
                result = (31 * result) + Ints.hashCode(this.array[i]);
            }
            return result;
        }

        @Override // java.util.AbstractCollection
        public String toString() {
            StringBuilder builder = new StringBuilder(size() * 5);
            builder.append('[').append(this.array[this.start]);
            for (int i = this.start + 1; i < this.end; i++) {
                builder.append(", ").append(this.array[i]);
            }
            return builder.append(']').toString();
        }

        int[] toIntArray() {
            return Arrays.copyOfRange(this.array, this.start, this.end);
        }
    }

    @NullableDecl
    @Beta
    public static Integer tryParse(String string) {
        return tryParse(string, 10);
    }

    @NullableDecl
    @Beta
    public static Integer tryParse(String string, int radix) {
        Long result = Longs.tryParse(string, radix);
        if (result == null || result.longValue() != result.intValue()) {
            return null;
        }
        return Integer.valueOf(result.intValue());
    }
}
