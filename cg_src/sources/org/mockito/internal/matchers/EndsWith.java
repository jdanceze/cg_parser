package org.mockito.internal.matchers;

import java.io.Serializable;
import org.mockito.ArgumentMatcher;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/matchers/EndsWith.class */
public class EndsWith implements ArgumentMatcher<String>, Serializable {
    private final String suffix;

    public EndsWith(String suffix) {
        this.suffix = suffix;
    }

    @Override // org.mockito.ArgumentMatcher
    public boolean matches(String actual) {
        return actual != null && actual.endsWith(this.suffix);
    }

    public String toString() {
        return "endsWith(\"" + this.suffix + "\")";
    }
}
