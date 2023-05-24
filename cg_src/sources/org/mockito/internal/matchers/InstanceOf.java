package org.mockito.internal.matchers;

import java.io.Serializable;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.util.Primitives;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/matchers/InstanceOf.class */
public class InstanceOf implements ArgumentMatcher<Object>, Serializable {
    private final Class<?> clazz;
    private String description;

    public InstanceOf(Class<?> clazz) {
        this(clazz, "isA(" + clazz.getCanonicalName() + ")");
    }

    public InstanceOf(Class<?> clazz, String describedAs) {
        this.clazz = clazz;
        this.description = describedAs;
    }

    @Override // org.mockito.ArgumentMatcher
    public boolean matches(Object actual) {
        return actual != null && (Primitives.isAssignableFromWrapper(actual.getClass(), this.clazz) || this.clazz.isAssignableFrom(actual.getClass()));
    }

    public String toString() {
        return this.description;
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/matchers/InstanceOf$VarArgAware.class */
    public static class VarArgAware extends InstanceOf implements VarargMatcher {
        public VarArgAware(Class<?> clazz) {
            super(clazz);
        }

        public VarArgAware(Class<?> clazz, String describedAs) {
            super(clazz, describedAs);
        }
    }
}
