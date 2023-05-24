package org.mockito.internal.matchers;

import java.io.Serializable;
import java.lang.Comparable;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/matchers/LessThan.class */
public class LessThan<T extends Comparable<T>> extends CompareTo<T> implements Serializable {
    public LessThan(T value) {
        super(value);
    }

    @Override // org.mockito.internal.matchers.CompareTo
    protected String getName() {
        return "lt";
    }

    @Override // org.mockito.internal.matchers.CompareTo
    protected boolean matchResult(int result) {
        return result < 0;
    }
}
