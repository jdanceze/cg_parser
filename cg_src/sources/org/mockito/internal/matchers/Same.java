package org.mockito.internal.matchers;

import java.io.Serializable;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.text.ValuePrinter;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/matchers/Same.class */
public class Same implements ArgumentMatcher<Object>, Serializable {
    private final Object wanted;

    public Same(Object wanted) {
        this.wanted = wanted;
    }

    @Override // org.mockito.ArgumentMatcher
    public boolean matches(Object actual) {
        return this.wanted == actual;
    }

    public String toString() {
        return "same(" + ValuePrinter.print(this.wanted) + ")";
    }
}
