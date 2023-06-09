package com.google.common.primitives;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Comparator;
import javax.resource.spi.work.WorkException;
import javax.resource.spi.work.WorkManager;
@Beta
@GwtCompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/primitives/UnsignedLongs.class */
public final class UnsignedLongs {
    public static final long MAX_VALUE = -1;

    private UnsignedLongs() {
    }

    private static long flip(long a) {
        return a ^ Long.MIN_VALUE;
    }

    public static int compare(long a, long b) {
        return Longs.compare(flip(a), flip(b));
    }

    public static long min(long... array) {
        Preconditions.checkArgument(array.length > 0);
        long min = flip(array[0]);
        for (int i = 1; i < array.length; i++) {
            long next = flip(array[i]);
            if (next < min) {
                min = next;
            }
        }
        return flip(min);
    }

    public static long max(long... array) {
        Preconditions.checkArgument(array.length > 0);
        long max = flip(array[0]);
        for (int i = 1; i < array.length; i++) {
            long next = flip(array[i]);
            if (next > max) {
                max = next;
            }
        }
        return flip(max);
    }

    public static String join(String separator, long... array) {
        Preconditions.checkNotNull(separator);
        if (array.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder(array.length * 5);
        builder.append(toString(array[0]));
        for (int i = 1; i < array.length; i++) {
            builder.append(separator).append(toString(array[i]));
        }
        return builder.toString();
    }

    public static Comparator<long[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/primitives/UnsignedLongs$LexicographicalComparator.class */
    enum LexicographicalComparator implements Comparator<long[]> {
        INSTANCE;

        @Override // java.util.Comparator
        public int compare(long[] left, long[] right) {
            int minLength = Math.min(left.length, right.length);
            for (int i = 0; i < minLength; i++) {
                if (left[i] != right[i]) {
                    return UnsignedLongs.compare(left[i], right[i]);
                }
            }
            return left.length - right.length;
        }

        @Override // java.lang.Enum
        public String toString() {
            return "UnsignedLongs.lexicographicalComparator()";
        }
    }

    public static void sort(long[] array) {
        Preconditions.checkNotNull(array);
        sort(array, 0, array.length);
    }

    public static void sort(long[] array, int fromIndex, int toIndex) {
        Preconditions.checkNotNull(array);
        Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
        for (int i = fromIndex; i < toIndex; i++) {
            array[i] = flip(array[i]);
        }
        Arrays.sort(array, fromIndex, toIndex);
        for (int i2 = fromIndex; i2 < toIndex; i2++) {
            array[i2] = flip(array[i2]);
        }
    }

    public static void sortDescending(long[] array) {
        Preconditions.checkNotNull(array);
        sortDescending(array, 0, array.length);
    }

    public static void sortDescending(long[] array, int fromIndex, int toIndex) {
        Preconditions.checkNotNull(array);
        Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
        for (int i = fromIndex; i < toIndex; i++) {
            int i2 = i;
            array[i2] = array[i2] ^ WorkManager.INDEFINITE;
        }
        Arrays.sort(array, fromIndex, toIndex);
        for (int i3 = fromIndex; i3 < toIndex; i3++) {
            int i4 = i3;
            array[i4] = array[i4] ^ WorkManager.INDEFINITE;
        }
    }

    public static long divide(long dividend, long divisor) {
        if (divisor < 0) {
            if (compare(dividend, divisor) < 0) {
                return 0L;
            }
            return 1L;
        } else if (dividend >= 0) {
            return dividend / divisor;
        } else {
            long quotient = ((dividend >>> 1) / divisor) << 1;
            long rem = dividend - (quotient * divisor);
            return quotient + (compare(rem, divisor) >= 0 ? 1 : 0);
        }
    }

    public static long remainder(long dividend, long divisor) {
        if (divisor < 0) {
            if (compare(dividend, divisor) < 0) {
                return dividend;
            }
            return dividend - divisor;
        } else if (dividend >= 0) {
            return dividend % divisor;
        } else {
            long quotient = ((dividend >>> 1) / divisor) << 1;
            long rem = dividend - (quotient * divisor);
            return rem - (compare(rem, divisor) >= 0 ? divisor : 0L);
        }
    }

    @CanIgnoreReturnValue
    public static long parseUnsignedLong(String string) {
        return parseUnsignedLong(string, 10);
    }

    @CanIgnoreReturnValue
    public static long parseUnsignedLong(String string, int radix) {
        Preconditions.checkNotNull(string);
        if (string.length() == 0) {
            throw new NumberFormatException("empty string");
        }
        if (radix < 2 || radix > 36) {
            throw new NumberFormatException("illegal radix: " + radix);
        }
        int maxSafePos = ParseOverflowDetection.maxSafeDigits[radix] - 1;
        long value = 0;
        for (int pos = 0; pos < string.length(); pos++) {
            int digit = Character.digit(string.charAt(pos), radix);
            if (digit == -1) {
                throw new NumberFormatException(string);
            }
            if (pos > maxSafePos && ParseOverflowDetection.overflowInParse(value, digit, radix)) {
                throw new NumberFormatException("Too large for unsigned long: " + string);
            }
            value = (value * radix) + digit;
        }
        return value;
    }

    @CanIgnoreReturnValue
    public static long decode(String stringValue) {
        ParseRequest request = ParseRequest.fromString(stringValue);
        try {
            return parseUnsignedLong(request.rawValue, request.radix);
        } catch (NumberFormatException e) {
            NumberFormatException decodeException = new NumberFormatException("Error parsing value: " + stringValue);
            decodeException.initCause(e);
            throw decodeException;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/primitives/UnsignedLongs$ParseOverflowDetection.class */
    public static final class ParseOverflowDetection {
        static final long[] maxValueDivs = new long[37];
        static final int[] maxValueMods = new int[37];
        static final int[] maxSafeDigits = new int[37];

        private ParseOverflowDetection() {
        }

        static {
            BigInteger overflow = new BigInteger("10000000000000000", 16);
            for (int i = 2; i <= 36; i++) {
                maxValueDivs[i] = UnsignedLongs.divide(-1L, i);
                maxValueMods[i] = (int) UnsignedLongs.remainder(-1L, i);
                maxSafeDigits[i] = overflow.toString(i).length() - 1;
            }
        }

        static boolean overflowInParse(long current, int digit, int radix) {
            if (current >= 0) {
                if (current < maxValueDivs[radix]) {
                    return false;
                }
                return current > maxValueDivs[radix] || digit > maxValueMods[radix];
            }
            return true;
        }
    }

    public static String toString(long x) {
        return toString(x, 10);
    }

    public static String toString(long x, int radix) {
        long quotient;
        Preconditions.checkArgument(radix >= 2 && radix <= 36, "radix (%s) must be between Character.MIN_RADIX and Character.MAX_RADIX", radix);
        if (x == 0) {
            return WorkException.UNDEFINED;
        }
        if (x > 0) {
            return Long.toString(x, radix);
        }
        char[] buf = new char[64];
        int i = buf.length;
        if ((radix & (radix - 1)) == 0) {
            int shift = Integer.numberOfTrailingZeros(radix);
            int mask = radix - 1;
            do {
                i--;
                buf[i] = Character.forDigit(((int) x) & mask, radix);
                x >>>= shift;
            } while (x != 0);
        } else {
            if ((radix & 1) == 0) {
                quotient = (x >>> 1) / (radix >>> 1);
            } else {
                quotient = divide(x, radix);
            }
            long rem = x - (quotient * radix);
            i--;
            buf[i] = Character.forDigit((int) rem, radix);
            long j = quotient;
            while (true) {
                long x2 = j;
                if (x2 <= 0) {
                    break;
                }
                i--;
                buf[i] = Character.forDigit((int) (x2 % radix), radix);
                j = x2 / radix;
            }
        }
        return new String(buf, i, buf.length - i);
    }
}
