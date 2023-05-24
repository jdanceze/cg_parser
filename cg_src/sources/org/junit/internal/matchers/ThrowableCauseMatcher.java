package org.junit.internal.matchers;

import java.lang.Throwable;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/matchers/ThrowableCauseMatcher.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/matchers/ThrowableCauseMatcher.class */
public class ThrowableCauseMatcher<T extends Throwable> extends org.hamcrest.TypeSafeMatcher<T> {
    private final Matcher<?> causeMatcher;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.hamcrest.TypeSafeMatcher
    protected /* bridge */ /* synthetic */ void describeMismatchSafely(Object x0, Description x1) {
        describeMismatchSafely((ThrowableCauseMatcher<T>) ((Throwable) x0), x1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.hamcrest.TypeSafeMatcher
    protected /* bridge */ /* synthetic */ boolean matchesSafely(Object x0) {
        return matchesSafely((ThrowableCauseMatcher<T>) ((Throwable) x0));
    }

    public ThrowableCauseMatcher(Matcher<?> causeMatcher) {
        this.causeMatcher = causeMatcher;
    }

    @Override // org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        description.appendText("exception with cause ");
        description.appendDescriptionOf(this.causeMatcher);
    }

    protected boolean matchesSafely(T item) {
        return this.causeMatcher.matches(item.getCause());
    }

    protected void describeMismatchSafely(T item, Description description) {
        description.appendText("cause ");
        this.causeMatcher.describeMismatch(item.getCause(), description);
    }

    @Factory
    public static <T extends Throwable> Matcher<T> hasCause(Matcher<?> matcher) {
        return new ThrowableCauseMatcher(matcher);
    }
}
