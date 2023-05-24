package org.mockito.internal;

import org.mockito.MockedStatic;
import org.mockito.MockingDetails;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.debugging.LocationImpl;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.listeners.VerificationStartedNotifier;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.StringUtil;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.invocation.Location;
import org.mockito.invocation.MockHandler;
import org.mockito.plugins.MockMaker;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.verification.VerificationMode;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/MockedStaticImpl.class */
public final class MockedStaticImpl<T> implements MockedStatic<T> {
    private final MockMaker.StaticMockControl<T> control;
    private boolean closed;
    private final Location location = new LocationImpl();

    /* JADX INFO: Access modifiers changed from: protected */
    public MockedStaticImpl(MockMaker.StaticMockControl<T> control) {
        this.control = control;
    }

    @Override // org.mockito.MockedStatic
    public <S> OngoingStubbing<S> when(MockedStatic.Verification verification) {
        assertNotClosed();
        try {
            verification.apply();
        } catch (Throwable th) {
        }
        MockingProgress mockingProgress = ThreadSafeMockingProgress.mockingProgress();
        mockingProgress.stubbingStarted();
        OngoingStubbing<S> stubbing = (OngoingStubbing<S>) mockingProgress.pullOngoingStubbing();
        if (stubbing == null) {
            mockingProgress.reset();
            throw Reporter.missingMethodInvocation();
        }
        return stubbing;
    }

    @Override // org.mockito.MockedStatic
    public void verify(VerificationMode mode, MockedStatic.Verification verification) {
        assertNotClosed();
        MockingDetails mockingDetails = Mockito.mockingDetails(this.control.getType());
        MockHandler handler = mockingDetails.getMockHandler();
        VerificationStartedNotifier.notifyVerificationStarted(handler.getMockSettings().getVerificationStartedListeners(), mockingDetails);
        MockingProgress mockingProgress = ThreadSafeMockingProgress.mockingProgress();
        VerificationMode actualMode = mockingProgress.maybeVerifyLazily(mode);
        mockingProgress.verificationStarted(new MockAwareVerificationMode(this.control.getType(), actualMode, mockingProgress.verificationListeners()));
        try {
            verification.apply();
        } catch (MockitoAssertionError | MockitoException e) {
            throw e;
        } catch (Throwable t) {
            throw new MockitoException(StringUtil.join("An unexpected error occurred while verifying a static stub", "", "To correctly verify a stub, invoke a single static method of " + this.control.getType().getName() + " in the provided lambda.", "For example, if a method 'sample' was defined, provide a lambda or anonymous class containing the code", "", "() -> " + this.control.getType().getSimpleName() + ".sample()", "or", this.control.getType().getSimpleName() + "::sample"), t);
        }
    }

    @Override // org.mockito.MockedStatic
    public void reset() {
        assertNotClosed();
        MockingProgress mockingProgress = ThreadSafeMockingProgress.mockingProgress();
        mockingProgress.validateState();
        mockingProgress.reset();
        mockingProgress.resetOngoingStubbing();
        MockUtil.resetMock(this.control.getType());
    }

    @Override // org.mockito.MockedStatic
    public void clearInvocations() {
        assertNotClosed();
        MockingProgress mockingProgress = ThreadSafeMockingProgress.mockingProgress();
        mockingProgress.validateState();
        mockingProgress.reset();
        mockingProgress.resetOngoingStubbing();
        MockUtil.getInvocationContainer(this.control.getType()).clearInvocations();
    }

    @Override // org.mockito.MockedStatic
    public void verifyNoMoreInteractions() {
        assertNotClosed();
        ThreadSafeMockingProgress.mockingProgress().validateState();
        InvocationContainerImpl invocations = MockUtil.getInvocationContainer(this.control.getType());
        VerificationDataImpl data = new VerificationDataImpl(invocations, null);
        VerificationModeFactory.noMoreInteractions().verify(data);
    }

    @Override // org.mockito.MockedStatic
    public void verifyNoInteractions() {
        assertNotClosed();
        ThreadSafeMockingProgress.mockingProgress().validateState();
        InvocationContainerImpl invocations = MockUtil.getInvocationContainer(this.control.getType());
        VerificationDataImpl data = new VerificationDataImpl(invocations, null);
        VerificationModeFactory.noInteractions().verify(data);
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

    public String toString() {
        return "static mock for " + this.control.getType().getName();
    }
}
