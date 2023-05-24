package org.mockito.internal.matchers;

import java.io.Serializable;
import org.mockito.ArgumentMatcher;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/matchers/NotNull.class */
public class NotNull implements ArgumentMatcher<Object>, Serializable {
    public static final NotNull NOT_NULL = new NotNull();

    private NotNull() {
    }

    @Override // org.mockito.ArgumentMatcher
    public boolean matches(Object actual) {
        return actual != null;
    }

    public String toString() {
        return "notNull()";
    }
}
