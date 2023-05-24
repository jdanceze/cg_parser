package org.powermock.api.mockito.internal.verification;

import java.lang.reflect.Method;
import org.powermock.api.mockito.verification.PrivateMethodVerification;
import org.powermock.api.mockito.verification.WithOrWithoutVerifiedArguments;
import org.powermock.reflect.Whitebox;
import org.powermock.tests.utils.impl.ArrayMergerImpl;
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/internal/verification/DefaultPrivateMethodVerification.class */
public class DefaultPrivateMethodVerification implements PrivateMethodVerification {
    private final Object objectToVerify;

    public DefaultPrivateMethodVerification(Object objectToVerify) {
        this.objectToVerify = objectToVerify;
    }

    @Override // org.powermock.api.mockito.verification.PrivateMethodVerification
    public void invoke(Object... arguments) throws Exception {
        Whitebox.invokeMethod(this.objectToVerify, arguments);
    }

    @Override // org.powermock.api.mockito.verification.PrivateMethodVerification
    public void invoke(String methodToExecute, Object... arguments) throws Exception {
        Whitebox.invokeMethod(this.objectToVerify, methodToExecute, arguments);
    }

    @Override // org.powermock.api.mockito.verification.PrivateMethodVerification
    public WithOrWithoutVerifiedArguments invoke(Method method) throws Exception {
        return new VerificationArguments(method);
    }

    /* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/internal/verification/DefaultPrivateMethodVerification$VerificationArguments.class */
    private class VerificationArguments implements WithOrWithoutVerifiedArguments {
        private final Method method;

        public VerificationArguments(Method method) {
            if (method == null) {
                throw new IllegalArgumentException("method cannot be null");
            }
            this.method = method;
            this.method.setAccessible(true);
        }

        @Override // org.powermock.api.mockito.verification.WithVerifiedArguments
        public void withArguments(Object firstArgument, Object... additionalArguments) throws Exception {
            if (additionalArguments == null || additionalArguments.length == 0) {
                this.method.invoke(DefaultPrivateMethodVerification.this.objectToVerify, firstArgument);
                return;
            }
            Object[] arguments = new ArrayMergerImpl().mergeArrays(Object.class, new Object[]{firstArgument}, additionalArguments);
            this.method.invoke(DefaultPrivateMethodVerification.this.objectToVerify, arguments);
        }

        @Override // org.powermock.api.mockito.verification.WithoutVerifiedArguments
        public void withNoArguments() throws Exception {
            this.method.invoke(DefaultPrivateMethodVerification.this.objectToVerify, new Object[0]);
        }
    }
}
