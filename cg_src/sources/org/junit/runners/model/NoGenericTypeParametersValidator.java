package org.junit.runners.model;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.List;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runners/model/NoGenericTypeParametersValidator.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runners/model/NoGenericTypeParametersValidator.class */
class NoGenericTypeParametersValidator {
    private final Method method;

    /* JADX INFO: Access modifiers changed from: package-private */
    public NoGenericTypeParametersValidator(Method method) {
        this.method = method;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void validate(List<Throwable> errors) {
        Type[] arr$ = this.method.getGenericParameterTypes();
        for (Type each : arr$) {
            validateNoTypeParameterOnType(each, errors);
        }
    }

    private void validateNoTypeParameterOnType(Type type, List<Throwable> errors) {
        if (type instanceof TypeVariable) {
            errors.add(new Exception("Method " + this.method.getName() + "() contains unresolved type variable " + type));
        } else if (type instanceof ParameterizedType) {
            validateNoTypeParameterOnParameterizedType((ParameterizedType) type, errors);
        } else if (type instanceof WildcardType) {
            validateNoTypeParameterOnWildcardType((WildcardType) type, errors);
        } else if (type instanceof GenericArrayType) {
            validateNoTypeParameterOnGenericArrayType((GenericArrayType) type, errors);
        }
    }

    private void validateNoTypeParameterOnParameterizedType(ParameterizedType parameterized, List<Throwable> errors) {
        Type[] arr$ = parameterized.getActualTypeArguments();
        for (Type each : arr$) {
            validateNoTypeParameterOnType(each, errors);
        }
    }

    private void validateNoTypeParameterOnWildcardType(WildcardType wildcard, List<Throwable> errors) {
        Type[] arr$ = wildcard.getUpperBounds();
        for (Type each : arr$) {
            validateNoTypeParameterOnType(each, errors);
        }
        Type[] arr$2 = wildcard.getLowerBounds();
        for (Type each2 : arr$2) {
            validateNoTypeParameterOnType(each2, errors);
        }
    }

    private void validateNoTypeParameterOnGenericArrayType(GenericArrayType arrayType, List<Throwable> errors) {
        validateNoTypeParameterOnType(arrayType.getGenericComponentType(), errors);
    }
}
