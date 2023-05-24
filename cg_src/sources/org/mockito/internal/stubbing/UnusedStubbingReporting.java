package org.mockito.internal.stubbing;

import org.mockito.quality.Strictness;
import org.mockito.stubbing.Stubbing;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/UnusedStubbingReporting.class */
public class UnusedStubbingReporting {
    public static boolean shouldBeReported(Stubbing stubbing) {
        return (stubbing.wasUsed() || stubbing.getStrictness() == Strictness.LENIENT) ? false : true;
    }
}
