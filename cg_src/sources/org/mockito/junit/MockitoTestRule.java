package org.mockito.junit;

import org.junit.rules.TestRule;
import org.mockito.Incubating;
import org.mockito.quality.Strictness;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/junit/MockitoTestRule.class */
public interface MockitoTestRule extends TestRule {
    MockitoTestRule silent();

    @Incubating
    MockitoTestRule strictness(Strictness strictness);
}
