package org.mockito.internal.configuration.injection;

import java.lang.reflect.Field;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/configuration/injection/MockInjectionStrategy.class */
public abstract class MockInjectionStrategy {
    private MockInjectionStrategy nextStrategy;

    protected abstract boolean processInjection(Field field, Object obj, Set<Object> set);

    public static MockInjectionStrategy nop() {
        return new MockInjectionStrategy() { // from class: org.mockito.internal.configuration.injection.MockInjectionStrategy.1
            @Override // org.mockito.internal.configuration.injection.MockInjectionStrategy
            protected boolean processInjection(Field field, Object fieldOwner, Set<Object> mockCandidates) {
                return false;
            }
        };
    }

    public MockInjectionStrategy thenTry(MockInjectionStrategy strategy) {
        if (this.nextStrategy != null) {
            this.nextStrategy.thenTry(strategy);
        } else {
            this.nextStrategy = strategy;
        }
        return strategy;
    }

    public boolean process(Field onField, Object fieldOwnedBy, Set<Object> mockCandidates) {
        if (processInjection(onField, fieldOwnedBy, mockCandidates)) {
            return true;
        }
        return relayProcessToNextStrategy(onField, fieldOwnedBy, mockCandidates);
    }

    private boolean relayProcessToNextStrategy(Field field, Object fieldOwner, Set<Object> mockCandidates) {
        return this.nextStrategy != null && this.nextStrategy.process(field, fieldOwner, mockCandidates);
    }
}
