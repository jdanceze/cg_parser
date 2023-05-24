package org.mockito.internal.verification.api;

import java.util.List;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/verification/api/VerificationData.class */
public interface VerificationData {
    List<Invocation> getAllInvocations();

    MatchableInvocation getTarget();

    @Deprecated
    InvocationMatcher getWanted();
}
