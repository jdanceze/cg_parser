package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
@GwtCompatible(serializable = true, emulated = true)
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/ImmutableSortedMap.class */
public final class ImmutableSortedMap<K, V> extends ImmutableSortedMapFauxverideShim<K, V> implements NavigableMap<K, V> {
    private static final Comparator<Comparable> NATURAL_ORDER = Ordering.natural();
    private static final ImmutableSortedMap<Comparable, Object> NATURAL_EMPTY_MAP = new ImmutableSortedMap<>(ImmutableSortedSet.emptySet(Ordering.natural()), ImmutableList.of());
    private final transient RegularImmutableSortedSet<K> keySet;
    private final transient ImmutableList<V> valueList;
    private transient ImmutableSortedMap<K, V> descendingMap;
    private static final long serialVersionUID = 0;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableMap, java.util.SortedMap
    public /* bridge */ /* synthetic */ SortedMap tailMap(Object obj) {
        return tailMap((ImmutableSortedMap<K, V>) obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableMap, java.util.SortedMap
    public /* bridge */ /* synthetic */ SortedMap headMap(Object obj) {
        return headMap((ImmutableSortedMap<K, V>) obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableMap
    public /* bridge */ /* synthetic */ NavigableMap tailMap(Object obj, boolean z) {
        return tailMap((ImmutableSortedMap<K, V>) obj, z);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableMap
    public /* bridge */ /* synthetic */ NavigableMap headMap(Object obj, boolean z) {
        return headMap((ImmutableSortedMap<K, V>) obj, z);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableMap
    public /* bridge */ /* synthetic */ NavigableMap subMap(Object obj, boolean z, Object obj2, boolean z2) {
        return subMap((boolean) obj, z, (boolean) obj2, z2);
    }

    static <K, V> ImmutableSortedMap<K, V> emptyMap(Comparator<? super K> comparator) {
        if (Ordering.natural().equals(comparator)) {
            return of();
        }
        return new ImmutableSortedMap<>(ImmutableSortedSet.emptySet(comparator), ImmutableList.of());
    }

    public static <K, V> ImmutableSortedMap<K, V> of() {
        return (ImmutableSortedMap<K, V>) NATURAL_EMPTY_MAP;
    }

    /* JADX WARN: Incorrect types in method signature: <K::Ljava/lang/Comparable<-TK;>;V:Ljava/lang/Object;>(TK;TV;)Lcom/google/common/collect/ImmutableSortedMap<TK;TV;>; */
    public static ImmutableSortedMap of(Comparable comparable, Object obj) {
        return of(Ordering.natural(), comparable, obj);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <K, V> ImmutableSortedMap<K, V> of(Comparator<? super K> comparator, K k1, V v1) {
        return new ImmutableSortedMap<>(new RegularImmutableSortedSet(ImmutableList.of(k1), (Comparator) Preconditions.checkNotNull(comparator)), ImmutableList.of(v1));
    }

    /* JADX WARN: Incorrect types in method signature: <K::Ljava/lang/Comparable<-TK;>;V:Ljava/lang/Object;>(TK;TV;TK;TV;)Lcom/google/common/collect/ImmutableSortedMap<TK;TV;>; */
    public static ImmutableSortedMap of(Comparable comparable, Object obj, Comparable comparable2, Object obj2) {
        return ofEntries(entryOf(comparable, obj), entryOf(comparable2, obj2));
    }

    /* JADX WARN: Incorrect types in method signature: <K::Ljava/lang/Comparable<-TK;>;V:Ljava/lang/Object;>(TK;TV;TK;TV;TK;TV;)Lcom/google/common/collect/ImmutableSortedMap<TK;TV;>; */
    public static ImmutableSortedMap of(Comparable comparable, Object obj, Comparable comparable2, Object obj2, Comparable comparable3, Object obj3) {
        return ofEntries(entryOf(comparable, obj), entryOf(comparable2, obj2), entryOf(comparable3, obj3));
    }

    /* JADX WARN: Incorrect types in method signature: <K::Ljava/lang/Comparable<-TK;>;V:Ljava/lang/Object;>(TK;TV;TK;TV;TK;TV;TK;TV;)Lcom/google/common/collect/ImmutableSortedMap<TK;TV;>; */
    public static ImmutableSortedMap of(Comparable comparable, Object obj, Comparable comparable2, Object obj2, Comparable comparable3, Object obj3, Comparable comparable4, Object obj4) {
        return ofEntries(entryOf(comparable, obj), entryOf(comparable2, obj2), entryOf(comparable3, obj3), entryOf(comparable4, obj4));
    }

    /* JADX WARN: Incorrect types in method signature: <K::Ljava/lang/Comparable<-TK;>;V:Ljava/lang/Object;>(TK;TV;TK;TV;TK;TV;TK;TV;TK;TV;)Lcom/google/common/collect/ImmutableSortedMap<TK;TV;>; */
    public static ImmutableSortedMap of(Comparable comparable, Object obj, Comparable comparable2, Object obj2, Comparable comparable3, Object obj3, Comparable comparable4, Object obj4, Comparable comparable5, Object obj5) {
        return ofEntries(entryOf(comparable, obj), entryOf(comparable2, obj2), entryOf(comparable3, obj3), entryOf(comparable4, obj4), entryOf(comparable5, obj5));
    }

    private static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> ofEntries(Map.Entry<K, V>... entries) {
        return fromEntries(Ordering.natural(), false, entries, entries.length);
    }

    public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
        Ordering<K> naturalOrder = (Ordering) NATURAL_ORDER;
        return copyOfInternal(map, naturalOrder);
    }

    public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> map, Comparator<? super K> comparator) {
        return copyOfInternal(map, (Comparator) Preconditions.checkNotNull(comparator));
    }

    @Beta
    public static <K, V> ImmutableSortedMap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
        Ordering<K> naturalOrder = (Ordering) NATURAL_ORDER;
        return copyOf(entries, naturalOrder);
    }

    @Beta
    public static <K, V> ImmutableSortedMap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries, Comparator<? super K> comparator) {
        return fromEntries((Comparator) Preconditions.checkNotNull(comparator), false, entries);
    }

    public static <K, V> ImmutableSortedMap<K, V> copyOfSorted(SortedMap<K, ? extends V> map) {
        Comparator<? super K> comparator = map.comparator();
        if (comparator == null) {
            comparator = NATURAL_ORDER;
        }
        if (map instanceof ImmutableSortedMap) {
            ImmutableSortedMap<K, V> kvMap = (ImmutableSortedMap) map;
            if (!kvMap.isPartialView()) {
                return kvMap;
            }
        }
        return fromEntries(comparator, true, map.entrySet());
    }

    private static <K, V> ImmutableSortedMap<K, V> copyOfInternal(Map<? extends K, ? extends V> map, Comparator<? super K> comparator) {
        boolean equals;
        boolean sameComparator = false;
        if (map instanceof SortedMap) {
            SortedMap<?, ?> sortedMap = (SortedMap) map;
            Comparator<?> comparator2 = sortedMap.comparator();
            if (comparator2 == null) {
                equals = comparator == NATURAL_ORDER;
            } else {
                equals = comparator.equals(comparator2);
            }
            sameComparator = equals;
        }
        if (sameComparator && (map instanceof ImmutableSortedMap)) {
            ImmutableSortedMap<K, V> kvMap = (ImmutableSortedMap) map;
            if (!kvMap.isPartialView()) {
                return kvMap;
            }
        }
        return fromEntries(comparator, sameComparator, map.entrySet());
    }

    private static <K, V> ImmutableSortedMap<K, V> fromEntries(Comparator<? super K> comparator, boolean sameComparator, Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
        Map.Entry<K, V>[] entryArray = (Map.Entry[]) Iterables.toArray(entries, EMPTY_ENTRY_ARRAY);
        return fromEntries(comparator, sameComparator, entryArray, entryArray.length);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static <K, V> ImmutableSortedMap<K, V> fromEntries(final Comparator<? super K> comparator, boolean sameComparator, Map.Entry<K, V>[] entryArray, int size) {
        switch (size) {
            case 0:
                return emptyMap(comparator);
            case 1:
                return of(comparator, entryArray[0].getKey(), entryArray[0].getValue());
            default:
                Object[] keys = new Object[size];
                Object[] values = new Object[size];
                if (sameComparator) {
                    for (int i = 0; i < size; i++) {
                        Object key = entryArray[i].getKey();
                        Object value = entryArray[i].getValue();
                        CollectPreconditions.checkEntryNotNull(key, value);
                        keys[i] = key;
                        values[i] = value;
                    }
                } else {
                    Arrays.sort(entryArray, 0, size, new Comparator<Map.Entry<K, V>>() { // from class: com.google.common.collect.ImmutableSortedMap.1
                        @Override // java.util.Comparator
                        public /* bridge */ /* synthetic */ int compare(Object obj, Object obj2) {
                            return compare((Map.Entry) ((Map.Entry) obj), (Map.Entry) ((Map.Entry) obj2));
                        }

                        public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                            return comparator.compare(e1.getKey(), e2.getKey());
                        }
                    });
                    Object key2 = entryArray[0].getKey();
                    keys[0] = key2;
                    values[0] = entryArray[0].getValue();
                    CollectPreconditions.checkEntryNotNull(keys[0], values[0]);
                    for (int i2 = 1; i2 < size; i2++) {
                        Object obj = (K) entryArray[i2].getKey();
                        V value2 = entryArray[i2].getValue();
                        CollectPreconditions.checkEntryNotNull(obj, value2);
                        keys[i2] = obj;
                        values[i2] = value2;
                        checkNoConflict(comparator.compare((Object) key2, obj) != 0, "key", entryArray[i2 - 1], entryArray[i2]);
                        key2 = obj;
                    }
                }
                return new ImmutableSortedMap<>(new RegularImmutableSortedSet(ImmutableList.asImmutableList(keys), comparator), ImmutableList.asImmutableList(values));
        }
    }

    public static <K extends Comparable<?>, V> Builder<K, V> naturalOrder() {
        return new Builder<>(Ordering.natural());
    }

    public static <K, V> Builder<K, V> orderedBy(Comparator<K> comparator) {
        return new Builder<>(comparator);
    }

    public static <K extends Comparable<?>, V> Builder<K, V> reverseOrder() {
        return new Builder<>(Ordering.natural().reverse());
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/ImmutableSortedMap$Builder.class */
    public static class Builder<K, V> extends ImmutableMap.Builder<K, V> {
        private transient Object[] keys;
        private transient Object[] values;
        private final Comparator<? super K> comparator;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableMap.Builder
        @CanIgnoreReturnValue
        public /* bridge */ /* synthetic */ ImmutableMap.Builder put(Object obj, Object obj2) {
            return put((Builder<K, V>) obj, obj2);
        }

        public Builder(Comparator<? super K> comparator) {
            this(comparator, 4);
        }

        private Builder(Comparator<? super K> comparator, int initialCapacity) {
            this.comparator = (Comparator) Preconditions.checkNotNull(comparator);
            this.keys = new Object[initialCapacity];
            this.values = new Object[initialCapacity];
        }

        private void ensureCapacity(int minCapacity) {
            if (minCapacity > this.keys.length) {
                int newCapacity = ImmutableCollection.Builder.expandedCapacity(this.keys.length, minCapacity);
                this.keys = Arrays.copyOf(this.keys, newCapacity);
                this.values = Arrays.copyOf(this.values, newCapacity);
            }
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        @CanIgnoreReturnValue
        public Builder<K, V> put(K key, V value) {
            ensureCapacity(this.size + 1);
            CollectPreconditions.checkEntryNotNull(key, value);
            this.keys[this.size] = key;
            this.values[this.size] = value;
            this.size++;
            return this;
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        @CanIgnoreReturnValue
        public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry) {
            super.put((Map.Entry) entry);
            return this;
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        @CanIgnoreReturnValue
        public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
            super.putAll((Map) map);
            return this;
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        @CanIgnoreReturnValue
        @Beta
        public Builder<K, V> putAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
            super.putAll((Iterable) entries);
            return this;
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        @CanIgnoreReturnValue
        @Beta
        @Deprecated
        public Builder<K, V> orderEntriesByValue(Comparator<? super V> valueComparator) {
            throw new UnsupportedOperationException("Not available on ImmutableSortedMap.Builder");
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        public ImmutableSortedMap<K, V> build() {
            switch (this.size) {
                case 0:
                    return ImmutableSortedMap.emptyMap(this.comparator);
                case 1:
                    return ImmutableSortedMap.of(this.comparator, this.keys[0], this.values[0]);
                default:
                    Object[] sortedKeys = Arrays.copyOf(this.keys, this.size);
                    Arrays.sort(sortedKeys, this.comparator);
                    Object[] sortedValues = new Object[this.size];
                    for (int i = 0; i < this.size; i++) {
                        if (i > 0 && this.comparator.compare(sortedKeys[i - 1], sortedKeys[i]) == 0) {
                            throw new IllegalArgumentException("keys required to be distinct but compared as equal: " + sortedKeys[i - 1] + " and " + sortedKeys[i]);
                        }
                        int index = Arrays.binarySearch(sortedKeys, this.keys[i], this.comparator);
                        sortedValues[index] = this.values[i];
                    }
                    return new ImmutableSortedMap<>(new RegularImmutableSortedSet(ImmutableList.asImmutableList(sortedKeys), this.comparator), ImmutableList.asImmutableList(sortedValues));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImmutableSortedMap(RegularImmutableSortedSet<K> keySet, ImmutableList<V> valueList) {
        this(keySet, valueList, null);
    }

    ImmutableSortedMap(RegularImmutableSortedSet<K> keySet, ImmutableList<V> valueList, ImmutableSortedMap<K, V> descendingMap) {
        this.keySet = keySet;
        this.valueList = valueList;
        this.descendingMap = descendingMap;
    }

    @Override // java.util.Map
    public int size() {
        return this.valueList.size();
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public V get(@NullableDecl Object key) {
        int index = this.keySet.indexOf(key);
        if (index == -1) {
            return null;
        }
        return this.valueList.get(index);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableMap
    public boolean isPartialView() {
        return this.keySet.isPartialView() || this.valueList.isPartialView();
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public ImmutableSet<Map.Entry<K, V>> entrySet() {
        return super.entrySet();
    }

    @Override // com.google.common.collect.ImmutableMap
    ImmutableSet<Map.Entry<K, V>> createEntrySet() {
        return isEmpty() ? ImmutableSet.of() : new ImmutableMapEntrySet<K, V>() { // from class: com.google.common.collect.ImmutableSortedMap.1EntrySet
            @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
            public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
                return asList().iterator();
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // com.google.common.collect.ImmutableSet
            public ImmutableList<Map.Entry<K, V>> createAsList() {
                return new ImmutableList<Map.Entry<K, V>>() { // from class: com.google.common.collect.ImmutableSortedMap.1EntrySet.1
                    @Override // java.util.List
                    public Map.Entry<K, V> get(int index) {
                        return new AbstractMap.SimpleImmutableEntry(ImmutableSortedMap.this.keySet.asList().get(index), ImmutableSortedMap.this.valueList.get(index));
                    }

                    /* JADX INFO: Access modifiers changed from: package-private */
                    @Override // com.google.common.collect.ImmutableCollection
                    public boolean isPartialView() {
                        return true;
                    }

                    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
                    public int size() {
                        return ImmutableSortedMap.this.size();
                    }
                };
            }

            @Override // com.google.common.collect.ImmutableMapEntrySet
            ImmutableMap<K, V> map() {
                return ImmutableSortedMap.this;
            }
        };
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public ImmutableSortedSet<K> keySet() {
        return this.keySet;
    }

    @Override // com.google.common.collect.ImmutableMap
    ImmutableSet<K> createKeySet() {
        throw new AssertionError("should never be called");
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public ImmutableCollection<V> values() {
        return this.valueList;
    }

    @Override // com.google.common.collect.ImmutableMap
    ImmutableCollection<V> createValues() {
        throw new AssertionError("should never be called");
    }

    @Override // java.util.SortedMap
    public Comparator<? super K> comparator() {
        return keySet().comparator();
    }

    @Override // java.util.SortedMap
    public K firstKey() {
        return keySet().first();
    }

    @Override // java.util.SortedMap
    public K lastKey() {
        return keySet().last();
    }

    private ImmutableSortedMap<K, V> getSubMap(int fromIndex, int toIndex) {
        if (fromIndex == 0 && toIndex == size()) {
            return this;
        }
        if (fromIndex == toIndex) {
            return emptyMap(comparator());
        }
        return new ImmutableSortedMap<>(this.keySet.getSubSet(fromIndex, toIndex), this.valueList.subList(fromIndex, toIndex));
    }

    @Override // java.util.NavigableMap, java.util.SortedMap
    public ImmutableSortedMap<K, V> headMap(K toKey) {
        return headMap((ImmutableSortedMap<K, V>) toKey, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableMap
    public ImmutableSortedMap<K, V> headMap(K toKey, boolean inclusive) {
        return getSubMap(0, this.keySet.headIndex(Preconditions.checkNotNull(toKey), inclusive));
    }

    @Override // java.util.NavigableMap, java.util.SortedMap
    public ImmutableSortedMap<K, V> subMap(K fromKey, K toKey) {
        return subMap((boolean) fromKey, true, (boolean) toKey, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableMap
    public ImmutableSortedMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
        Preconditions.checkNotNull(fromKey);
        Preconditions.checkNotNull(toKey);
        Preconditions.checkArgument(comparator().compare(fromKey, toKey) <= 0, "expected fromKey <= toKey but %s > %s", fromKey, toKey);
        return headMap((ImmutableSortedMap<K, V>) toKey, toInclusive).tailMap((ImmutableSortedMap<K, V>) fromKey, fromInclusive);
    }

    @Override // java.util.NavigableMap, java.util.SortedMap
    public ImmutableSortedMap<K, V> tailMap(K fromKey) {
        return tailMap((ImmutableSortedMap<K, V>) fromKey, true);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableMap
    public ImmutableSortedMap<K, V> tailMap(K fromKey, boolean inclusive) {
        return getSubMap(this.keySet.tailIndex(Preconditions.checkNotNull(fromKey), inclusive), size());
    }

    @Override // java.util.NavigableMap
    public Map.Entry<K, V> lowerEntry(K key) {
        return headMap((ImmutableSortedMap<K, V>) key, false).lastEntry();
    }

    @Override // java.util.NavigableMap
    public K lowerKey(K key) {
        return (K) Maps.keyOrNull(lowerEntry(key));
    }

    @Override // java.util.NavigableMap
    public Map.Entry<K, V> floorEntry(K key) {
        return headMap((ImmutableSortedMap<K, V>) key, true).lastEntry();
    }

    @Override // java.util.NavigableMap
    public K floorKey(K key) {
        return (K) Maps.keyOrNull(floorEntry(key));
    }

    @Override // java.util.NavigableMap
    public Map.Entry<K, V> ceilingEntry(K key) {
        return tailMap((ImmutableSortedMap<K, V>) key, true).firstEntry();
    }

    @Override // java.util.NavigableMap
    public K ceilingKey(K key) {
        return (K) Maps.keyOrNull(ceilingEntry(key));
    }

    @Override // java.util.NavigableMap
    public Map.Entry<K, V> higherEntry(K key) {
        return tailMap((ImmutableSortedMap<K, V>) key, false).firstEntry();
    }

    @Override // java.util.NavigableMap
    public K higherKey(K key) {
        return (K) Maps.keyOrNull(higherEntry(key));
    }

    @Override // java.util.NavigableMap
    public Map.Entry<K, V> firstEntry() {
        if (isEmpty()) {
            return null;
        }
        return entrySet().asList().get(0);
    }

    @Override // java.util.NavigableMap
    public Map.Entry<K, V> lastEntry() {
        if (isEmpty()) {
            return null;
        }
        return entrySet().asList().get(size() - 1);
    }

    @Override // java.util.NavigableMap
    @CanIgnoreReturnValue
    @Deprecated
    public final Map.Entry<K, V> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.NavigableMap
    @CanIgnoreReturnValue
    @Deprecated
    public final Map.Entry<K, V> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.NavigableMap
    public ImmutableSortedMap<K, V> descendingMap() {
        ImmutableSortedMap<K, V> result = this.descendingMap;
        if (result == null) {
            if (isEmpty()) {
                return emptyMap(Ordering.from(comparator()).reverse());
            }
            return new ImmutableSortedMap<>((RegularImmutableSortedSet) this.keySet.descendingSet(), this.valueList.reverse(), this);
        }
        return result;
    }

    @Override // java.util.NavigableMap
    public ImmutableSortedSet<K> navigableKeySet() {
        return this.keySet;
    }

    @Override // java.util.NavigableMap
    public ImmutableSortedSet<K> descendingKeySet() {
        return this.keySet.descendingSet();
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/ImmutableSortedMap$SerializedForm.class */
    private static class SerializedForm extends ImmutableMap.SerializedForm {
        private final Comparator<Object> comparator;
        private static final long serialVersionUID = 0;

        SerializedForm(ImmutableSortedMap<?, ?> sortedMap) {
            super(sortedMap);
            this.comparator = sortedMap.comparator();
        }

        @Override // com.google.common.collect.ImmutableMap.SerializedForm
        Object readResolve() {
            Builder<Object, Object> builder = new Builder<>(this.comparator);
            return createMap(builder);
        }
    }

    @Override // com.google.common.collect.ImmutableMap
    Object writeReplace() {
        return new SerializedForm(this);
    }
}
