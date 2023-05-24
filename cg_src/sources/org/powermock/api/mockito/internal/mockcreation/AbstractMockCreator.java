package org.powermock.api.mockito.internal.mockcreation;

import org.powermock.core.agent.JavaAgentClassRegister;
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/internal/mockcreation/AbstractMockCreator.class */
public abstract class AbstractMockCreator implements MockCreator {
    private JavaAgentClassRegister agentClassRegister;

    /* JADX INFO: Access modifiers changed from: package-private */
    public <T> void validateType(Class<T> type, boolean isStatic, boolean isSpy) {
        createTypeValidator(type, isStatic, isSpy).validate();
    }

    private <T> MockTypeValidator<T> createTypeValidator(Class<T> type, boolean isStatic, boolean isSpy) {
        return MockTypeValidatorFactory.createValidator(type, isStatic, isSpy, this.agentClassRegister);
    }
}
