package org.junit.rules;

import java.util.ArrayList;
import java.util.List;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.matchers.JUnitMatchers;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/rules/ExpectedExceptionMatcherBuilder.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/rules/ExpectedExceptionMatcherBuilder.class */
class ExpectedExceptionMatcherBuilder {
    private final List<Matcher<?>> matchers = new ArrayList();

    /* JADX INFO: Access modifiers changed from: package-private */
    public void add(Matcher<?> matcher) {
        this.matchers.add(matcher);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean expectsThrowable() {
        return !this.matchers.isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Matcher<Throwable> build() {
        return JUnitMatchers.isThrowable(allOfTheMatchers());
    }

    private Matcher<Throwable> allOfTheMatchers() {
        if (this.matchers.size() == 1) {
            return cast(this.matchers.get(0));
        }
        return CoreMatchers.allOf(castedMatchers());
    }

    private List<Matcher<? super Throwable>> castedMatchers() {
        return new ArrayList(this.matchers);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private Matcher<Throwable> cast(Matcher<?> singleMatcher) {
        return singleMatcher;
    }
}
