package org.mockito.internal.matchers;

import java.io.Serializable;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.text.ValuePrinter;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/matchers/Equals.class */
public class Equals implements ArgumentMatcher<Object>, ContainsExtraTypeInfo, Serializable {
    private final Object wanted;

    public Equals(Object wanted) {
        this.wanted = wanted;
    }

    @Override // org.mockito.ArgumentMatcher
    public boolean matches(Object actual) {
        return Equality.areEqual(this.wanted, actual);
    }

    public String toString() {
        return describe(this.wanted);
    }

    private String describe(Object object) {
        return ValuePrinter.print(object);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final Object getWanted() {
        return this.wanted;
    }

    public boolean equals(Object o) {
        if (o == null || !getClass().equals(o.getClass())) {
            return false;
        }
        Equals other = (Equals) o;
        return (this.wanted == null && other.wanted == null) || (this.wanted != null && this.wanted.equals(other.wanted));
    }

    public int hashCode() {
        return 1;
    }

    @Override // org.mockito.internal.matchers.ContainsExtraTypeInfo
    public String toStringWithType() {
        return "(" + this.wanted.getClass().getSimpleName() + ") " + describe(this.wanted);
    }

    @Override // org.mockito.internal.matchers.ContainsExtraTypeInfo
    public boolean typeMatches(Object target) {
        return (this.wanted == null || target == null || target.getClass() != this.wanted.getClass()) ? false : true;
    }
}
