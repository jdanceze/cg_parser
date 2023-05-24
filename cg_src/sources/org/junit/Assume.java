package org.junit;

import java.util.Arrays;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/Assume.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/Assume.class */
public class Assume {
    public static void assumeTrue(boolean b) {
        assumeThat(Boolean.valueOf(b), CoreMatchers.is(true));
    }

    public static void assumeFalse(boolean b) {
        assumeThat(Boolean.valueOf(b), CoreMatchers.is(false));
    }

    public static void assumeTrue(String message, boolean b) {
        if (!b) {
            throw new AssumptionViolatedException(message);
        }
    }

    public static void assumeFalse(String message, boolean b) {
        assumeTrue(message, !b);
    }

    public static void assumeNotNull(Object... objects) {
        assumeThat(objects, CoreMatchers.notNullValue());
        assumeThat(Arrays.asList(objects), CoreMatchers.everyItem(CoreMatchers.notNullValue()));
    }

    public static <T> void assumeThat(T actual, Matcher<T> matcher) {
        if (!matcher.matches(actual)) {
            throw new AssumptionViolatedException(actual, matcher);
        }
    }

    public static <T> void assumeThat(String message, T actual, Matcher<T> matcher) {
        if (!matcher.matches(actual)) {
            throw new AssumptionViolatedException(message, actual, matcher);
        }
    }

    public static void assumeNoException(Throwable e) {
        assumeThat(e, CoreMatchers.nullValue());
    }

    public static void assumeNoException(String message, Throwable e) {
        assumeThat(message, e, CoreMatchers.nullValue());
    }
}
