package org.mockito.internal.session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.mockito.MockitoSession;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.framework.DefaultMockitoSession;
import org.mockito.plugins.MockitoLogger;
import org.mockito.quality.Strictness;
import org.mockito.session.MockitoSessionBuilder;
import org.mockito.session.MockitoSessionLogger;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/session/DefaultMockitoSessionBuilder.class */
public class DefaultMockitoSessionBuilder implements MockitoSessionBuilder {
    private List<Object> testClassInstances = new ArrayList();
    private String name;
    private Strictness strictness;
    private MockitoSessionLogger logger;

    @Override // org.mockito.session.MockitoSessionBuilder
    public MockitoSessionBuilder initMocks(Object testClassInstance) {
        if (testClassInstance != null) {
            this.testClassInstances.add(testClassInstance);
        }
        return this;
    }

    @Override // org.mockito.session.MockitoSessionBuilder
    public MockitoSessionBuilder initMocks(Object... testClassInstances) {
        if (testClassInstances != null) {
            for (Object instance : testClassInstances) {
                initMocks(instance);
            }
        }
        return this;
    }

    @Override // org.mockito.session.MockitoSessionBuilder
    public MockitoSessionBuilder name(String name) {
        this.name = name;
        return this;
    }

    @Override // org.mockito.session.MockitoSessionBuilder
    public MockitoSessionBuilder strictness(Strictness strictness) {
        this.strictness = strictness;
        return this;
    }

    @Override // org.mockito.session.MockitoSessionBuilder
    public MockitoSessionBuilder logger(MockitoSessionLogger logger) {
        this.logger = logger;
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.mockito.session.MockitoSessionBuilder
    public MockitoSession startMocking() {
        List<Object> effectiveTestClassInstances;
        String effectiveName;
        MockitoLogger mockitoLoggerAdapter;
        if (this.testClassInstances.isEmpty()) {
            effectiveTestClassInstances = Collections.emptyList();
            effectiveName = this.name == null ? "<Unnamed Session>" : this.name;
        } else {
            effectiveTestClassInstances = new ArrayList<>(this.testClassInstances);
            Object lastTestClassInstance = this.testClassInstances.get(this.testClassInstances.size() - 1);
            effectiveName = this.name == null ? lastTestClassInstance.getClass().getName() : this.name;
        }
        Strictness effectiveStrictness = this.strictness == null ? Strictness.STRICT_STUBS : this.strictness;
        if (this.logger == null) {
            mockitoLoggerAdapter = Plugins.getMockitoLogger();
        } else {
            mockitoLoggerAdapter = new MockitoLoggerAdapter(this.logger);
        }
        MockitoLogger logger = mockitoLoggerAdapter;
        return new DefaultMockitoSession(effectiveTestClassInstances, effectiveName, effectiveStrictness, logger);
    }
}
