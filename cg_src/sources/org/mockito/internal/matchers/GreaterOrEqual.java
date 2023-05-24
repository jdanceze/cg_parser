package org.mockito.internal.matchers;

import java.io.Serializable;
import java.lang.Comparable;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/matchers/GreaterOrEqual.class */
public class GreaterOrEqual<T extends Comparable<T>> extends CompareTo<T> implements Serializable {
    public GreaterOrEqual(T value) {
        super(value);
    }

    @Override // org.mockito.internal.matchers.CompareTo
    protected String getName() {
        return "geq";
    }

    @Override // org.mockito.internal.matchers.CompareTo
    protected boolean matchResult(int result) {
        return result >= 0;
    }
}
