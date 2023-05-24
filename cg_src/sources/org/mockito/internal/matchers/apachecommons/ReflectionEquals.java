package org.mockito.internal.matchers.apachecommons;

import java.io.Serializable;
import org.mockito.ArgumentMatcher;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/matchers/apachecommons/ReflectionEquals.class */
public class ReflectionEquals implements ArgumentMatcher<Object>, Serializable {
    private final Object wanted;
    private final String[] excludeFields;

    public ReflectionEquals(Object wanted, String... excludeFields) {
        this.wanted = wanted;
        this.excludeFields = excludeFields;
    }

    @Override // org.mockito.ArgumentMatcher
    public boolean matches(Object actual) {
        return EqualsBuilder.reflectionEquals(this.wanted, actual, this.excludeFields);
    }

    public String toString() {
        return "refEq(" + this.wanted + ")";
    }
}
