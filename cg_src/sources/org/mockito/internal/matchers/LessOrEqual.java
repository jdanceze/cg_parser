package org.mockito.internal.matchers;

import java.io.Serializable;
import java.lang.Comparable;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/matchers/LessOrEqual.class */
public class LessOrEqual<T extends Comparable<T>> extends CompareTo<T> implements Serializable {
    public LessOrEqual(T value) {
        super(value);
    }

    @Override // org.mockito.internal.matchers.CompareTo
    protected String getName() {
        return "leq";
    }

    @Override // org.mockito.internal.matchers.CompareTo
    protected boolean matchResult(int result) {
        return result <= 0;
    }
}
