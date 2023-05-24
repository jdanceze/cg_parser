package org.mockito.internal.matchers;

import java.io.Serializable;
import org.mockito.ArgumentMatcher;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/matchers/Not.class */
public class Not implements ArgumentMatcher<Object>, Serializable {
    private final ArgumentMatcher matcher;

    public Not(ArgumentMatcher<?> matcher) {
        this.matcher = matcher;
    }

    @Override // org.mockito.ArgumentMatcher
    public boolean matches(Object actual) {
        return !this.matcher.matches(actual);
    }

    public String toString() {
        return "not(" + this.matcher + ")";
    }
}
