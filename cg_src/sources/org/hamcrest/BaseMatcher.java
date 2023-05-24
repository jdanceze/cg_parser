package org.hamcrest;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/BaseMatcher.class
  gencallgraphv3.jar:hamcrest-core-1.3.jar:org/hamcrest/BaseMatcher.class
 */
/* loaded from: gencallgraphv3.jar:org.hamcrest.core_1.3.0.v20180420-1519.jar:org/hamcrest/BaseMatcher.class */
public abstract class BaseMatcher<T> implements Matcher<T> {
    @Override // org.hamcrest.Matcher
    @Deprecated
    public final void _dont_implement_Matcher___instead_extend_BaseMatcher_() {
    }

    @Override // org.hamcrest.Matcher
    public void describeMismatch(Object item, Description description) {
        description.appendText("was ").appendValue(item);
    }

    public String toString() {
        return StringDescription.toString(this);
    }
}
