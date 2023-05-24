package org.mockito.internal.exceptions;

import org.mockito.invocation.DescribedInvocation;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/exceptions/VerificationAwareInvocation.class */
public interface VerificationAwareInvocation extends DescribedInvocation {
    boolean isVerified();
}
