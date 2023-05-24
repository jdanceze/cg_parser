package org.powermock.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Modifier;
import org.powermock.core.spi.MethodInvocationControl;
import org.powermock.core.spi.NewInvocationControl;
import org.powermock.reflect.internal.TypeUtils;
import org.powermock.reflect.internal.WhiteboxImpl;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/MockGateway.class */
public class MockGateway {
    public static final String DONT_MOCK_NEXT_CALL = "DontMockNextCall";
    public static final Object PROCEED = new Object();
    public static final Object SUPPRESS = new Object();
    public static boolean MOCK_STANDARD_METHODS = true;
    public static boolean MOCK_GET_CLASS_METHOD = false;
    public static boolean MOCK_ANNOTATION_METHODS = false;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/MockGateway$NoMockito.class */
    public static final class NoMockito {
        static final boolean noMockito;

        static {
            noMockito = Package.getPackage("org.mockito") == null;
        }

        private NoMockito() {
        }
    }

    public static Object newInstanceCall(Class<?> type, Object[] args, Class<?>[] sig) throws Throwable {
        NewInvocationControl<?> newInvocationControl = MockRepository.getNewInstanceControl(type);
        if (newInvocationControl != null) {
            if (type.isMemberClass() && Modifier.isStatic(type.getModifiers())) {
                if (args.length > 0 && (((args[0] == null && sig[0].getCanonicalName() == null) || (args[args.length - 1] == null && sig[args.length - 1].getCanonicalName() == null)) && sig.length > 0)) {
                    args = copyArgumentsForInnerOrLocalOrAnonymousClass(args, sig[0], false);
                }
            } else if ((type.isLocalClass() || type.isAnonymousClass() || type.isMemberClass()) && args.length > 0 && sig.length > 0 && sig[0].equals(type.getEnclosingClass())) {
                args = copyArgumentsForInnerOrLocalOrAnonymousClass(args, sig[0], true);
            }
            return newInvocationControl.invoke(type, args, sig);
        } else if (MockRepository.shouldSuppressConstructor(WhiteboxImpl.getConstructor(type, sig))) {
            return WhiteboxImpl.getFirstParentConstructor(type);
        } else {
            return PROCEED;
        }
    }

    public static Object fieldCall(Object instanceOrClassContainingTheField, Class<?> classDefiningField, String fieldName, Class<?> fieldType) {
        if (MockRepository.shouldSuppressField(WhiteboxImpl.getField(classDefiningField, fieldName))) {
            return TypeUtils.getDefaultValue(fieldType);
        }
        return PROCEED;
    }

    public static Object staticConstructorCall(String className) {
        if (MockRepository.shouldSuppressStaticInitializerFor(className)) {
            return "suppress";
        }
        return PROCEED;
    }

    public static Object constructorCall(Class<?> type, Object[] args, Class<?>[] sig) throws Throwable {
        Constructor<?> constructor = WhiteboxImpl.getConstructor(type, sig);
        if (MockRepository.shouldSuppressConstructor(constructor)) {
            return null;
        }
        return PROCEED;
    }

    public static boolean suppressConstructorCall(Class<?> type, Object[] args, Class<?>[] sig) throws Throwable {
        return constructorCall(type, args, sig) != PROCEED;
    }

    public static Object methodCall(Object instance, String methodName, Object[] args, Class<?>[] sig, String returnTypeAsString) throws Throwable {
        return doMethodCall(instance, methodName, args, sig, returnTypeAsString);
    }

    public static Object methodCall(Class<?> type, String methodName, Object[] args, Class<?>[] sig, String returnTypeAsString) throws Throwable {
        return doMethodCall(type, methodName, args, sig, returnTypeAsString);
    }

    private static Object doMethodCall(Object object, String methodName, Object[] args, Class<?>[] sig, String returnTypeAsString) throws Throwable {
        if (!shouldMockMethod(methodName, sig)) {
            return PROCEED;
        }
        MockInvocation mockInvocation = new MockInvocation(object, methodName, sig);
        MethodInvocationControl methodInvocationControl = mockInvocation.getMethodInvocationControl();
        Object returnValue = null;
        if (isEqualsMethod(mockInvocation) && !isStaticMethod(mockInvocation)) {
            returnValue = tryHandleEqualsMethod(mockInvocation);
        }
        if (returnValue != null) {
            return returnValue;
        }
        return doMethodCall(object, args, returnTypeAsString, mockInvocation, methodInvocationControl);
    }

    private static Object doMethodCall(Object object, Object[] args, String returnTypeAsString, MockInvocation mockInvocation, MethodInvocationControl methodInvocationControl) throws Throwable {
        Object returnValue;
        if (MockRepository.shouldSuppressMethod(mockInvocation.getMethod(), mockInvocation.getObjectType())) {
            returnValue = TypeUtils.getDefaultValue(returnTypeAsString);
        } else if (MockRepository.shouldStubMethod(mockInvocation.getMethod())) {
            returnValue = MockRepository.getMethodToStub(mockInvocation.getMethod());
        } else if (methodInvocationControl != null && methodInvocationControl.isMocked(mockInvocation.getMethod()) && shouldMockThisCall()) {
            returnValue = methodInvocationControl.invoke(object, mockInvocation.getMethod(), args);
            if (returnValue == SUPPRESS) {
                returnValue = TypeUtils.getDefaultValue(returnTypeAsString);
            }
        } else if (MockRepository.hasMethodProxy(mockInvocation.getMethod())) {
            InvocationHandler invocationHandler = MockRepository.removeMethodProxy(mockInvocation.getMethod());
            try {
                returnValue = invocationHandler.invoke(object, mockInvocation.getMethod(), args);
                MockRepository.putMethodProxy(mockInvocation.getMethod(), invocationHandler);
            } catch (Throwable th) {
                MockRepository.putMethodProxy(mockInvocation.getMethod(), invocationHandler);
                throw th;
            }
        } else {
            returnValue = PROCEED;
        }
        return returnValue;
    }

    private static Object tryHandleEqualsMethod(MockInvocation mockInvocation) {
        if (mockInvocation.getMethod().getParameterTypes().length == 1 && mockInvocation.getMethod().getParameterTypes()[0] == Object.class && Modifier.isFinal(mockInvocation.getMethod().getModifiers())) {
            return PROCEED;
        }
        if (calledFromMockito()) {
            return PROCEED;
        }
        return null;
    }

    private static boolean isEqualsMethod(MockInvocation mockInvocation) {
        return "equals".equals(mockInvocation.getMethod().getName());
    }

    private static boolean isStaticMethod(MockInvocation mockInvocation) {
        return Modifier.isStatic(mockInvocation.getMethod().getModifiers());
    }

    private static boolean calledFromMockito() {
        if (NoMockito.noMockito) {
            return false;
        }
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            if (stackTraceElement.getClassName().startsWith("org.mockito.")) {
                return true;
            }
        }
        return false;
    }

    private static boolean shouldMockMethod(String methodName, Class<?>[] sig) {
        if (isJavaStandardMethod(methodName, sig) && !MOCK_STANDARD_METHODS) {
            return false;
        }
        if (!isGetClassMethod(methodName, sig) || MOCK_GET_CLASS_METHOD) {
            return !isAnnotationMethod(methodName, sig) || MOCK_ANNOTATION_METHODS;
        }
        return false;
    }

    private static boolean isJavaStandardMethod(String methodName, Class<?>[] sig) {
        return (methodName.equals("equals") && sig.length == 1) || (methodName.equals("hashCode") && sig.length == 0) || (methodName.equals("toString") && sig.length == 0);
    }

    private static boolean isGetClassMethod(String methodName, Class<?>[] sig) {
        return methodName.equals("getClass") && sig.length == 0;
    }

    private static boolean isAnnotationMethod(String methodName, Class<?>[] sig) {
        return (methodName.equals("isAnnotationPresent") && sig.length == 1) || (methodName.equals("getAnnotation") && sig.length == 1);
    }

    private static boolean shouldMockThisCall() {
        Object shouldSkipMockingOfNextCall = MockRepository.getAdditionalState(DONT_MOCK_NEXT_CALL);
        boolean shouldMockThisCall = shouldSkipMockingOfNextCall == null;
        MockRepository.removeAdditionalState(DONT_MOCK_NEXT_CALL);
        return shouldMockThisCall;
    }

    private static Object[] copyArgumentsForInnerOrLocalOrAnonymousClass(Object[] args, Class<?> sig, boolean excludeEnclosingInstance) {
        int start;
        int end;
        Object[] newArgs = new Object[args.length - 1];
        int j = 0;
        if ((args[0] == null && sig == null) || excludeEnclosingInstance) {
            start = 1;
            end = args.length;
        } else {
            start = 0;
            end = args.length - 1;
        }
        for (int i = start; i < end; i++) {
            int i2 = j;
            j++;
            newArgs[i2] = args[i];
        }
        return newArgs;
    }
}
