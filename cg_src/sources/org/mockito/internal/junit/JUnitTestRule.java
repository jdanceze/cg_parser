package org.mockito.internal.junit;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mockito.junit.MockitoTestRule;
import org.mockito.plugins.MockitoLogger;
import org.mockito.quality.Strictness;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/junit/JUnitTestRule.class */
public final class JUnitTestRule implements MockitoTestRule {
    private final Object testInstance;
    private final JUnitSessionStore sessionStore;

    public JUnitTestRule(MockitoLogger logger, Strictness strictness, Object testInstance) {
        this.sessionStore = new JUnitSessionStore(logger, strictness);
        this.testInstance = testInstance;
    }

    @Override // org.junit.rules.TestRule
    public Statement apply(Statement base, Description description) {
        return this.sessionStore.createStatement(base, description.getDisplayName(), this.testInstance);
    }

    @Override // org.mockito.junit.MockitoTestRule
    public MockitoTestRule silent() {
        return strictness(Strictness.LENIENT);
    }

    @Override // org.mockito.junit.MockitoTestRule
    public MockitoTestRule strictness(Strictness strictness) {
        this.sessionStore.setStrictness(strictness);
        return this;
    }
}
