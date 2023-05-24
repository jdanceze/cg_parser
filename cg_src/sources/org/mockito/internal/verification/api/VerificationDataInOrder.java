package org.mockito.internal.verification.api;

import java.util.List;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/verification/api/VerificationDataInOrder.class */
public interface VerificationDataInOrder {
    List<Invocation> getAllInvocations();

    MatchableInvocation getWanted();

    InOrderContext getOrderingContext();
}
