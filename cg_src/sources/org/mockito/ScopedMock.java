package org.mockito;
@Incubating
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/ScopedMock.class */
public interface ScopedMock extends AutoCloseable {
    boolean isClosed();

    @Override // java.lang.AutoCloseable
    void close();

    void closeOnDemand();
}
