package org.powermock.api.mockito.verification;

import java.lang.reflect.Method;
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/verification/PrivateMethodVerification.class */
public interface PrivateMethodVerification {
    @Deprecated
    void invoke(Object... objArr) throws Exception;

    WithOrWithoutVerifiedArguments invoke(Method method) throws Exception;

    void invoke(String str, Object... objArr) throws Exception;
}
