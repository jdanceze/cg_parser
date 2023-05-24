package soot.util;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
/* loaded from: gencallgraphv3.jar:soot/util/SharedCloseable.class */
public final class SharedCloseable<T extends Closeable> implements AutoCloseable {
    private static final AtomicLongFieldUpdater<SharedCloseable> NUM_REFS_UPDATER = AtomicLongFieldUpdater.newUpdater(SharedCloseable.class, "numRefs");
    private volatile long numRefs = 1;
    private final T resource;

    public SharedCloseable(T resource) {
        this.resource = resource;
    }

    public SharedCloseable<T> acquire() {
        long oldCount = NUM_REFS_UPDATER.getAndIncrement(this);
        if (oldCount <= 0) {
            NUM_REFS_UPDATER.getAndDecrement(this);
            throw new IllegalStateException("Already closed");
        }
        return this;
    }

    public boolean release() {
        long newCount = NUM_REFS_UPDATER.decrementAndGet(this);
        if (newCount == 0) {
            try {
                this.resource.close();
                return true;
            } catch (IOException e) {
                return true;
            }
        } else if (newCount < 0) {
            throw new IllegalStateException("Already closed");
        } else {
            return false;
        }
    }

    public T get() {
        return this.resource;
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        release();
    }
}
