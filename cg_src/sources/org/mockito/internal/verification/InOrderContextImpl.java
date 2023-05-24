package org.mockito.internal.verification;

import org.mockito.internal.util.collections.IdentitySet;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.invocation.Invocation;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/verification/InOrderContextImpl.class */
public class InOrderContextImpl implements InOrderContext {
    final IdentitySet verified = new IdentitySet();

    @Override // org.mockito.internal.verification.api.InOrderContext
    public boolean isVerified(Invocation invocation) {
        return this.verified.contains(invocation);
    }

    @Override // org.mockito.internal.verification.api.InOrderContext
    public void markVerified(Invocation i) {
        this.verified.add(i);
    }
}
