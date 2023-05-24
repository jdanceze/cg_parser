package org.mockito.internal.util.concurrent;

import java.util.Iterator;
import java.util.Map;
import org.mockito.internal.util.concurrent.WeakConcurrentMap;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/concurrent/WeakConcurrentSet.class */
public class WeakConcurrentSet<V> implements Runnable, Iterable<V> {
    final WeakConcurrentMap<V, Boolean> target;

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/concurrent/WeakConcurrentSet$Cleaner.class */
    public enum Cleaner {
        THREAD,
        INLINE,
        MANUAL
    }

    public WeakConcurrentSet(Cleaner cleaner) {
        switch (cleaner) {
            case INLINE:
                this.target = new WeakConcurrentMap.WithInlinedExpunction();
                return;
            case THREAD:
            case MANUAL:
                this.target = new WeakConcurrentMap<>(cleaner == Cleaner.THREAD);
                return;
            default:
                throw new AssertionError();
        }
    }

    public boolean add(V value) {
        return this.target.put(value, Boolean.TRUE) == null;
    }

    public boolean contains(V value) {
        return this.target.containsKey(value);
    }

    public boolean remove(V value) {
        return this.target.remove((WeakConcurrentMap<V, Boolean>) value) != null;
    }

    public void clear() {
        this.target.clear();
    }

    public int approximateSize() {
        return this.target.approximateSize();
    }

    @Override // java.lang.Runnable
    public void run() {
        this.target.run();
    }

    public void expungeStaleEntries() {
        this.target.expungeStaleEntries();
    }

    public Thread getCleanerThread() {
        return this.target.getCleanerThread();
    }

    @Override // java.lang.Iterable
    public Iterator<V> iterator() {
        return new ReducingIterator(this.target.iterator());
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/concurrent/WeakConcurrentSet$ReducingIterator.class */
    private static class ReducingIterator<V> implements Iterator<V> {
        private final Iterator<Map.Entry<V, Boolean>> iterator;

        private ReducingIterator(Iterator<Map.Entry<V, Boolean>> iterator) {
            this.iterator = iterator;
        }

        @Override // java.util.Iterator
        public void remove() {
            this.iterator.remove();
        }

        @Override // java.util.Iterator
        public V next() {
            return this.iterator.next().getKey();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.iterator.hasNext();
        }
    }
}
