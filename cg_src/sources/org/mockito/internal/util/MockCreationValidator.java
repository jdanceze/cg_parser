package org.mockito.internal.util;

import java.util.Collection;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/MockCreationValidator.class */
public class MockCreationValidator {
    public void validateType(Class<?> classToMock) {
        MockMaker.TypeMockability typeMockability = MockUtil.typeMockabilityOf(classToMock);
        if (!typeMockability.mockable()) {
            throw Reporter.cannotMockClass(classToMock, typeMockability.nonMockableReason());
        }
    }

    public void validateExtraInterfaces(Class<?> classToMock, Collection<Class<?>> extraInterfaces) {
        if (extraInterfaces == null) {
            return;
        }
        for (Class<?> i : extraInterfaces) {
            if (classToMock == i) {
                throw Reporter.extraInterfacesCannotContainMockedType(classToMock);
            }
        }
    }

    public void validateMockedType(Class<?> classToMock, Object spiedInstance) {
        if (classToMock != null && spiedInstance != null && !classToMock.equals(spiedInstance.getClass())) {
            throw Reporter.mockedTypeIsInconsistentWithSpiedInstanceType(classToMock, spiedInstance);
        }
    }

    public void validateDelegatedInstance(Class<?> classToMock, Object delegatedInstance) {
        if (classToMock != null && delegatedInstance != null && delegatedInstance.getClass().isAssignableFrom(classToMock)) {
            throw Reporter.mockedTypeIsInconsistentWithDelegatedInstanceType(classToMock, delegatedInstance);
        }
    }

    public void validateConstructorUse(boolean usingConstructor, SerializableMode mode) {
        if (usingConstructor && mode == SerializableMode.ACROSS_CLASSLOADERS) {
            throw Reporter.usingConstructorWithFancySerializable(mode);
        }
    }
}
