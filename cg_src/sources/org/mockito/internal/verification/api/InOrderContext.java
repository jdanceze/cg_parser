package org.mockito.internal.verification.api;

import org.mockito.invocation.Invocation;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/verification/api/InOrderContext.class */
public interface InOrderContext {
    boolean isVerified(Invocation invocation);

    void markVerified(Invocation invocation);
}
