package org.mockito.invocation;

import java.util.List;
import org.mockito.ArgumentMatcher;
import org.mockito.NotExtensible;
@NotExtensible
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/invocation/Invocation.class */
public interface Invocation extends InvocationOnMock, DescribedInvocation {
    boolean isVerified();

    int getSequenceNumber();

    Location getLocation();

    Object[] getRawArguments();

    List<ArgumentMatcher> getArgumentsAsMatchers();

    Class<?> getRawReturnType();

    void markVerified();

    StubInfo stubInfo();

    void markStubbed(StubInfo stubInfo);

    boolean isIgnoredForVerification();

    void ignoreForVerification();
}
