package org.mockito.hamcrest;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import org.hamcrest.Matcher;
import org.mockito.internal.hamcrest.HamcrestArgumentMatcher;
import org.mockito.internal.hamcrest.MatcherGenericTypeExtractor;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.util.Primitives;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/hamcrest/MockitoHamcrest.class */
public class MockitoHamcrest {
    public static <T> T argThat(Matcher<T> matcher) {
        reportMatcher(matcher);
        return (T) Primitives.defaultValue(MatcherGenericTypeExtractor.genericTypeOfMatcher(matcher.getClass()));
    }

    public static char charThat(Matcher<Character> matcher) {
        reportMatcher(matcher);
        return (char) 0;
    }

    public static boolean booleanThat(Matcher<Boolean> matcher) {
        reportMatcher(matcher);
        return false;
    }

    public static byte byteThat(Matcher<Byte> matcher) {
        reportMatcher(matcher);
        return (byte) 0;
    }

    public static short shortThat(Matcher<Short> matcher) {
        reportMatcher(matcher);
        return (short) 0;
    }

    public static int intThat(Matcher<Integer> matcher) {
        reportMatcher(matcher);
        return 0;
    }

    public static long longThat(Matcher<Long> matcher) {
        reportMatcher(matcher);
        return 0L;
    }

    public static float floatThat(Matcher<Float> matcher) {
        reportMatcher(matcher);
        return 0.0f;
    }

    public static double doubleThat(Matcher<Double> matcher) {
        reportMatcher(matcher);
        return Const.default_value_double;
    }

    private static <T> void reportMatcher(Matcher<T> matcher) {
        ThreadSafeMockingProgress.mockingProgress().getArgumentMatcherStorage().reportMatcher(new HamcrestArgumentMatcher(matcher));
    }
}
