package org.mockito.internal.invocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/invocation/ArgumentsProcessor.class */
public class ArgumentsProcessor {
    public static Object[] expandArgs(MockitoMethod method, Object[] args) {
        int nParams = method.getParameterTypes().length;
        if (args != null && args.length > nParams) {
            args = Arrays.copyOf(args, nParams);
        }
        return expandVarArgs(method.isVarArgs(), args);
    }

    private static Object[] expandVarArgs(boolean isVarArgs, Object[] args) {
        if (!isVarArgs || isNullOrEmpty(args) || (args[args.length - 1] != null && !args[args.length - 1].getClass().isArray())) {
            return args == null ? new Object[0] : args;
        }
        int nonVarArgsCount = args.length - 1;
        Object[] varArgs = args[nonVarArgsCount] == null ? new Object[]{null} : ArrayEquals.createObjectArray(args[nonVarArgsCount]);
        int varArgsCount = varArgs.length;
        Object[] newArgs = new Object[nonVarArgsCount + varArgsCount];
        System.arraycopy(args, 0, newArgs, 0, nonVarArgsCount);
        System.arraycopy(varArgs, 0, newArgs, nonVarArgsCount, varArgsCount);
        return newArgs;
    }

    private static <T> boolean isNullOrEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static List<ArgumentMatcher> argumentsToMatchers(Object[] arguments) {
        List<ArgumentMatcher> matchers = new ArrayList<>(arguments.length);
        for (Object arg : arguments) {
            if (arg != null && arg.getClass().isArray()) {
                matchers.add(new ArrayEquals(arg));
            } else {
                matchers.add(new Equals(arg));
            }
        }
        return matchers;
    }
}
