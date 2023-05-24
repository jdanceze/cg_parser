package org.mockito.internal.util.concurrent;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/concurrent/WeakConcurrentMap.class */
public class WeakConcurrentMap<K, V> extends ReferenceQueue<K> implements Runnable, Iterable<Map.Entry<K, V>> {
    private static final AtomicLong ID = new AtomicLong();
    public final ConcurrentMap<WeakKey<K>, V> target = new ConcurrentHashMap();
    private final Thread thread;

    public WeakConcurrentMap(boolean cleanerThread) {
        if (cleanerThread) {
            this.thread = new Thread(this);
            this.thread.setName("weak-ref-cleaner-" + ID.getAndIncrement());
            this.thread.setPriority(1);
            this.thread.setDaemon(true);
            this.thread.start();
            return;
        }
        this.thread = null;
    }

    public V get(K key) {
        V previousValue;
        if (key == null) {
            throw new NullPointerException();
        }
        V value = this.target.get(new LatentKey(key));
        if (value == null) {
            value = defaultValue(key);
            if (value != null && (previousValue = this.target.putIfAbsent(new WeakKey<>(key, this), value)) != null) {
                value = previousValue;
            }
        }
        return value;
    }

    public boolean containsKey(K key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return this.target.containsKey(new LatentKey(key));
    }

    public V put(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException();
        }
        return this.target.put(new WeakKey<>(key, this), value);
    }

    public V remove(K key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return this.target.remove(new LatentKey(key));
    }

    public void clear() {
        this.target.clear();
    }

    protected V defaultValue(K key) {
        return null;
    }

    public Thread getCleanerThread() {
        return this.thread;
    }

    public void expungeStaleEntries() {
        while (true) {
            Reference<?> reference = poll();
            if (reference != null) {
                this.target.remove(reference);
            } else {
                return;
            }
        }
    }

    public int approximateSize() {
        return this.target.size();
    }

    @Override // java.lang.Runnable
    public void run() {
        while (true) {
            try {
                this.target.remove(remove());
            } catch (InterruptedException e) {
                clear();
                return;
            }
        }
    }

    @Override // java.lang.Iterable
    public Iterator<Map.Entry<K, V>> iterator() {
        return new EntryIterator(this.target.entrySet().iterator());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/concurrent/WeakConcurrentMap$WeakKey.class */
    public static class WeakKey<T> extends WeakReference<T> {
        private final int hashCode;

        WeakKey(T key, ReferenceQueue<? super T> queue) {
            super(key, queue);
            this.hashCode = System.identityHashCode(key);
        }

        public int hashCode() {
            return this.hashCode;
        }

        public boolean equals(Object other) {
            return other instanceof LatentKey ? ((LatentKey) other).key == get() : ((WeakKey) other).get() == get();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/concurrent/WeakConcurrentMap$LatentKey.class */
    public static class LatentKey<T> {
        final T key;
        private final int hashCode;

        LatentKey(T key) {
            this.key = key;
            this.hashCode = System.identityHashCode(key);
        }

        public boolean equals(Object other) {
            return other instanceof LatentKey ? ((LatentKey) other).key == this.key : ((WeakKey) other).get() == this.key;
        }

        public int hashCode() {
            return this.hashCode;
        }
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/concurrent/WeakConcurrentMap$WithInlinedExpunction.class */
    public static class WithInlinedExpunction<K, V> extends WeakConcurrentMap<K, V> {
        public WithInlinedExpunction() {
            super(false);
        }

        @Override // org.mockito.internal.util.concurrent.WeakConcurrentMap
        public V get(K key) {
            expungeStaleEntries();
            return (V) super.get(key);
        }

        @Override // org.mockito.internal.util.concurrent.WeakConcurrentMap
        public boolean containsKey(K key) {
            expungeStaleEntries();
            return super.containsKey(key);
        }

        @Override // org.mockito.internal.util.concurrent.WeakConcurrentMap
        public V put(K key, V value) {
            expungeStaleEntries();
            return (V) super.put(key, value);
        }

        @Override // org.mockito.internal.util.concurrent.WeakConcurrentMap
        public V remove(K key) {
            expungeStaleEntries();
            return (V) super.remove((WithInlinedExpunction<K, V>) key);
        }

        @Override // org.mockito.internal.util.concurrent.WeakConcurrentMap, java.lang.Iterable
        public Iterator<Map.Entry<K, V>> iterator() {
            expungeStaleEntries();
            return super.iterator();
        }

        @Override // org.mockito.internal.util.concurrent.WeakConcurrentMap
        public int approximateSize() {
            expungeStaleEntries();
            return super.approximateSize();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/concurrent/WeakConcurrentMap$EntryIterator.class */
    public class EntryIterator implements Iterator<Map.Entry<K, V>> {
        private final Iterator<Map.Entry<WeakKey<K>, V>> iterator;
        private Map.Entry<WeakKey<K>, V> nextEntry;
        private K nextKey;

        private EntryIterator(Iterator<Map.Entry<WeakKey<K>, V>> iterator) {
            this.iterator = iterator;
            findNext();
        }

        private void findNext() {
            while (this.iterator.hasNext()) {
                this.nextEntry = this.iterator.next();
                this.nextKey = (K) this.nextEntry.getKey().get();
                if (this.nextKey != null) {
                    return;
                }
            }
            this.nextEntry = null;
            this.nextKey = null;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.nextKey != null;
        }

        @Override // java.util.Iterator
        public Map.Entry<K, V> next() {
            if (this.nextKey == null) {
                throw new NoSuchElementException();
            }
            try {
                return new SimpleEntry(this.nextKey, this.nextEntry);
            } finally {
                findNext();
            }
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/concurrent/WeakConcurrentMap$SimpleEntry.class */
    public class SimpleEntry implements Map.Entry<K, V> {
        private final K key;
        final Map.Entry<WeakKey<K>, V> entry;

        private SimpleEntry(K key, Map.Entry<WeakKey<K>, V> entry) {
            this.key = key;
            this.entry = entry;
        }

        @Override // java.util.Map.Entry
        public K getKey() {
            return this.key;
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            return this.entry.getValue();
        }

        @Override // java.util.Map.Entry
        public V setValue(V value) {
            if (value == null) {
                throw new NullPointerException();
            }
            return this.entry.setValue(value);
        }
    }
}
