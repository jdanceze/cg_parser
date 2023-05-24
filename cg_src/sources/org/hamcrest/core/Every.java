package org.hamcrest.core;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/core/Every.class
  gencallgraphv3.jar:hamcrest-core-1.3.jar:org/hamcrest/core/Every.class
 */
/* loaded from: gencallgraphv3.jar:org.hamcrest.core_1.3.0.v20180420-1519.jar:org/hamcrest/core/Every.class */
public class Every<T> extends TypeSafeDiagnosingMatcher<Iterable<T>> {
    private final Matcher<? super T> matcher;

    @Override // org.hamcrest.TypeSafeDiagnosingMatcher
    public /* bridge */ /* synthetic */ boolean matchesSafely(Object x0, Description x1) {
        return matchesSafely((Iterable) ((Iterable) x0), x1);
    }

    public Every(Matcher<? super T> matcher) {
        this.matcher = matcher;
    }

    public boolean matchesSafely(Iterable<T> collection, Description mismatchDescription) {
        for (T t : collection) {
            if (!this.matcher.matches(t)) {
                mismatchDescription.appendText("an item ");
                this.matcher.describeMismatch(t, mismatchDescription);
                return false;
            }
        }
        return true;
    }

    @Override // org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        description.appendText("every item is ").appendDescriptionOf(this.matcher);
    }

    @Factory
    public static <U> Matcher<Iterable<U>> everyItem(Matcher<U> itemMatcher) {
        return new Every(itemMatcher);
    }
}
