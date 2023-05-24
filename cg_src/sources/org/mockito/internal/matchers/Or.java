package org.mockito.internal.matchers;

import java.io.Serializable;
import org.mockito.ArgumentMatcher;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/matchers/Or.class */
public class Or implements ArgumentMatcher<Object>, Serializable {
    private final ArgumentMatcher m1;
    private final ArgumentMatcher m2;

    public Or(ArgumentMatcher<?> m1, ArgumentMatcher<?> m2) {
        this.m1 = m1;
        this.m2 = m2;
    }

    @Override // org.mockito.ArgumentMatcher
    public boolean matches(Object actual) {
        return this.m1.matches(actual) || this.m2.matches(actual);
    }

    public String toString() {
        return "or(" + this.m1 + ", " + this.m2 + ")";
    }
}
