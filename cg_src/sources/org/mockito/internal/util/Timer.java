package org.mockito.internal.util;

import org.mockito.internal.exceptions.Reporter;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/Timer.class */
public class Timer {
    private final long durationMillis;
    private long startTime = -1;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Timer.class.desiredAssertionStatus();
    }

    public Timer(long durationMillis) {
        validateInput(durationMillis);
        this.durationMillis = durationMillis;
    }

    public boolean isCounting() {
        if ($assertionsDisabled || this.startTime != -1) {
            return System.currentTimeMillis() - this.startTime <= this.durationMillis;
        }
        throw new AssertionError();
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
    }

    private void validateInput(long durationMillis) {
        if (durationMillis < 0) {
            throw Reporter.cannotCreateTimerWithNegativeDurationTime(durationMillis);
        }
    }

    public long duration() {
        return this.durationMillis;
    }
}
