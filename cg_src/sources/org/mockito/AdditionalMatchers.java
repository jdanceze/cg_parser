package org.mockito;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.CompareEqual;
import org.mockito.internal.matchers.EqualsWithDelta;
import org.mockito.internal.matchers.Find;
import org.mockito.internal.matchers.GreaterOrEqual;
import org.mockito.internal.matchers.GreaterThan;
import org.mockito.internal.matchers.LessOrEqual;
import org.mockito.internal.matchers.LessThan;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/AdditionalMatchers.class */
public class AdditionalMatchers {
    public static <T extends Comparable<T>> T geq(T value) {
        reportMatcher(new GreaterOrEqual(value));
        return null;
    }

    public static byte geq(byte value) {
        reportMatcher(new GreaterOrEqual(Byte.valueOf(value)));
        return (byte) 0;
    }

    public static double geq(double value) {
        reportMatcher(new GreaterOrEqual(Double.valueOf(value)));
        return Const.default_value_double;
    }

    public static float geq(float value) {
        reportMatcher(new GreaterOrEqual(Float.valueOf(value)));
        return 0.0f;
    }

    public static int geq(int value) {
        reportMatcher(new GreaterOrEqual(Integer.valueOf(value)));
        return 0;
    }

    public static long geq(long value) {
        reportMatcher(new GreaterOrEqual(Long.valueOf(value)));
        return 0L;
    }

    public static short geq(short value) {
        reportMatcher(new GreaterOrEqual(Short.valueOf(value)));
        return (short) 0;
    }

    public static <T extends Comparable<T>> T leq(T value) {
        reportMatcher(new LessOrEqual(value));
        return null;
    }

    public static byte leq(byte value) {
        reportMatcher(new LessOrEqual(Byte.valueOf(value)));
        return (byte) 0;
    }

    public static double leq(double value) {
        reportMatcher(new LessOrEqual(Double.valueOf(value)));
        return Const.default_value_double;
    }

    public static float leq(float value) {
        reportMatcher(new LessOrEqual(Float.valueOf(value)));
        return 0.0f;
    }

    public static int leq(int value) {
        reportMatcher(new LessOrEqual(Integer.valueOf(value)));
        return 0;
    }

    public static long leq(long value) {
        reportMatcher(new LessOrEqual(Long.valueOf(value)));
        return 0L;
    }

    public static short leq(short value) {
        reportMatcher(new LessOrEqual(Short.valueOf(value)));
        return (short) 0;
    }

    public static <T extends Comparable<T>> T gt(T value) {
        reportMatcher(new GreaterThan(value));
        return null;
    }

    public static byte gt(byte value) {
        reportMatcher(new GreaterThan(Byte.valueOf(value)));
        return (byte) 0;
    }

    public static double gt(double value) {
        reportMatcher(new GreaterThan(Double.valueOf(value)));
        return Const.default_value_double;
    }

    public static float gt(float value) {
        reportMatcher(new GreaterThan(Float.valueOf(value)));
        return 0.0f;
    }

    public static int gt(int value) {
        reportMatcher(new GreaterThan(Integer.valueOf(value)));
        return 0;
    }

    public static long gt(long value) {
        reportMatcher(new GreaterThan(Long.valueOf(value)));
        return 0L;
    }

    public static short gt(short value) {
        reportMatcher(new GreaterThan(Short.valueOf(value)));
        return (short) 0;
    }

    public static <T extends Comparable<T>> T lt(T value) {
        reportMatcher(new LessThan(value));
        return null;
    }

    public static byte lt(byte value) {
        reportMatcher(new LessThan(Byte.valueOf(value)));
        return (byte) 0;
    }

    public static double lt(double value) {
        reportMatcher(new LessThan(Double.valueOf(value)));
        return Const.default_value_double;
    }

    public static float lt(float value) {
        reportMatcher(new LessThan(Float.valueOf(value)));
        return 0.0f;
    }

    public static int lt(int value) {
        reportMatcher(new LessThan(Integer.valueOf(value)));
        return 0;
    }

    public static long lt(long value) {
        reportMatcher(new LessThan(Long.valueOf(value)));
        return 0L;
    }

    public static short lt(short value) {
        reportMatcher(new LessThan(Short.valueOf(value)));
        return (short) 0;
    }

    public static <T extends Comparable<T>> T cmpEq(T value) {
        reportMatcher(new CompareEqual(value));
        return null;
    }

    public static String find(String regex) {
        reportMatcher(new Find(regex));
        return null;
    }

    public static <T> T[] aryEq(T[] value) {
        reportMatcher(new ArrayEquals(value));
        return null;
    }

    public static short[] aryEq(short[] value) {
        reportMatcher(new ArrayEquals(value));
        return null;
    }

    public static long[] aryEq(long[] value) {
        reportMatcher(new ArrayEquals(value));
        return null;
    }

    public static int[] aryEq(int[] value) {
        reportMatcher(new ArrayEquals(value));
        return null;
    }

    public static float[] aryEq(float[] value) {
        reportMatcher(new ArrayEquals(value));
        return null;
    }

    public static double[] aryEq(double[] value) {
        reportMatcher(new ArrayEquals(value));
        return null;
    }

    public static char[] aryEq(char[] value) {
        reportMatcher(new ArrayEquals(value));
        return null;
    }

    public static byte[] aryEq(byte[] value) {
        reportMatcher(new ArrayEquals(value));
        return null;
    }

    public static boolean[] aryEq(boolean[] value) {
        reportMatcher(new ArrayEquals(value));
        return null;
    }

    public static boolean and(boolean first, boolean second) {
        ThreadSafeMockingProgress.mockingProgress().getArgumentMatcherStorage().reportAnd();
        return false;
    }

    public static byte and(byte first, byte second) {
        ThreadSafeMockingProgress.mockingProgress().getArgumentMatcherStorage().reportAnd();
        return (byte) 0;
    }

    public static char and(char first, char second) {
        ThreadSafeMockingProgress.mockingProgress().getArgumentMatcherStorage().reportAnd();
        return (char) 0;
    }

    public static double and(double first, double second) {
        ThreadSafeMockingProgress.mockingProgress().getArgumentMatcherStorage().reportAnd();
        return Const.default_value_double;
    }

    public static float and(float first, float second) {
        ThreadSafeMockingProgress.mockingProgress().getArgumentMatcherStorage().reportAnd();
        return 0.0f;
    }

    public static int and(int first, int second) {
        ThreadSafeMockingProgress.mockingProgress().getArgumentMatcherStorage().reportAnd();
        return 0;
    }

    public static long and(long first, long second) {
        ThreadSafeMockingProgress.mockingProgress().getArgumentMatcherStorage().reportAnd();
        return 0L;
    }

    public static short and(short first, short second) {
        ThreadSafeMockingProgress.mockingProgress().getArgumentMatcherStorage().reportAnd();
        return (short) 0;
    }

    public static <T> T and(T first, T second) {
        ThreadSafeMockingProgress.mockingProgress().getArgumentMatcherStorage().reportAnd();
        return null;
    }

    public static boolean or(boolean first, boolean second) {
        ThreadSafeMockingProgress.mockingProgress().getArgumentMatcherStorage().reportOr();
        return false;
    }

    public static <T> T or(T first, T second) {
        ThreadSafeMockingProgress.mockingProgress().getArgumentMatcherStorage().reportOr();
        return null;
    }

    public static short or(short first, short second) {
        ThreadSafeMockingProgress.mockingProgress().getArgumentMatcherStorage().reportOr();
        return (short) 0;
    }

    public static long or(long first, long second) {
        ThreadSafeMockingProgress.mockingProgress().getArgumentMatcherStorage().reportOr();
        return 0L;
    }

    public static int or(int first, int second) {
        ThreadSafeMockingProgress.mockingProgress().getArgumentMatcherStorage().reportOr();
        return 0;
    }

    public static float or(float first, float second) {
        ThreadSafeMockingProgress.mockingProgress().getArgumentMatcherStorage().reportOr();
        return 0.0f;
    }

    public static double or(double first, double second) {
        ThreadSafeMockingProgress.mockingProgress().getArgumentMatcherStorage().reportOr();
        return Const.default_value_double;
    }

    public static char or(char first, char second) {
        ThreadSafeMockingProgress.mockingProgress().getArgumentMatcherStorage().reportOr();
        return (char) 0;
    }

    public static byte or(byte first, byte second) {
        ThreadSafeMockingProgress.mockingProgress().getArgumentMatcherStorage().reportOr();
        return (byte) 0;
    }

    public static <T> T not(T first) {
        ThreadSafeMockingProgress.mockingProgress().getArgumentMatcherStorage().reportNot();
        return null;
    }

    public static short not(short first) {
        ThreadSafeMockingProgress.mockingProgress().getArgumentMatcherStorage().reportNot();
        return (short) 0;
    }

    public static int not(int first) {
        ThreadSafeMockingProgress.mockingProgress().getArgumentMatcherStorage().reportNot();
        return 0;
    }

    public static long not(long first) {
        ThreadSafeMockingProgress.mockingProgress().getArgumentMatcherStorage().reportNot();
        return 0L;
    }

    public static float not(float first) {
        ThreadSafeMockingProgress.mockingProgress().getArgumentMatcherStorage().reportNot();
        return 0.0f;
    }

    public static double not(double first) {
        ThreadSafeMockingProgress.mockingProgress().getArgumentMatcherStorage().reportNot();
        return Const.default_value_double;
    }

    public static char not(char first) {
        ThreadSafeMockingProgress.mockingProgress().getArgumentMatcherStorage().reportNot();
        return (char) 0;
    }

    public static boolean not(boolean first) {
        ThreadSafeMockingProgress.mockingProgress().getArgumentMatcherStorage().reportNot();
        return false;
    }

    public static byte not(byte first) {
        ThreadSafeMockingProgress.mockingProgress().getArgumentMatcherStorage().reportNot();
        return (byte) 0;
    }

    public static double eq(double value, double delta) {
        reportMatcher(new EqualsWithDelta(Double.valueOf(value), Double.valueOf(delta)));
        return Const.default_value_double;
    }

    public static float eq(float value, float delta) {
        reportMatcher(new EqualsWithDelta(Float.valueOf(value), Float.valueOf(delta)));
        return 0.0f;
    }

    private static void reportMatcher(ArgumentMatcher<?> matcher) {
        ThreadSafeMockingProgress.mockingProgress().getArgumentMatcherStorage().reportMatcher(matcher);
    }
}
