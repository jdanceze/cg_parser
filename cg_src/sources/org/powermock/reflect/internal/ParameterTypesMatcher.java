package org.powermock.reflect.internal;
/* loaded from: gencallgraphv3.jar:powermock-reflect-2.0.9.jar:org/powermock/reflect/internal/ParameterTypesMatcher.class */
class ParameterTypesMatcher {
    private boolean isVarArgs;
    private Class<?>[] expectedParameterTypes;
    private Class<?>[] actualParameterTypes;

    public ParameterTypesMatcher(boolean isVarArgs, Class<?>[] expectedParameterTypes, Class<?>... actualParameterTypes) {
        this.isVarArgs = isVarArgs;
        this.expectedParameterTypes = expectedParameterTypes;
        this.actualParameterTypes = actualParameterTypes;
    }

    private boolean isRemainParamsVarArgs(int index, Class<?> actualParameterType) {
        return this.isVarArgs && index == this.expectedParameterTypes.length - 1 && actualParameterType.getComponentType().isAssignableFrom(this.expectedParameterTypes[index]);
    }

    private boolean isParameterTypesNotMatch(Class<?> actualParameterType, Class<?> expectedParameterType) {
        return (actualParameterType == null || expectedParameterType == null || actualParameterType.isAssignableFrom(expectedParameterType)) ? false : true;
    }

    public boolean match() {
        assertParametersTypesNotNull();
        if (isParametersLengthMatch()) {
            return false;
        }
        return isParametersMatch().booleanValue();
    }

    private boolean isParametersLengthMatch() {
        return this.expectedParameterTypes.length != this.actualParameterTypes.length;
    }

    private void assertParametersTypesNotNull() {
        if (this.expectedParameterTypes == null || this.actualParameterTypes == null) {
            throw new IllegalArgumentException("parameter types cannot be null");
        }
    }

    private Boolean isParametersMatch() {
        for (int index = 0; index < this.expectedParameterTypes.length; index++) {
            Class<?> actualParameterType = this.actualParameterTypes[index];
            if (isRemainParamsVarArgs(index, actualParameterType)) {
                return true;
            }
            Class<?> expectedParameterType = this.expectedParameterTypes[index];
            if (isParameterTypesNotMatch(actualParameterType, expectedParameterType)) {
                return false;
            }
        }
        return true;
    }
}
