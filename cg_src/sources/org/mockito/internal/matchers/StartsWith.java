package org.mockito.internal.matchers;

import java.io.Serializable;
import org.mockito.ArgumentMatcher;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/matchers/StartsWith.class */
public class StartsWith implements ArgumentMatcher<String>, Serializable {
    private final String prefix;

    public StartsWith(String prefix) {
        this.prefix = prefix;
    }

    @Override // org.mockito.ArgumentMatcher
    public boolean matches(String actual) {
        return actual != null && actual.startsWith(this.prefix);
    }

    public String toString() {
        return "startsWith(\"" + this.prefix + "\")";
    }
}
