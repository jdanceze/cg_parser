package org.powermock.api.mockito.internal.mockcreation;

import org.powermock.api.mockito.expectation.reporter.MockitoPowerMockReporter;
import org.powermock.core.agent.JavaAgentClassRegister;
import org.powermock.core.classloader.PowerMockModified;
import org.powermock.core.reporter.PowerMockReporter;
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/internal/mockcreation/MockTypeValidatorFactory.class */
public class MockTypeValidatorFactory {
    public static <T> MockTypeValidator<T> createValidator(Class<T> type, boolean isStatic, boolean isSpy, JavaAgentClassRegister agentClassRegister) {
        if (!isStatic || isSpy || isLoadedByBootstrap(type)) {
            return new NullMockTypeValidator();
        }
        if (agentClassRegister == null) {
            return new DefaultMockTypeValidator(type);
        }
        return new JavaAgentMockTypeValidator(type, agentClassRegister);
    }

    private static boolean isLoadedByBootstrap(Class type) {
        return type.getClassLoader() == null;
    }

    /* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/internal/mockcreation/MockTypeValidatorFactory$DefaultMockTypeValidator.class */
    private static class DefaultMockTypeValidator<T> extends AbstractMockTypeValidator<T> {
        DefaultMockTypeValidator(Class<T> type) {
            super(type);
        }

        @Override // org.powermock.api.mockito.internal.mockcreation.MockTypeValidatorFactory.AbstractMockTypeValidator, org.powermock.api.mockito.internal.mockcreation.MockTypeValidator
        public void validate() {
            if (!isModifiedByPowerMock()) {
                this.reporter.classNotPrepared(this.type);
            }
        }

        private boolean isModifiedByPowerMock() {
            return PowerMockModified.class.isAssignableFrom(this.type);
        }
    }

    /* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/internal/mockcreation/MockTypeValidatorFactory$JavaAgentMockTypeValidator.class */
    private static class JavaAgentMockTypeValidator<T> extends AbstractMockTypeValidator<T> {
        private final JavaAgentClassRegister agentClassRegister;

        private JavaAgentMockTypeValidator(Class<T> type, JavaAgentClassRegister agentClassRegister) {
            super(type);
            this.agentClassRegister = agentClassRegister;
        }

        @Override // org.powermock.api.mockito.internal.mockcreation.MockTypeValidatorFactory.AbstractMockTypeValidator, org.powermock.api.mockito.internal.mockcreation.MockTypeValidator
        public void validate() {
            if (!isModifiedByAgent()) {
                this.reporter.classNotPrepared(this.type);
            }
        }

        private boolean isModifiedByAgent() {
            return this.agentClassRegister.isModifiedByAgent(this.type.getClassLoader(), this.type.getName());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/internal/mockcreation/MockTypeValidatorFactory$AbstractMockTypeValidator.class */
    public static abstract class AbstractMockTypeValidator<T> implements MockTypeValidator<T> {
        final PowerMockReporter reporter;
        final Class<T> type;

        @Override // org.powermock.api.mockito.internal.mockcreation.MockTypeValidator
        public abstract void validate();

        private AbstractMockTypeValidator(Class<T> type) {
            this.type = type;
            this.reporter = new MockitoPowerMockReporter();
        }
    }

    /* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/internal/mockcreation/MockTypeValidatorFactory$NullMockTypeValidator.class */
    private static class NullMockTypeValidator<T> implements MockTypeValidator<T> {
        private NullMockTypeValidator() {
        }

        @Override // org.powermock.api.mockito.internal.mockcreation.MockTypeValidator
        public void validate() {
        }
    }
}
