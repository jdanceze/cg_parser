package net.bytebuddy.matcher;

import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.matcher.ElementMatcher;
@HashCodeAndEqualsPlugin.Enhance
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/matcher/BooleanMatcher.class */
public class BooleanMatcher<T> extends ElementMatcher.Junction.AbstractBase<T> {
    private final boolean matches;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.matches == ((BooleanMatcher) obj).matches;
    }

    public int hashCode() {
        return (17 * 31) + (this.matches ? 1 : 0);
    }

    public BooleanMatcher(boolean matches) {
        this.matches = matches;
    }

    @Override // net.bytebuddy.matcher.ElementMatcher
    public boolean matches(T target) {
        return this.matches;
    }

    public String toString() {
        return Boolean.toString(this.matches);
    }
}
