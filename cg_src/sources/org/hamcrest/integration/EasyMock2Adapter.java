package org.hamcrest.integration;

import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/integration/EasyMock2Adapter.class */
public class EasyMock2Adapter implements IArgumentMatcher {
    private final Matcher<?> hamcrestMatcher;

    public static IArgumentMatcher adapt(Matcher<?> matcher) {
        EasyMock2Adapter easyMock2Matcher = new EasyMock2Adapter(matcher);
        EasyMock.reportMatcher(easyMock2Matcher);
        return easyMock2Matcher;
    }

    public EasyMock2Adapter(Matcher<?> matcher) {
        this.hamcrestMatcher = matcher;
    }

    public boolean matches(Object argument) {
        return this.hamcrestMatcher.matches(argument);
    }

    public void appendTo(StringBuffer buffer) {
        this.hamcrestMatcher.describeTo(new StringDescription(buffer));
    }
}
