package org.mockito.internal.invocation;

import java.io.Serializable;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Location;
import org.mockito.invocation.StubInfo;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/invocation/StubInfoImpl.class */
public class StubInfoImpl implements StubInfo, Serializable {
    private static final long serialVersionUID = 2125827349332068867L;
    private final DescribedInvocation stubbedAt;

    public StubInfoImpl(DescribedInvocation stubbedAt) {
        this.stubbedAt = stubbedAt;
    }

    @Override // org.mockito.invocation.StubInfo
    public Location stubbedAt() {
        return this.stubbedAt.getLocation();
    }
}
