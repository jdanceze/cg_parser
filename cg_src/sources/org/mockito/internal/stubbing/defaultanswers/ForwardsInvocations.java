package org.mockito.internal.stubbing.defaultanswers;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.plugins.MemberAccessor;
import org.mockito.stubbing.Answer;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/defaultanswers/ForwardsInvocations.class */
public class ForwardsInvocations implements Answer<Object>, Serializable {
    private static final long serialVersionUID = -8343690268123254910L;
    private Object delegatedObject;

    public ForwardsInvocations(Object delegatedObject) {
        this.delegatedObject = null;
        this.delegatedObject = delegatedObject;
    }

    @Override // org.mockito.stubbing.Answer
    public Object answer(InvocationOnMock invocation) throws Throwable {
        Method mockMethod = invocation.getMethod();
        try {
            Method delegateMethod = getDelegateMethod(mockMethod);
            if (!compatibleReturnTypes(mockMethod.getReturnType(), delegateMethod.getReturnType())) {
                throw Reporter.delegatedMethodHasWrongReturnType(mockMethod, delegateMethod, invocation.getMock(), this.delegatedObject);
            }
            MemberAccessor accessor = Plugins.getMemberAccessor();
            Object[] rawArguments = ((Invocation) invocation).getRawArguments();
            return accessor.invoke(delegateMethod, this.delegatedObject, rawArguments);
        } catch (NoSuchMethodException e) {
            throw Reporter.delegatedMethodDoesNotExistOnDelegate(mockMethod, invocation.getMock(), this.delegatedObject);
        } catch (InvocationTargetException e2) {
            throw e2.getCause();
        }
    }

    private Method getDelegateMethod(Method mockMethod) throws NoSuchMethodException {
        if (mockMethod.getDeclaringClass().isAssignableFrom(this.delegatedObject.getClass())) {
            return mockMethod;
        }
        return this.delegatedObject.getClass().getMethod(mockMethod.getName(), mockMethod.getParameterTypes());
    }

    private static boolean compatibleReturnTypes(Class<?> superType, Class<?> subType) {
        return superType.equals(subType) || superType.isAssignableFrom(subType);
    }
}
