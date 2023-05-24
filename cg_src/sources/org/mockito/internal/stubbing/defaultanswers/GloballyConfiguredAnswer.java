package org.mockito.internal.stubbing.defaultanswers;

import java.io.Serializable;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/defaultanswers/GloballyConfiguredAnswer.class */
public class GloballyConfiguredAnswer implements Answer<Object>, Serializable {
    private static final long serialVersionUID = 3585893470101750917L;

    @Override // org.mockito.stubbing.Answer
    public Object answer(InvocationOnMock invocation) throws Throwable {
        return new GlobalConfiguration().getDefaultAnswer().answer(invocation);
    }
}
