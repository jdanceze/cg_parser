package com.google.common.collect;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Equivalence;
import com.google.common.base.Preconditions;
import com.google.common.collect.MapMaker;
import com.google.common.collect.MapMakerInternalMap.InternalEntry;
import com.google.common.collect.MapMakerInternalMap.Segment;
import com.google.common.primitives.Ints;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.concurrent.GuardedBy;
import com.google.j2objc.annotations.Weak;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.ReentrantLock;
import org.checkerframework.checker.nullness.compatqual.MonotonicNonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* JADX INFO: Access modifiers changed from: package-private */
@GwtIncompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap.class */
public class MapMakerInternalMap<K, V, E extends InternalEntry<K, V, E>, S extends Segment<K, V, E, S>> extends AbstractMap<K, V> implements ConcurrentMap<K, V>, Serializable {
    static final int MAXIMUM_CAPACITY = 1073741824;
    static final int MAX_SEGMENTS = 65536;
    static final int CONTAINS_VALUE_RETRIES = 3;
    static final int DRAIN_THRESHOLD = 63;
    static final int DRAIN_MAX = 16;
    static final long CLEANUP_EXECUTOR_DELAY_SECS = 60;
    final transient int segmentMask;
    final transient int segmentShift;
    final transient Segment<K, V, E, S>[] segments;
    final int concurrencyLevel;
    final Equivalence<Object> keyEquivalence;
    final transient InternalEntryHelper<K, V, E, S> entryHelper;
    static final WeakValueReference<Object, Object, DummyInternalEntry> UNSET_WEAK_VALUE_REFERENCE = new WeakValueReference<Object, Object, DummyInternalEntry>() { // from class: com.google.common.collect.MapMakerInternalMap.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.common.collect.MapMakerInternalMap.WeakValueReference
        public DummyInternalEntry getEntry() {
            return null;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.WeakValueReference
        public void clear() {
        }

        @Override // com.google.common.collect.MapMakerInternalMap.WeakValueReference
        public Object get() {
            return null;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.WeakValueReference
        public WeakValueReference<Object, Object, DummyInternalEntry> copyFor(ReferenceQueue<Object> queue, DummyInternalEntry entry) {
            return this;
        }
    };
    @MonotonicNonNullDecl
    transient Set<K> keySet;
    @MonotonicNonNullDecl
    transient Collection<V> values;
    @MonotonicNonNullDecl
    transient Set<Map.Entry<K, V>> entrySet;
    private static final long serialVersionUID = 5;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$InternalEntry.class */
    public interface InternalEntry<K, V, E extends InternalEntry<K, V, E>> {
        E getNext();

        int getHash();

        K getKey();

        V getValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$InternalEntryHelper.class */
    public interface InternalEntryHelper<K, V, E extends InternalEntry<K, V, E>, S extends Segment<K, V, E, S>> {
        Strength keyStrength();

        Strength valueStrength();

        S newSegment(MapMakerInternalMap<K, V, E, S> mapMakerInternalMap, int i, int i2);

        E newEntry(S s, K k, int i, @NullableDecl E e);

        E copy(S s, E e, @NullableDecl E e2);

        void setValue(S s, E e, V v);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$Strength.class */
    public enum Strength {
        STRONG { // from class: com.google.common.collect.MapMakerInternalMap.Strength.1
            @Override // com.google.common.collect.MapMakerInternalMap.Strength
            Equivalence<Object> defaultEquivalence() {
                return Equivalence.equals();
            }
        },
        WEAK { // from class: com.google.common.collect.MapMakerInternalMap.Strength.2
            @Override // com.google.common.collect.MapMakerInternalMap.Strength
            Equivalence<Object> defaultEquivalence() {
                return Equivalence.identity();
            }
        };

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract Equivalence<Object> defaultEquivalence();
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$StrongValueEntry.class */
    interface StrongValueEntry<K, V, E extends InternalEntry<K, V, E>> extends InternalEntry<K, V, E> {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$WeakValueEntry.class */
    public interface WeakValueEntry<K, V, E extends InternalEntry<K, V, E>> extends InternalEntry<K, V, E> {
        WeakValueReference<K, V, E> getValueReference();

        void clearValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$WeakValueReference.class */
    public interface WeakValueReference<K, V, E extends InternalEntry<K, V, E>> {
        @NullableDecl
        V get();

        E getEntry();

        void clear();

        WeakValueReference<K, V, E> copyFor(ReferenceQueue<V> referenceQueue, E e);
    }

    private MapMakerInternalMap(MapMaker builder, InternalEntryHelper<K, V, E, S> entryHelper) {
        int segmentCount;
        int segmentSize;
        this.concurrencyLevel = Math.min(builder.getConcurrencyLevel(), 65536);
        this.keyEquivalence = builder.getKeyEquivalence();
        this.entryHelper = entryHelper;
        int initialCapacity = Math.min(builder.getInitialCapacity(), 1073741824);
        int segmentShift = 0;
        int i = 1;
        while (true) {
            segmentCount = i;
            if (segmentCount >= this.concurrencyLevel) {
                break;
            }
            segmentShift++;
            i = segmentCount << 1;
        }
        this.segmentShift = 32 - segmentShift;
        this.segmentMask = segmentCount - 1;
        this.segments = newSegmentArray(segmentCount);
        int segmentCapacity = initialCapacity / segmentCount;
        segmentCapacity = segmentCapacity * segmentCount < initialCapacity ? segmentCapacity + 1 : segmentCapacity;
        int i2 = 1;
        while (true) {
            segmentSize = i2;
            if (segmentSize >= segmentCapacity) {
                break;
            }
            i2 = segmentSize << 1;
        }
        for (int i3 = 0; i3 < this.segments.length; i3++) {
            this.segments[i3] = createSegment(segmentSize, -1);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <K, V> MapMakerInternalMap<K, V, ? extends InternalEntry<K, V, ?>, ?> create(MapMaker builder) {
        if (builder.getKeyStrength() == Strength.STRONG && builder.getValueStrength() == Strength.STRONG) {
            return new MapMakerInternalMap<>(builder, StrongKeyStrongValueEntry.Helper.instance());
        }
        if (builder.getKeyStrength() == Strength.STRONG && builder.getValueStrength() == Strength.WEAK) {
            return new MapMakerInternalMap<>(builder, StrongKeyWeakValueEntry.Helper.instance());
        }
        if (builder.getKeyStrength() == Strength.WEAK && builder.getValueStrength() == Strength.STRONG) {
            return new MapMakerInternalMap<>(builder, WeakKeyStrongValueEntry.Helper.instance());
        }
        if (builder.getKeyStrength() == Strength.WEAK && builder.getValueStrength() == Strength.WEAK) {
            return new MapMakerInternalMap<>(builder, WeakKeyWeakValueEntry.Helper.instance());
        }
        throw new AssertionError();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <K> MapMakerInternalMap<K, MapMaker.Dummy, ? extends InternalEntry<K, MapMaker.Dummy, ?>, ?> createWithDummyValues(MapMaker builder) {
        if (builder.getKeyStrength() == Strength.STRONG && builder.getValueStrength() == Strength.STRONG) {
            return new MapMakerInternalMap<>(builder, StrongKeyDummyValueEntry.Helper.instance());
        }
        if (builder.getKeyStrength() == Strength.WEAK && builder.getValueStrength() == Strength.STRONG) {
            return new MapMakerInternalMap<>(builder, WeakKeyDummyValueEntry.Helper.instance());
        }
        if (builder.getValueStrength() == Strength.WEAK) {
            throw new IllegalArgumentException("Map cannot have both weak and dummy values");
        }
        throw new AssertionError();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$AbstractStrongKeyEntry.class */
    public static abstract class AbstractStrongKeyEntry<K, V, E extends InternalEntry<K, V, E>> implements InternalEntry<K, V, E> {
        final K key;
        final int hash;
        @NullableDecl
        final E next;

        AbstractStrongKeyEntry(K key, int hash, @NullableDecl E next) {
            this.key = key;
            this.hash = hash;
            this.next = next;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public K getKey() {
            return this.key;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public int getHash() {
            return this.hash;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public E getNext() {
            return this.next;
        }
    }

    static <K, V, E extends InternalEntry<K, V, E>> WeakValueReference<K, V, E> unsetWeakValueReference() {
        return (WeakValueReference<K, V, E>) UNSET_WEAK_VALUE_REFERENCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$StrongKeyStrongValueEntry.class */
    public static final class StrongKeyStrongValueEntry<K, V> extends AbstractStrongKeyEntry<K, V, StrongKeyStrongValueEntry<K, V>> implements StrongValueEntry<K, V, StrongKeyStrongValueEntry<K, V>> {
        @NullableDecl
        private volatile V value;

        StrongKeyStrongValueEntry(K key, int hash, @NullableDecl StrongKeyStrongValueEntry<K, V> next) {
            super(key, hash, next);
            this.value = null;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        @NullableDecl
        public V getValue() {
            return this.value;
        }

        void setValue(V value) {
            this.value = value;
        }

        StrongKeyStrongValueEntry<K, V> copy(StrongKeyStrongValueEntry<K, V> newNext) {
            StrongKeyStrongValueEntry<K, V> newEntry = new StrongKeyStrongValueEntry<>(this.key, this.hash, newNext);
            newEntry.value = this.value;
            return newEntry;
        }

        /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$StrongKeyStrongValueEntry$Helper.class */
        static final class Helper<K, V> implements InternalEntryHelper<K, V, StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>> {
            private static final Helper<?, ?> INSTANCE = new Helper<>();

            Helper() {
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ void setValue(Segment segment, InternalEntry internalEntry, Object obj) {
                setValue((StrongKeyStrongValueSegment<K, StrongKeyStrongValueEntry<K, V>>) segment, (StrongKeyStrongValueEntry<K, StrongKeyStrongValueEntry<K, V>>) internalEntry, (StrongKeyStrongValueEntry<K, V>) obj);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ InternalEntry copy(Segment segment, InternalEntry internalEntry, @NullableDecl InternalEntry internalEntry2) {
                return copy((StrongKeyStrongValueSegment) ((StrongKeyStrongValueSegment) segment), (StrongKeyStrongValueEntry) ((StrongKeyStrongValueEntry) internalEntry), (StrongKeyStrongValueEntry) ((StrongKeyStrongValueEntry) internalEntry2));
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ InternalEntry newEntry(Segment segment, Object obj, int i, @NullableDecl InternalEntry internalEntry) {
                return newEntry((StrongKeyStrongValueSegment<StrongKeyStrongValueSegment<K, V>, V>) segment, (StrongKeyStrongValueSegment<K, V>) obj, i, (StrongKeyStrongValueEntry<StrongKeyStrongValueSegment<K, V>, V>) internalEntry);
            }

            static <K, V> Helper<K, V> instance() {
                return (Helper<K, V>) INSTANCE;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength keyStrength() {
                return Strength.STRONG;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength valueStrength() {
                return Strength.STRONG;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public StrongKeyStrongValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
                return new StrongKeyStrongValueSegment<>(map, initialCapacity, maxSegmentSize);
            }

            public StrongKeyStrongValueEntry<K, V> copy(StrongKeyStrongValueSegment<K, V> segment, StrongKeyStrongValueEntry<K, V> entry, @NullableDecl StrongKeyStrongValueEntry<K, V> newNext) {
                return entry.copy(newNext);
            }

            public void setValue(StrongKeyStrongValueSegment<K, V> segment, StrongKeyStrongValueEntry<K, V> entry, V value) {
                entry.setValue(value);
            }

            public StrongKeyStrongValueEntry<K, V> newEntry(StrongKeyStrongValueSegment<K, V> segment, K key, int hash, @NullableDecl StrongKeyStrongValueEntry<K, V> next) {
                return new StrongKeyStrongValueEntry<>(key, hash, next);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$StrongKeyWeakValueEntry.class */
    public static final class StrongKeyWeakValueEntry<K, V> extends AbstractStrongKeyEntry<K, V, StrongKeyWeakValueEntry<K, V>> implements WeakValueEntry<K, V, StrongKeyWeakValueEntry<K, V>> {
        private volatile WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> valueReference;

        StrongKeyWeakValueEntry(K key, int hash, @NullableDecl StrongKeyWeakValueEntry<K, V> next) {
            super(key, hash, next);
            this.valueReference = MapMakerInternalMap.unsetWeakValueReference();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public V getValue() {
            return this.valueReference.get();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.WeakValueEntry
        public void clearValue() {
            this.valueReference.clear();
        }

        void setValue(V value, ReferenceQueue<V> queueForValues) {
            WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> previous = this.valueReference;
            this.valueReference = new WeakValueReferenceImpl(queueForValues, value, this);
            previous.clear();
        }

        StrongKeyWeakValueEntry<K, V> copy(ReferenceQueue<V> queueForValues, StrongKeyWeakValueEntry<K, V> newNext) {
            StrongKeyWeakValueEntry<K, V> newEntry = new StrongKeyWeakValueEntry<>(this.key, this.hash, newNext);
            newEntry.valueReference = this.valueReference.copyFor(queueForValues, newEntry);
            return newEntry;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.WeakValueEntry
        public WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> getValueReference() {
            return this.valueReference;
        }

        /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$StrongKeyWeakValueEntry$Helper.class */
        static final class Helper<K, V> implements InternalEntryHelper<K, V, StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>> {
            private static final Helper<?, ?> INSTANCE = new Helper<>();

            Helper() {
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ void setValue(Segment segment, InternalEntry internalEntry, Object obj) {
                setValue((StrongKeyWeakValueSegment<K, StrongKeyWeakValueEntry<K, V>>) segment, (StrongKeyWeakValueEntry<K, StrongKeyWeakValueEntry<K, V>>) internalEntry, (StrongKeyWeakValueEntry<K, V>) obj);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ InternalEntry copy(Segment segment, InternalEntry internalEntry, @NullableDecl InternalEntry internalEntry2) {
                return copy((StrongKeyWeakValueSegment) ((StrongKeyWeakValueSegment) segment), (StrongKeyWeakValueEntry) ((StrongKeyWeakValueEntry) internalEntry), (StrongKeyWeakValueEntry) ((StrongKeyWeakValueEntry) internalEntry2));
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ InternalEntry newEntry(Segment segment, Object obj, int i, @NullableDecl InternalEntry internalEntry) {
                return newEntry((StrongKeyWeakValueSegment<StrongKeyWeakValueSegment<K, V>, V>) segment, (StrongKeyWeakValueSegment<K, V>) obj, i, (StrongKeyWeakValueEntry<StrongKeyWeakValueSegment<K, V>, V>) internalEntry);
            }

            static <K, V> Helper<K, V> instance() {
                return (Helper<K, V>) INSTANCE;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength keyStrength() {
                return Strength.STRONG;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength valueStrength() {
                return Strength.WEAK;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public StrongKeyWeakValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
                return new StrongKeyWeakValueSegment<>(map, initialCapacity, maxSegmentSize);
            }

            public StrongKeyWeakValueEntry<K, V> copy(StrongKeyWeakValueSegment<K, V> segment, StrongKeyWeakValueEntry<K, V> entry, @NullableDecl StrongKeyWeakValueEntry<K, V> newNext) {
                if (Segment.isCollected(entry)) {
                    return null;
                }
                return entry.copy(((StrongKeyWeakValueSegment) segment).queueForValues, newNext);
            }

            public void setValue(StrongKeyWeakValueSegment<K, V> segment, StrongKeyWeakValueEntry<K, V> entry, V value) {
                entry.setValue(value, ((StrongKeyWeakValueSegment) segment).queueForValues);
            }

            public StrongKeyWeakValueEntry<K, V> newEntry(StrongKeyWeakValueSegment<K, V> segment, K key, int hash, @NullableDecl StrongKeyWeakValueEntry<K, V> next) {
                return new StrongKeyWeakValueEntry<>(key, hash, next);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$StrongKeyDummyValueEntry.class */
    public static final class StrongKeyDummyValueEntry<K> extends AbstractStrongKeyEntry<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>> implements StrongValueEntry<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>> {
        StrongKeyDummyValueEntry(K key, int hash, @NullableDecl StrongKeyDummyValueEntry<K> next) {
            super(key, hash, next);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public MapMaker.Dummy getValue() {
            return MapMaker.Dummy.VALUE;
        }

        void setValue(MapMaker.Dummy value) {
        }

        StrongKeyDummyValueEntry<K> copy(StrongKeyDummyValueEntry<K> newNext) {
            return new StrongKeyDummyValueEntry<>(this.key, this.hash, newNext);
        }

        /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$StrongKeyDummyValueEntry$Helper.class */
        static final class Helper<K> implements InternalEntryHelper<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>, StrongKeyDummyValueSegment<K>> {
            private static final Helper<?> INSTANCE = new Helper<>();

            Helper() {
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ void setValue(Segment segment, InternalEntry internalEntry, MapMaker.Dummy dummy) {
                setValue((StrongKeyDummyValueSegment) ((StrongKeyDummyValueSegment) segment), (StrongKeyDummyValueEntry) ((StrongKeyDummyValueEntry) internalEntry), dummy);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ InternalEntry copy(Segment segment, InternalEntry internalEntry, @NullableDecl InternalEntry internalEntry2) {
                return copy((StrongKeyDummyValueSegment) ((StrongKeyDummyValueSegment) segment), (StrongKeyDummyValueEntry) ((StrongKeyDummyValueEntry) internalEntry), (StrongKeyDummyValueEntry) ((StrongKeyDummyValueEntry) internalEntry2));
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ InternalEntry newEntry(Segment segment, Object obj, int i, @NullableDecl InternalEntry internalEntry) {
                return newEntry((StrongKeyDummyValueSegment<StrongKeyDummyValueSegment<K>>) segment, (StrongKeyDummyValueSegment<K>) obj, i, (StrongKeyDummyValueEntry<StrongKeyDummyValueSegment<K>>) internalEntry);
            }

            static <K> Helper<K> instance() {
                return (Helper<K>) INSTANCE;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength keyStrength() {
                return Strength.STRONG;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength valueStrength() {
                return Strength.STRONG;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public StrongKeyDummyValueSegment<K> newSegment(MapMakerInternalMap<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>, StrongKeyDummyValueSegment<K>> map, int initialCapacity, int maxSegmentSize) {
                return new StrongKeyDummyValueSegment<>(map, initialCapacity, maxSegmentSize);
            }

            public StrongKeyDummyValueEntry<K> copy(StrongKeyDummyValueSegment<K> segment, StrongKeyDummyValueEntry<K> entry, @NullableDecl StrongKeyDummyValueEntry<K> newNext) {
                return entry.copy(newNext);
            }

            public void setValue(StrongKeyDummyValueSegment<K> segment, StrongKeyDummyValueEntry<K> entry, MapMaker.Dummy value) {
            }

            public StrongKeyDummyValueEntry<K> newEntry(StrongKeyDummyValueSegment<K> segment, K key, int hash, @NullableDecl StrongKeyDummyValueEntry<K> next) {
                return new StrongKeyDummyValueEntry<>(key, hash, next);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$AbstractWeakKeyEntry.class */
    public static abstract class AbstractWeakKeyEntry<K, V, E extends InternalEntry<K, V, E>> extends WeakReference<K> implements InternalEntry<K, V, E> {
        final int hash;
        @NullableDecl
        final E next;

        AbstractWeakKeyEntry(ReferenceQueue<K> queue, K key, int hash, @NullableDecl E next) {
            super(key, queue);
            this.hash = hash;
            this.next = next;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public K getKey() {
            return (K) get();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public int getHash() {
            return this.hash;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public E getNext() {
            return this.next;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$WeakKeyDummyValueEntry.class */
    public static final class WeakKeyDummyValueEntry<K> extends AbstractWeakKeyEntry<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>> implements StrongValueEntry<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>> {
        WeakKeyDummyValueEntry(ReferenceQueue<K> queue, K key, int hash, @NullableDecl WeakKeyDummyValueEntry<K> next) {
            super(queue, key, hash, next);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public MapMaker.Dummy getValue() {
            return MapMaker.Dummy.VALUE;
        }

        void setValue(MapMaker.Dummy value) {
        }

        WeakKeyDummyValueEntry<K> copy(ReferenceQueue<K> queueForKeys, WeakKeyDummyValueEntry<K> newNext) {
            return new WeakKeyDummyValueEntry<>(queueForKeys, getKey(), this.hash, newNext);
        }

        /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$WeakKeyDummyValueEntry$Helper.class */
        static final class Helper<K> implements InternalEntryHelper<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>, WeakKeyDummyValueSegment<K>> {
            private static final Helper<?> INSTANCE = new Helper<>();

            Helper() {
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ void setValue(Segment segment, InternalEntry internalEntry, MapMaker.Dummy dummy) {
                setValue((WeakKeyDummyValueSegment) ((WeakKeyDummyValueSegment) segment), (WeakKeyDummyValueEntry) ((WeakKeyDummyValueEntry) internalEntry), dummy);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ InternalEntry copy(Segment segment, InternalEntry internalEntry, @NullableDecl InternalEntry internalEntry2) {
                return copy((WeakKeyDummyValueSegment) ((WeakKeyDummyValueSegment) segment), (WeakKeyDummyValueEntry) ((WeakKeyDummyValueEntry) internalEntry), (WeakKeyDummyValueEntry) ((WeakKeyDummyValueEntry) internalEntry2));
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ InternalEntry newEntry(Segment segment, Object obj, int i, @NullableDecl InternalEntry internalEntry) {
                return newEntry((WeakKeyDummyValueSegment<WeakKeyDummyValueSegment<K>>) segment, (WeakKeyDummyValueSegment<K>) obj, i, (WeakKeyDummyValueEntry<WeakKeyDummyValueSegment<K>>) internalEntry);
            }

            static <K> Helper<K> instance() {
                return (Helper<K>) INSTANCE;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength keyStrength() {
                return Strength.WEAK;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength valueStrength() {
                return Strength.STRONG;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public WeakKeyDummyValueSegment<K> newSegment(MapMakerInternalMap<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>, WeakKeyDummyValueSegment<K>> map, int initialCapacity, int maxSegmentSize) {
                return new WeakKeyDummyValueSegment<>(map, initialCapacity, maxSegmentSize);
            }

            public WeakKeyDummyValueEntry<K> copy(WeakKeyDummyValueSegment<K> segment, WeakKeyDummyValueEntry<K> entry, @NullableDecl WeakKeyDummyValueEntry<K> newNext) {
                if (entry.getKey() == null) {
                    return null;
                }
                return entry.copy(((WeakKeyDummyValueSegment) segment).queueForKeys, newNext);
            }

            public void setValue(WeakKeyDummyValueSegment<K> segment, WeakKeyDummyValueEntry<K> entry, MapMaker.Dummy value) {
            }

            public WeakKeyDummyValueEntry<K> newEntry(WeakKeyDummyValueSegment<K> segment, K key, int hash, @NullableDecl WeakKeyDummyValueEntry<K> next) {
                return new WeakKeyDummyValueEntry<>(((WeakKeyDummyValueSegment) segment).queueForKeys, key, hash, next);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$WeakKeyStrongValueEntry.class */
    public static final class WeakKeyStrongValueEntry<K, V> extends AbstractWeakKeyEntry<K, V, WeakKeyStrongValueEntry<K, V>> implements StrongValueEntry<K, V, WeakKeyStrongValueEntry<K, V>> {
        @NullableDecl
        private volatile V value;

        WeakKeyStrongValueEntry(ReferenceQueue<K> queue, K key, int hash, @NullableDecl WeakKeyStrongValueEntry<K, V> next) {
            super(queue, key, hash, next);
            this.value = null;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        @NullableDecl
        public V getValue() {
            return this.value;
        }

        void setValue(V value) {
            this.value = value;
        }

        WeakKeyStrongValueEntry<K, V> copy(ReferenceQueue<K> queueForKeys, WeakKeyStrongValueEntry<K, V> newNext) {
            WeakKeyStrongValueEntry<K, V> newEntry = new WeakKeyStrongValueEntry<>(queueForKeys, getKey(), this.hash, newNext);
            newEntry.setValue(this.value);
            return newEntry;
        }

        /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$WeakKeyStrongValueEntry$Helper.class */
        static final class Helper<K, V> implements InternalEntryHelper<K, V, WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>> {
            private static final Helper<?, ?> INSTANCE = new Helper<>();

            Helper() {
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ void setValue(Segment segment, InternalEntry internalEntry, Object obj) {
                setValue((WeakKeyStrongValueSegment<K, WeakKeyStrongValueEntry<K, V>>) segment, (WeakKeyStrongValueEntry<K, WeakKeyStrongValueEntry<K, V>>) internalEntry, (WeakKeyStrongValueEntry<K, V>) obj);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ InternalEntry copy(Segment segment, InternalEntry internalEntry, @NullableDecl InternalEntry internalEntry2) {
                return copy((WeakKeyStrongValueSegment) ((WeakKeyStrongValueSegment) segment), (WeakKeyStrongValueEntry) ((WeakKeyStrongValueEntry) internalEntry), (WeakKeyStrongValueEntry) ((WeakKeyStrongValueEntry) internalEntry2));
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ InternalEntry newEntry(Segment segment, Object obj, int i, @NullableDecl InternalEntry internalEntry) {
                return newEntry((WeakKeyStrongValueSegment<WeakKeyStrongValueSegment<K, V>, V>) segment, (WeakKeyStrongValueSegment<K, V>) obj, i, (WeakKeyStrongValueEntry<WeakKeyStrongValueSegment<K, V>, V>) internalEntry);
            }

            static <K, V> Helper<K, V> instance() {
                return (Helper<K, V>) INSTANCE;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength keyStrength() {
                return Strength.WEAK;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength valueStrength() {
                return Strength.STRONG;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public WeakKeyStrongValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
                return new WeakKeyStrongValueSegment<>(map, initialCapacity, maxSegmentSize);
            }

            public WeakKeyStrongValueEntry<K, V> copy(WeakKeyStrongValueSegment<K, V> segment, WeakKeyStrongValueEntry<K, V> entry, @NullableDecl WeakKeyStrongValueEntry<K, V> newNext) {
                if (entry.getKey() == null) {
                    return null;
                }
                return entry.copy(((WeakKeyStrongValueSegment) segment).queueForKeys, newNext);
            }

            public void setValue(WeakKeyStrongValueSegment<K, V> segment, WeakKeyStrongValueEntry<K, V> entry, V value) {
                entry.setValue(value);
            }

            public WeakKeyStrongValueEntry<K, V> newEntry(WeakKeyStrongValueSegment<K, V> segment, K key, int hash, @NullableDecl WeakKeyStrongValueEntry<K, V> next) {
                return new WeakKeyStrongValueEntry<>(((WeakKeyStrongValueSegment) segment).queueForKeys, key, hash, next);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$WeakKeyWeakValueEntry.class */
    public static final class WeakKeyWeakValueEntry<K, V> extends AbstractWeakKeyEntry<K, V, WeakKeyWeakValueEntry<K, V>> implements WeakValueEntry<K, V, WeakKeyWeakValueEntry<K, V>> {
        private volatile WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> valueReference;

        WeakKeyWeakValueEntry(ReferenceQueue<K> queue, K key, int hash, @NullableDecl WeakKeyWeakValueEntry<K, V> next) {
            super(queue, key, hash, next);
            this.valueReference = MapMakerInternalMap.unsetWeakValueReference();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public V getValue() {
            return this.valueReference.get();
        }

        WeakKeyWeakValueEntry<K, V> copy(ReferenceQueue<K> queueForKeys, ReferenceQueue<V> queueForValues, WeakKeyWeakValueEntry<K, V> newNext) {
            WeakKeyWeakValueEntry<K, V> newEntry = new WeakKeyWeakValueEntry<>(queueForKeys, getKey(), this.hash, newNext);
            newEntry.valueReference = this.valueReference.copyFor(queueForValues, newEntry);
            return newEntry;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.WeakValueEntry
        public void clearValue() {
            this.valueReference.clear();
        }

        void setValue(V value, ReferenceQueue<V> queueForValues) {
            WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> previous = this.valueReference;
            this.valueReference = new WeakValueReferenceImpl(queueForValues, value, this);
            previous.clear();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.WeakValueEntry
        public WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> getValueReference() {
            return this.valueReference;
        }

        /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$WeakKeyWeakValueEntry$Helper.class */
        static final class Helper<K, V> implements InternalEntryHelper<K, V, WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>> {
            private static final Helper<?, ?> INSTANCE = new Helper<>();

            Helper() {
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ void setValue(Segment segment, InternalEntry internalEntry, Object obj) {
                setValue((WeakKeyWeakValueSegment<K, WeakKeyWeakValueEntry<K, V>>) segment, (WeakKeyWeakValueEntry<K, WeakKeyWeakValueEntry<K, V>>) internalEntry, (WeakKeyWeakValueEntry<K, V>) obj);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ InternalEntry copy(Segment segment, InternalEntry internalEntry, @NullableDecl InternalEntry internalEntry2) {
                return copy((WeakKeyWeakValueSegment) ((WeakKeyWeakValueSegment) segment), (WeakKeyWeakValueEntry) ((WeakKeyWeakValueEntry) internalEntry), (WeakKeyWeakValueEntry) ((WeakKeyWeakValueEntry) internalEntry2));
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ InternalEntry newEntry(Segment segment, Object obj, int i, @NullableDecl InternalEntry internalEntry) {
                return newEntry((WeakKeyWeakValueSegment<WeakKeyWeakValueSegment<K, V>, V>) segment, (WeakKeyWeakValueSegment<K, V>) obj, i, (WeakKeyWeakValueEntry<WeakKeyWeakValueSegment<K, V>, V>) internalEntry);
            }

            static <K, V> Helper<K, V> instance() {
                return (Helper<K, V>) INSTANCE;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength keyStrength() {
                return Strength.WEAK;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength valueStrength() {
                return Strength.WEAK;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public WeakKeyWeakValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
                return new WeakKeyWeakValueSegment<>(map, initialCapacity, maxSegmentSize);
            }

            public WeakKeyWeakValueEntry<K, V> copy(WeakKeyWeakValueSegment<K, V> segment, WeakKeyWeakValueEntry<K, V> entry, @NullableDecl WeakKeyWeakValueEntry<K, V> newNext) {
                if (entry.getKey() == null || Segment.isCollected(entry)) {
                    return null;
                }
                return entry.copy(((WeakKeyWeakValueSegment) segment).queueForKeys, ((WeakKeyWeakValueSegment) segment).queueForValues, newNext);
            }

            public void setValue(WeakKeyWeakValueSegment<K, V> segment, WeakKeyWeakValueEntry<K, V> entry, V value) {
                entry.setValue(value, ((WeakKeyWeakValueSegment) segment).queueForValues);
            }

            public WeakKeyWeakValueEntry<K, V> newEntry(WeakKeyWeakValueSegment<K, V> segment, K key, int hash, @NullableDecl WeakKeyWeakValueEntry<K, V> next) {
                return new WeakKeyWeakValueEntry<>(((WeakKeyWeakValueSegment) segment).queueForKeys, key, hash, next);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$DummyInternalEntry.class */
    public static final class DummyInternalEntry implements InternalEntry<Object, Object, DummyInternalEntry> {
        private DummyInternalEntry() {
            throw new AssertionError();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public DummyInternalEntry getNext() {
            throw new AssertionError();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public int getHash() {
            throw new AssertionError();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public Object getKey() {
            throw new AssertionError();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public Object getValue() {
            throw new AssertionError();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$WeakValueReferenceImpl.class */
    public static final class WeakValueReferenceImpl<K, V, E extends InternalEntry<K, V, E>> extends WeakReference<V> implements WeakValueReference<K, V, E> {
        @Weak
        final E entry;

        WeakValueReferenceImpl(ReferenceQueue<V> queue, V referent, E entry) {
            super(referent, queue);
            this.entry = entry;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.WeakValueReference
        public E getEntry() {
            return this.entry;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.WeakValueReference
        public WeakValueReference<K, V, E> copyFor(ReferenceQueue<V> queue, E entry) {
            return new WeakValueReferenceImpl(queue, get(), entry);
        }
    }

    static int rehash(int h) {
        int h2 = h + ((h << 15) ^ (-12931));
        int h3 = h2 ^ (h2 >>> 10);
        int h4 = h3 + (h3 << 3);
        int h5 = h4 ^ (h4 >>> 6);
        int h6 = h5 + (h5 << 2) + (h5 << 14);
        return h6 ^ (h6 >>> 16);
    }

    @VisibleForTesting
    E copyEntry(E original, E newNext) {
        int hash = original.getHash();
        return segmentFor(hash).copyEntry(original, newNext);
    }

    int hash(Object key) {
        int h = this.keyEquivalence.hash(key);
        return rehash(h);
    }

    void reclaimValue(WeakValueReference<K, V, E> valueReference) {
        E entry = valueReference.getEntry();
        int hash = entry.getHash();
        segmentFor(hash).reclaimValue((K) entry.getKey(), hash, valueReference);
    }

    void reclaimKey(E entry) {
        int hash = entry.getHash();
        segmentFor(hash).reclaimKey(entry, hash);
    }

    @VisibleForTesting
    boolean isLiveForTesting(InternalEntry<K, V, ?> entry) {
        return segmentFor(entry.getHash()).getLiveValueForTesting(entry) != null;
    }

    Segment<K, V, E, S> segmentFor(int hash) {
        return this.segments[(hash >>> this.segmentShift) & this.segmentMask];
    }

    Segment<K, V, E, S> createSegment(int initialCapacity, int maxSegmentSize) {
        return (S) this.entryHelper.newSegment(this, initialCapacity, maxSegmentSize);
    }

    V getLiveValue(E entry) {
        V value;
        if (entry.getKey() == null || (value = (V) entry.getValue()) == null) {
            return null;
        }
        return value;
    }

    final Segment<K, V, E, S>[] newSegmentArray(int ssize) {
        return new Segment[ssize];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$Segment.class */
    public static abstract class Segment<K, V, E extends InternalEntry<K, V, E>, S extends Segment<K, V, E, S>> extends ReentrantLock {
        @Weak
        final MapMakerInternalMap<K, V, E, S> map;
        volatile int count;
        int modCount;
        int threshold;
        @MonotonicNonNullDecl
        volatile AtomicReferenceArray<E> table;
        final int maxSegmentSize;
        final AtomicInteger readCount = new AtomicInteger();

        abstract S self();

        abstract E castForTesting(InternalEntry<K, V, ?> internalEntry);

        Segment(MapMakerInternalMap<K, V, E, S> map, int initialCapacity, int maxSegmentSize) {
            this.map = map;
            this.maxSegmentSize = maxSegmentSize;
            initTable(newEntryArray(initialCapacity));
        }

        @GuardedBy("this")
        void maybeDrainReferenceQueues() {
        }

        void maybeClearReferenceQueues() {
        }

        void setValue(E entry, V value) {
            this.map.entryHelper.setValue(self(), entry, value);
        }

        E copyEntry(E original, E newNext) {
            return this.map.entryHelper.copy(self(), original, newNext);
        }

        AtomicReferenceArray<E> newEntryArray(int size) {
            return new AtomicReferenceArray<>(size);
        }

        void initTable(AtomicReferenceArray<E> newTable) {
            this.threshold = (newTable.length() * 3) / 4;
            if (this.threshold == this.maxSegmentSize) {
                this.threshold++;
            }
            this.table = newTable;
        }

        ReferenceQueue<K> getKeyReferenceQueueForTesting() {
            throw new AssertionError();
        }

        ReferenceQueue<V> getValueReferenceQueueForTesting() {
            throw new AssertionError();
        }

        WeakValueReference<K, V, E> getWeakValueReferenceForTesting(InternalEntry<K, V, ?> entry) {
            throw new AssertionError();
        }

        WeakValueReference<K, V, E> newWeakValueReferenceForTesting(InternalEntry<K, V, ?> entry, V value) {
            throw new AssertionError();
        }

        void setWeakValueReferenceForTesting(InternalEntry<K, V, ?> entry, WeakValueReference<K, V, ? extends InternalEntry<K, V, ?>> valueReference) {
            throw new AssertionError();
        }

        void setTableEntryForTesting(int i, InternalEntry<K, V, ?> entry) {
            this.table.set(i, castForTesting(entry));
        }

        E copyForTesting(InternalEntry<K, V, ?> entry, @NullableDecl InternalEntry<K, V, ?> newNext) {
            return this.map.entryHelper.copy(self(), castForTesting(entry), castForTesting(newNext));
        }

        void setValueForTesting(InternalEntry<K, V, ?> entry, V value) {
            this.map.entryHelper.setValue(self(), castForTesting(entry), value);
        }

        E newEntryForTesting(K key, int hash, @NullableDecl InternalEntry<K, V, ?> next) {
            return this.map.entryHelper.newEntry(self(), key, hash, castForTesting(next));
        }

        @CanIgnoreReturnValue
        boolean removeTableEntryForTesting(InternalEntry<K, V, ?> entry) {
            return removeEntryForTesting(castForTesting(entry));
        }

        E removeFromChainForTesting(InternalEntry<K, V, ?> first, InternalEntry<K, V, ?> entry) {
            return removeFromChain(castForTesting(first), castForTesting(entry));
        }

        @NullableDecl
        V getLiveValueForTesting(InternalEntry<K, V, ?> entry) {
            return getLiveValue(castForTesting(entry));
        }

        void tryDrainReferenceQueues() {
            if (tryLock()) {
                try {
                    maybeDrainReferenceQueues();
                } finally {
                    unlock();
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @GuardedBy("this")
        void drainKeyReferenceQueue(ReferenceQueue<K> keyReferenceQueue) {
            int i = 0;
            do {
                Reference<? extends K> ref = keyReferenceQueue.poll();
                if (ref != null) {
                    this.map.reclaimKey((InternalEntry) ref);
                    i++;
                } else {
                    return;
                }
            } while (i != 16);
        }

        @GuardedBy("this")
        void drainValueReferenceQueue(ReferenceQueue<V> valueReferenceQueue) {
            int i = 0;
            do {
                Reference<? extends V> ref = valueReferenceQueue.poll();
                if (ref != null) {
                    WeakValueReference<K, V, E> valueReference = (WeakValueReference) ref;
                    this.map.reclaimValue(valueReference);
                    i++;
                } else {
                    return;
                }
            } while (i != 16);
        }

        <T> void clearReferenceQueue(ReferenceQueue<T> referenceQueue) {
            do {
            } while (referenceQueue.poll() != null);
        }

        E getFirst(int hash) {
            AtomicReferenceArray<E> table = this.table;
            return table.get(hash & (table.length() - 1));
        }

        E getEntry(Object key, int hash) {
            if (this.count != 0) {
                E first = getFirst(hash);
                while (true) {
                    E e = first;
                    if (e != null) {
                        if (e.getHash() == hash) {
                            Object key2 = e.getKey();
                            if (key2 == null) {
                                tryDrainReferenceQueues();
                            } else if (this.map.keyEquivalence.equivalent(key, key2)) {
                                return e;
                            }
                        }
                        first = (E) e.getNext();
                    } else {
                        return null;
                    }
                }
            } else {
                return null;
            }
        }

        E getLiveEntry(Object key, int hash) {
            return getEntry(key, hash);
        }

        V get(Object key, int hash) {
            try {
                E e = getLiveEntry(key, hash);
                if (e == null) {
                    return null;
                }
                V value = (V) e.getValue();
                if (value == null) {
                    tryDrainReferenceQueues();
                }
                postReadCleanup();
                return value;
            } finally {
                postReadCleanup();
            }
        }

        boolean containsKey(Object key, int hash) {
            boolean z;
            try {
                if (this.count != 0) {
                    E e = getLiveEntry(key, hash);
                    if (e != null) {
                        if (e.getValue() != null) {
                            z = true;
                            return z;
                        }
                    }
                    z = false;
                    return z;
                }
                postReadCleanup();
                return false;
            } finally {
                postReadCleanup();
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v28, types: [com.google.common.collect.MapMakerInternalMap$InternalEntry] */
        @VisibleForTesting
        boolean containsValue(Object value) {
            try {
                if (this.count != 0) {
                    AtomicReferenceArray<E> table = this.table;
                    int length = table.length();
                    for (int i = 0; i < length; i++) {
                        for (E e = table.get(i); e != null; e = e.getNext()) {
                            V entryValue = getLiveValue(e);
                            if (entryValue != null && this.map.valueEquivalence().equivalent(value, entryValue)) {
                                return true;
                            }
                        }
                    }
                }
                postReadCleanup();
                return false;
            } finally {
                postReadCleanup();
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        V put(K key, int hash, V value, boolean onlyIfAbsent) {
            lock();
            try {
                preWriteCleanup();
                int newCount = this.count + 1;
                if (newCount > this.threshold) {
                    expand();
                    newCount = this.count + 1;
                }
                AtomicReferenceArray<E> table = this.table;
                int index = hash & (table.length() - 1);
                E first = table.get(index);
                for (E e = first; e != null; e = e.getNext()) {
                    Object key2 = e.getKey();
                    if (e.getHash() == hash && key2 != null && this.map.keyEquivalence.equivalent(key, key2)) {
                        V entryValue = (V) e.getValue();
                        if (entryValue == null) {
                            this.modCount++;
                            setValue(e, value);
                            this.count = this.count;
                            unlock();
                            return null;
                        } else if (onlyIfAbsent) {
                            return entryValue;
                        } else {
                            this.modCount++;
                            setValue(e, value);
                            unlock();
                            return entryValue;
                        }
                    }
                }
                this.modCount++;
                E newEntry = this.map.entryHelper.newEntry(self(), key, hash, first);
                setValue(newEntry, value);
                table.set(index, newEntry);
                this.count = newCount;
                unlock();
                return null;
            } finally {
                unlock();
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v30 */
        /* JADX WARN: Type inference failed for: r0v46, types: [com.google.common.collect.MapMakerInternalMap$InternalEntry] */
        /* JADX WARN: Type inference failed for: r4v0, types: [com.google.common.collect.MapMakerInternalMap$Segment<K, V, E extends com.google.common.collect.MapMakerInternalMap$InternalEntry<K, V, E>, S extends com.google.common.collect.MapMakerInternalMap$Segment<K, V, E, S>>, com.google.common.collect.MapMakerInternalMap$Segment] */
        @GuardedBy("this")
        void expand() {
            AtomicReferenceArray<E> oldTable = this.table;
            int oldCapacity = oldTable.length();
            if (oldCapacity >= 1073741824) {
                return;
            }
            int newCount = this.count;
            AtomicReferenceArray atomicReferenceArray = (AtomicReferenceArray<E>) newEntryArray(oldCapacity << 1);
            this.threshold = (atomicReferenceArray.length() * 3) / 4;
            int newMask = atomicReferenceArray.length() - 1;
            for (int oldIndex = 0; oldIndex < oldCapacity; oldIndex++) {
                E head = oldTable.get(oldIndex);
                if (head != null) {
                    InternalEntry next = head.getNext();
                    int headIndex = head.getHash() & newMask;
                    if (next == null) {
                        atomicReferenceArray.set(headIndex, head);
                    } else {
                        E tail = head;
                        int tailIndex = headIndex;
                        InternalEntry internalEntry = next;
                        while (true) {
                            E e = internalEntry;
                            if (e == null) {
                                break;
                            }
                            int newIndex = e.getHash() & newMask;
                            if (newIndex != tailIndex) {
                                tailIndex = newIndex;
                                tail = e;
                            }
                            internalEntry = e.getNext();
                        }
                        atomicReferenceArray.set(tailIndex, tail);
                        E e2 = head;
                        while (true) {
                            E e3 = e2;
                            if (e3 != tail) {
                                int newIndex2 = e3.getHash() & newMask;
                                InternalEntry copyEntry = copyEntry(e3, (InternalEntry) atomicReferenceArray.get(newIndex2));
                                if (copyEntry != null) {
                                    atomicReferenceArray.set(newIndex2, copyEntry);
                                } else {
                                    newCount--;
                                }
                                e2 = e3.getNext();
                            }
                        }
                    }
                }
            }
            this.table = atomicReferenceArray;
            this.count = newCount;
        }

        /* JADX WARN: Multi-variable type inference failed */
        boolean replace(K key, int hash, V oldValue, V newValue) {
            lock();
            try {
                preWriteCleanup();
                AtomicReferenceArray<E> table = this.table;
                int index = hash & (table.length() - 1);
                E first = table.get(index);
                for (E e = first; e != null; e = e.getNext()) {
                    Object key2 = e.getKey();
                    if (e.getHash() == hash && key2 != null && this.map.keyEquivalence.equivalent(key, key2)) {
                        Object value = e.getValue();
                        if (value != null) {
                            if (!this.map.valueEquivalence().equivalent(oldValue, value)) {
                                unlock();
                                return false;
                            }
                            this.modCount++;
                            setValue(e, newValue);
                            unlock();
                            return true;
                        }
                        if (isCollected(e)) {
                            int i = this.count - 1;
                            this.modCount++;
                            E newFirst = removeFromChain(first, e);
                            int newCount = this.count - 1;
                            table.set(index, newFirst);
                            this.count = newCount;
                        }
                        return false;
                    }
                }
                unlock();
                return false;
            } finally {
                unlock();
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        V replace(K key, int hash, V newValue) {
            lock();
            try {
                preWriteCleanup();
                AtomicReferenceArray<E> table = this.table;
                int index = hash & (table.length() - 1);
                E first = table.get(index);
                for (E e = first; e != null; e = e.getNext()) {
                    Object key2 = e.getKey();
                    if (e.getHash() == hash && key2 != null && this.map.keyEquivalence.equivalent(key, key2)) {
                        V entryValue = (V) e.getValue();
                        if (entryValue != null) {
                            this.modCount++;
                            setValue(e, newValue);
                            unlock();
                            return entryValue;
                        }
                        if (isCollected(e)) {
                            int i = this.count - 1;
                            this.modCount++;
                            E newFirst = removeFromChain(first, e);
                            int newCount = this.count - 1;
                            table.set(index, newFirst);
                            this.count = newCount;
                        }
                        return null;
                    }
                }
                unlock();
                return null;
            } finally {
                unlock();
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @CanIgnoreReturnValue
        V remove(Object key, int hash) {
            lock();
            try {
                preWriteCleanup();
                int i = this.count - 1;
                AtomicReferenceArray<E> table = this.table;
                int index = hash & (table.length() - 1);
                E first = table.get(index);
                for (E e = first; e != null; e = e.getNext()) {
                    Object key2 = e.getKey();
                    if (e.getHash() == hash && key2 != null && this.map.keyEquivalence.equivalent(key, key2)) {
                        V entryValue = (V) e.getValue();
                        if (entryValue == null && !isCollected(e)) {
                            return null;
                        }
                        this.modCount++;
                        E newFirst = removeFromChain(first, e);
                        int newCount = this.count - 1;
                        table.set(index, newFirst);
                        this.count = newCount;
                        unlock();
                        return entryValue;
                    }
                }
                unlock();
                return null;
            } finally {
                unlock();
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        boolean remove(Object key, int hash, Object value) {
            lock();
            try {
                preWriteCleanup();
                int i = this.count - 1;
                AtomicReferenceArray<E> table = this.table;
                int index = hash & (table.length() - 1);
                E first = table.get(index);
                for (E e = first; e != null; e = e.getNext()) {
                    Object key2 = e.getKey();
                    if (e.getHash() == hash && key2 != null && this.map.keyEquivalence.equivalent(key, key2)) {
                        boolean explicitRemoval = false;
                        if (this.map.valueEquivalence().equivalent(value, e.getValue())) {
                            explicitRemoval = true;
                        } else if (!isCollected(e)) {
                            return false;
                        }
                        this.modCount++;
                        E newFirst = removeFromChain(first, e);
                        int newCount = this.count - 1;
                        table.set(index, newFirst);
                        this.count = newCount;
                        boolean z = explicitRemoval;
                        unlock();
                        return z;
                    }
                }
                unlock();
                return false;
            } finally {
                unlock();
            }
        }

        void clear() {
            if (this.count != 0) {
                lock();
                try {
                    AtomicReferenceArray<E> table = this.table;
                    for (int i = 0; i < table.length(); i++) {
                        table.set(i, null);
                    }
                    maybeClearReferenceQueues();
                    this.readCount.set(0);
                    this.modCount++;
                    this.count = 0;
                } finally {
                    unlock();
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v13, types: [com.google.common.collect.MapMakerInternalMap$InternalEntry] */
        /* JADX WARN: Type inference failed for: r0v3, types: [com.google.common.collect.MapMakerInternalMap$InternalEntry] */
        @GuardedBy("this")
        E removeFromChain(E first, E entry) {
            int newCount = this.count;
            E newFirst = entry.getNext();
            E e = first;
            while (true) {
                E e2 = e;
                if (e2 != entry) {
                    E next = copyEntry(e2, newFirst);
                    if (next != null) {
                        newFirst = next;
                    } else {
                        newCount--;
                    }
                    e = e2.getNext();
                } else {
                    this.count = newCount;
                    return newFirst;
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v12, types: [com.google.common.collect.MapMakerInternalMap$InternalEntry] */
        /* JADX WARN: Type inference failed for: r0v31, types: [com.google.common.collect.MapMakerInternalMap$InternalEntry] */
        /* JADX WARN: Type inference failed for: r4v0, types: [com.google.common.collect.MapMakerInternalMap$Segment<K, V, E extends com.google.common.collect.MapMakerInternalMap$InternalEntry<K, V, E>, S extends com.google.common.collect.MapMakerInternalMap$Segment<K, V, E, S>>, com.google.common.collect.MapMakerInternalMap$Segment] */
        @CanIgnoreReturnValue
        boolean reclaimKey(E entry, int hash) {
            lock();
            try {
                int i = this.count - 1;
                AtomicReferenceArray atomicReferenceArray = (AtomicReferenceArray<E>) this.table;
                int index = hash & (atomicReferenceArray.length() - 1);
                ?? r0 = (InternalEntry) atomicReferenceArray.get(index);
                for (E e = r0; e != null; e = e.getNext()) {
                    if (e == entry) {
                        this.modCount++;
                        InternalEntry removeFromChain = removeFromChain(r0, e);
                        int newCount = this.count - 1;
                        atomicReferenceArray.set(index, removeFromChain);
                        this.count = newCount;
                        unlock();
                        return true;
                    }
                }
                return false;
            } finally {
                unlock();
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @CanIgnoreReturnValue
        boolean reclaimValue(K key, int hash, WeakValueReference<K, V, E> valueReference) {
            lock();
            try {
                int i = this.count - 1;
                AtomicReferenceArray<E> table = this.table;
                int index = hash & (table.length() - 1);
                E first = table.get(index);
                for (E e = first; e != null; e = e.getNext()) {
                    Object key2 = e.getKey();
                    if (e.getHash() == hash && key2 != null && this.map.keyEquivalence.equivalent(key, key2)) {
                        WeakValueReference<K, V, E> v = ((WeakValueEntry) e).getValueReference();
                        if (v == valueReference) {
                            this.modCount++;
                            E newFirst = removeFromChain(first, e);
                            int newCount = this.count - 1;
                            table.set(index, newFirst);
                            this.count = newCount;
                            unlock();
                            return true;
                        }
                        return false;
                    }
                }
                unlock();
                return false;
            } finally {
                unlock();
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @CanIgnoreReturnValue
        boolean clearValueForTesting(K key, int hash, WeakValueReference<K, V, ? extends InternalEntry<K, V, ?>> valueReference) {
            lock();
            try {
                AtomicReferenceArray<E> table = this.table;
                int index = hash & (table.length() - 1);
                E first = table.get(index);
                for (E e = first; e != null; e = e.getNext()) {
                    Object key2 = e.getKey();
                    if (e.getHash() == hash && key2 != null && this.map.keyEquivalence.equivalent(key, key2)) {
                        WeakValueReference<K, V, E> v = ((WeakValueEntry) e).getValueReference();
                        if (v == valueReference) {
                            E newFirst = removeFromChain(first, e);
                            table.set(index, newFirst);
                            unlock();
                            return true;
                        }
                        return false;
                    }
                }
                unlock();
                return false;
            } finally {
                unlock();
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v13 */
        /* JADX WARN: Type inference failed for: r4v0, types: [com.google.common.collect.MapMakerInternalMap$Segment<K, V, E extends com.google.common.collect.MapMakerInternalMap$InternalEntry<K, V, E>, S extends com.google.common.collect.MapMakerInternalMap$Segment<K, V, E, S>>, com.google.common.collect.MapMakerInternalMap$Segment] */
        @GuardedBy("this")
        boolean removeEntryForTesting(E entry) {
            int hash = entry.getHash();
            int i = this.count - 1;
            AtomicReferenceArray atomicReferenceArray = (AtomicReferenceArray<E>) this.table;
            int index = hash & (atomicReferenceArray.length() - 1);
            InternalEntry internalEntry = (InternalEntry) atomicReferenceArray.get(index);
            InternalEntry internalEntry2 = internalEntry;
            while (true) {
                E e = internalEntry2;
                if (e != null) {
                    if (e != entry) {
                        internalEntry2 = e.getNext();
                    } else {
                        this.modCount++;
                        InternalEntry removeFromChain = removeFromChain(internalEntry, e);
                        int newCount = this.count - 1;
                        atomicReferenceArray.set(index, removeFromChain);
                        this.count = newCount;
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }

        static <K, V, E extends InternalEntry<K, V, E>> boolean isCollected(E entry) {
            return entry.getValue() == null;
        }

        @NullableDecl
        V getLiveValue(E entry) {
            if (entry.getKey() == null) {
                tryDrainReferenceQueues();
                return null;
            }
            V value = (V) entry.getValue();
            if (value == null) {
                tryDrainReferenceQueues();
                return null;
            }
            return value;
        }

        void postReadCleanup() {
            if ((this.readCount.incrementAndGet() & 63) == 0) {
                runCleanup();
            }
        }

        @GuardedBy("this")
        void preWriteCleanup() {
            runLockedCleanup();
        }

        void runCleanup() {
            runLockedCleanup();
        }

        void runLockedCleanup() {
            if (tryLock()) {
                try {
                    maybeDrainReferenceQueues();
                    this.readCount.set(0);
                } finally {
                    unlock();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$StrongKeyStrongValueSegment.class */
    public static final class StrongKeyStrongValueSegment<K, V> extends Segment<K, V, StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>> {
        StrongKeyStrongValueSegment(MapMakerInternalMap<K, V, StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
            super(map, initialCapacity, maxSegmentSize);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public StrongKeyStrongValueSegment<K, V> self() {
            return this;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public StrongKeyStrongValueEntry<K, V> castForTesting(InternalEntry<K, V, ?> entry) {
            return (StrongKeyStrongValueEntry) entry;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$StrongKeyWeakValueSegment.class */
    public static final class StrongKeyWeakValueSegment<K, V> extends Segment<K, V, StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>> {
        private final ReferenceQueue<V> queueForValues;

        StrongKeyWeakValueSegment(MapMakerInternalMap<K, V, StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
            super(map, initialCapacity, maxSegmentSize);
            this.queueForValues = new ReferenceQueue<>();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public StrongKeyWeakValueSegment<K, V> self() {
            return this;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        ReferenceQueue<V> getValueReferenceQueueForTesting() {
            return this.queueForValues;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public StrongKeyWeakValueEntry<K, V> castForTesting(InternalEntry<K, V, ?> entry) {
            return (StrongKeyWeakValueEntry) entry;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> getWeakValueReferenceForTesting(InternalEntry<K, V, ?> e) {
            return castForTesting((InternalEntry) e).getValueReference();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> newWeakValueReferenceForTesting(InternalEntry<K, V, ?> e, V value) {
            return new WeakValueReferenceImpl(this.queueForValues, value, castForTesting((InternalEntry) e));
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public void setWeakValueReferenceForTesting(InternalEntry<K, V, ?> e, WeakValueReference<K, V, ? extends InternalEntry<K, V, ?>> valueReference) {
            StrongKeyWeakValueEntry<K, V> entry = castForTesting((InternalEntry) e);
            WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> previous = ((StrongKeyWeakValueEntry) entry).valueReference;
            ((StrongKeyWeakValueEntry) entry).valueReference = valueReference;
            previous.clear();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        void maybeDrainReferenceQueues() {
            drainValueReferenceQueue(this.queueForValues);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        void maybeClearReferenceQueues() {
            clearReferenceQueue((ReferenceQueue<V>) this.queueForValues);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$StrongKeyDummyValueSegment.class */
    public static final class StrongKeyDummyValueSegment<K> extends Segment<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>, StrongKeyDummyValueSegment<K>> {
        StrongKeyDummyValueSegment(MapMakerInternalMap<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>, StrongKeyDummyValueSegment<K>> map, int initialCapacity, int maxSegmentSize) {
            super(map, initialCapacity, maxSegmentSize);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public StrongKeyDummyValueSegment<K> self() {
            return this;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public StrongKeyDummyValueEntry<K> castForTesting(InternalEntry<K, MapMaker.Dummy, ?> entry) {
            return (StrongKeyDummyValueEntry) entry;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$WeakKeyStrongValueSegment.class */
    public static final class WeakKeyStrongValueSegment<K, V> extends Segment<K, V, WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>> {
        private final ReferenceQueue<K> queueForKeys;

        WeakKeyStrongValueSegment(MapMakerInternalMap<K, V, WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
            super(map, initialCapacity, maxSegmentSize);
            this.queueForKeys = new ReferenceQueue<>();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public WeakKeyStrongValueSegment<K, V> self() {
            return this;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        ReferenceQueue<K> getKeyReferenceQueueForTesting() {
            return this.queueForKeys;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public WeakKeyStrongValueEntry<K, V> castForTesting(InternalEntry<K, V, ?> entry) {
            return (WeakKeyStrongValueEntry) entry;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        void maybeDrainReferenceQueues() {
            drainKeyReferenceQueue(this.queueForKeys);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        void maybeClearReferenceQueues() {
            clearReferenceQueue((ReferenceQueue<K>) this.queueForKeys);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$WeakKeyWeakValueSegment.class */
    public static final class WeakKeyWeakValueSegment<K, V> extends Segment<K, V, WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>> {
        private final ReferenceQueue<K> queueForKeys;
        private final ReferenceQueue<V> queueForValues;

        WeakKeyWeakValueSegment(MapMakerInternalMap<K, V, WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
            super(map, initialCapacity, maxSegmentSize);
            this.queueForKeys = new ReferenceQueue<>();
            this.queueForValues = new ReferenceQueue<>();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public WeakKeyWeakValueSegment<K, V> self() {
            return this;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        ReferenceQueue<K> getKeyReferenceQueueForTesting() {
            return this.queueForKeys;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        ReferenceQueue<V> getValueReferenceQueueForTesting() {
            return this.queueForValues;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public WeakKeyWeakValueEntry<K, V> castForTesting(InternalEntry<K, V, ?> entry) {
            return (WeakKeyWeakValueEntry) entry;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> getWeakValueReferenceForTesting(InternalEntry<K, V, ?> e) {
            return castForTesting((InternalEntry) e).getValueReference();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> newWeakValueReferenceForTesting(InternalEntry<K, V, ?> e, V value) {
            return new WeakValueReferenceImpl(this.queueForValues, value, castForTesting((InternalEntry) e));
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public void setWeakValueReferenceForTesting(InternalEntry<K, V, ?> e, WeakValueReference<K, V, ? extends InternalEntry<K, V, ?>> valueReference) {
            WeakKeyWeakValueEntry<K, V> entry = castForTesting((InternalEntry) e);
            WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> previous = ((WeakKeyWeakValueEntry) entry).valueReference;
            ((WeakKeyWeakValueEntry) entry).valueReference = valueReference;
            previous.clear();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        void maybeDrainReferenceQueues() {
            drainKeyReferenceQueue(this.queueForKeys);
            drainValueReferenceQueue(this.queueForValues);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        void maybeClearReferenceQueues() {
            clearReferenceQueue((ReferenceQueue<K>) this.queueForKeys);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$WeakKeyDummyValueSegment.class */
    public static final class WeakKeyDummyValueSegment<K> extends Segment<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>, WeakKeyDummyValueSegment<K>> {
        private final ReferenceQueue<K> queueForKeys;

        WeakKeyDummyValueSegment(MapMakerInternalMap<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>, WeakKeyDummyValueSegment<K>> map, int initialCapacity, int maxSegmentSize) {
            super(map, initialCapacity, maxSegmentSize);
            this.queueForKeys = new ReferenceQueue<>();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public WeakKeyDummyValueSegment<K> self() {
            return this;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        ReferenceQueue<K> getKeyReferenceQueueForTesting() {
            return this.queueForKeys;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public WeakKeyDummyValueEntry<K> castForTesting(InternalEntry<K, MapMaker.Dummy, ?> entry) {
            return (WeakKeyDummyValueEntry) entry;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        void maybeDrainReferenceQueues() {
            drainKeyReferenceQueue(this.queueForKeys);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        void maybeClearReferenceQueues() {
            clearReferenceQueue((ReferenceQueue<K>) this.queueForKeys);
        }
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$CleanupMapTask.class */
    static final class CleanupMapTask implements Runnable {
        final WeakReference<MapMakerInternalMap<?, ?, ?, ?>> mapReference;

        public CleanupMapTask(MapMakerInternalMap<?, ?, ?, ?> map) {
            this.mapReference = new WeakReference<>(map);
        }

        @Override // java.lang.Runnable
        public void run() {
            Segment<?, ?, ?, ?>[] segmentArr;
            MapMakerInternalMap<?, ?, ?, ?> map = this.mapReference.get();
            if (map == null) {
                throw new CancellationException();
            }
            for (Segment<?, ?, ?, ?> segment : map.segments) {
                segment.runCleanup();
            }
        }
    }

    @VisibleForTesting
    Strength keyStrength() {
        return this.entryHelper.keyStrength();
    }

    @VisibleForTesting
    Strength valueStrength() {
        return this.entryHelper.valueStrength();
    }

    @VisibleForTesting
    Equivalence<Object> valueEquivalence() {
        return this.entryHelper.valueStrength().defaultEquivalence();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean isEmpty() {
        long sum = 0;
        Segment<K, V, E, S>[] segments = this.segments;
        for (int i = 0; i < segments.length; i++) {
            if (segments[i].count != 0) {
                return false;
            }
            sum += segments[i].modCount;
        }
        if (sum != 0) {
            for (int i2 = 0; i2 < segments.length; i2++) {
                if (segments[i2].count != 0) {
                    return false;
                }
                sum -= segments[i2].modCount;
            }
            if (sum != 0) {
                return false;
            }
            return true;
        }
        return true;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        Segment<K, V, E, S>[] segments = this.segments;
        long sum = 0;
        for (Segment<K, V, E, S> segment : segments) {
            sum += segment.count;
        }
        return Ints.saturatedCast(sum);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V get(@NullableDecl Object key) {
        if (key == null) {
            return null;
        }
        int hash = hash(key);
        return segmentFor(hash).get(key, hash);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public E getEntry(@NullableDecl Object key) {
        if (key == null) {
            return null;
        }
        int hash = hash(key);
        return segmentFor(hash).getEntry(key, hash);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(@NullableDecl Object key) {
        if (key == null) {
            return false;
        }
        int hash = hash(key);
        return segmentFor(hash).containsKey(key, hash);
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x0089, code lost:
        r19 = r19 + 1;
     */
    @Override // java.util.AbstractMap, java.util.Map
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean containsValue(@org.checkerframework.checker.nullness.compatqual.NullableDecl java.lang.Object r6) {
        /*
            r5 = this;
            r0 = r6
            if (r0 != 0) goto L6
            r0 = 0
            return r0
        L6:
            r0 = r5
            com.google.common.collect.MapMakerInternalMap$Segment<K, V, E extends com.google.common.collect.MapMakerInternalMap$InternalEntry<K, V, E>, S extends com.google.common.collect.MapMakerInternalMap$Segment<K, V, E, S>>[] r0 = r0.segments
            r7 = r0
            r0 = -1
            r8 = r0
            r0 = 0
            r10 = r0
        L12:
            r0 = r10
            r1 = 3
            if (r0 >= r1) goto Lb3
            r0 = 0
            r11 = r0
            r0 = r7
            r13 = r0
            r0 = r13
            int r0 = r0.length
            r14 = r0
            r0 = 0
            r15 = r0
        L26:
            r0 = r15
            r1 = r14
            if (r0 >= r1) goto La0
            r0 = r13
            r1 = r15
            r0 = r0[r1]
            r16 = r0
            r0 = r16
            int r0 = r0.count
            r17 = r0
            r0 = r16
            java.util.concurrent.atomic.AtomicReferenceArray<E extends com.google.common.collect.MapMakerInternalMap$InternalEntry<K, V, E>> r0 = r0.table
            r18 = r0
            r0 = 0
            r19 = r0
        L45:
            r0 = r19
            r1 = r18
            int r1 = r1.length()
            if (r0 >= r1) goto L8f
            r0 = r18
            r1 = r19
            java.lang.Object r0 = r0.get(r1)
            com.google.common.collect.MapMakerInternalMap$InternalEntry r0 = (com.google.common.collect.MapMakerInternalMap.InternalEntry) r0
            r20 = r0
        L5b:
            r0 = r20
            if (r0 == 0) goto L89
            r0 = r16
            r1 = r20
            java.lang.Object r0 = r0.getLiveValue(r1)
            r21 = r0
            r0 = r21
            if (r0 == 0) goto L7d
            r0 = r5
            com.google.common.base.Equivalence r0 = r0.valueEquivalence()
            r1 = r6
            r2 = r21
            boolean r0 = r0.equivalent(r1, r2)
            if (r0 == 0) goto L7d
            r0 = 1
            return r0
        L7d:
            r0 = r20
            com.google.common.collect.MapMakerInternalMap$InternalEntry r0 = r0.getNext()
            r20 = r0
            goto L5b
        L89:
            int r19 = r19 + 1
            goto L45
        L8f:
            r0 = r11
            r1 = r16
            int r1 = r1.modCount
            long r1 = (long) r1
            long r0 = r0 + r1
            r11 = r0
            int r15 = r15 + 1
            goto L26
        La0:
            r0 = r11
            r1 = r8
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 != 0) goto Laa
            goto Lb3
        Laa:
            r0 = r11
            r8 = r0
            int r10 = r10 + 1
            goto L12
        Lb3:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.MapMakerInternalMap.containsValue(java.lang.Object):boolean");
    }

    @Override // java.util.AbstractMap, java.util.Map
    @CanIgnoreReturnValue
    public V put(K key, V value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        int hash = hash(key);
        return segmentFor(hash).put(key, hash, value, false);
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    @CanIgnoreReturnValue
    public V putIfAbsent(K key, V value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        int hash = hash(key);
        return segmentFor(hash).put(key, hash, value, true);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    @CanIgnoreReturnValue
    public V remove(@NullableDecl Object key) {
        if (key == null) {
            return null;
        }
        int hash = hash(key);
        return segmentFor(hash).remove(key, hash);
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    @CanIgnoreReturnValue
    public boolean remove(@NullableDecl Object key, @NullableDecl Object value) {
        if (key == null || value == null) {
            return false;
        }
        int hash = hash(key);
        return segmentFor(hash).remove(key, hash, value);
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    @CanIgnoreReturnValue
    public boolean replace(K key, @NullableDecl V oldValue, V newValue) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(newValue);
        if (oldValue == null) {
            return false;
        }
        int hash = hash(key);
        return segmentFor(hash).replace(key, hash, oldValue, newValue);
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    @CanIgnoreReturnValue
    public V replace(K key, V value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        int hash = hash(key);
        return segmentFor(hash).replace(key, hash, value);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        Segment<K, V, E, S>[] segmentArr;
        for (Segment<K, V, E, S> segment : this.segments) {
            segment.clear();
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<K> keySet() {
        Set<K> ks = this.keySet;
        if (ks != null) {
            return ks;
        }
        KeySet keySet = new KeySet();
        this.keySet = keySet;
        return keySet;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Collection<V> values() {
        Collection<V> vs = this.values;
        if (vs != null) {
            return vs;
        }
        Values values = new Values();
        this.values = values;
        return values;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> es = this.entrySet;
        if (es != null) {
            return es;
        }
        EntrySet entrySet = new EntrySet();
        this.entrySet = entrySet;
        return entrySet;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$HashIterator.class */
    public abstract class HashIterator<T> implements Iterator<T> {
        int nextSegmentIndex;
        int nextTableIndex = -1;
        @MonotonicNonNullDecl
        Segment<K, V, E, S> currentSegment;
        @MonotonicNonNullDecl
        AtomicReferenceArray<E> currentTable;
        @NullableDecl
        E nextEntry;
        @NullableDecl
        MapMakerInternalMap<K, V, E, S>.WriteThroughEntry nextExternal;
        @NullableDecl
        MapMakerInternalMap<K, V, E, S>.WriteThroughEntry lastReturned;

        @Override // java.util.Iterator
        public abstract T next();

        HashIterator() {
            this.nextSegmentIndex = MapMakerInternalMap.this.segments.length - 1;
            advance();
        }

        final void advance() {
            this.nextExternal = null;
            if (nextInChain() || nextInTable()) {
                return;
            }
            while (this.nextSegmentIndex >= 0) {
                Segment<K, V, E, S>[] segmentArr = MapMakerInternalMap.this.segments;
                int i = this.nextSegmentIndex;
                this.nextSegmentIndex = i - 1;
                this.currentSegment = segmentArr[i];
                if (this.currentSegment.count != 0) {
                    this.currentTable = this.currentSegment.table;
                    this.nextTableIndex = this.currentTable.length() - 1;
                    if (nextInTable()) {
                        return;
                    }
                }
            }
        }

        boolean nextInChain() {
            if (this.nextEntry != null) {
                this.nextEntry = (E) this.nextEntry.getNext();
                while (this.nextEntry != null) {
                    if (!advanceTo(this.nextEntry)) {
                        this.nextEntry = (E) this.nextEntry.getNext();
                    } else {
                        return true;
                    }
                }
                return false;
            }
            return false;
        }

        boolean nextInTable() {
            while (this.nextTableIndex >= 0) {
                AtomicReferenceArray<E> atomicReferenceArray = this.currentTable;
                int i = this.nextTableIndex;
                this.nextTableIndex = i - 1;
                E e = atomicReferenceArray.get(i);
                this.nextEntry = e;
                if (e != null && (advanceTo(this.nextEntry) || nextInChain())) {
                    return true;
                }
            }
            return false;
        }

        boolean advanceTo(E entry) {
            try {
                Object key = entry.getKey();
                Object liveValue = MapMakerInternalMap.this.getLiveValue(entry);
                if (liveValue != null) {
                    this.nextExternal = new WriteThroughEntry(key, liveValue);
                    this.currentSegment.postReadCleanup();
                    return true;
                }
                return false;
            } finally {
                this.currentSegment.postReadCleanup();
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.nextExternal != null;
        }

        MapMakerInternalMap<K, V, E, S>.WriteThroughEntry nextEntry() {
            if (this.nextExternal == null) {
                throw new NoSuchElementException();
            }
            this.lastReturned = this.nextExternal;
            advance();
            return this.lastReturned;
        }

        @Override // java.util.Iterator
        public void remove() {
            CollectPreconditions.checkRemove(this.lastReturned != null);
            MapMakerInternalMap.this.remove(this.lastReturned.getKey());
            this.lastReturned = null;
        }
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$KeyIterator.class */
    final class KeyIterator extends MapMakerInternalMap<K, V, E, S>.HashIterator<K> {
        KeyIterator() {
            super();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.HashIterator, java.util.Iterator
        public K next() {
            return nextEntry().getKey();
        }
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$ValueIterator.class */
    final class ValueIterator extends MapMakerInternalMap<K, V, E, S>.HashIterator<V> {
        ValueIterator() {
            super();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.HashIterator, java.util.Iterator
        public V next() {
            return nextEntry().getValue();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$WriteThroughEntry.class */
    public final class WriteThroughEntry extends AbstractMapEntry<K, V> {
        final K key;
        V value;

        WriteThroughEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        public K getKey() {
            return this.key;
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        public V getValue() {
            return this.value;
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        public boolean equals(@NullableDecl Object object) {
            if (object instanceof Map.Entry) {
                Map.Entry<?, ?> that = (Map.Entry) object;
                return this.key.equals(that.getKey()) && this.value.equals(that.getValue());
            }
            return false;
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        public int hashCode() {
            return this.key.hashCode() ^ this.value.hashCode();
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        public V setValue(V newValue) {
            V oldValue = (V) MapMakerInternalMap.this.put(this.key, newValue);
            this.value = newValue;
            return oldValue;
        }
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$EntryIterator.class */
    final class EntryIterator extends MapMakerInternalMap<K, V, E, S>.HashIterator<Map.Entry<K, V>> {
        EntryIterator() {
            super();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.HashIterator, java.util.Iterator
        public Map.Entry<K, V> next() {
            return nextEntry();
        }
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$KeySet.class */
    final class KeySet extends SafeToArraySet<K> {
        KeySet() {
            super();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return MapMakerInternalMap.this.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return MapMakerInternalMap.this.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            return MapMakerInternalMap.this.containsKey(o);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            return MapMakerInternalMap.this.remove(o) != null;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            MapMakerInternalMap.this.clear();
        }
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$Values.class */
    final class Values extends AbstractCollection<V> {
        Values() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return MapMakerInternalMap.this.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return MapMakerInternalMap.this.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object o) {
            return MapMakerInternalMap.this.containsValue(o);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            MapMakerInternalMap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public Object[] toArray() {
            return MapMakerInternalMap.toArrayList(this).toArray();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public <T> T[] toArray(T[] a) {
            return (T[]) MapMakerInternalMap.toArrayList(this).toArray(a);
        }
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$EntrySet.class */
    final class EntrySet extends SafeToArraySet<Map.Entry<K, V>> {
        EntrySet() {
            super();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            Map.Entry<?, ?> e;
            Object key;
            Object obj;
            return (o instanceof Map.Entry) && (key = (e = (Map.Entry) o).getKey()) != null && (obj = MapMakerInternalMap.this.get(key)) != null && MapMakerInternalMap.this.valueEquivalence().equivalent(e.getValue(), obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            Map.Entry<?, ?> e;
            Object key;
            return (o instanceof Map.Entry) && (key = (e = (Map.Entry) o).getKey()) != null && MapMakerInternalMap.this.remove(key, e.getValue());
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return MapMakerInternalMap.this.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return MapMakerInternalMap.this.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            MapMakerInternalMap.this.clear();
        }
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$SafeToArraySet.class */
    private static abstract class SafeToArraySet<E> extends AbstractSet<E> {
        private SafeToArraySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public Object[] toArray() {
            return MapMakerInternalMap.toArrayList(this).toArray();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public <T> T[] toArray(T[] a) {
            return (T[]) MapMakerInternalMap.toArrayList(this).toArray(a);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <E> ArrayList<E> toArrayList(Collection<E> c) {
        ArrayList<E> result = new ArrayList<>(c.size());
        Iterators.addAll(result, c.iterator());
        return result;
    }

    Object writeReplace() {
        return new SerializationProxy(this.entryHelper.keyStrength(), this.entryHelper.valueStrength(), this.keyEquivalence, this.entryHelper.valueStrength().defaultEquivalence(), this.concurrencyLevel, this);
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$AbstractSerializationProxy.class */
    static abstract class AbstractSerializationProxy<K, V> extends ForwardingConcurrentMap<K, V> implements Serializable {
        private static final long serialVersionUID = 3;
        final Strength keyStrength;
        final Strength valueStrength;
        final Equivalence<Object> keyEquivalence;
        final Equivalence<Object> valueEquivalence;
        final int concurrencyLevel;
        transient ConcurrentMap<K, V> delegate;

        AbstractSerializationProxy(Strength keyStrength, Strength valueStrength, Equivalence<Object> keyEquivalence, Equivalence<Object> valueEquivalence, int concurrencyLevel, ConcurrentMap<K, V> delegate) {
            this.keyStrength = keyStrength;
            this.valueStrength = valueStrength;
            this.keyEquivalence = keyEquivalence;
            this.valueEquivalence = valueEquivalence;
            this.concurrencyLevel = concurrencyLevel;
            this.delegate = delegate;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.ForwardingConcurrentMap, com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
        public ConcurrentMap<K, V> delegate() {
            return this.delegate;
        }

        void writeMapTo(ObjectOutputStream out) throws IOException {
            out.writeInt(this.delegate.size());
            for (Map.Entry<K, V> entry : this.delegate.entrySet()) {
                out.writeObject(entry.getKey());
                out.writeObject(entry.getValue());
            }
            out.writeObject(null);
        }

        MapMaker readMapMaker(ObjectInputStream in) throws IOException {
            int size = in.readInt();
            return new MapMaker().initialCapacity(size).setKeyStrength(this.keyStrength).setValueStrength(this.valueStrength).keyEquivalence(this.keyEquivalence).concurrencyLevel(this.concurrencyLevel);
        }

        /* JADX WARN: Multi-variable type inference failed */
        void readEntries(ObjectInputStream in) throws IOException, ClassNotFoundException {
            while (true) {
                Object readObject = in.readObject();
                if (readObject != null) {
                    this.delegate.put(readObject, in.readObject());
                } else {
                    return;
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/MapMakerInternalMap$SerializationProxy.class */
    private static final class SerializationProxy<K, V> extends AbstractSerializationProxy<K, V> {
        private static final long serialVersionUID = 3;

        SerializationProxy(Strength keyStrength, Strength valueStrength, Equivalence<Object> keyEquivalence, Equivalence<Object> valueEquivalence, int concurrencyLevel, ConcurrentMap<K, V> delegate) {
            super(keyStrength, valueStrength, keyEquivalence, valueEquivalence, concurrencyLevel, delegate);
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();
            writeMapTo(out);
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            MapMaker mapMaker = readMapMaker(in);
            this.delegate = mapMaker.makeMap();
            readEntries(in);
        }

        private Object readResolve() {
            return this.delegate;
        }
    }
}
