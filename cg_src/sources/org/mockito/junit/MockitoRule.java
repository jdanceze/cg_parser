package org.mockito.junit;

import org.junit.rules.MethodRule;
import org.mockito.Incubating;
import org.mockito.quality.Strictness;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/junit/MockitoRule.class */
public interface MockitoRule extends MethodRule {
    MockitoRule silent();

    @Incubating
    MockitoRule strictness(Strictness strictness);
}
