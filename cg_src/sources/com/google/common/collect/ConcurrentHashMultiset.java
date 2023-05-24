package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset;
import com.google.common.collect.Serialization;
import com.google.common.primitives.Ints;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
@GwtIncompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/ConcurrentHashMultiset.class */
public final class ConcurrentHashMultiset<E> extends AbstractMultiset<E> implements Serializable {
    private final transient ConcurrentMap<E, AtomicInteger> countMap;
    private static final long serialVersionUID = 1;

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ Set entrySet() {
        return super.entrySet();
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ Set elementSet() {
        return super.elementSet();
    }

    @Override // com.google.common.collect.AbstractMultiset, java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ boolean contains(@NullableDecl Object obj) {
        return super.contains(obj);
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/ConcurrentHashMultiset$FieldSettersHolder.class */
    private static class FieldSettersHolder {
        static final Serialization.FieldSetter<ConcurrentHashMultiset> COUNT_MAP_FIELD_SETTER = Serialization.getFieldSetter(ConcurrentHashMultiset.class, "countMap");

        private FieldSettersHolder() {
        }
    }

    public static <E> ConcurrentHashMultiset<E> create() {
        return new ConcurrentHashMultiset<>(new ConcurrentHashMap());
    }

    public static <E> ConcurrentHashMultiset<E> create(Iterable<? extends E> elements) {
        ConcurrentHashMultiset<E> multiset = create();
        Iterables.addAll(multiset, elements);
        return multiset;
    }

    @Beta
    public static <E> ConcurrentHashMultiset<E> create(ConcurrentMap<E, AtomicInteger> countMap) {
        return new ConcurrentHashMultiset<>(countMap);
    }

    @VisibleForTesting
    ConcurrentHashMultiset(ConcurrentMap<E, AtomicInteger> countMap) {
        Preconditions.checkArgument(countMap.isEmpty(), "the backing map (%s) must be empty", countMap);
        this.countMap = countMap;
    }

    @Override // com.google.common.collect.Multiset
    public int count(@NullableDecl Object element) {
        AtomicInteger existingCounter = (AtomicInteger) Maps.safeGet(this.countMap, element);
        if (existingCounter == null) {
            return 0;
        }
        return existingCounter.get();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
    public int size() {
        long sum = 0;
        for (AtomicInteger value : this.countMap.values()) {
            sum += value.get();
        }
        return Ints.saturatedCast(sum);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public Object[] toArray() {
        return snapshot().toArray();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public <T> T[] toArray(T[] array) {
        return (T[]) snapshot().toArray(array);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private List<E> snapshot() {
        ArrayList newArrayListWithExpectedSize = Lists.newArrayListWithExpectedSize(size());
        for (Multiset.Entry<E> entry : entrySet()) {
            E element = entry.getElement();
            for (int i = entry.getCount(); i > 0; i--) {
                newArrayListWithExpectedSize.add(element);
            }
        }
        return newArrayListWithExpectedSize;
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x008c, code lost:
        r0 = new java.util.concurrent.atomic.AtomicInteger(r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x00a2, code lost:
        if (r6.countMap.putIfAbsent(r7, r0) == null) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x00b5, code lost:
        return 0;
     */
    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    @com.google.errorprone.annotations.CanIgnoreReturnValue
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int add(E r7, int r8) {
        /*
            r6 = this;
            r0 = r7
            java.lang.Object r0 = com.google.common.base.Preconditions.checkNotNull(r0)
            r0 = r8
            if (r0 != 0) goto Lf
            r0 = r6
            r1 = r7
            int r0 = r0.count(r1)
            return r0
        Lf:
            r0 = r8
            java.lang.String r1 = "occurences"
            com.google.common.collect.CollectPreconditions.checkPositive(r0, r1)
        L15:
            r0 = r6
            java.util.concurrent.ConcurrentMap<E, java.util.concurrent.atomic.AtomicInteger> r0 = r0.countMap
            r1 = r7
            java.lang.Object r0 = com.google.common.collect.Maps.safeGet(r0, r1)
            java.util.concurrent.atomic.AtomicInteger r0 = (java.util.concurrent.atomic.AtomicInteger) r0
            r9 = r0
            r0 = r9
            if (r0 != 0) goto L41
            r0 = r6
            java.util.concurrent.ConcurrentMap<E, java.util.concurrent.atomic.AtomicInteger> r0 = r0.countMap
            r1 = r7
            java.util.concurrent.atomic.AtomicInteger r2 = new java.util.concurrent.atomic.AtomicInteger
            r3 = r2
            r4 = r8
            r3.<init>(r4)
            java.lang.Object r0 = r0.putIfAbsent(r1, r2)
            java.util.concurrent.atomic.AtomicInteger r0 = (java.util.concurrent.atomic.AtomicInteger) r0
            r9 = r0
            r0 = r9
            if (r0 != 0) goto L41
            r0 = 0
            return r0
        L41:
            r0 = r9
            int r0 = r0.get()
            r10 = r0
            r0 = r10
            if (r0 == 0) goto L8c
            r0 = r10
            r1 = r8
            int r0 = com.google.common.math.IntMath.checkedAdd(r0, r1)     // Catch: java.lang.ArithmeticException -> L65
            r11 = r0
            r0 = r9
            r1 = r10
            r2 = r11
            boolean r0 = r0.compareAndSet(r1, r2)     // Catch: java.lang.ArithmeticException -> L65
            if (r0 == 0) goto L62
            r0 = r10
            return r0
        L62:
            goto Lb7
        L65:
            r11 = move-exception
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            r1 = r0
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r3 = r2
            r3.<init>()
            java.lang.String r3 = "Overflow adding "
            java.lang.StringBuilder r2 = r2.append(r3)
            r3 = r8
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = " occurrences to a count of "
            java.lang.StringBuilder r2 = r2.append(r3)
            r3 = r10
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r0
        L8c:
            java.util.concurrent.atomic.AtomicInteger r0 = new java.util.concurrent.atomic.AtomicInteger
            r1 = r0
            r2 = r8
            r1.<init>(r2)
            r11 = r0
            r0 = r6
            java.util.concurrent.ConcurrentMap<E, java.util.concurrent.atomic.AtomicInteger> r0 = r0.countMap
            r1 = r7
            r2 = r11
            java.lang.Object r0 = r0.putIfAbsent(r1, r2)
            if (r0 == 0) goto Lb5
            r0 = r6
            java.util.concurrent.ConcurrentMap<E, java.util.concurrent.atomic.AtomicInteger> r0 = r0.countMap
            r1 = r7
            r2 = r9
            r3 = r11
            boolean r0 = r0.replace(r1, r2, r3)
            if (r0 == 0) goto Lba
        Lb5:
            r0 = 0
            return r0
        Lb7:
            goto L41
        Lba:
            goto L15
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.ConcurrentHashMultiset.add(java.lang.Object, int):int");
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    @CanIgnoreReturnValue
    public int remove(@NullableDecl Object element, int occurrences) {
        int oldValue;
        int newValue;
        if (occurrences == 0) {
            return count(element);
        }
        CollectPreconditions.checkPositive(occurrences, "occurences");
        AtomicInteger existingCounter = (AtomicInteger) Maps.safeGet(this.countMap, element);
        if (existingCounter == null) {
            return 0;
        }
        do {
            oldValue = existingCounter.get();
            if (oldValue != 0) {
                newValue = Math.max(0, oldValue - occurrences);
            } else {
                return 0;
            }
        } while (!existingCounter.compareAndSet(oldValue, newValue));
        if (newValue == 0) {
            this.countMap.remove(element, existingCounter);
        }
        return oldValue;
    }

    @CanIgnoreReturnValue
    public boolean removeExactly(@NullableDecl Object element, int occurrences) {
        int oldValue;
        int newValue;
        if (occurrences == 0) {
            return true;
        }
        CollectPreconditions.checkPositive(occurrences, "occurences");
        AtomicInteger existingCounter = (AtomicInteger) Maps.safeGet(this.countMap, element);
        if (existingCounter == null) {
            return false;
        }
        do {
            oldValue = existingCounter.get();
            if (oldValue < occurrences) {
                return false;
            }
            newValue = oldValue - occurrences;
        } while (!existingCounter.compareAndSet(oldValue, newValue));
        if (newValue == 0) {
            this.countMap.remove(element, existingCounter);
            return true;
        }
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x004a, code lost:
        if (r8 != 0) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x004d, code lost:
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x004f, code lost:
        r0 = new java.util.concurrent.atomic.AtomicInteger(r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0065, code lost:
        if (r6.countMap.putIfAbsent(r7, r0) == null) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0078, code lost:
        return 0;
     */
    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    @com.google.errorprone.annotations.CanIgnoreReturnValue
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int setCount(E r7, int r8) {
        /*
            r6 = this;
            r0 = r7
            java.lang.Object r0 = com.google.common.base.Preconditions.checkNotNull(r0)
            r0 = r8
            java.lang.String r1 = "count"
            int r0 = com.google.common.collect.CollectPreconditions.checkNonnegative(r0, r1)
        Lc:
            r0 = r6
            java.util.concurrent.ConcurrentMap<E, java.util.concurrent.atomic.AtomicInteger> r0 = r0.countMap
            r1 = r7
            java.lang.Object r0 = com.google.common.collect.Maps.safeGet(r0, r1)
            java.util.concurrent.atomic.AtomicInteger r0 = (java.util.concurrent.atomic.AtomicInteger) r0
            r9 = r0
            r0 = r9
            if (r0 != 0) goto L3e
            r0 = r8
            if (r0 != 0) goto L22
            r0 = 0
            return r0
        L22:
            r0 = r6
            java.util.concurrent.ConcurrentMap<E, java.util.concurrent.atomic.AtomicInteger> r0 = r0.countMap
            r1 = r7
            java.util.concurrent.atomic.AtomicInteger r2 = new java.util.concurrent.atomic.AtomicInteger
            r3 = r2
            r4 = r8
            r3.<init>(r4)
            java.lang.Object r0 = r0.putIfAbsent(r1, r2)
            java.util.concurrent.atomic.AtomicInteger r0 = (java.util.concurrent.atomic.AtomicInteger) r0
            r9 = r0
            r0 = r9
            if (r0 != 0) goto L3e
            r0 = 0
            return r0
        L3e:
            r0 = r9
            int r0 = r0.get()
            r10 = r0
            r0 = r10
            if (r0 != 0) goto L7d
            r0 = r8
            if (r0 != 0) goto L4f
            r0 = 0
            return r0
        L4f:
            java.util.concurrent.atomic.AtomicInteger r0 = new java.util.concurrent.atomic.AtomicInteger
            r1 = r0
            r2 = r8
            r1.<init>(r2)
            r11 = r0
            r0 = r6
            java.util.concurrent.ConcurrentMap<E, java.util.concurrent.atomic.AtomicInteger> r0 = r0.countMap
            r1 = r7
            r2 = r11
            java.lang.Object r0 = r0.putIfAbsent(r1, r2)
            if (r0 == 0) goto L78
            r0 = r6
            java.util.concurrent.ConcurrentMap<E, java.util.concurrent.atomic.AtomicInteger> r0 = r0.countMap
            r1 = r7
            r2 = r9
            r3 = r11
            boolean r0 = r0.replace(r1, r2, r3)
            if (r0 == 0) goto L7a
        L78:
            r0 = 0
            return r0
        L7a:
            goto L9d
        L7d:
            r0 = r9
            r1 = r10
            r2 = r8
            boolean r0 = r0.compareAndSet(r1, r2)
            if (r0 == 0) goto L9a
            r0 = r8
            if (r0 != 0) goto L97
            r0 = r6
            java.util.concurrent.ConcurrentMap<E, java.util.concurrent.atomic.AtomicInteger> r0 = r0.countMap
            r1 = r7
            r2 = r9
            boolean r0 = r0.remove(r1, r2)
        L97:
            r0 = r10
            return r0
        L9a:
            goto L3e
        L9d:
            goto Lc
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.ConcurrentHashMultiset.setCount(java.lang.Object, int):int");
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    @CanIgnoreReturnValue
    public boolean setCount(E element, int expectedOldCount, int newCount) {
        Preconditions.checkNotNull(element);
        CollectPreconditions.checkNonnegative(expectedOldCount, "oldCount");
        CollectPreconditions.checkNonnegative(newCount, "newCount");
        AtomicInteger existingCounter = (AtomicInteger) Maps.safeGet(this.countMap, element);
        if (existingCounter == null) {
            if (expectedOldCount != 0) {
                return false;
            }
            return newCount == 0 || this.countMap.putIfAbsent(element, new AtomicInteger(newCount)) == null;
        }
        int oldValue = existingCounter.get();
        if (oldValue == expectedOldCount) {
            if (oldValue == 0) {
                if (newCount == 0) {
                    this.countMap.remove(element, existingCounter);
                    return true;
                }
                AtomicInteger newCounter = new AtomicInteger(newCount);
                return this.countMap.putIfAbsent(element, newCounter) == null || this.countMap.replace(element, existingCounter, newCounter);
            } else if (existingCounter.compareAndSet(oldValue, newCount)) {
                if (newCount == 0) {
                    this.countMap.remove(element, existingCounter);
                    return true;
                }
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override // com.google.common.collect.AbstractMultiset
    Set<E> createElementSet() {
        final Set<E> delegate = this.countMap.keySet();
        return new ForwardingSet<E>() { // from class: com.google.common.collect.ConcurrentHashMultiset.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
            public Set<E> delegate() {
                return delegate;
            }

            @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
            public boolean contains(@NullableDecl Object object) {
                return object != null && Collections2.safeContains(delegate, object);
            }

            @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
            public boolean containsAll(Collection<?> collection) {
                return standardContainsAll(collection);
            }

            @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
            public boolean remove(Object object) {
                return object != null && Collections2.safeRemove(delegate, object);
            }

            @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
            public boolean removeAll(Collection<?> c) {
                return standardRemoveAll(c);
            }
        };
    }

    @Override // com.google.common.collect.AbstractMultiset
    Iterator<E> elementIterator() {
        throw new AssertionError("should never be called");
    }

    @Override // com.google.common.collect.AbstractMultiset
    @Deprecated
    public Set<Multiset.Entry<E>> createEntrySet() {
        return new EntrySet();
    }

    @Override // com.google.common.collect.AbstractMultiset
    int distinctElements() {
        return this.countMap.size();
    }

    @Override // com.google.common.collect.AbstractMultiset, java.util.AbstractCollection, java.util.Collection
    public boolean isEmpty() {
        return this.countMap.isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.AbstractMultiset
    public Iterator<Multiset.Entry<E>> entryIterator() {
        final Iterator<Multiset.Entry<E>> readOnlyIterator = new AbstractIterator<Multiset.Entry<E>>() { // from class: com.google.common.collect.ConcurrentHashMultiset.2
            private final Iterator<Map.Entry<E, AtomicInteger>> mapEntries;

            {
                this.mapEntries = ConcurrentHashMultiset.this.countMap.entrySet().iterator();
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.common.collect.AbstractIterator
            public Multiset.Entry<E> computeNext() {
                while (this.mapEntries.hasNext()) {
                    Map.Entry<E, AtomicInteger> mapEntry = this.mapEntries.next();
                    int count = mapEntry.getValue().get();
                    if (count != 0) {
                        return Multisets.immutableEntry(mapEntry.getKey(), count);
                    }
                }
                return endOfData();
            }
        };
        return new ForwardingIterator<Multiset.Entry<E>>() { // from class: com.google.common.collect.ConcurrentHashMultiset.3
            @NullableDecl
            private Multiset.Entry<E> last;

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.common.collect.ForwardingIterator, com.google.common.collect.ForwardingObject
            public Iterator<Multiset.Entry<E>> delegate() {
                return readOnlyIterator;
            }

            @Override // com.google.common.collect.ForwardingIterator, java.util.Iterator
            public Multiset.Entry<E> next() {
                this.last = (Multiset.Entry) super.next();
                return this.last;
            }

            @Override // com.google.common.collect.ForwardingIterator, java.util.Iterator
            public void remove() {
                CollectPreconditions.checkRemove(this.last != null);
                ConcurrentHashMultiset.this.setCount(this.last.getElement(), 0);
                this.last = null;
            }
        };
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, com.google.common.collect.Multiset
    public Iterator<E> iterator() {
        return Multisets.iteratorImpl(this);
    }

    @Override // com.google.common.collect.AbstractMultiset, java.util.AbstractCollection, java.util.Collection
    public void clear() {
        this.countMap.clear();
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/ConcurrentHashMultiset$EntrySet.class */
    private class EntrySet extends AbstractMultiset<E>.EntrySet {
        private EntrySet() {
            super();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.AbstractMultiset.EntrySet, com.google.common.collect.Multisets.EntrySet
        public ConcurrentHashMultiset<E> multiset() {
            return ConcurrentHashMultiset.this;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public Object[] toArray() {
            return snapshot().toArray();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public <T> T[] toArray(T[] array) {
            return (T[]) snapshot().toArray(array);
        }

        private List<Multiset.Entry<E>> snapshot() {
            List<Multiset.Entry<E>> list = Lists.newArrayListWithExpectedSize(size());
            Iterators.addAll(list, iterator());
            return list;
        }
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(this.countMap);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        ConcurrentMap<E, Integer> deserializedCountMap = (ConcurrentMap) stream.readObject();
        FieldSettersHolder.COUNT_MAP_FIELD_SETTER.set((Serialization.FieldSetter<ConcurrentHashMultiset>) this, (Object) deserializedCountMap);
    }
}
