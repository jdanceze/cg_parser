package org.mockito.internal.matchers;

import java.io.Serializable;
import org.mockito.ArgumentMatcher;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/matchers/EqualsWithDelta.class */
public class EqualsWithDelta implements ArgumentMatcher<Number>, Serializable {
    private final Number wanted;
    private final Number delta;

    public EqualsWithDelta(Number value, Number delta) {
        this.wanted = value;
        this.delta = delta;
    }

    @Override // org.mockito.ArgumentMatcher
    public boolean matches(Number actual) {
        if ((this.wanted == null) ^ (actual == null)) {
            return false;
        }
        if (this.wanted == actual) {
            return true;
        }
        return this.wanted.doubleValue() - this.delta.doubleValue() <= actual.doubleValue() && actual.doubleValue() <= this.wanted.doubleValue() + this.delta.doubleValue();
    }

    public String toString() {
        return "eq(" + this.wanted + ", " + this.delta + ")";
    }
}
