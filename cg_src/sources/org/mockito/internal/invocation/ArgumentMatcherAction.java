package org.mockito.internal.invocation;

import org.mockito.ArgumentMatcher;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/invocation/ArgumentMatcherAction.class */
public interface ArgumentMatcherAction {
    boolean apply(ArgumentMatcher<?> argumentMatcher, Object obj);
}
