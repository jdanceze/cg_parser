package org.mockito.internal.matchers;

import java.io.Serializable;
import org.mockito.ArgumentMatcher;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/matchers/And.class */
public class And implements ArgumentMatcher<Object>, Serializable {
    private ArgumentMatcher m1;
    private ArgumentMatcher m2;

    public And(ArgumentMatcher<?> m1, ArgumentMatcher<?> m2) {
        this.m1 = m1;
        this.m2 = m2;
    }

    @Override // org.mockito.ArgumentMatcher
    public boolean matches(Object actual) {
        return this.m1.matches(actual) && this.m2.matches(actual);
    }

    public String toString() {
        return "and(" + this.m1 + ", " + this.m2 + ")";
    }
}
