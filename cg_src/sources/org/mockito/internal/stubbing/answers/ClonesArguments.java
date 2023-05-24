package org.mockito.internal.stubbing.answers;

import java.lang.reflect.Array;
import org.mockito.creation.instance.Instantiator;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/answers/ClonesArguments.class */
public class ClonesArguments implements Answer<Object> {
    @Override // org.mockito.stubbing.Answer
    public Object answer(InvocationOnMock invocation) throws Throwable {
        Object[] arguments = invocation.getArguments();
        for (int i = 0; i < arguments.length; i++) {
            Object from = arguments[i];
            if (from != null) {
                if (from.getClass().isArray()) {
                    int len = Array.getLength(from);
                    Object newInstance = Array.newInstance(from.getClass().getComponentType(), len);
                    for (int j = 0; j < len; j++) {
                        Array.set(newInstance, j, Array.get(from, j));
                    }
                    arguments[i] = newInstance;
                } else {
                    Instantiator instantiator = Plugins.getInstantiatorProvider().getInstantiator(null);
                    Object newInstance2 = instantiator.newInstance(from.getClass());
                    new LenientCopyTool().copyToRealObject(from, newInstance2);
                    arguments[i] = newInstance2;
                }
            }
        }
        return new ReturnsEmptyValues().answer(invocation);
    }
}
