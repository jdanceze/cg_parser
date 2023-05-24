package org.junit;

import org.hamcrest.Matcher;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/AssumptionViolatedException.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/AssumptionViolatedException.class */
public class AssumptionViolatedException extends org.junit.internal.AssumptionViolatedException {
    private static final long serialVersionUID = 1;

    public <T> AssumptionViolatedException(T actual, Matcher<T> matcher) {
        super((Object) actual, (Matcher<?>) matcher);
    }

    public <T> AssumptionViolatedException(String message, T expected, Matcher<T> matcher) {
        super(message, expected, matcher);
    }

    public AssumptionViolatedException(String message) {
        super(message);
    }

    public AssumptionViolatedException(String message, Throwable t) {
        super(message, t);
    }
}
