package org.mockito.junit;

import org.junit.rules.TestRule;
import org.mockito.Incubating;
import org.mockito.exceptions.base.MockitoAssertionError;
@Incubating
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/junit/VerificationCollector.class */
public interface VerificationCollector extends TestRule {
    @Incubating
    void collectAndReport() throws MockitoAssertionError;

    @Incubating
    VerificationCollector assertLazily();
}
