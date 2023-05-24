package org.mockito.internal.creation.bytebuddy;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/MockAccess.class */
public interface MockAccess {
    MockMethodInterceptor getMockitoInterceptor();

    void setMockitoInterceptor(MockMethodInterceptor mockMethodInterceptor);
}
