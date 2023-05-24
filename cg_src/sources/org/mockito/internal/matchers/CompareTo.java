package org.mockito.internal.matchers;

import java.io.Serializable;
import java.lang.Comparable;
import org.mockito.ArgumentMatcher;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/matchers/CompareTo.class */
public abstract class CompareTo<T extends Comparable<T>> implements ArgumentMatcher<T>, Serializable {
    private final T wanted;

    protected abstract String getName();

    protected abstract boolean matchResult(int i);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.mockito.ArgumentMatcher
    public /* bridge */ /* synthetic */ boolean matches(Object obj) {
        return matches((CompareTo<T>) ((Comparable) obj));
    }

    public CompareTo(T value) {
        this.wanted = value;
    }

    public final boolean matches(T actual) {
        if (actual == null || !actual.getClass().isInstance(this.wanted)) {
            return false;
        }
        int result = actual.compareTo(this.wanted);
        return matchResult(result);
    }

    public final String toString() {
        return getName() + "(" + this.wanted + ")";
    }
}
