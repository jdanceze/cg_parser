package org.mockito.internal.stubbing.answers;

import java.io.Serializable;
import java.lang.reflect.Method;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.ValidableAnswer;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/answers/ReturnsArgumentAt.class */
public class ReturnsArgumentAt implements Answer<Object>, ValidableAnswer, Serializable {
    private static final long serialVersionUID = -589315085166295101L;
    public static final int LAST_ARGUMENT = -1;
    private final int wantedArgumentPosition;

    public ReturnsArgumentAt(int wantedArgumentPosition) {
        if (wantedArgumentPosition != -1 && wantedArgumentPosition < 0) {
            throw Reporter.invalidArgumentRangeAtIdentityAnswerCreationTime();
        }
        this.wantedArgumentPosition = wantedArgumentPosition;
    }

    @Override // org.mockito.stubbing.Answer
    public Object answer(InvocationOnMock invocation) throws Throwable {
        int argumentPosition = inferWantedArgumentPosition(invocation);
        validateIndexWithinInvocationRange(invocation, argumentPosition);
        if (wantedArgIndexIsVarargAndSameTypeAsReturnType(invocation.getMethod(), argumentPosition)) {
            return ((Invocation) invocation).getRawArguments()[argumentPosition];
        }
        return invocation.getArgument(argumentPosition);
    }

    @Override // org.mockito.stubbing.ValidableAnswer
    public void validateFor(InvocationOnMock invocation) {
        int argumentPosition = inferWantedArgumentPosition(invocation);
        validateIndexWithinInvocationRange(invocation, argumentPosition);
        validateArgumentTypeCompatibility((Invocation) invocation, argumentPosition);
    }

    private int inferWantedArgumentPosition(InvocationOnMock invocation) {
        return this.wantedArgumentPosition == -1 ? invocation.getArguments().length - 1 : this.wantedArgumentPosition;
    }

    private void validateIndexWithinInvocationRange(InvocationOnMock invocation, int argumentPosition) {
        if (!wantedArgumentPositionIsValidForInvocation(invocation, argumentPosition)) {
            throw Reporter.invalidArgumentPositionRangeAtInvocationTime(invocation, this.wantedArgumentPosition == -1, this.wantedArgumentPosition);
        }
    }

    private void validateArgumentTypeCompatibility(Invocation invocation, int argumentPosition) {
        InvocationInfo invocationInfo = new InvocationInfo(invocation);
        Class<?> inferredArgumentType = inferArgumentType(invocation, argumentPosition);
        if (!invocationInfo.isValidReturnType(inferredArgumentType)) {
            throw Reporter.wrongTypeOfArgumentToReturn(invocation, invocationInfo.printMethodReturnType(), inferredArgumentType, this.wantedArgumentPosition);
        }
    }

    private boolean wantedArgIndexIsVarargAndSameTypeAsReturnType(Method method, int argumentPosition) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        return method.isVarArgs() && argumentPosition == parameterTypes.length - 1 && method.getReturnType().isAssignableFrom(parameterTypes[argumentPosition]);
    }

    private boolean wantedArgumentPositionIsValidForInvocation(InvocationOnMock invocation, int argumentPosition) {
        if (argumentPosition < 0) {
            return false;
        }
        return invocation.getMethod().isVarArgs() || invocation.getArguments().length > argumentPosition;
    }

    private Class<?> inferArgumentType(Invocation invocation, int argumentIndex) {
        Class<?>[] parameterTypes = invocation.getMethod().getParameterTypes();
        if (!invocation.getMethod().isVarArgs()) {
            Class<?> argumentType = parameterTypes[argumentIndex];
            Object argumentValue = invocation.getArgument(argumentIndex);
            if (argumentType.isPrimitive() || argumentValue == null) {
                return argumentType;
            }
            return argumentValue.getClass();
        }
        int varargIndex = parameterTypes.length - 1;
        if (argumentIndex < varargIndex) {
            return parameterTypes[argumentIndex];
        }
        if (wantedArgIndexIsVarargAndSameTypeAsReturnType(invocation.getMethod(), argumentIndex)) {
            return parameterTypes[argumentIndex];
        }
        return parameterTypes[varargIndex].getComponentType();
    }
}
