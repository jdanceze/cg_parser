package com.google.common.math;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Booleans;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Iterator;
@GwtCompatible(emulated = true)
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/math/DoubleMath.class */
public final class DoubleMath {
    private static final double MIN_INT_AS_DOUBLE = -2.147483648E9d;
    private static final double MAX_INT_AS_DOUBLE = 2.147483647E9d;
    private static final double MIN_LONG_AS_DOUBLE = -9.223372036854776E18d;
    private static final double MAX_LONG_AS_DOUBLE_PLUS_ONE = 9.223372036854776E18d;
    @VisibleForTesting
    static final int MAX_FACTORIAL = 170;
    private static final double LN_2 = Math.log(2.0d);
    @VisibleForTesting
    static final double[] everySixteenthFactorial = {1.0d, 2.0922789888E13d, 2.631308369336935E35d, 1.2413915592536073E61d, 1.2688693218588417E89d, 7.156945704626381E118d, 9.916779348709496E149d, 1.974506857221074E182d, 3.856204823625804E215d, 5.5502938327393044E249d, 4.7147236359920616E284d};

    @GwtIncompatible
    static double roundIntermediate(double x, RoundingMode mode) {
        if (!DoubleUtils.isFinite(x)) {
            throw new ArithmeticException("input is infinite or NaN");
        }
        switch (AnonymousClass1.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(isMathematicalInteger(x));
                return x;
            case 2:
                if (x >= Const.default_value_double || isMathematicalInteger(x)) {
                    return x;
                }
                return ((long) x) - 1;
            case 3:
                if (x <= Const.default_value_double || isMathematicalInteger(x)) {
                    return x;
                }
                return ((long) x) + 1;
            case 4:
                return x;
            case 5:
                if (isMathematicalInteger(x)) {
                    return x;
                }
                return ((long) x) + (x > Const.default_value_double ? 1 : -1);
            case 6:
                return Math.rint(x);
            case 7:
                double z = Math.rint(x);
                if (Math.abs(x - z) == 0.5d) {
                    return x + Math.copySign(0.5d, x);
                }
                return z;
            case 8:
                double z2 = Math.rint(x);
                if (Math.abs(x - z2) == 0.5d) {
                    return x;
                }
                return z2;
            default:
                throw new AssertionError();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.google.common.math.DoubleMath$1  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/math/DoubleMath$1.class */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$java$math$RoundingMode = new int[RoundingMode.values().length];

        static {
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.UNNECESSARY.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.FLOOR.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.CEILING.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.DOWN.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.UP.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_EVEN.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_UP.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_DOWN.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
        }
    }

    @GwtIncompatible
    public static int roundToInt(double x, RoundingMode mode) {
        double z = roundIntermediate(x, mode);
        MathPreconditions.checkInRangeForRoundingInputs((z > -2.147483649E9d) & (z < 2.147483648E9d), x, mode);
        return (int) z;
    }

    @GwtIncompatible
    public static long roundToLong(double x, RoundingMode mode) {
        double z = roundIntermediate(x, mode);
        MathPreconditions.checkInRangeForRoundingInputs((MIN_LONG_AS_DOUBLE - z < 1.0d) & (z < MAX_LONG_AS_DOUBLE_PLUS_ONE), x, mode);
        return (long) z;
    }

    @GwtIncompatible
    public static BigInteger roundToBigInteger(double x, RoundingMode mode) {
        double x2 = roundIntermediate(x, mode);
        if ((MIN_LONG_AS_DOUBLE - x2 < 1.0d) & (x2 < MAX_LONG_AS_DOUBLE_PLUS_ONE)) {
            return BigInteger.valueOf((long) x2);
        }
        int exponent = Math.getExponent(x2);
        long significand = DoubleUtils.getSignificand(x2);
        BigInteger result = BigInteger.valueOf(significand).shiftLeft(exponent - 52);
        return x2 < Const.default_value_double ? result.negate() : result;
    }

    @GwtIncompatible
    public static boolean isPowerOfTwo(double x) {
        if (x > Const.default_value_double && DoubleUtils.isFinite(x)) {
            long significand = DoubleUtils.getSignificand(x);
            return (significand & (significand - 1)) == 0;
        }
        return false;
    }

    public static double log2(double x) {
        return Math.log(x) / LN_2;
    }

    @GwtIncompatible
    public static int log2(double x, RoundingMode mode) {
        boolean increment;
        Preconditions.checkArgument(x > Const.default_value_double && DoubleUtils.isFinite(x), "x must be positive and finite");
        int exponent = Math.getExponent(x);
        if (!DoubleUtils.isNormal(x)) {
            return log2(x * 4.503599627370496E15d, mode) - 52;
        }
        switch (AnonymousClass1.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(x));
            case 2:
                increment = false;
                break;
            case 3:
                increment = !isPowerOfTwo(x);
                break;
            case 4:
                increment = (exponent < 0) & (!isPowerOfTwo(x));
                break;
            case 5:
                increment = (exponent >= 0) & (!isPowerOfTwo(x));
                break;
            case 6:
            case 7:
            case 8:
                double xScaled = DoubleUtils.scaleNormalize(x);
                increment = xScaled * xScaled > 2.0d;
                break;
            default:
                throw new AssertionError();
        }
        return increment ? exponent + 1 : exponent;
    }

    @GwtIncompatible
    public static boolean isMathematicalInteger(double x) {
        return DoubleUtils.isFinite(x) && (x == Const.default_value_double || 52 - Long.numberOfTrailingZeros(DoubleUtils.getSignificand(x)) <= Math.getExponent(x));
    }

    public static double factorial(int n) {
        MathPreconditions.checkNonNegative("n", n);
        if (n > 170) {
            return Double.POSITIVE_INFINITY;
        }
        double accum = 1.0d;
        for (int i = 1 + (n & (-16)); i <= n; i++) {
            accum *= i;
        }
        return accum * everySixteenthFactorial[n >> 4];
    }

    public static boolean fuzzyEquals(double a, double b, double tolerance) {
        MathPreconditions.checkNonNegative("tolerance", tolerance);
        return Math.copySign(a - b, 1.0d) <= tolerance || a == b || (Double.isNaN(a) && Double.isNaN(b));
    }

    public static int fuzzyCompare(double a, double b, double tolerance) {
        if (fuzzyEquals(a, b, tolerance)) {
            return 0;
        }
        if (a < b) {
            return -1;
        }
        if (a > b) {
            return 1;
        }
        return Booleans.compare(Double.isNaN(a), Double.isNaN(b));
    }

    @GwtIncompatible
    @Deprecated
    public static double mean(double... values) {
        Preconditions.checkArgument(values.length > 0, "Cannot take mean of 0 values");
        long count = 1;
        double mean = checkFinite(values[0]);
        for (int index = 1; index < values.length; index++) {
            checkFinite(values[index]);
            count++;
            mean += (values[index] - mean) / count;
        }
        return mean;
    }

    @Deprecated
    public static double mean(int... values) {
        Preconditions.checkArgument(values.length > 0, "Cannot take mean of 0 values");
        long sum = 0;
        for (int i : values) {
            sum += i;
        }
        return sum / values.length;
    }

    @Deprecated
    public static double mean(long... values) {
        Preconditions.checkArgument(values.length > 0, "Cannot take mean of 0 values");
        long count = 1;
        double mean = values[0];
        for (int index = 1; index < values.length; index++) {
            count++;
            mean += (values[index] - mean) / count;
        }
        return mean;
    }

    @GwtIncompatible
    @Deprecated
    public static double mean(Iterable<? extends Number> values) {
        return mean(values.iterator());
    }

    @GwtIncompatible
    @Deprecated
    public static double mean(Iterator<? extends Number> values) {
        Preconditions.checkArgument(values.hasNext(), "Cannot take mean of 0 values");
        long count = 1;
        double checkFinite = checkFinite(values.next().doubleValue());
        while (true) {
            double mean = checkFinite;
            if (values.hasNext()) {
                double value = checkFinite(values.next().doubleValue());
                count++;
                checkFinite = mean + ((value - mean) / count);
            } else {
                return mean;
            }
        }
    }

    @CanIgnoreReturnValue
    @GwtIncompatible
    private static double checkFinite(double argument) {
        Preconditions.checkArgument(DoubleUtils.isFinite(argument));
        return argument;
    }

    private DoubleMath() {
    }
}
