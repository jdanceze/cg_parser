package org.mockito.internal.matchers;

import java.io.Serializable;
import org.mockito.ArgumentMatcher;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/matchers/Contains.class */
public class Contains implements ArgumentMatcher<String>, Serializable {
    private final String substring;

    public Contains(String substring) {
        this.substring = substring;
    }

    @Override // org.mockito.ArgumentMatcher
    public boolean matches(String actual) {
        return actual != null && actual.contains(this.substring);
    }

    public String toString() {
        return "contains(\"" + this.substring + "\")";
    }
}
