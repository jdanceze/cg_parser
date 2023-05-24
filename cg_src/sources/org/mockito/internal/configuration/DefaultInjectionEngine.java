package org.mockito.internal.configuration;

import java.lang.reflect.Field;
import java.util.Set;
import org.mockito.internal.configuration.injection.MockInjection;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/configuration/DefaultInjectionEngine.class */
public class DefaultInjectionEngine {
    public void injectMocksOnFields(Set<Field> needingInjection, Set<Object> mocks, Object testClassInstance) {
        MockInjection.onFields(needingInjection, testClassInstance).withMocks(mocks).tryConstructorInjection().tryPropertyOrFieldInjection().handleSpyAnnotation().apply();
    }
}
