package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.j2objc.annotations.RetainedWith;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import org.checkerframework.checker.nullness.compatqual.MonotonicNonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
@GwtCompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/HashBiMap.class */
public final class HashBiMap<K, V> extends AbstractMap<K, V> implements BiMap<K, V>, Serializable {
    private static final int ABSENT = -1;
    private static final int ENDPOINT = -2;
    transient K[] keys;
    transient V[] values;
    transient int size;
    transient int modCount;
    private transient int[] hashTableKToV;
    private transient int[] hashTableVToK;
    private transient int[] nextInBucketKToV;
    private transient int[] nextInBucketVToK;
    @NullableDecl
    private transient int firstInInsertionOrder;
    @NullableDecl
    private transient int lastInInsertionOrder;
    private transient int[] prevInInsertionOrder;
    private transient int[] nextInInsertionOrder;
    private transient Set<K> keySet;
    private transient Set<V> valueSet;
    private transient Set<Map.Entry<K, V>> entrySet;
    @RetainedWith
    @MonotonicNonNullDecl
    private transient BiMap<V, K> inverse;

    public static <K, V> HashBiMap<K, V> create() {
        return create(16);
    }

    public static <K, V> HashBiMap<K, V> create(int expectedSize) {
        return new HashBiMap<>(expectedSize);
    }

    public static <K, V> HashBiMap<K, V> create(Map<? extends K, ? extends V> map) {
        HashBiMap<K, V> bimap = create(map.size());
        bimap.putAll(map);
        return bimap;
    }

    private HashBiMap(int expectedSize) {
        init(expectedSize);
    }

    void init(int expectedSize) {
        CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
        int tableSize = Hashing.closedTableSize(expectedSize, 1.0d);
        this.size = 0;
        this.keys = (K[]) new Object[expectedSize];
        this.values = (V[]) new Object[expectedSize];
        this.hashTableKToV = createFilledWithAbsent(tableSize);
        this.hashTableVToK = createFilledWithAbsent(tableSize);
        this.nextInBucketKToV = createFilledWithAbsent(expectedSize);
        this.nextInBucketVToK = createFilledWithAbsent(expectedSize);
        this.firstInInsertionOrder = -2;
        this.lastInInsertionOrder = -2;
        this.prevInInsertionOrder = createFilledWithAbsent(expectedSize);
        this.nextInInsertionOrder = createFilledWithAbsent(expectedSize);
    }

    private static int[] createFilledWithAbsent(int size) {
        int[] array = new int[size];
        Arrays.fill(array, -1);
        return array;
    }

    private static int[] expandAndFillWithAbsent(int[] array, int newSize) {
        int oldSize = array.length;
        int[] result = Arrays.copyOf(array, newSize);
        Arrays.fill(result, oldSize, newSize, -1);
        return result;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        return this.size;
    }

    private void ensureCapacity(int minCapacity) {
        if (this.nextInBucketKToV.length < minCapacity) {
            int oldCapacity = this.nextInBucketKToV.length;
            int newCapacity = ImmutableCollection.Builder.expandedCapacity(oldCapacity, minCapacity);
            this.keys = (K[]) Arrays.copyOf(this.keys, newCapacity);
            this.values = (V[]) Arrays.copyOf(this.values, newCapacity);
            this.nextInBucketKToV = expandAndFillWithAbsent(this.nextInBucketKToV, newCapacity);
            this.nextInBucketVToK = expandAndFillWithAbsent(this.nextInBucketVToK, newCapacity);
            this.prevInInsertionOrder = expandAndFillWithAbsent(this.prevInInsertionOrder, newCapacity);
            this.nextInInsertionOrder = expandAndFillWithAbsent(this.nextInInsertionOrder, newCapacity);
        }
        if (this.hashTableKToV.length < minCapacity) {
            int newTableSize = Hashing.closedTableSize(minCapacity, 1.0d);
            this.hashTableKToV = createFilledWithAbsent(newTableSize);
            this.hashTableVToK = createFilledWithAbsent(newTableSize);
            for (int entryToRehash = 0; entryToRehash < this.size; entryToRehash++) {
                int keyHash = Hashing.smearedHash(this.keys[entryToRehash]);
                int keyBucket = bucket(keyHash);
                this.nextInBucketKToV[entryToRehash] = this.hashTableKToV[keyBucket];
                this.hashTableKToV[keyBucket] = entryToRehash;
                int valueHash = Hashing.smearedHash(this.values[entryToRehash]);
                int valueBucket = bucket(valueHash);
                this.nextInBucketVToK[entryToRehash] = this.hashTableVToK[valueBucket];
                this.hashTableVToK[valueBucket] = entryToRehash;
            }
        }
    }

    private int bucket(int hash) {
        return hash & (this.hashTableKToV.length - 1);
    }

    int findEntryByKey(@NullableDecl Object key) {
        return findEntryByKey(key, Hashing.smearedHash(key));
    }

    int findEntryByKey(@NullableDecl Object key, int keyHash) {
        return findEntry(key, keyHash, this.hashTableKToV, this.nextInBucketKToV, this.keys);
    }

    int findEntryByValue(@NullableDecl Object value) {
        return findEntryByValue(value, Hashing.smearedHash(value));
    }

    int findEntryByValue(@NullableDecl Object value, int valueHash) {
        return findEntry(value, valueHash, this.hashTableVToK, this.nextInBucketVToK, this.values);
    }

    int findEntry(@NullableDecl Object o, int oHash, int[] hashTable, int[] nextInBucket, Object[] array) {
        int i = hashTable[bucket(oHash)];
        while (true) {
            int entry = i;
            if (entry != -1) {
                if (!Objects.equal(array[entry], o)) {
                    i = nextInBucket[entry];
                } else {
                    return entry;
                }
            } else {
                return -1;
            }
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(@NullableDecl Object key) {
        return findEntryByKey(key) != -1;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsValue(@NullableDecl Object value) {
        return findEntryByValue(value) != -1;
    }

    @Override // java.util.AbstractMap, java.util.Map
    @NullableDecl
    public V get(@NullableDecl Object key) {
        int entry = findEntryByKey(key);
        if (entry == -1) {
            return null;
        }
        return this.values[entry];
    }

    @NullableDecl
    K getInverse(@NullableDecl Object value) {
        int entry = findEntryByValue(value);
        if (entry == -1) {
            return null;
        }
        return this.keys[entry];
    }

    @Override // java.util.AbstractMap, java.util.Map, com.google.common.collect.BiMap
    @CanIgnoreReturnValue
    public V put(@NullableDecl K key, @NullableDecl V value) {
        return put(key, value, false);
    }

    @NullableDecl
    V put(@NullableDecl K key, @NullableDecl V value, boolean force) {
        int keyHash = Hashing.smearedHash(key);
        int entryForKey = findEntryByKey(key, keyHash);
        if (entryForKey != -1) {
            V oldValue = this.values[entryForKey];
            if (Objects.equal(oldValue, value)) {
                return value;
            }
            replaceValueInEntry(entryForKey, value, force);
            return oldValue;
        }
        int valueHash = Hashing.smearedHash(value);
        int valueEntry = findEntryByValue(value, valueHash);
        if (force) {
            if (valueEntry != -1) {
                removeEntryValueHashKnown(valueEntry, valueHash);
            }
        } else {
            Preconditions.checkArgument(valueEntry == -1, "Value already present: %s", value);
        }
        ensureCapacity(this.size + 1);
        this.keys[this.size] = key;
        this.values[this.size] = value;
        insertIntoTableKToV(this.size, keyHash);
        insertIntoTableVToK(this.size, valueHash);
        setSucceeds(this.lastInInsertionOrder, this.size);
        setSucceeds(this.size, -2);
        this.size++;
        this.modCount++;
        return null;
    }

    @Override // com.google.common.collect.BiMap
    @CanIgnoreReturnValue
    @NullableDecl
    public V forcePut(@NullableDecl K key, @NullableDecl V value) {
        return put(key, value, true);
    }

    @NullableDecl
    K putInverse(@NullableDecl V value, @NullableDecl K key, boolean force) {
        int valueHash = Hashing.smearedHash(value);
        int entryForValue = findEntryByValue(value, valueHash);
        if (entryForValue != -1) {
            K oldKey = this.keys[entryForValue];
            if (Objects.equal(oldKey, key)) {
                return key;
            }
            replaceKeyInEntry(entryForValue, key, force);
            return oldKey;
        }
        int predecessor = this.lastInInsertionOrder;
        int keyHash = Hashing.smearedHash(key);
        int keyEntry = findEntryByKey(key, keyHash);
        if (force) {
            if (keyEntry != -1) {
                predecessor = this.prevInInsertionOrder[keyEntry];
                removeEntryKeyHashKnown(keyEntry, keyHash);
            }
        } else {
            Preconditions.checkArgument(keyEntry == -1, "Key already present: %s", key);
        }
        ensureCapacity(this.size + 1);
        this.keys[this.size] = key;
        this.values[this.size] = value;
        insertIntoTableKToV(this.size, keyHash);
        insertIntoTableVToK(this.size, valueHash);
        int successor = predecessor == -2 ? this.firstInInsertionOrder : this.nextInInsertionOrder[predecessor];
        setSucceeds(predecessor, this.size);
        setSucceeds(this.size, successor);
        this.size++;
        this.modCount++;
        return null;
    }

    private void setSucceeds(int prev, int next) {
        if (prev == -2) {
            this.firstInInsertionOrder = next;
        } else {
            this.nextInInsertionOrder[prev] = next;
        }
        if (next == -2) {
            this.lastInInsertionOrder = prev;
        } else {
            this.prevInInsertionOrder[next] = prev;
        }
    }

    private void insertIntoTableKToV(int entry, int keyHash) {
        Preconditions.checkArgument(entry != -1);
        int keyBucket = bucket(keyHash);
        this.nextInBucketKToV[entry] = this.hashTableKToV[keyBucket];
        this.hashTableKToV[keyBucket] = entry;
    }

    private void insertIntoTableVToK(int entry, int valueHash) {
        Preconditions.checkArgument(entry != -1);
        int valueBucket = bucket(valueHash);
        this.nextInBucketVToK[entry] = this.hashTableVToK[valueBucket];
        this.hashTableVToK[valueBucket] = entry;
    }

    private void deleteFromTableKToV(int entry, int keyHash) {
        Preconditions.checkArgument(entry != -1);
        int keyBucket = bucket(keyHash);
        if (this.hashTableKToV[keyBucket] == entry) {
            this.hashTableKToV[keyBucket] = this.nextInBucketKToV[entry];
            this.nextInBucketKToV[entry] = -1;
            return;
        }
        int prevInBucket = this.hashTableKToV[keyBucket];
        int i = this.nextInBucketKToV[prevInBucket];
        while (true) {
            int entryInBucket = i;
            if (entryInBucket != -1) {
                if (entryInBucket == entry) {
                    this.nextInBucketKToV[prevInBucket] = this.nextInBucketKToV[entry];
                    this.nextInBucketKToV[entry] = -1;
                    return;
                }
                prevInBucket = entryInBucket;
                i = this.nextInBucketKToV[entryInBucket];
            } else {
                throw new AssertionError("Expected to find entry with key " + this.keys[entry]);
            }
        }
    }

    private void deleteFromTableVToK(int entry, int valueHash) {
        Preconditions.checkArgument(entry != -1);
        int valueBucket = bucket(valueHash);
        if (this.hashTableVToK[valueBucket] == entry) {
            this.hashTableVToK[valueBucket] = this.nextInBucketVToK[entry];
            this.nextInBucketVToK[entry] = -1;
            return;
        }
        int prevInBucket = this.hashTableVToK[valueBucket];
        int i = this.nextInBucketVToK[prevInBucket];
        while (true) {
            int entryInBucket = i;
            if (entryInBucket != -1) {
                if (entryInBucket == entry) {
                    this.nextInBucketVToK[prevInBucket] = this.nextInBucketVToK[entry];
                    this.nextInBucketVToK[entry] = -1;
                    return;
                }
                prevInBucket = entryInBucket;
                i = this.nextInBucketVToK[entryInBucket];
            } else {
                throw new AssertionError("Expected to find entry with value " + this.values[entry]);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void replaceValueInEntry(int entry, @NullableDecl V newValue, boolean force) {
        Preconditions.checkArgument(entry != -1);
        int newValueHash = Hashing.smearedHash(newValue);
        int newValueIndex = findEntryByValue(newValue, newValueHash);
        if (newValueIndex != -1) {
            if (force) {
                removeEntryValueHashKnown(newValueIndex, newValueHash);
                if (entry == this.size) {
                    entry = newValueIndex;
                }
            } else {
                throw new IllegalArgumentException("Value already present in map: " + newValue);
            }
        }
        deleteFromTableVToK(entry, Hashing.smearedHash(this.values[entry]));
        this.values[entry] = newValue;
        insertIntoTableVToK(entry, newValueHash);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void replaceKeyInEntry(int entry, @NullableDecl K newKey, boolean force) {
        Preconditions.checkArgument(entry != -1);
        int newKeyHash = Hashing.smearedHash(newKey);
        int newKeyIndex = findEntryByKey(newKey, newKeyHash);
        int newPredecessor = this.lastInInsertionOrder;
        int newSuccessor = -2;
        if (newKeyIndex != -1) {
            if (force) {
                newPredecessor = this.prevInInsertionOrder[newKeyIndex];
                newSuccessor = this.nextInInsertionOrder[newKeyIndex];
                removeEntryKeyHashKnown(newKeyIndex, newKeyHash);
                if (entry == this.size) {
                    entry = newKeyIndex;
                }
            } else {
                throw new IllegalArgumentException("Key already present in map: " + newKey);
            }
        }
        if (newPredecessor == entry) {
            newPredecessor = this.prevInInsertionOrder[entry];
        } else if (newPredecessor == this.size) {
            newPredecessor = newKeyIndex;
        }
        if (newSuccessor == entry) {
            newSuccessor = this.nextInInsertionOrder[entry];
        } else if (newSuccessor == this.size) {
            newSuccessor = newKeyIndex;
        }
        int oldPredecessor = this.prevInInsertionOrder[entry];
        int oldSuccessor = this.nextInInsertionOrder[entry];
        setSucceeds(oldPredecessor, oldSuccessor);
        deleteFromTableKToV(entry, Hashing.smearedHash(this.keys[entry]));
        this.keys[entry] = newKey;
        insertIntoTableKToV(entry, Hashing.smearedHash(newKey));
        setSucceeds(newPredecessor, entry);
        setSucceeds(entry, newSuccessor);
    }

    @Override // java.util.AbstractMap, java.util.Map
    @CanIgnoreReturnValue
    @NullableDecl
    public V remove(@NullableDecl Object key) {
        int keyHash = Hashing.smearedHash(key);
        int entry = findEntryByKey(key, keyHash);
        if (entry == -1) {
            return null;
        }
        V value = this.values[entry];
        removeEntryKeyHashKnown(entry, keyHash);
        return value;
    }

    @NullableDecl
    K removeInverse(@NullableDecl Object value) {
        int valueHash = Hashing.smearedHash(value);
        int entry = findEntryByValue(value, valueHash);
        if (entry == -1) {
            return null;
        }
        K key = this.keys[entry];
        removeEntryValueHashKnown(entry, valueHash);
        return key;
    }

    void removeEntry(int entry) {
        removeEntryKeyHashKnown(entry, Hashing.smearedHash(this.keys[entry]));
    }

    private void removeEntry(int entry, int keyHash, int valueHash) {
        Preconditions.checkArgument(entry != -1);
        deleteFromTableKToV(entry, keyHash);
        deleteFromTableVToK(entry, valueHash);
        int oldPredecessor = this.prevInInsertionOrder[entry];
        int oldSuccessor = this.nextInInsertionOrder[entry];
        setSucceeds(oldPredecessor, oldSuccessor);
        moveEntryToIndex(this.size - 1, entry);
        this.keys[this.size - 1] = null;
        this.values[this.size - 1] = null;
        this.size--;
        this.modCount++;
    }

    void removeEntryKeyHashKnown(int entry, int keyHash) {
        removeEntry(entry, keyHash, Hashing.smearedHash(this.values[entry]));
    }

    void removeEntryValueHashKnown(int entry, int valueHash) {
        removeEntry(entry, Hashing.smearedHash(this.keys[entry]), valueHash);
    }

    private void moveEntryToIndex(int src, int dest) {
        if (src == dest) {
            return;
        }
        int predecessor = this.prevInInsertionOrder[src];
        int successor = this.nextInInsertionOrder[src];
        setSucceeds(predecessor, dest);
        setSucceeds(dest, successor);
        K key = this.keys[src];
        V value = this.values[src];
        this.keys[dest] = key;
        this.values[dest] = value;
        int keyHash = Hashing.smearedHash(key);
        int keyBucket = bucket(keyHash);
        if (this.hashTableKToV[keyBucket] == src) {
            this.hashTableKToV[keyBucket] = dest;
        } else {
            int prevInBucket = this.hashTableKToV[keyBucket];
            int i = this.nextInBucketKToV[prevInBucket];
            while (true) {
                int entryInBucket = i;
                if (entryInBucket == src) {
                    break;
                }
                prevInBucket = entryInBucket;
                i = this.nextInBucketKToV[entryInBucket];
            }
            this.nextInBucketKToV[prevInBucket] = dest;
        }
        this.nextInBucketKToV[dest] = this.nextInBucketKToV[src];
        this.nextInBucketKToV[src] = -1;
        int valueHash = Hashing.smearedHash(value);
        int valueBucket = bucket(valueHash);
        if (this.hashTableVToK[valueBucket] == src) {
            this.hashTableVToK[valueBucket] = dest;
        } else {
            int prevInBucket2 = this.hashTableVToK[valueBucket];
            int i2 = this.nextInBucketVToK[prevInBucket2];
            while (true) {
                int entryInBucket2 = i2;
                if (entryInBucket2 == src) {
                    break;
                }
                prevInBucket2 = entryInBucket2;
                i2 = this.nextInBucketVToK[entryInBucket2];
            }
            this.nextInBucketVToK[prevInBucket2] = dest;
        }
        this.nextInBucketVToK[dest] = this.nextInBucketVToK[src];
        this.nextInBucketVToK[src] = -1;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        Arrays.fill(this.keys, 0, this.size, (Object) null);
        Arrays.fill(this.values, 0, this.size, (Object) null);
        Arrays.fill(this.hashTableKToV, -1);
        Arrays.fill(this.hashTableVToK, -1);
        Arrays.fill(this.nextInBucketKToV, 0, this.size, -1);
        Arrays.fill(this.nextInBucketVToK, 0, this.size, -1);
        Arrays.fill(this.prevInInsertionOrder, 0, this.size, -1);
        Arrays.fill(this.nextInInsertionOrder, 0, this.size, -1);
        this.size = 0;
        this.firstInInsertionOrder = -2;
        this.lastInInsertionOrder = -2;
        this.modCount++;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/HashBiMap$View.class */
    public static abstract class View<K, V, T> extends AbstractSet<T> {
        final HashBiMap<K, V> biMap;

        abstract T forEntry(int i);

        View(HashBiMap<K, V> biMap) {
            this.biMap = biMap;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<T> iterator() {
            return new Iterator<T>() { // from class: com.google.common.collect.HashBiMap.View.1
                private int index;
                private int indexToRemove = -1;
                private int expectedModCount;
                private int remaining;

                {
                    this.index = ((HashBiMap) View.this.biMap).firstInInsertionOrder;
                    this.expectedModCount = View.this.biMap.modCount;
                    this.remaining = View.this.biMap.size;
                }

                private void checkForComodification() {
                    if (View.this.biMap.modCount != this.expectedModCount) {
                        throw new ConcurrentModificationException();
                    }
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    checkForComodification();
                    return this.index != -2 && this.remaining > 0;
                }

                @Override // java.util.Iterator
                public T next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    T result = (T) View.this.forEntry(this.index);
                    this.indexToRemove = this.index;
                    this.index = ((HashBiMap) View.this.biMap).nextInInsertionOrder[this.index];
                    this.remaining--;
                    return result;
                }

                @Override // java.util.Iterator
                public void remove() {
                    checkForComodification();
                    CollectPreconditions.checkRemove(this.indexToRemove != -1);
                    View.this.biMap.removeEntry(this.indexToRemove);
                    if (this.index == View.this.biMap.size) {
                        this.index = this.indexToRemove;
                    }
                    this.indexToRemove = -1;
                    this.expectedModCount = View.this.biMap.modCount;
                }
            };
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.biMap.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            this.biMap.clear();
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<K> keySet() {
        Set<K> result = this.keySet;
        if (result == null) {
            KeySet keySet = new KeySet();
            this.keySet = keySet;
            return keySet;
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/HashBiMap$KeySet.class */
    public final class KeySet extends View<K, V, K> {
        KeySet() {
            super(HashBiMap.this);
        }

        @Override // com.google.common.collect.HashBiMap.View
        K forEntry(int entry) {
            return HashBiMap.this.keys[entry];
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@NullableDecl Object o) {
            return HashBiMap.this.containsKey(o);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(@NullableDecl Object o) {
            int oHash = Hashing.smearedHash(o);
            int entry = HashBiMap.this.findEntryByKey(o, oHash);
            if (entry != -1) {
                HashBiMap.this.removeEntryKeyHashKnown(entry, oHash);
                return true;
            }
            return false;
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<V> values() {
        Set<V> result = this.valueSet;
        if (result == null) {
            ValueSet valueSet = new ValueSet();
            this.valueSet = valueSet;
            return valueSet;
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/HashBiMap$ValueSet.class */
    public final class ValueSet extends View<K, V, V> {
        ValueSet() {
            super(HashBiMap.this);
        }

        @Override // com.google.common.collect.HashBiMap.View
        V forEntry(int entry) {
            return HashBiMap.this.values[entry];
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@NullableDecl Object o) {
            return HashBiMap.this.containsValue(o);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(@NullableDecl Object o) {
            int oHash = Hashing.smearedHash(o);
            int entry = HashBiMap.this.findEntryByValue(o, oHash);
            if (entry != -1) {
                HashBiMap.this.removeEntryValueHashKnown(entry, oHash);
                return true;
            }
            return false;
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> result = this.entrySet;
        if (result == null) {
            EntrySet entrySet = new EntrySet();
            this.entrySet = entrySet;
            return entrySet;
        }
        return result;
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/HashBiMap$EntrySet.class */
    final class EntrySet extends View<K, V, Map.Entry<K, V>> {
        EntrySet() {
            super(HashBiMap.this);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@NullableDecl Object o) {
            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> e = (Map.Entry) o;
                Object k = e.getKey();
                Object v = e.getValue();
                int eIndex = HashBiMap.this.findEntryByKey(k);
                return eIndex != -1 && Objects.equal(v, HashBiMap.this.values[eIndex]);
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        @CanIgnoreReturnValue
        public boolean remove(@NullableDecl Object o) {
            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> e = (Map.Entry) o;
                Object k = e.getKey();
                Object v = e.getValue();
                int kHash = Hashing.smearedHash(k);
                int eIndex = HashBiMap.this.findEntryByKey(k, kHash);
                if (eIndex != -1 && Objects.equal(v, HashBiMap.this.values[eIndex])) {
                    HashBiMap.this.removeEntryKeyHashKnown(eIndex, kHash);
                    return true;
                }
                return false;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.HashBiMap.View
        public Map.Entry<K, V> forEntry(int entry) {
            return new EntryForKey(entry);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/HashBiMap$EntryForKey.class */
    public final class EntryForKey extends AbstractMapEntry<K, V> {
        @NullableDecl
        final K key;
        int index;

        EntryForKey(int index) {
            this.key = HashBiMap.this.keys[index];
            this.index = index;
        }

        void updateIndex() {
            if (this.index == -1 || this.index > HashBiMap.this.size || !Objects.equal(HashBiMap.this.keys[this.index], this.key)) {
                this.index = HashBiMap.this.findEntryByKey(this.key);
            }
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        public K getKey() {
            return this.key;
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        @NullableDecl
        public V getValue() {
            updateIndex();
            if (this.index == -1) {
                return null;
            }
            return HashBiMap.this.values[this.index];
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        public V setValue(V value) {
            updateIndex();
            if (this.index == -1) {
                return (V) HashBiMap.this.put(this.key, value);
            }
            V oldValue = HashBiMap.this.values[this.index];
            if (!Objects.equal(oldValue, value)) {
                HashBiMap.this.replaceValueInEntry(this.index, value, false);
                return oldValue;
            }
            return value;
        }
    }

    @Override // com.google.common.collect.BiMap
    public BiMap<V, K> inverse() {
        BiMap<V, K> result = this.inverse;
        if (result == null) {
            Inverse inverse = new Inverse(this);
            this.inverse = inverse;
            return inverse;
        }
        return result;
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/HashBiMap$Inverse.class */
    static class Inverse<K, V> extends AbstractMap<V, K> implements BiMap<V, K>, Serializable {
        private final HashBiMap<K, V> forward;
        private transient Set<Map.Entry<V, K>> inverseEntrySet;

        Inverse(HashBiMap<K, V> forward) {
            this.forward = forward;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public int size() {
            return this.forward.size;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(@NullableDecl Object key) {
            return this.forward.containsValue(key);
        }

        @Override // java.util.AbstractMap, java.util.Map
        @NullableDecl
        public K get(@NullableDecl Object key) {
            return this.forward.getInverse(key);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsValue(@NullableDecl Object value) {
            return this.forward.containsKey(value);
        }

        @Override // java.util.AbstractMap, java.util.Map, com.google.common.collect.BiMap
        @CanIgnoreReturnValue
        @NullableDecl
        public K put(@NullableDecl V value, @NullableDecl K key) {
            return this.forward.putInverse(value, key, false);
        }

        @Override // com.google.common.collect.BiMap
        @CanIgnoreReturnValue
        @NullableDecl
        public K forcePut(@NullableDecl V value, @NullableDecl K key) {
            return this.forward.putInverse(value, key, true);
        }

        @Override // com.google.common.collect.BiMap
        public BiMap<K, V> inverse() {
            return this.forward;
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CanIgnoreReturnValue
        @NullableDecl
        public K remove(@NullableDecl Object value) {
            return this.forward.removeInverse(value);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public void clear() {
            this.forward.clear();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<V> keySet() {
            return this.forward.values();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<K> values() {
            return this.forward.keySet();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<Map.Entry<V, K>> entrySet() {
            Set<Map.Entry<V, K>> result = this.inverseEntrySet;
            if (result == null) {
                InverseEntrySet inverseEntrySet = new InverseEntrySet(this.forward);
                this.inverseEntrySet = inverseEntrySet;
                return inverseEntrySet;
            }
            return result;
        }

        @GwtIncompatible("serialization")
        private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
            in.defaultReadObject();
            ((HashBiMap) this.forward).inverse = this;
        }
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/HashBiMap$InverseEntrySet.class */
    static class InverseEntrySet<K, V> extends View<K, V, Map.Entry<V, K>> {
        InverseEntrySet(HashBiMap<K, V> biMap) {
            super(biMap);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@NullableDecl Object o) {
            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> e = (Map.Entry) o;
                Object v = e.getKey();
                Object k = e.getValue();
                int eIndex = this.biMap.findEntryByValue(v);
                return eIndex != -1 && Objects.equal(this.biMap.keys[eIndex], k);
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> e = (Map.Entry) o;
                Object v = e.getKey();
                Object k = e.getValue();
                int vHash = Hashing.smearedHash(v);
                int eIndex = this.biMap.findEntryByValue(v, vHash);
                if (eIndex != -1 && Objects.equal(this.biMap.keys[eIndex], k)) {
                    this.biMap.removeEntryValueHashKnown(eIndex, vHash);
                    return true;
                }
                return false;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.HashBiMap.View
        public Map.Entry<V, K> forEntry(int entry) {
            return new EntryForValue(this.biMap, entry);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/HashBiMap$EntryForValue.class */
    public static final class EntryForValue<K, V> extends AbstractMapEntry<V, K> {
        final HashBiMap<K, V> biMap;
        final V value;
        int index;

        EntryForValue(HashBiMap<K, V> biMap, int index) {
            this.biMap = biMap;
            this.value = biMap.values[index];
            this.index = index;
        }

        private void updateIndex() {
            if (this.index == -1 || this.index > this.biMap.size || !Objects.equal(this.value, this.biMap.values[this.index])) {
                this.index = this.biMap.findEntryByValue(this.value);
            }
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        public V getKey() {
            return this.value;
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        public K getValue() {
            updateIndex();
            if (this.index == -1) {
                return null;
            }
            return this.biMap.keys[this.index];
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        public K setValue(K key) {
            updateIndex();
            if (this.index == -1) {
                return this.biMap.putInverse(this.value, key, false);
            }
            K oldKey = this.biMap.keys[this.index];
            if (!Objects.equal(oldKey, key)) {
                this.biMap.replaceKeyInEntry(this.index, key, false);
                return oldKey;
            }
            return key;
        }
    }

    @GwtIncompatible
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        Serialization.writeMap(this, stream);
    }

    @GwtIncompatible
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        int size = Serialization.readCount(stream);
        init(16);
        Serialization.populateMap(this, stream, size);
    }
}
