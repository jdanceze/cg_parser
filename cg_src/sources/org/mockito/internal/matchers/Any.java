package org.mockito.internal.matchers;

import java.io.Serializable;
import org.mockito.ArgumentMatcher;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/matchers/Any.class */
public class Any implements ArgumentMatcher<Object>, VarargMatcher, Serializable {
    public static final Any ANY = new Any();

    @Override // org.mockito.ArgumentMatcher
    public boolean matches(Object actual) {
        return true;
    }

    public String toString() {
        return "<any>";
    }
}
