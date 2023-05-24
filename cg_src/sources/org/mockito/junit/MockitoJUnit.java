package org.mockito.junit;

import org.mockito.Incubating;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.junit.JUnitRule;
import org.mockito.internal.junit.JUnitTestRule;
import org.mockito.internal.junit.VerificationCollectorImpl;
import org.mockito.quality.Strictness;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/junit/MockitoJUnit.class */
public class MockitoJUnit {
    public static MockitoRule rule() {
        return new JUnitRule(Plugins.getMockitoLogger(), Strictness.WARN);
    }

    public static MockitoTestRule testRule(Object testInstance) {
        return new JUnitTestRule(Plugins.getMockitoLogger(), Strictness.WARN, testInstance);
    }

    @Incubating
    public static VerificationCollector collector() {
        return new VerificationCollectorImpl();
    }
}
