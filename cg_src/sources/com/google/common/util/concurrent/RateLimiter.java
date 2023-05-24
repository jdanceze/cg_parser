package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.SmoothRateLimiter;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.checkerframework.checker.nullness.compatqual.MonotonicNonNullDecl;
@Beta
@GwtIncompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/util/concurrent/RateLimiter.class */
public abstract class RateLimiter {
    private final SleepingStopwatch stopwatch;
    @MonotonicNonNullDecl
    private volatile Object mutexDoNotUseDirectly;

    abstract void doSetRate(double d, long j);

    abstract double doGetRate();

    abstract long queryEarliestAvailable(long j);

    abstract long reserveEarliestAvailable(int i, long j);

    public static RateLimiter create(double permitsPerSecond) {
        return create(permitsPerSecond, SleepingStopwatch.createFromSystemTimer());
    }

    @VisibleForTesting
    static RateLimiter create(double permitsPerSecond, SleepingStopwatch stopwatch) {
        RateLimiter rateLimiter = new SmoothRateLimiter.SmoothBursty(stopwatch, 1.0d);
        rateLimiter.setRate(permitsPerSecond);
        return rateLimiter;
    }

    public static RateLimiter create(double permitsPerSecond, long warmupPeriod, TimeUnit unit) {
        Preconditions.checkArgument(warmupPeriod >= 0, "warmupPeriod must not be negative: %s", warmupPeriod);
        return create(permitsPerSecond, warmupPeriod, unit, 3.0d, SleepingStopwatch.createFromSystemTimer());
    }

    @VisibleForTesting
    static RateLimiter create(double permitsPerSecond, long warmupPeriod, TimeUnit unit, double coldFactor, SleepingStopwatch stopwatch) {
        RateLimiter rateLimiter = new SmoothRateLimiter.SmoothWarmingUp(stopwatch, warmupPeriod, unit, coldFactor);
        rateLimiter.setRate(permitsPerSecond);
        return rateLimiter;
    }

    private Object mutex() {
        Object mutex = this.mutexDoNotUseDirectly;
        if (mutex == null) {
            synchronized (this) {
                mutex = this.mutexDoNotUseDirectly;
                if (mutex == null) {
                    Object obj = new Object();
                    mutex = obj;
                    this.mutexDoNotUseDirectly = obj;
                }
            }
        }
        return mutex;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RateLimiter(SleepingStopwatch stopwatch) {
        this.stopwatch = (SleepingStopwatch) Preconditions.checkNotNull(stopwatch);
    }

    public final void setRate(double permitsPerSecond) {
        Preconditions.checkArgument(permitsPerSecond > Const.default_value_double && !Double.isNaN(permitsPerSecond), "rate must be positive");
        synchronized (mutex()) {
            doSetRate(permitsPerSecond, this.stopwatch.readMicros());
        }
    }

    public final double getRate() {
        double doGetRate;
        synchronized (mutex()) {
            doGetRate = doGetRate();
        }
        return doGetRate;
    }

    @CanIgnoreReturnValue
    public double acquire() {
        return acquire(1);
    }

    @CanIgnoreReturnValue
    public double acquire(int permits) {
        long microsToWait = reserve(permits);
        this.stopwatch.sleepMicrosUninterruptibly(microsToWait);
        return (1.0d * microsToWait) / TimeUnit.SECONDS.toMicros(1L);
    }

    final long reserve(int permits) {
        long reserveAndGetWaitLength;
        checkPermits(permits);
        synchronized (mutex()) {
            reserveAndGetWaitLength = reserveAndGetWaitLength(permits, this.stopwatch.readMicros());
        }
        return reserveAndGetWaitLength;
    }

    public boolean tryAcquire(long timeout, TimeUnit unit) {
        return tryAcquire(1, timeout, unit);
    }

    public boolean tryAcquire(int permits) {
        return tryAcquire(permits, 0L, TimeUnit.MICROSECONDS);
    }

    public boolean tryAcquire() {
        return tryAcquire(1, 0L, TimeUnit.MICROSECONDS);
    }

    public boolean tryAcquire(int permits, long timeout, TimeUnit unit) {
        long timeoutMicros = Math.max(unit.toMicros(timeout), 0L);
        checkPermits(permits);
        synchronized (mutex()) {
            long nowMicros = this.stopwatch.readMicros();
            if (!canAcquire(nowMicros, timeoutMicros)) {
                return false;
            }
            long microsToWait = reserveAndGetWaitLength(permits, nowMicros);
            this.stopwatch.sleepMicrosUninterruptibly(microsToWait);
            return true;
        }
    }

    private boolean canAcquire(long nowMicros, long timeoutMicros) {
        return queryEarliestAvailable(nowMicros) - timeoutMicros <= nowMicros;
    }

    final long reserveAndGetWaitLength(int permits, long nowMicros) {
        long momentAvailable = reserveEarliestAvailable(permits, nowMicros);
        return Math.max(momentAvailable - nowMicros, 0L);
    }

    public String toString() {
        return String.format(Locale.ROOT, "RateLimiter[stableRate=%3.1fqps]", Double.valueOf(getRate()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/util/concurrent/RateLimiter$SleepingStopwatch.class */
    public static abstract class SleepingStopwatch {
        protected abstract long readMicros();

        protected abstract void sleepMicrosUninterruptibly(long j);

        protected SleepingStopwatch() {
        }

        public static SleepingStopwatch createFromSystemTimer() {
            return new SleepingStopwatch() { // from class: com.google.common.util.concurrent.RateLimiter.SleepingStopwatch.1
                final Stopwatch stopwatch = Stopwatch.createStarted();

                @Override // com.google.common.util.concurrent.RateLimiter.SleepingStopwatch
                protected long readMicros() {
                    return this.stopwatch.elapsed(TimeUnit.MICROSECONDS);
                }

                @Override // com.google.common.util.concurrent.RateLimiter.SleepingStopwatch
                protected void sleepMicrosUninterruptibly(long micros) {
                    if (micros > 0) {
                        Uninterruptibles.sleepUninterruptibly(micros, TimeUnit.MICROSECONDS);
                    }
                }
            };
        }
    }

    private static void checkPermits(int permits) {
        Preconditions.checkArgument(permits > 0, "Requested permits (%s) must be positive", permits);
    }
}
