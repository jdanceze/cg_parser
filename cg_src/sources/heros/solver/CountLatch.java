package heros.solver;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/solver/CountLatch.class */
public class CountLatch {
    private final Sync sync;

    /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/solver/CountLatch$Sync.class */
    private static final class Sync extends AbstractQueuedSynchronizer {
        Sync(int count) {
            setState(count);
        }

        int getCount() {
            return getState();
        }

        void reset() {
            setState(0);
        }

        @Override // java.util.concurrent.locks.AbstractQueuedSynchronizer
        protected int tryAcquireShared(int acquires) {
            return getState() == 0 ? 1 : -1;
        }

        protected int acquireNonBlocking(int acquires) {
            int c;
            int nextc;
            do {
                c = getState();
                nextc = c + 1;
            } while (!compareAndSetState(c, nextc));
            return 1;
        }

        @Override // java.util.concurrent.locks.AbstractQueuedSynchronizer
        protected boolean tryReleaseShared(int releases) {
            int c;
            int nextc;
            do {
                c = getState();
                if (c == 0) {
                    return false;
                }
                nextc = c - 1;
            } while (!compareAndSetState(c, nextc));
            return nextc == 0;
        }
    }

    public CountLatch(int count) {
        this.sync = new Sync(count);
    }

    public void awaitZero() throws InterruptedException {
        this.sync.acquireShared(1);
    }

    public boolean awaitZero(long timeout, TimeUnit unit) throws InterruptedException {
        return this.sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
    }

    public void increment() {
        this.sync.acquireNonBlocking(1);
    }

    public void decrement() {
        this.sync.releaseShared(1);
    }

    public void resetAndInterrupt() {
        this.sync.reset();
        for (int i = 0; i < 3; i++) {
            for (Thread t : this.sync.getQueuedThreads()) {
                t.interrupt();
            }
        }
        this.sync.reset();
    }

    public String toString() {
        return super.toString() + "[Count = " + this.sync.getCount() + "]";
    }

    public boolean isAtZero() {
        return this.sync.getCount() == 0;
    }
}
