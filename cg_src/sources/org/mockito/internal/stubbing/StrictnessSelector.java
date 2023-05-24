package org.mockito.internal.stubbing;

import org.mockito.mock.MockCreationSettings;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Stubbing;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/StrictnessSelector.class */
public class StrictnessSelector {
    public static Strictness determineStrictness(Stubbing stubbing, MockCreationSettings mockSettings, Strictness testLevelStrictness) {
        if (stubbing != null && stubbing.getStrictness() != null) {
            return stubbing.getStrictness();
        }
        if (mockSettings.isLenient()) {
            return Strictness.LENIENT;
        }
        return testLevelStrictness;
    }
}
