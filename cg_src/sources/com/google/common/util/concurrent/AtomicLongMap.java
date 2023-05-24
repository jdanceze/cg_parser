package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.checkerframework.checker.nullness.compatqual.MonotonicNonNullDecl;
@GwtCompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/util/concurrent/AtomicLongMap.class */
public final class AtomicLongMap<K> implements Serializable {
    private final ConcurrentHashMap<K, AtomicLong> map;
    @MonotonicNonNullDecl
    private transient Map<K, Long> asMap;

    private AtomicLongMap(ConcurrentHashMap<K, AtomicLong> map) {
        this.map = (ConcurrentHashMap) Preconditions.checkNotNull(map);
    }

    public static <K> AtomicLongMap<K> create() {
        return new AtomicLongMap<>(new ConcurrentHashMap());
    }

    public static <K> AtomicLongMap<K> create(Map<? extends K, ? extends Long> m) {
        AtomicLongMap<K> result = create();
        result.putAll(m);
        return result;
    }

    public long get(K key) {
        AtomicLong atomic = this.map.get(key);
        if (atomic == null) {
            return 0L;
        }
        return atomic.get();
    }

    @CanIgnoreReturnValue
    public long incrementAndGet(K key) {
        return addAndGet(key, 1L);
    }

    @CanIgnoreReturnValue
    public long decrementAndGet(K key) {
        return addAndGet(key, -1L);
    }

    @CanIgnoreReturnValue
    public long addAndGet(K key, long delta) {
        AtomicLong atomic;
        do {
            atomic = this.map.get(key);
            if (atomic == null) {
                atomic = this.map.putIfAbsent(key, new AtomicLong(delta));
                if (atomic == null) {
                    return delta;
                }
            }
            while (true) {
                long oldValue = atomic.get();
                if (oldValue == 0) {
                    break;
                }
                long newValue = oldValue + delta;
                if (atomic.compareAndSet(oldValue, newValue)) {
                    return newValue;
                }
            }
        } while (!this.map.replace(key, atomic, new AtomicLong(delta)));
        return delta;
    }

    @CanIgnoreReturnValue
    public long getAndIncrement(K key) {
        return getAndAdd(key, 1L);
    }

    @CanIgnoreReturnValue
    public long getAndDecrement(K key) {
        return getAndAdd(key, -1L);
    }

    @CanIgnoreReturnValue
    public long getAndAdd(K key, long delta) {
        AtomicLong atomic;
        do {
            atomic = this.map.get(key);
            if (atomic == null) {
                atomic = this.map.putIfAbsent(key, new AtomicLong(delta));
                if (atomic == null) {
                    return 0L;
                }
            }
            while (true) {
                long oldValue = atomic.get();
                if (oldValue == 0) {
                    break;
                }
                long newValue = oldValue + delta;
                if (atomic.compareAndSet(oldValue, newValue)) {
                    return oldValue;
                }
            }
        } while (!this.map.replace(key, atomic, new AtomicLong(delta)));
        return 0L;
    }

    @CanIgnoreReturnValue
    public long put(K key, long newValue) {
        AtomicLong atomic;
        do {
            atomic = this.map.get(key);
            if (atomic == null) {
                atomic = this.map.putIfAbsent(key, new AtomicLong(newValue));
                if (atomic == null) {
                    return 0L;
                }
            }
            while (true) {
                long oldValue = atomic.get();
                if (oldValue == 0) {
                    break;
                } else if (atomic.compareAndSet(oldValue, newValue)) {
                    return oldValue;
                }
            }
        } while (!this.map.replace(key, atomic, new AtomicLong(newValue)));
        return 0L;
    }

    public void putAll(Map<? extends K, ? extends Long> m) {
        for (Map.Entry<? extends K, ? extends Long> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue().longValue());
        }
    }

    @CanIgnoreReturnValue
    public long remove(K key) {
        long oldValue;
        AtomicLong atomic = this.map.get(key);
        if (atomic == null) {
            return 0L;
        }
        do {
            oldValue = atomic.get();
            if (oldValue == 0) {
                break;
            }
        } while (!atomic.compareAndSet(oldValue, 0L));
        this.map.remove(key, atomic);
        return oldValue;
    }

    boolean remove(K key, long value) {
        AtomicLong atomic = this.map.get(key);
        if (atomic == null) {
            return false;
        }
        long oldValue = atomic.get();
        if (oldValue != value) {
            return false;
        }
        if (oldValue == 0 || atomic.compareAndSet(oldValue, 0L)) {
            this.map.remove(key, atomic);
            return true;
        }
        return false;
    }

    @CanIgnoreReturnValue
    @Beta
    public boolean removeIfZero(K key) {
        return remove(key, 0L);
    }

    public void removeAllZeros() {
        Iterator<Map.Entry<K, AtomicLong>> entryIterator = this.map.entrySet().iterator();
        while (entryIterator.hasNext()) {
            Map.Entry<K, AtomicLong> entry = entryIterator.next();
            AtomicLong atomic = entry.getValue();
            if (atomic != null && atomic.get() == 0) {
                entryIterator.remove();
            }
        }
    }

    public long sum() {
        long sum = 0;
        for (AtomicLong value : this.map.values()) {
            sum += value.get();
        }
        return sum;
    }

    public Map<K, Long> asMap() {
        Map<K, Long> result = this.asMap;
        if (result == null) {
            Map<K, Long> createAsMap = createAsMap();
            this.asMap = createAsMap;
            return createAsMap;
        }
        return result;
    }

    private Map<K, Long> createAsMap() {
        return Collections.unmodifiableMap(Maps.transformValues(this.map, new Function<AtomicLong, Long>() { // from class: com.google.common.util.concurrent.AtomicLongMap.1
            @Override // com.google.common.base.Function
            public Long apply(AtomicLong atomic) {
                return Long.valueOf(atomic.get());
            }
        }));
    }

    public boolean containsKey(Object key) {
        return this.map.containsKey(key);
    }

    public int size() {
        return this.map.size();
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    public void clear() {
        this.map.clear();
    }

    public String toString() {
        return this.map.toString();
    }

    long putIfAbsent(K key, long newValue) {
        AtomicLong atomic;
        do {
            atomic = this.map.get(key);
            if (atomic == null) {
                atomic = this.map.putIfAbsent(key, new AtomicLong(newValue));
                if (atomic == null) {
                    return 0L;
                }
            }
            long oldValue = atomic.get();
            if (oldValue != 0) {
                return oldValue;
            }
        } while (!this.map.replace(key, atomic, new AtomicLong(newValue)));
        return 0L;
    }

    boolean replace(K key, long expectedOldValue, long newValue) {
        if (expectedOldValue == 0) {
            return putIfAbsent(key, newValue) == 0;
        }
        AtomicLong atomic = this.map.get(key);
        if (atomic == null) {
            return false;
        }
        return atomic.compareAndSet(expectedOldValue, newValue);
    }
}
