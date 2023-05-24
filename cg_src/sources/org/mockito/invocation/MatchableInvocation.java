package org.mockito.invocation;

import java.util.List;
import org.mockito.ArgumentMatcher;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/invocation/MatchableInvocation.class */
public interface MatchableInvocation extends DescribedInvocation {
    Invocation getInvocation();

    List<ArgumentMatcher> getMatchers();

    boolean matches(Invocation invocation);

    boolean hasSimilarMethod(Invocation invocation);

    boolean hasSameMethod(Invocation invocation);

    void captureArgumentsFrom(Invocation invocation);
}
