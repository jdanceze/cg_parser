package org.mockito.internal.invocation;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.matchers.LocalizedMatcher;
import org.mockito.internal.progress.ArgumentMatcherStorage;
import org.mockito.invocation.Invocation;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/invocation/MatchersBinder.class */
public class MatchersBinder implements Serializable {
    public InvocationMatcher bindMatchers(ArgumentMatcherStorage argumentMatcherStorage, Invocation invocation) {
        List<LocalizedMatcher> lastMatchers = argumentMatcherStorage.pullLocalizedMatchers();
        validateMatchers(invocation, lastMatchers);
        List<ArgumentMatcher> matchers = new LinkedList<>();
        for (LocalizedMatcher m : lastMatchers) {
            matchers.add(m.getMatcher());
        }
        return new InvocationMatcher(invocation, matchers);
    }

    private void validateMatchers(Invocation invocation, List<LocalizedMatcher> lastMatchers) {
        if (!lastMatchers.isEmpty()) {
            int recordedMatchersSize = lastMatchers.size();
            int expectedMatchersSize = invocation.getArguments().length;
            if (expectedMatchersSize != recordedMatchersSize) {
                throw Reporter.invalidUseOfMatchers(expectedMatchersSize, lastMatchers);
            }
        }
    }
}
