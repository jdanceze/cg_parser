package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.concurrent.GuardedBy;
import com.google.j2objc.annotations.Weak;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.tools.ant.taskdefs.optional.clearcase.ClearCase;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
@Beta
@GwtIncompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/util/concurrent/Monitor.class */
public final class Monitor {
    private final boolean fair;
    private final ReentrantLock lock;
    @GuardedBy(ClearCase.COMMAND_LOCK)
    private Guard activeGuards;

    @Beta
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/util/concurrent/Monitor$Guard.class */
    public static abstract class Guard {
        @Weak
        final Monitor monitor;
        final Condition condition;
        @GuardedBy("monitor.lock")
        int waiterCount = 0;
        @NullableDecl
        @GuardedBy("monitor.lock")
        Guard next;

        public abstract boolean isSatisfied();

        /* JADX INFO: Access modifiers changed from: protected */
        public Guard(Monitor monitor) {
            this.monitor = (Monitor) Preconditions.checkNotNull(monitor, "monitor");
            this.condition = monitor.lock.newCondition();
        }
    }

    public Monitor() {
        this(false);
    }

    public Monitor(boolean fair) {
        this.activeGuards = null;
        this.fair = fair;
        this.lock = new ReentrantLock(fair);
    }

    public void enter() {
        this.lock.lock();
    }

    public boolean enter(long time, TimeUnit unit) {
        boolean tryLock;
        long timeoutNanos = toSafeNanos(time, unit);
        ReentrantLock lock = this.lock;
        if (!this.fair && lock.tryLock()) {
            return true;
        }
        boolean interrupted = Thread.interrupted();
        try {
            long startTime = System.nanoTime();
            long remainingNanos = timeoutNanos;
            while (true) {
                try {
                    tryLock = lock.tryLock(remainingNanos, TimeUnit.NANOSECONDS);
                    break;
                } catch (InterruptedException e) {
                    interrupted = true;
                    remainingNanos = remainingNanos(startTime, timeoutNanos);
                }
            }
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
            return tryLock;
        } catch (Throwable th) {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
            throw th;
        }
    }

    public void enterInterruptibly() throws InterruptedException {
        this.lock.lockInterruptibly();
    }

    public boolean enterInterruptibly(long time, TimeUnit unit) throws InterruptedException {
        return this.lock.tryLock(time, unit);
    }

    public boolean tryEnter() {
        return this.lock.tryLock();
    }

    public void enterWhen(Guard guard) throws InterruptedException {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
        lock.lockInterruptibly();
        boolean satisfied = false;
        try {
            if (!guard.isSatisfied()) {
                await(guard, signalBeforeWaiting);
            }
            satisfied = true;
        } finally {
            if (!satisfied) {
                leave();
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0042, code lost:
        if (r0.tryLock() != false) goto L14;
     */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0099  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean enterWhen(com.google.common.util.concurrent.Monitor.Guard r8, long r9, java.util.concurrent.TimeUnit r11) throws java.lang.InterruptedException {
        /*
            Method dump skipped, instructions count: 230
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.Monitor.enterWhen(com.google.common.util.concurrent.Monitor$Guard, long, java.util.concurrent.TimeUnit):boolean");
    }

    public void enterWhenUninterruptibly(Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
        lock.lock();
        boolean satisfied = false;
        try {
            if (!guard.isSatisfied()) {
                awaitUninterruptibly(guard, signalBeforeWaiting);
            }
            satisfied = true;
        } finally {
            if (!satisfied) {
                leave();
            }
        }
    }

    public boolean enterWhenUninterruptibly(Guard guard, long time, TimeUnit unit) {
        long remainingNanos;
        long timeoutNanos = toSafeNanos(time, unit);
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        long startTime = 0;
        boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
        boolean interrupted = Thread.interrupted();
        try {
            if (this.fair || !lock.tryLock()) {
                startTime = initNanoTime(timeoutNanos);
                long remainingNanos2 = timeoutNanos;
                while (true) {
                    try {
                        break;
                    } catch (InterruptedException e) {
                        interrupted = true;
                        remainingNanos2 = remainingNanos(startTime, timeoutNanos);
                    }
                }
                if (!lock.tryLock(remainingNanos2, TimeUnit.NANOSECONDS)) {
                    return false;
                }
            }
            boolean satisfied = false;
            while (true) {
                try {
                    if (guard.isSatisfied()) {
                        satisfied = true;
                        break;
                    }
                    if (startTime == 0) {
                        startTime = initNanoTime(timeoutNanos);
                        remainingNanos = timeoutNanos;
                    } else {
                        remainingNanos = remainingNanos(startTime, timeoutNanos);
                    }
                    satisfied = awaitNanos(guard, remainingNanos, signalBeforeWaiting);
                    break;
                } catch (InterruptedException e2) {
                    interrupted = true;
                    signalBeforeWaiting = false;
                } catch (Throwable th) {
                    if (!satisfied) {
                        lock.unlock();
                    }
                    throw th;
                }
            }
            boolean z = satisfied;
            if (!satisfied) {
                lock.unlock();
            }
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
            return z;
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public boolean enterIf(Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        lock.lock();
        boolean satisfied = false;
        try {
            boolean isSatisfied = guard.isSatisfied();
            satisfied = isSatisfied;
            if (!satisfied) {
                lock.unlock();
            }
            return isSatisfied;
        } catch (Throwable th) {
            if (!satisfied) {
                lock.unlock();
            }
            throw th;
        }
    }

    public boolean enterIf(Guard guard, long time, TimeUnit unit) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        if (!enter(time, unit)) {
            return false;
        }
        boolean satisfied = false;
        try {
            boolean isSatisfied = guard.isSatisfied();
            satisfied = isSatisfied;
            if (!satisfied) {
                this.lock.unlock();
            }
            return isSatisfied;
        } catch (Throwable th) {
            if (!satisfied) {
                this.lock.unlock();
            }
            throw th;
        }
    }

    public boolean enterIfInterruptibly(Guard guard) throws InterruptedException {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        boolean satisfied = false;
        try {
            boolean isSatisfied = guard.isSatisfied();
            satisfied = isSatisfied;
            if (!satisfied) {
                lock.unlock();
            }
            return isSatisfied;
        } catch (Throwable th) {
            if (!satisfied) {
                lock.unlock();
            }
            throw th;
        }
    }

    public boolean enterIfInterruptibly(Guard guard, long time, TimeUnit unit) throws InterruptedException {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        if (!lock.tryLock(time, unit)) {
            return false;
        }
        boolean satisfied = false;
        try {
            boolean isSatisfied = guard.isSatisfied();
            satisfied = isSatisfied;
            if (!satisfied) {
                lock.unlock();
            }
            return isSatisfied;
        } catch (Throwable th) {
            if (!satisfied) {
                lock.unlock();
            }
            throw th;
        }
    }

    public boolean tryEnterIf(Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        if (!lock.tryLock()) {
            return false;
        }
        boolean satisfied = false;
        try {
            boolean isSatisfied = guard.isSatisfied();
            satisfied = isSatisfied;
            if (!satisfied) {
                lock.unlock();
            }
            return isSatisfied;
        } catch (Throwable th) {
            if (!satisfied) {
                lock.unlock();
            }
            throw th;
        }
    }

    public void waitFor(Guard guard) throws InterruptedException {
        if (!((guard.monitor == this) & this.lock.isHeldByCurrentThread())) {
            throw new IllegalMonitorStateException();
        }
        if (!guard.isSatisfied()) {
            await(guard, true);
        }
    }

    public boolean waitFor(Guard guard, long time, TimeUnit unit) throws InterruptedException {
        long timeoutNanos = toSafeNanos(time, unit);
        if (!((guard.monitor == this) & this.lock.isHeldByCurrentThread())) {
            throw new IllegalMonitorStateException();
        }
        if (guard.isSatisfied()) {
            return true;
        }
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        return awaitNanos(guard, timeoutNanos, true);
    }

    public void waitForUninterruptibly(Guard guard) {
        if (!((guard.monitor == this) & this.lock.isHeldByCurrentThread())) {
            throw new IllegalMonitorStateException();
        }
        if (!guard.isSatisfied()) {
            awaitUninterruptibly(guard, true);
        }
    }

    public boolean waitForUninterruptibly(Guard guard, long time, TimeUnit unit) {
        long timeoutNanos = toSafeNanos(time, unit);
        if (!((guard.monitor == this) & this.lock.isHeldByCurrentThread())) {
            throw new IllegalMonitorStateException();
        }
        if (guard.isSatisfied()) {
            return true;
        }
        boolean signalBeforeWaiting = true;
        long startTime = initNanoTime(timeoutNanos);
        boolean interrupted = Thread.interrupted();
        long remainingNanos = timeoutNanos;
        while (true) {
            try {
                try {
                    boolean awaitNanos = awaitNanos(guard, remainingNanos, signalBeforeWaiting);
                    if (interrupted) {
                        Thread.currentThread().interrupt();
                    }
                    return awaitNanos;
                } catch (InterruptedException e) {
                    interrupted = true;
                    if (!guard.isSatisfied()) {
                        signalBeforeWaiting = false;
                        remainingNanos = remainingNanos(startTime, timeoutNanos);
                    } else {
                        if (1 != 0) {
                            Thread.currentThread().interrupt();
                        }
                        return true;
                    }
                }
            } catch (Throwable th) {
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
                throw th;
            }
        }
    }

    public void leave() {
        ReentrantLock lock = this.lock;
        try {
            if (lock.getHoldCount() == 1) {
                signalNextWaiter();
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean isFair() {
        return this.fair;
    }

    public boolean isOccupied() {
        return this.lock.isLocked();
    }

    public boolean isOccupiedByCurrentThread() {
        return this.lock.isHeldByCurrentThread();
    }

    public int getOccupiedDepth() {
        return this.lock.getHoldCount();
    }

    public int getQueueLength() {
        return this.lock.getQueueLength();
    }

    public boolean hasQueuedThreads() {
        return this.lock.hasQueuedThreads();
    }

    public boolean hasQueuedThread(Thread thread) {
        return this.lock.hasQueuedThread(thread);
    }

    public boolean hasWaiters(Guard guard) {
        return getWaitQueueLength(guard) > 0;
    }

    public int getWaitQueueLength(Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        this.lock.lock();
        try {
            return guard.waiterCount;
        } finally {
            this.lock.unlock();
        }
    }

    private static long toSafeNanos(long time, TimeUnit unit) {
        long timeoutNanos = unit.toNanos(time);
        if (timeoutNanos <= 0) {
            return 0L;
        }
        if (timeoutNanos > 6917529027641081853L) {
            return 6917529027641081853L;
        }
        return timeoutNanos;
    }

    private static long initNanoTime(long timeoutNanos) {
        if (timeoutNanos <= 0) {
            return 0L;
        }
        long startTime = System.nanoTime();
        if (startTime == 0) {
            return 1L;
        }
        return startTime;
    }

    private static long remainingNanos(long startTime, long timeoutNanos) {
        if (timeoutNanos <= 0) {
            return 0L;
        }
        return timeoutNanos - (System.nanoTime() - startTime);
    }

    @GuardedBy(ClearCase.COMMAND_LOCK)
    private void signalNextWaiter() {
        Guard guard = this.activeGuards;
        while (true) {
            Guard guard2 = guard;
            if (guard2 != null) {
                if (!isSatisfied(guard2)) {
                    guard = guard2.next;
                } else {
                    guard2.condition.signal();
                    return;
                }
            } else {
                return;
            }
        }
    }

    @GuardedBy(ClearCase.COMMAND_LOCK)
    private boolean isSatisfied(Guard guard) {
        try {
            return guard.isSatisfied();
        } catch (Throwable throwable) {
            signalAllWaiters();
            throw throwable;
        }
    }

    @GuardedBy(ClearCase.COMMAND_LOCK)
    private void signalAllWaiters() {
        Guard guard = this.activeGuards;
        while (true) {
            Guard guard2 = guard;
            if (guard2 != null) {
                guard2.condition.signalAll();
                guard = guard2.next;
            } else {
                return;
            }
        }
    }

    @GuardedBy(ClearCase.COMMAND_LOCK)
    private void beginWaitingFor(Guard guard) {
        int waiters = guard.waiterCount;
        guard.waiterCount = waiters + 1;
        if (waiters == 0) {
            guard.next = this.activeGuards;
            this.activeGuards = guard;
        }
    }

    @GuardedBy(ClearCase.COMMAND_LOCK)
    private void endWaitingFor(Guard guard) {
        int waiters = guard.waiterCount - 1;
        guard.waiterCount = waiters;
        if (waiters == 0) {
            Guard p = this.activeGuards;
            Guard pred = null;
            while (p != guard) {
                pred = p;
                p = p.next;
            }
            if (pred == null) {
                this.activeGuards = p.next;
            } else {
                pred.next = p.next;
            }
            p.next = null;
        }
    }

    @GuardedBy(ClearCase.COMMAND_LOCK)
    private void await(Guard guard, boolean signalBeforeWaiting) throws InterruptedException {
        if (signalBeforeWaiting) {
            signalNextWaiter();
        }
        beginWaitingFor(guard);
        do {
            try {
                guard.condition.await();
            } finally {
                endWaitingFor(guard);
            }
        } while (!guard.isSatisfied());
    }

    @GuardedBy(ClearCase.COMMAND_LOCK)
    private void awaitUninterruptibly(Guard guard, boolean signalBeforeWaiting) {
        if (signalBeforeWaiting) {
            signalNextWaiter();
        }
        beginWaitingFor(guard);
        do {
            try {
                guard.condition.awaitUninterruptibly();
            } finally {
                endWaitingFor(guard);
            }
        } while (!guard.isSatisfied());
    }

    @GuardedBy(ClearCase.COMMAND_LOCK)
    private boolean awaitNanos(Guard guard, long nanos, boolean signalBeforeWaiting) throws InterruptedException {
        boolean firstTime = true;
        while (nanos > 0) {
            if (firstTime) {
                if (signalBeforeWaiting) {
                    try {
                        signalNextWaiter();
                    } finally {
                        if (!firstTime) {
                            endWaitingFor(guard);
                        }
                    }
                }
                beginWaitingFor(guard);
                firstTime = false;
            }
            nanos = guard.condition.awaitNanos(nanos);
            if (guard.isSatisfied()) {
                if (!firstTime) {
                    endWaitingFor(guard);
                }
                return true;
            }
        }
        return false;
    }
}
