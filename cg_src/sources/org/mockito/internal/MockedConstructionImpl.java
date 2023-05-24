package org.mockito.internal;

import java.util.Collections;
import java.util.List;
import org.mockito.MockedConstruction;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.debugging.LocationImpl;
import org.mockito.internal.util.StringUtil;
import org.mockito.invocation.Location;
import org.mockito.plugins.MockMaker;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/MockedConstructionImpl.class */
public final class MockedConstructionImpl<T> implements MockedConstruction<T> {
    private final MockMaker.ConstructionMockControl<T> control;
    private boolean closed;
    private final Location location = new LocationImpl();

    /* JADX INFO: Access modifiers changed from: protected */
    public MockedConstructionImpl(MockMaker.ConstructionMockControl<T> control) {
        this.control = control;
    }

    @Override // org.mockito.MockedConstruction
    public List<T> constructed() {
        return Collections.unmodifiableList(this.control.getMocks());
    }

    @Override // org.mockito.ScopedMock
    public boolean isClosed() {
        return this.closed;
    }

    @Override // org.mockito.ScopedMock, java.lang.AutoCloseable
    public void close() {
        assertNotClosed();
        this.closed = true;
        this.control.disable();
    }

    @Override // org.mockito.ScopedMock
    public void closeOnDemand() {
        if (!this.closed) {
            close();
        }
    }

    private void assertNotClosed() {
        if (this.closed) {
            throw new MockitoException(StringUtil.join("The static mock created at", this.location.toString(), "is already resolved and cannot longer be used"));
        }
    }
}
