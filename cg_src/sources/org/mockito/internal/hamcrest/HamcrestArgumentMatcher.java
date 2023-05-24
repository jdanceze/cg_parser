package org.mockito.internal.hamcrest;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.VarargMatcher;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/hamcrest/HamcrestArgumentMatcher.class */
public class HamcrestArgumentMatcher<T> implements ArgumentMatcher<T> {
    private final Matcher matcher;

    public HamcrestArgumentMatcher(Matcher<T> matcher) {
        this.matcher = matcher;
    }

    @Override // org.mockito.ArgumentMatcher
    public boolean matches(Object argument) {
        return this.matcher.matches(argument);
    }

    public boolean isVarargMatcher() {
        return this.matcher instanceof VarargMatcher;
    }

    public String toString() {
        return StringDescription.toString(this.matcher);
    }
}
