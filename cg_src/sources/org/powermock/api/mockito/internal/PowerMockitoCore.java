package org.powermock.api.mockito.internal;

import java.util.concurrent.Callable;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;
import org.mockito.verification.VerificationMode;
import org.powermock.api.mockito.expectation.PowerMockitoStubber;
import org.powermock.api.mockito.internal.expectation.PowerMockitoStubberImpl;
import org.powermock.api.mockito.internal.invocation.MockitoNewInvocationControl;
import org.powermock.api.mockito.internal.mockcreation.DefaultMockCreator;
import org.powermock.api.mockito.internal.stubbing.PowerMockCallRealMethod;
import org.powermock.api.mockito.internal.verification.DefaultConstructorArgumentsVerification;
import org.powermock.core.MockRepository;
import org.powermock.core.classloader.ClassloaderWrapper;
import org.powermock.reflect.Whitebox;
import org.powermock.utils.Asserts;
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/internal/PowerMockitoCore.class */
public class PowerMockitoCore {
    private static final PowerMockCallRealMethod POWER_MOCK_CALL_REAL_METHOD = new PowerMockCallRealMethod();
    private static final String NO_OBJECT_CREATION_ERROR_MESSAGE_TEMPLATE = "No instantiation of class %s was recorded during the test. Note that only expected object creations (e.g. those using whenNew(..)) can be verified.";

    public PowerMockitoStubber doAnswer(final Answer answer) {
        return doAnswer(new Callable<Stubber>() { // from class: org.powermock.api.mockito.internal.PowerMockitoCore.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Stubber call() throws Exception {
                return Mockito.doAnswer(answer);
            }
        });
    }

    public PowerMockitoStubber doThrow(final Throwable toBeThrown) {
        return doAnswer(new Callable<Stubber>() { // from class: org.powermock.api.mockito.internal.PowerMockitoCore.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Stubber call() throws Exception {
                return Mockito.doThrow(toBeThrown);
            }
        });
    }

    public PowerMockitoStubber doCallRealMethod() {
        return doAnswer(new Callable<Stubber>() { // from class: org.powermock.api.mockito.internal.PowerMockitoCore.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Stubber call() throws Exception {
                return Mockito.doCallRealMethod();
            }
        });
    }

    public PowerMockitoStubber doNothing() {
        return doAnswer(new Callable<Stubber>() { // from class: org.powermock.api.mockito.internal.PowerMockitoCore.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Stubber call() throws Exception {
                return Mockito.doNothing();
            }
        });
    }

    public PowerMockitoStubber doReturn(final Object toBeReturned) {
        return doAnswer(new Callable<Stubber>() { // from class: org.powermock.api.mockito.internal.PowerMockitoCore.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Stubber call() throws Exception {
                return Mockito.doReturn(toBeReturned);
            }
        });
    }

    public PowerMockitoStubber doAnswer(final Object toBeReturned, final Object... othersToBeReturned) {
        return doAnswer(new Callable<Stubber>() { // from class: org.powermock.api.mockito.internal.PowerMockitoCore.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Stubber call() throws Exception {
                return Mockito.doReturn(toBeReturned, othersToBeReturned);
            }
        });
    }

    public <T> DefaultConstructorArgumentsVerification<T> verifyNew(Class<T> mock, VerificationMode mode) {
        Asserts.assertNotNull(mock, "Class to verify cannot be null");
        Asserts.assertNotNull(mode, "Verify mode cannot be null");
        MockitoNewInvocationControl<T> invocationControl = (MockitoNewInvocationControl) MockRepository.getNewInstanceControl(mock);
        Asserts.assertNotNull(invocationControl, String.format(NO_OBJECT_CREATION_ERROR_MESSAGE_TEMPLATE, Whitebox.getType(mock).getName()));
        invocationControl.verify(mode);
        return new DefaultConstructorArgumentsVerification<>(invocationControl, mock);
    }

    public <T> T spy(T object) {
        MockSettings mockSettings = Mockito.withSettings().spiedInstance(object).defaultAnswer(POWER_MOCK_CALL_REAL_METHOD);
        return (T) DefaultMockCreator.mock(Whitebox.getType(object), false, true, object, mockSettings, null);
    }

    private PowerMockitoStubber doAnswer(Callable<Stubber> callable) {
        Stubber stubber = (Stubber) ClassloaderWrapper.runWithClass(callable);
        return new PowerMockitoStubberImpl(stubber);
    }
}
