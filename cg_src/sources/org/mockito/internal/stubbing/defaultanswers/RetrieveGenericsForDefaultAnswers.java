package org.mockito.internal.stubbing.defaultanswers;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockCreationSettings;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/defaultanswers/RetrieveGenericsForDefaultAnswers.class */
class RetrieveGenericsForDefaultAnswers {
    private static final MockitoCore MOCKITO_CORE = new MockitoCore();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/defaultanswers/RetrieveGenericsForDefaultAnswers$AnswerCallback.class */
    public interface AnswerCallback {
        Object apply(Class<?> cls);
    }

    RetrieveGenericsForDefaultAnswers() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object returnTypeForMockWithCorrectGenerics(InvocationOnMock invocation, AnswerCallback answerCallback) {
        Class<?> type = invocation.getMethod().getReturnType();
        Type returnType = invocation.getMethod().getGenericReturnType();
        Object defaultReturnValue = null;
        if (returnType instanceof TypeVariable) {
            type = findTypeFromGeneric(invocation, (TypeVariable) returnType);
            if (type != null) {
                defaultReturnValue = delegateChains(type);
            }
        }
        if (defaultReturnValue != null) {
            return defaultReturnValue;
        }
        if (type != null) {
            if (!MOCKITO_CORE.isTypeMockable(type)) {
                return null;
            }
            return answerCallback.apply(type);
        }
        return answerCallback.apply(null);
    }

    private static Object delegateChains(Class<?> type) {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object result = returnsEmptyValues.returnValueFor(type);
        if (result == null) {
            Class<?> cls = type;
            while (true) {
                Class<?> emptyValueForClass = cls;
                if (emptyValueForClass == null || result != null) {
                    break;
                }
                Class<?>[] classes = emptyValueForClass.getInterfaces();
                for (Class<?> clazz : classes) {
                    result = returnsEmptyValues.returnValueFor(clazz);
                    if (result != null) {
                        break;
                    }
                }
                cls = emptyValueForClass.getSuperclass();
            }
        }
        if (result == null) {
            result = new ReturnsMoreEmptyValues().returnValueFor(type);
        }
        return result;
    }

    private static Class<?> findTypeFromGeneric(InvocationOnMock invocation, TypeVariable returnType) {
        MockCreationSettings mockSettings = MockUtil.getMockHandler(invocation.getMock()).getMockSettings();
        GenericMetadataSupport returnTypeSupport = GenericMetadataSupport.inferFrom(mockSettings.getTypeToMock()).resolveGenericReturnType(invocation.getMethod());
        Class<?> rawType = returnTypeSupport.rawType();
        if (rawType == Object.class) {
            return findTypeFromGenericInArguments(invocation, returnType);
        }
        return rawType;
    }

    private static Class<?> findTypeFromGenericInArguments(InvocationOnMock invocation, TypeVariable returnType) {
        Type[] parameterTypes = invocation.getMethod().getGenericParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            Type argType = parameterTypes[i];
            if (returnType.equals(argType)) {
                Object argument = invocation.getArgument(i);
                if (argument == null) {
                    return null;
                }
                return argument.getClass();
            } else if ((argType instanceof GenericArrayType) && returnType.equals(((GenericArrayType) argType).getGenericComponentType())) {
                return invocation.getArgument(i).getClass();
            }
        }
        return null;
    }
}
