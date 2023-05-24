package org.junit;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.junit.function.ThrowingRunnable;
import org.junit.internal.ArrayComparisonFailure;
import org.junit.internal.ExactComparisonCriteria;
import org.junit.internal.InexactComparisonCriteria;
import soot.coffi.Instruction;
import soot.jimple.Jimple;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/Assert.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/Assert.class */
public class Assert {
    protected Assert() {
    }

    public static void assertTrue(String message, boolean condition) {
        if (!condition) {
            fail(message);
        }
    }

    public static void assertTrue(boolean condition) {
        assertTrue(null, condition);
    }

    public static void assertFalse(String message, boolean condition) {
        assertTrue(message, !condition);
    }

    public static void assertFalse(boolean condition) {
        assertFalse(null, condition);
    }

    public static void fail(String message) {
        if (message == null) {
            throw new AssertionError();
        }
        throw new AssertionError(message);
    }

    public static void fail() {
        fail(null);
    }

    public static void assertEquals(String message, Object expected, Object actual) {
        if (equalsRegardingNull(expected, actual)) {
            return;
        }
        if ((expected instanceof String) && (actual instanceof String)) {
            String cleanMessage = message == null ? "" : message;
            throw new ComparisonFailure(cleanMessage, (String) expected, (String) actual);
        } else {
            failNotEquals(message, expected, actual);
        }
    }

    private static boolean equalsRegardingNull(Object expected, Object actual) {
        if (expected == null) {
            return actual == null;
        }
        return isEquals(expected, actual);
    }

    private static boolean isEquals(Object expected, Object actual) {
        return expected.equals(actual);
    }

    public static void assertEquals(Object expected, Object actual) {
        assertEquals((String) null, expected, actual);
    }

    public static void assertNotEquals(String message, Object unexpected, Object actual) {
        if (equalsRegardingNull(unexpected, actual)) {
            failEquals(message, actual);
        }
    }

    public static void assertNotEquals(Object unexpected, Object actual) {
        assertNotEquals((String) null, unexpected, actual);
    }

    private static void failEquals(String message, Object actual) {
        String formatted = "Values should be different. ";
        if (message != null) {
            formatted = message + ". ";
        }
        fail(formatted + "Actual: " + actual);
    }

    public static void assertNotEquals(String message, long unexpected, long actual) {
        if (unexpected == actual) {
            failEquals(message, Long.valueOf(actual));
        }
    }

    public static void assertNotEquals(long unexpected, long actual) {
        assertNotEquals((String) null, unexpected, actual);
    }

    public static void assertNotEquals(String message, double unexpected, double actual, double delta) {
        if (!doubleIsDifferent(unexpected, actual, delta)) {
            failEquals(message, Double.valueOf(actual));
        }
    }

    public static void assertNotEquals(double unexpected, double actual, double delta) {
        assertNotEquals((String) null, unexpected, actual, delta);
    }

    public static void assertNotEquals(float unexpected, float actual, float delta) {
        assertNotEquals((String) null, unexpected, actual, delta);
    }

    public static void assertArrayEquals(String message, Object[] expecteds, Object[] actuals) throws ArrayComparisonFailure {
        internalArrayEquals(message, expecteds, actuals);
    }

    public static void assertArrayEquals(Object[] expecteds, Object[] actuals) {
        assertArrayEquals((String) null, expecteds, actuals);
    }

    public static void assertArrayEquals(String message, boolean[] expecteds, boolean[] actuals) throws ArrayComparisonFailure {
        internalArrayEquals(message, expecteds, actuals);
    }

    public static void assertArrayEquals(boolean[] expecteds, boolean[] actuals) {
        assertArrayEquals((String) null, expecteds, actuals);
    }

    public static void assertArrayEquals(String message, byte[] expecteds, byte[] actuals) throws ArrayComparisonFailure {
        internalArrayEquals(message, expecteds, actuals);
    }

    public static void assertArrayEquals(byte[] expecteds, byte[] actuals) {
        assertArrayEquals((String) null, expecteds, actuals);
    }

    public static void assertArrayEquals(String message, char[] expecteds, char[] actuals) throws ArrayComparisonFailure {
        internalArrayEquals(message, expecteds, actuals);
    }

    public static void assertArrayEquals(char[] expecteds, char[] actuals) {
        assertArrayEquals((String) null, expecteds, actuals);
    }

    public static void assertArrayEquals(String message, short[] expecteds, short[] actuals) throws ArrayComparisonFailure {
        internalArrayEquals(message, expecteds, actuals);
    }

    public static void assertArrayEquals(short[] expecteds, short[] actuals) {
        assertArrayEquals((String) null, expecteds, actuals);
    }

    public static void assertArrayEquals(String message, int[] expecteds, int[] actuals) throws ArrayComparisonFailure {
        internalArrayEquals(message, expecteds, actuals);
    }

    public static void assertArrayEquals(int[] expecteds, int[] actuals) {
        assertArrayEquals((String) null, expecteds, actuals);
    }

    public static void assertArrayEquals(String message, long[] expecteds, long[] actuals) throws ArrayComparisonFailure {
        internalArrayEquals(message, expecteds, actuals);
    }

    public static void assertArrayEquals(long[] expecteds, long[] actuals) {
        assertArrayEquals((String) null, expecteds, actuals);
    }

    public static void assertArrayEquals(String message, double[] expecteds, double[] actuals, double delta) throws ArrayComparisonFailure {
        new InexactComparisonCriteria(delta).arrayEquals(message, expecteds, actuals);
    }

    public static void assertArrayEquals(double[] expecteds, double[] actuals, double delta) {
        assertArrayEquals((String) null, expecteds, actuals, delta);
    }

    public static void assertArrayEquals(String message, float[] expecteds, float[] actuals, float delta) throws ArrayComparisonFailure {
        new InexactComparisonCriteria(delta).arrayEquals(message, expecteds, actuals);
    }

    public static void assertArrayEquals(float[] expecteds, float[] actuals, float delta) {
        assertArrayEquals((String) null, expecteds, actuals, delta);
    }

    private static void internalArrayEquals(String message, Object expecteds, Object actuals) throws ArrayComparisonFailure {
        new ExactComparisonCriteria().arrayEquals(message, expecteds, actuals);
    }

    public static void assertEquals(String message, double expected, double actual, double delta) {
        if (doubleIsDifferent(expected, actual, delta)) {
            failNotEquals(message, Double.valueOf(expected), Double.valueOf(actual));
        }
    }

    public static void assertEquals(String message, float expected, float actual, float delta) {
        if (floatIsDifferent(expected, actual, delta)) {
            failNotEquals(message, Float.valueOf(expected), Float.valueOf(actual));
        }
    }

    public static void assertNotEquals(String message, float unexpected, float actual, float delta) {
        if (!floatIsDifferent(unexpected, actual, delta)) {
            failEquals(message, Float.valueOf(actual));
        }
    }

    private static boolean doubleIsDifferent(double d1, double d2, double delta) {
        if (Double.compare(d1, d2) == 0 || Math.abs(d1 - d2) <= delta) {
            return false;
        }
        return true;
    }

    private static boolean floatIsDifferent(float f1, float f2, float delta) {
        if (Float.compare(f1, f2) == 0 || Math.abs(f1 - f2) <= delta) {
            return false;
        }
        return true;
    }

    public static void assertEquals(long expected, long actual) {
        assertEquals((String) null, expected, actual);
    }

    public static void assertEquals(String message, long expected, long actual) {
        if (expected != actual) {
            failNotEquals(message, Long.valueOf(expected), Long.valueOf(actual));
        }
    }

    @Deprecated
    public static void assertEquals(double expected, double actual) {
        assertEquals((String) null, expected, actual);
    }

    @Deprecated
    public static void assertEquals(String message, double expected, double actual) {
        fail("Use assertEquals(expected, actual, delta) to compare floating-point numbers");
    }

    public static void assertEquals(double expected, double actual, double delta) {
        assertEquals((String) null, expected, actual, delta);
    }

    public static void assertEquals(float expected, float actual, float delta) {
        assertEquals((String) null, expected, actual, delta);
    }

    public static void assertNotNull(String message, Object object) {
        assertTrue(message, object != null);
    }

    public static void assertNotNull(Object object) {
        assertNotNull(null, object);
    }

    public static void assertNull(String message, Object object) {
        if (object == null) {
            return;
        }
        failNotNull(message, object);
    }

    public static void assertNull(Object object) {
        assertNull(null, object);
    }

    private static void failNotNull(String message, Object actual) {
        String formatted = "";
        if (message != null) {
            formatted = message + Instruction.argsep;
        }
        fail(formatted + "expected null, but was:<" + actual + ">");
    }

    public static void assertSame(String message, Object expected, Object actual) {
        if (expected == actual) {
            return;
        }
        failNotSame(message, expected, actual);
    }

    public static void assertSame(Object expected, Object actual) {
        assertSame(null, expected, actual);
    }

    public static void assertNotSame(String message, Object unexpected, Object actual) {
        if (unexpected == actual) {
            failSame(message);
        }
    }

    public static void assertNotSame(Object unexpected, Object actual) {
        assertNotSame(null, unexpected, actual);
    }

    private static void failSame(String message) {
        String formatted = "";
        if (message != null) {
            formatted = message + Instruction.argsep;
        }
        fail(formatted + "expected not same");
    }

    private static void failNotSame(String message, Object expected, Object actual) {
        String formatted = "";
        if (message != null) {
            formatted = message + Instruction.argsep;
        }
        fail(formatted + "expected same:<" + expected + "> was not:<" + actual + ">");
    }

    private static void failNotEquals(String message, Object expected, Object actual) {
        fail(format(message, expected, actual));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String format(String message, Object expected, Object actual) {
        String formatted = "";
        if (message != null && !"".equals(message)) {
            formatted = message + Instruction.argsep;
        }
        String expectedString = String.valueOf(expected);
        String actualString = String.valueOf(actual);
        if (equalsRegardingNull(expectedString, actualString)) {
            return formatted + "expected: " + formatClassAndValue(expected, expectedString) + " but was: " + formatClassAndValue(actual, actualString);
        }
        return formatted + "expected:<" + expectedString + "> but was:<" + actualString + ">";
    }

    private static String formatClass(Class<?> value) {
        String className = value.getCanonicalName();
        return className == null ? value.getName() : className;
    }

    private static String formatClassAndValue(Object value, String valueString) {
        String className = value == null ? Jimple.NULL : value.getClass().getName();
        return className + "<" + valueString + ">";
    }

    @Deprecated
    public static void assertEquals(String message, Object[] expecteds, Object[] actuals) {
        assertArrayEquals(message, expecteds, actuals);
    }

    @Deprecated
    public static void assertEquals(Object[] expecteds, Object[] actuals) {
        assertArrayEquals(expecteds, actuals);
    }

    @Deprecated
    public static <T> void assertThat(T actual, Matcher<? super T> matcher) {
        assertThat("", actual, matcher);
    }

    @Deprecated
    public static <T> void assertThat(String reason, T actual, Matcher<? super T> matcher) {
        MatcherAssert.assertThat(reason, actual, matcher);
    }

    public static <T extends Throwable> T assertThrows(Class<T> expectedThrowable, ThrowingRunnable runnable) {
        return (T) assertThrows(null, expectedThrowable, runnable);
    }

    public static <T extends Throwable> T assertThrows(String message, Class<T> expectedThrowable, ThrowingRunnable runnable) {
        Class<?> cls;
        try {
            runnable.run();
            String notThrownMessage = buildPrefix(message) + String.format("expected %s to be thrown, but nothing was thrown", formatClass(expectedThrowable));
            throw new AssertionError(notThrownMessage);
        } catch (Throwable t) {
            t = (T) th;
            if (expectedThrowable.isInstance(t)) {
                return t;
            }
            String expected = formatClass(expectedThrowable);
            String actual = formatClass(t.getClass());
            if (expected.equals(actual)) {
                expected = expected + "@" + Integer.toHexString(System.identityHashCode(expectedThrowable));
                actual = actual + "@" + Integer.toHexString(System.identityHashCode(cls));
            }
            String mismatchMessage = buildPrefix(message) + format("unexpected exception type thrown;", expected, actual);
            AssertionError assertionError = new AssertionError(mismatchMessage);
            assertionError.initCause(t);
            throw assertionError;
        }
    }

    private static String buildPrefix(String message) {
        return (message == null || message.length() == 0) ? "" : message + ": ";
    }
}
