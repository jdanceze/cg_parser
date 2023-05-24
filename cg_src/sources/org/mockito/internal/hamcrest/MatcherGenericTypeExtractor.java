package org.mockito.internal.hamcrest;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Matcher;
import org.mockito.internal.util.reflection.GenericTypeExtractor;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/hamcrest/MatcherGenericTypeExtractor.class */
public class MatcherGenericTypeExtractor {
    public static Class<?> genericTypeOfMatcher(Class<?> matcherClass) {
        return GenericTypeExtractor.genericTypeOf(matcherClass, BaseMatcher.class, Matcher.class);
    }
}
