package org.mockito.internal.invocation;

import java.lang.reflect.Method;
import org.mockito.ArgumentMatcher;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/invocation/TypeSafeMatching.class */
public class TypeSafeMatching implements ArgumentMatcherAction {
    private static final ArgumentMatcherAction TYPE_SAFE_MATCHING_ACTION = new TypeSafeMatching();

    private TypeSafeMatching() {
    }

    public static ArgumentMatcherAction matchesTypeSafe() {
        return TYPE_SAFE_MATCHING_ACTION;
    }

    @Override // org.mockito.internal.invocation.ArgumentMatcherAction
    public boolean apply(ArgumentMatcher matcher, Object argument) {
        return isCompatible(matcher, argument) && matcher.matches(argument);
    }

    private static boolean isCompatible(ArgumentMatcher<?> argumentMatcher, Object argument) {
        if (argument == null) {
            return true;
        }
        Class<?> expectedArgumentType = getArgumentType(argumentMatcher);
        return expectedArgumentType.isInstance(argument);
    }

    private static Class<?> getArgumentType(ArgumentMatcher<?> argumentMatcher) {
        Method[] methods = argumentMatcher.getClass().getMethods();
        for (Method method : methods) {
            if (isMatchesMethod(method)) {
                return method.getParameterTypes()[0];
            }
        }
        throw new NoSuchMethodError("Method 'matches(T)' not found in ArgumentMatcher: " + argumentMatcher + " !\r\n Please file a bug with this stack trace at: https://github.com/mockito/mockito/issues/new ");
    }

    private static boolean isMatchesMethod(Method method) {
        if (method.getParameterTypes().length != 1 || method.isBridge()) {
            return false;
        }
        return "matches".equals(method.getName());
    }
}
