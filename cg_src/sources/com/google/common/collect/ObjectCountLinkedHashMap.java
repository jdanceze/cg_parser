package com.google.common.collect;

import android.widget.ExpandableListView;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import java.util.Arrays;
@GwtCompatible(serializable = true, emulated = true)
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/ObjectCountLinkedHashMap.class */
class ObjectCountLinkedHashMap<K> extends ObjectCountHashMap<K> {
    private static final int ENDPOINT = -2;
    @VisibleForTesting
    transient long[] links;
    private transient int firstEntry;
    private transient int lastEntry;

    public static <K> ObjectCountLinkedHashMap<K> create() {
        return new ObjectCountLinkedHashMap<>();
    }

    public static <K> ObjectCountLinkedHashMap<K> createWithExpectedSize(int expectedSize) {
        return new ObjectCountLinkedHashMap<>(expectedSize);
    }

    ObjectCountLinkedHashMap() {
        this(3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ObjectCountLinkedHashMap(int expectedSize) {
        this(expectedSize, 1.0f);
    }

    ObjectCountLinkedHashMap(int expectedSize, float loadFactor) {
        super(expectedSize, loadFactor);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ObjectCountLinkedHashMap(ObjectCountHashMap<K> map) {
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
    @Override // com.google.common.collect.ObjectCountHashMap
    public void init(int expectedSize, float loadFactor) {
        super.init(expectedSize, loadFactor);
        this.firstEntry = -2;
        this.lastEntry = -2;
        this.links = new long[expectedSize];
        Arrays.fill(this.links, -1L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ObjectCountHashMap
    public int firstIndex() {
        if (this.firstEntry == -2) {
            return -1;
        }
        return this.firstEntry;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ObjectCountHashMap
    public int nextIndex(int index) {
        int result = getSuccessor(index);
        if (result == -2) {
            return -1;
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ObjectCountHashMap
    public int nextIndexAfterRemove(int oldNextIndex, int removedIndex) {
        return oldNextIndex == size() ? removedIndex : oldNextIndex;
    }

    private int getPredecessor(int entry) {
        return (int) (this.links[entry] >>> 32);
    }

    private int getSuccessor(int entry) {
        return (int) this.links[entry];
    }

    private void setSuccessor(int entry, int succ) {
        this.links[entry] = (this.links[entry] & (ExpandableListView.PACKED_POSITION_VALUE_NULL ^ (-1))) | (succ & ExpandableListView.PACKED_POSITION_VALUE_NULL);
    }

    private void setPredecessor(int entry, int pred) {
        this.links[entry] = (this.links[entry] & ((-4294967296L) ^ (-1))) | (pred << 32);
    }

    private void setSucceeds(int pred, int succ) {
        if (pred == -2) {
            this.firstEntry = succ;
        } else {
            setSuccessor(pred, succ);
        }
        if (succ == -2) {
            this.lastEntry = pred;
        } else {
            setPredecessor(succ, pred);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ObjectCountHashMap
    public void insertEntry(int entryIndex, K key, int value, int hash) {
        super.insertEntry(entryIndex, key, value, hash);
        setSucceeds(this.lastEntry, entryIndex);
        setSucceeds(entryIndex, -2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ObjectCountHashMap
    public void moveLastEntry(int dstIndex) {
        int srcIndex = size() - 1;
        setSucceeds(getPredecessor(dstIndex), getSuccessor(dstIndex));
        if (dstIndex < srcIndex) {
            setSucceeds(getPredecessor(srcIndex), dstIndex);
            setSucceeds(dstIndex, getSuccessor(srcIndex));
        }
        super.moveLastEntry(dstIndex);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ObjectCountHashMap
    public void resizeEntries(int newCapacity) {
        super.resizeEntries(newCapacity);
        int oldCapacity = this.links.length;
        this.links = Arrays.copyOf(this.links, newCapacity);
        Arrays.fill(this.links, oldCapacity, newCapacity, -1L);
    }

    @Override // com.google.common.collect.ObjectCountHashMap
    public void clear() {
        super.clear();
        this.firstEntry = -2;
        this.lastEntry = -2;
    }
}
