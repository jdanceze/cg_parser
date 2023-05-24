package org.powermock.api.mockito.internal.verification;

import org.mockito.exceptions.base.MockitoAssertionError;
import org.powermock.api.mockito.internal.invocation.InvocationControlAssertionError;
import org.powermock.api.mockito.internal.invocation.MockitoNewInvocationControl;
import org.powermock.api.mockito.verification.ConstructorArgumentsVerification;
import org.powermock.core.spi.NewInvocationControl;
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/internal/verification/DefaultConstructorArgumentsVerification.class */
public class DefaultConstructorArgumentsVerification<T> implements ConstructorArgumentsVerification {
    private final MockitoNewInvocationControl<T> invocationControl;
    private final Class<?> type;

    public DefaultConstructorArgumentsVerification(NewInvocationControl<T> invocationControl, Class<?> type) {
        this.type = type;
        this.invocationControl = (MockitoNewInvocationControl) invocationControl;
    }

    @Override // org.powermock.api.mockito.verification.ConstructorArgumentsVerification
    public void withArguments(Object argument, Object... arguments) throws Exception {
        Object[] realArguments;
        if (arguments == null) {
            realArguments = new Object[]{argument, null};
        } else {
            realArguments = new Object[arguments.length + 1];
            realArguments[0] = argument;
            System.arraycopy(arguments, 0, realArguments, 1, arguments.length);
        }
        invokeSubstitute(realArguments);
    }

    private void invokeSubstitute(Object... arguments) throws Exception {
        try {
            this.invocationControl.getSubstitute().performSubstitutionLogic(arguments);
        } catch (MockitoAssertionError e) {
            InvocationControlAssertionError.throwAssertionErrorForNewSubstitutionFailure(e, this.type);
        }
    }

    @Override // org.powermock.api.mockito.verification.ConstructorArgumentsVerification
    public void withNoArguments() throws Exception {
        invokeSubstitute(new Object[0]);
    }
}
