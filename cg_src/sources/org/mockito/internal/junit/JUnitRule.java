package org.mockito.internal.junit;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.mockito.junit.MockitoRule;
import org.mockito.plugins.MockitoLogger;
import org.mockito.quality.Strictness;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/junit/JUnitRule.class */
public final class JUnitRule implements MockitoRule {
    private final JUnitSessionStore sessionStore;

    public JUnitRule(MockitoLogger logger, Strictness strictness) {
        this.sessionStore = new JUnitSessionStore(logger, strictness);
    }

    @Override // org.junit.rules.MethodRule
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        return this.sessionStore.createStatement(base, target.getClass().getSimpleName() + "." + method.getName(), target);
    }

    @Override // org.mockito.junit.MockitoRule
    public MockitoRule silent() {
        return strictness(Strictness.LENIENT);
    }

    @Override // org.mockito.junit.MockitoRule
    public MockitoRule strictness(Strictness strictness) {
        this.sessionStore.setStrictness(strictness);
        return this;
    }
}
