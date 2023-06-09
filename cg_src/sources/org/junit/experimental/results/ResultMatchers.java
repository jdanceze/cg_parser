package org.junit.experimental.results;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/experimental/results/ResultMatchers.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/experimental/results/ResultMatchers.class */
public class ResultMatchers {
    public static Matcher<PrintableResult> isSuccessful() {
        return failureCountIs(0);
    }

    public static Matcher<PrintableResult> failureCountIs(final int count) {
        return new TypeSafeMatcher<PrintableResult>() { // from class: org.junit.experimental.results.ResultMatchers.1
            @Override // org.hamcrest.SelfDescribing
            public void describeTo(Description description) {
                description.appendText("has " + count + " failures");
            }

            @Override // org.hamcrest.TypeSafeMatcher
            public boolean matchesSafely(PrintableResult item) {
                return item.failureCount() == count;
            }
        };
    }

    public static Matcher<Object> hasSingleFailureContaining(final String string) {
        return new BaseMatcher<Object>() { // from class: org.junit.experimental.results.ResultMatchers.2
            @Override // org.hamcrest.Matcher
            public boolean matches(Object item) {
                return item.toString().contains(string) && ResultMatchers.failureCountIs(1).matches(item);
            }

            @Override // org.hamcrest.SelfDescribing
            public void describeTo(Description description) {
                description.appendText("has single failure containing " + string);
            }
        };
    }

    public static Matcher<PrintableResult> hasSingleFailureMatching(final Matcher<Throwable> matcher) {
        return new TypeSafeMatcher<PrintableResult>() { // from class: org.junit.experimental.results.ResultMatchers.3
            @Override // org.hamcrest.TypeSafeMatcher
            public boolean matchesSafely(PrintableResult item) {
                return item.failureCount() == 1 && Matcher.this.matches(item.failures().get(0).getException());
            }

            @Override // org.hamcrest.SelfDescribing
            public void describeTo(Description description) {
                description.appendText("has failure with exception matching ");
                Matcher.this.describeTo(description);
            }
        };
    }

    public static Matcher<PrintableResult> hasFailureContaining(final String string) {
        return new TypeSafeMatcher<PrintableResult>() { // from class: org.junit.experimental.results.ResultMatchers.4
            @Override // org.hamcrest.TypeSafeMatcher
            public boolean matchesSafely(PrintableResult item) {
                return item.failureCount() > 0 && item.toString().contains(string);
            }

            @Override // org.hamcrest.SelfDescribing
            public void describeTo(Description description) {
                description.appendText("has failure containing " + string);
            }
        };
    }
}
