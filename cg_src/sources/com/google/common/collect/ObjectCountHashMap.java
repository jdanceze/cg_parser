package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Arrays;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* JADX INFO: Access modifiers changed from: package-private */
@GwtCompatible(serializable = true, emulated = true)
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/ObjectCountHashMap.class */
public class ObjectCountHashMap<K> {
    private static final int MAXIMUM_CAPACITY = 1073741824;
    static final float DEFAULT_LOAD_FACTOR = 1.0f;
    private static final long NEXT_MASK = 4294967295L;
    private static final long HASH_MASK = -4294967296L;
    static final int DEFAULT_SIZE = 3;
    static final int UNSET = -1;
    transient Object[] keys;
    transient int[] values;
    transient int size;
    transient int modCount;
    private transient int[] table;
    @VisibleForTesting
    transient long[] entries;
    private transient float loadFactor;
    private transient int threshold;

    public static <K> ObjectCountHashMap<K> create() {
        return new ObjectCountHashMap<>();
    }

    public static <K> ObjectCountHashMap<K> createWithExpectedSize(int expectedSize) {
        return new ObjectCountHashMap<>(expectedSize);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ObjectCountHashMap() {
        init(3, 1.0f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ObjectCountHashMap(ObjectCountHashMap<? extends K> map) {
        init(map.size(), 1.0f);
        int firstIndex = map.firstIndex();
        while (true) {
            int i = firstIndex;
            if (i != -1) {
                put(map.getKey(i), map.getValue(i));
                firstIndex = map.nextIndex(i);
            } else {
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ObjectCountHashMap(int capacity) {
        this(capacity, 1.0f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ObjectCountHashMap(int expectedSize, float loadFactor) {
        init(expectedSize, loadFactor);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void init(int expectedSize, float loadFactor) {
        Preconditions.checkArgument(expectedSize >= 0, "Initial capacity must be non-negative");
        Preconditions.checkArgument(loadFactor > 0.0f, "Illegal load factor");
        int buckets = Hashing.closedTableSize(expectedSize, loadFactor);
        this.table = newTable(buckets);
        this.loadFactor = loadFactor;
        this.keys = new Object[expectedSize];
        this.values = new int[expectedSize];
        this.entries = newEntries(expectedSize);
        this.threshold = Math.max(1, (int) (buckets * loadFactor));
    }

    private static int[] newTable(int size) {
        int[] array = new int[size];
        Arrays.fill(array, -1);
        return array;
    }

    private static long[] newEntries(int size) {
        long[] array = new long[size];
        Arrays.fill(array, -1L);
        return array;
    }

    private int hashTableMask() {
        return this.table.length - 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int firstIndex() {
        return this.size == 0 ? -1 : 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int nextIndex(int index) {
        if (index + 1 < this.size) {
            return index + 1;
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int nextIndexAfterRemove(int oldNextIndex, int removedIndex) {
        return oldNextIndex - 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int size() {
        return this.size;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public K getKey(int index) {
        Preconditions.checkElementIndex(index, this.size);
        return (K) this.keys[index];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getValue(int index) {
        Preconditions.checkElementIndex(index, this.size);
        return this.values[index];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setValue(int index, int newValue) {
        Preconditions.checkElementIndex(index, this.size);
        this.values[index] = newValue;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Multiset.Entry<K> getEntry(int index) {
        Preconditions.checkElementIndex(index, this.size);
        return new MapEntry(index);
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/ObjectCountHashMap$MapEntry.class */
    class MapEntry extends Multisets.AbstractEntry<K> {
        @NullableDecl
        final K key;
        int lastKnownIndex;

        MapEntry(int index) {
            this.key = (K) ObjectCountHashMap.this.keys[index];
            this.lastKnownIndex = index;
        }

        @Override // com.google.common.collect.Multiset.Entry
        public K getElement() {
            return this.key;
        }

        void updateLastKnownIndex() {
            if (this.lastKnownIndex == -1 || this.lastKnownIndex >= ObjectCountHashMap.this.size() || !Objects.equal(this.key, ObjectCountHashMap.this.keys[this.lastKnownIndex])) {
                this.lastKnownIndex = ObjectCountHashMap.this.indexOf(this.key);
            }
        }

        @Override // com.google.common.collect.Multiset.Entry
        public int getCount() {
            updateLastKnownIndex();
            if (this.lastKnownIndex == -1) {
                return 0;
            }
            return ObjectCountHashMap.this.values[this.lastKnownIndex];
        }

        @CanIgnoreReturnValue
        public int setCount(int count) {
            updateLastKnownIndex();
            if (this.lastKnownIndex == -1) {
                ObjectCountHashMap.this.put(this.key, count);
                return 0;
            }
            int old = ObjectCountHashMap.this.values[this.lastKnownIndex];
            ObjectCountHashMap.this.values[this.lastKnownIndex] = count;
            return old;
        }
    }

    private static int getHash(long entry) {
        return (int) (entry >>> 32);
    }

    private static int getNext(long entry) {
        return (int) entry;
    }

    private static long swapNext(long entry, int newNext) {
        return (HASH_MASK & entry) | (4294967295L & newNext);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void ensureCapacity(int minCapacity) {
        if (minCapacity > this.entries.length) {
            resizeEntries(minCapacity);
        }
        if (minCapacity >= this.threshold) {
            int newTableSize = Math.max(2, Integer.highestOneBit(minCapacity - 1) << 1);
            resizeTable(newTableSize);
        }
    }

    @CanIgnoreReturnValue
    public int put(@NullableDecl K key, int value) {
        int last;
        long entry;
        CollectPreconditions.checkPositive(value, "count");
        long[] entries = this.entries;
        Object[] keys = this.keys;
        int[] values = this.values;
        int hash = Hashing.smearedHash(key);
        int tableIndex = hash & hashTableMask();
        int newEntryIndex = this.size;
        int next = this.table[tableIndex];
        if (next == -1) {
            this.table[tableIndex] = newEntryIndex;
        } else {
            do {
                last = next;
                entry = entries[next];
                if (getHash(entry) == hash && Objects.equal(key, keys[next])) {
                    int oldValue = values[next];
                    values[next] = value;
                    return oldValue;
                }
                next = getNext(entry);
            } while (next != -1);
            entries[last] = swapNext(entry, newEntryIndex);
        }
        if (newEntryIndex == Integer.MAX_VALUE) {
            throw new IllegalStateException("Cannot contain more than Integer.MAX_VALUE elements!");
        }
        int newSize = newEntryIndex + 1;
        resizeMeMaybe(newSize);
        insertEntry(newEntryIndex, key, value, hash);
        this.size = newSize;
        if (newEntryIndex >= this.threshold) {
            resizeTable(2 * this.table.length);
        }
        this.modCount++;
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void insertEntry(int entryIndex, @NullableDecl K key, int value, int hash) {
        this.entries[entryIndex] = (hash << 32) | 4294967295L;
        this.keys[entryIndex] = key;
        this.values[entryIndex] = value;
    }

    private void resizeMeMaybe(int newSize) {
        int entriesSize = this.entries.length;
        if (newSize > entriesSize) {
            int newCapacity = entriesSize + Math.max(1, entriesSize >>> 1);
            if (newCapacity < 0) {
                newCapacity = Integer.MAX_VALUE;
            }
            if (newCapacity != entriesSize) {
                resizeEntries(newCapacity);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resizeEntries(int newCapacity) {
        this.keys = Arrays.copyOf(this.keys, newCapacity);
        this.values = Arrays.copyOf(this.values, newCapacity);
        long[] entries = this.entries;
        int oldCapacity = entries.length;
        long[] entries2 = Arrays.copyOf(entries, newCapacity);
        if (newCapacity > oldCapacity) {
            Arrays.fill(entries2, oldCapacity, newCapacity, -1L);
        }
        this.entries = entries2;
    }

    private void resizeTable(int newCapacity) {
        int[] oldTable = this.table;
        int oldCapacity = oldTable.length;
        if (oldCapacity >= 1073741824) {
            this.threshold = Integer.MAX_VALUE;
            return;
        }
        int newThreshold = 1 + ((int) (newCapacity * this.loadFactor));
        int[] newTable = newTable(newCapacity);
        long[] entries = this.entries;
        int mask = newTable.length - 1;
        for (int i = 0; i < this.size; i++) {
            long oldEntry = entries[i];
            int hash = getHash(oldEntry);
            int tableIndex = hash & mask;
            int next = newTable[tableIndex];
            newTable[tableIndex] = i;
            entries[i] = (hash << 32) | (4294967295L & next);
        }
        this.threshold = newThreshold;
        this.table = newTable;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int indexOf(@NullableDecl Object key) {
        int hash = Hashing.smearedHash(key);
        int i = this.table[hash & hashTableMask()];
        while (true) {
            int next = i;
            if (next != -1) {
                long entry = this.entries[next];
                if (getHash(entry) == hash && Objects.equal(key, this.keys[next])) {
                    return next;
                }
                i = getNext(entry);
            } else {
                return -1;
            }
        }
    }

    public boolean containsKey(@NullableDecl Object key) {
        return indexOf(key) != -1;
    }

    public int get(@NullableDecl Object key) {
        int index = indexOf(key);
        if (index == -1) {
            return 0;
        }
        return this.values[index];
    }

    @CanIgnoreReturnValue
    public int remove(@NullableDecl Object key) {
        return remove(key, Hashing.smearedHash(key));
    }

    private int remove(@NullableDecl Object key, int hash) {
        int tableIndex = hash & hashTableMask();
        int next = this.table[tableIndex];
        if (next == -1) {
            return 0;
        }
        int last = -1;
        do {
            if (getHash(this.entries[next]) == hash && Objects.equal(key, this.keys[next])) {
                int oldValue = this.values[next];
                if (last == -1) {
                    this.table[tableIndex] = getNext(this.entries[next]);
                } else {
                    this.entries[last] = swapNext(this.entries[last], getNext(this.entries[next]));
                }
                moveLastEntry(next);
                this.size--;
                this.modCount++;
                return oldValue;
            }
            last = next;
            next = getNext(this.entries[next]);
        } while (next != -1);
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @CanIgnoreReturnValue
    public int removeEntry(int entryIndex) {
        return remove(this.keys[entryIndex], getHash(this.entries[entryIndex]));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void moveLastEntry(int dstIndex) {
        int previous;
        long entry;
        int srcIndex = size() - 1;
        if (dstIndex < srcIndex) {
            this.keys[dstIndex] = this.keys[srcIndex];
            this.values[dstIndex] = this.values[srcIndex];
            this.keys[srcIndex] = null;
            this.values[srcIndex] = 0;
            long lastEntry = this.entries[srcIndex];
            this.entries[dstIndex] = lastEntry;
            this.entries[srcIndex] = -1;
            int tableIndex = getHash(lastEntry) & hashTableMask();
            int lastNext = this.table[tableIndex];
            if (lastNext == srcIndex) {
                this.table[tableIndex] = dstIndex;
                return;
            }
            do {
                previous = lastNext;
                entry = this.entries[lastNext];
                lastNext = getNext(entry);
            } while (lastNext != srcIndex);
            this.entries[previous] = swapNext(entry, dstIndex);
            return;
        }
        this.keys[dstIndex] = null;
        this.values[dstIndex] = 0;
        this.entries[dstIndex] = -1;
    }

    public void clear() {
        this.modCount++;
        Arrays.fill(this.keys, 0, this.size, (Object) null);
        Arrays.fill(this.values, 0, this.size, 0);
        Arrays.fill(this.table, -1);
        Arrays.fill(this.entries, -1L);
        this.size = 0;
    }
}
