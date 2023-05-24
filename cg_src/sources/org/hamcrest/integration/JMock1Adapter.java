package org.hamcrest.integration;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.jmock.core.Constraint;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/integration/JMock1Adapter.class */
public class JMock1Adapter implements Constraint {
    private final Matcher<?> hamcrestMatcher;

    public static Constraint adapt(Matcher<?> matcher) {
        return new JMock1Adapter(matcher);
    }

    public JMock1Adapter(Matcher<?> matcher) {
        this.hamcrestMatcher = matcher;
    }

    public boolean eval(Object o) {
        return this.hamcrestMatcher.matches(o);
    }

    public StringBuffer describeTo(StringBuffer buffer) {
        this.hamcrestMatcher.describeTo(new StringDescription(buffer));
        return buffer;
    }
}
