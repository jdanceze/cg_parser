package org.powermock.api.mockito.internal.invocation;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.verification.VerificationMode;
import org.powermock.core.spi.NewInvocationControl;
import org.powermock.core.spi.support.InvocationSubstitute;
import org.powermock.reflect.internal.WhiteboxImpl;
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/internal/invocation/MockitoNewInvocationControl.class */
public class MockitoNewInvocationControl<T> implements NewInvocationControl<OngoingStubbing<T>> {
    private final InvocationSubstitute<T> substitute;

    public MockitoNewInvocationControl(InvocationSubstitute<T> substitute) {
        if (substitute == null) {
            throw new IllegalArgumentException("Internal error: substitute cannot be null.");
        }
        this.substitute = substitute;
    }

    @Override // org.powermock.core.spi.NewInvocationControl
    public Object invoke(Class<?> type, Object[] args, Class<?>[] sig) throws Exception {
        Constructor<?> constructor = WhiteboxImpl.getConstructor(type, sig);
        if (constructor.isVarArgs()) {
            Object varArgs = args[args.length - 1];
            int varArgsLength = Array.getLength(varArgs);
            args = new Object[(args.length + varArgsLength) - 1];
            System.arraycopy(args, 0, args, 0, args.length - 1);
            int i = args.length - 1;
            int j = 0;
            while (i < args.length) {
                args[i] = Array.get(varArgs, j);
                i++;
                j++;
            }
        }
        try {
            return this.substitute.performSubstitutionLogic(args);
        } catch (MockitoAssertionError e) {
            InvocationControlAssertionError.throwAssertionErrorForNewSubstitutionFailure(e, type);
            return null;
        }
    }

    @Override // org.powermock.core.spi.NewInvocationControl
    public OngoingStubbing<T> expectSubstitutionLogic(Object... arguments) throws Exception {
        return Mockito.when(this.substitute.performSubstitutionLogic(arguments));
    }

    public InvocationSubstitute<T> getSubstitute() {
        return this.substitute;
    }

    @Override // org.powermock.core.spi.DefaultBehavior
    public synchronized Object replay(Object... mocks) {
        return null;
    }

    public synchronized void verify(VerificationMode verificationMode) {
        Mockito.verify(this.substitute, verificationMode);
    }

    @Override // org.powermock.core.spi.DefaultBehavior
    public synchronized Object reset(Object... mocks) {
        Mockito.reset(this.substitute);
        return null;
    }

    public void verifyNoMoreInteractions() {
        try {
            Mockito.verifyNoMoreInteractions(this.substitute);
        } catch (MockitoAssertionError e) {
            InvocationControlAssertionError.updateErrorMessageForVerifyNoMoreInteractions(e);
            throw e;
        }
    }
}
