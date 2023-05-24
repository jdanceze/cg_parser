package org.powermock.api.support;

import java.lang.reflect.Method;
import org.powermock.core.MockRepository;
import org.powermock.reflect.Whitebox;
import org.powermock.reflect.exceptions.MethodNotFoundException;
import org.powermock.reflect.exceptions.TooManyMethodsFoundException;
/* loaded from: gencallgraphv3.jar:powermock-api-support-2.0.9.jar:org/powermock/api/support/Stubber.class */
public class Stubber {
    public static void stubMethod(Method method, Object returnObject) {
        MockRepository.putMethodToStub(method, returnObject);
    }

    public static void stubMethod(Class<?> declaringClass, String methodName, Object returnObject) {
        if (declaringClass == null) {
            throw new IllegalArgumentException("declaringClass cannot be null");
        }
        if (methodName == null || methodName.length() == 0) {
            throw new IllegalArgumentException("methodName cannot be empty");
        }
        Method[] methods = Whitebox.getMethods(declaringClass, methodName);
        if (methods.length == 0) {
            throw new MethodNotFoundException(String.format("Couldn't find a method with name %s in the class hierarchy of %s", methodName, declaringClass.getName()));
        }
        if (methods.length > 1) {
            throw new TooManyMethodsFoundException(String.format("Found %d methods with name %s in the class hierarchy of %s.", Integer.valueOf(methods.length), methodName, declaringClass.getName()));
        }
        MockRepository.putMethodToStub(methods[0], returnObject);
    }
}
