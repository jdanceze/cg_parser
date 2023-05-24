package org.hamcrest.core;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/core/Is.class
  gencallgraphv3.jar:hamcrest-core-1.3.jar:org/hamcrest/core/Is.class
 */
/* loaded from: gencallgraphv3.jar:org.hamcrest.core_1.3.0.v20180420-1519.jar:org/hamcrest/core/Is.class */
public class Is<T> extends BaseMatcher<T> {
    private final Matcher<T> matcher;

    public Is(Matcher<T> matcher) {
        this.matcher = matcher;
    }

    @Override // org.hamcrest.Matcher
    public boolean matches(Object arg) {
        return this.matcher.matches(arg);
    }

    @Override // org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        description.appendText("is ").appendDescriptionOf(this.matcher);
    }

    @Override // org.hamcrest.BaseMatcher, org.hamcrest.Matcher
    public void describeMismatch(Object item, Description mismatchDescription) {
        this.matcher.describeMismatch(item, mismatchDescription);
    }

    @Factory
    public static <T> Matcher<T> is(Matcher<T> matcher) {
        return new Is(matcher);
    }

    @Factory
    public static <T> Matcher<T> is(T value) {
        return is(IsEqual.equalTo(value));
    }

    @Factory
    @Deprecated
    public static <T> Matcher<T> is(Class<T> type) {
        Matcher<T> typeMatcher = IsInstanceOf.instanceOf(type);
        return is((Matcher) typeMatcher);
    }

    @Factory
    public static <T> Matcher<T> isA(Class<T> type) {
        Matcher<T> typeMatcher = IsInstanceOf.instanceOf(type);
        return is((Matcher) typeMatcher);
    }
}
