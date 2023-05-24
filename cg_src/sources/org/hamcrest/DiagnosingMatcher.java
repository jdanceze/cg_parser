package org.hamcrest;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/DiagnosingMatcher.class
  gencallgraphv3.jar:hamcrest-core-1.3.jar:org/hamcrest/DiagnosingMatcher.class
 */
/* loaded from: gencallgraphv3.jar:org.hamcrest.core_1.3.0.v20180420-1519.jar:org/hamcrest/DiagnosingMatcher.class */
public abstract class DiagnosingMatcher<T> extends BaseMatcher<T> {
    protected abstract boolean matches(Object obj, Description description);

    @Override // org.hamcrest.Matcher
    public final boolean matches(Object item) {
        return matches(item, Description.NONE);
    }

    @Override // org.hamcrest.BaseMatcher, org.hamcrest.Matcher
    public final void describeMismatch(Object item, Description mismatchDescription) {
        matches(item, mismatchDescription);
    }
}
