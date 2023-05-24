package soot.util;

import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:soot/util/LargePriorityQueue.class */
class LargePriorityQueue<E> extends PriorityQueue<E> {
    private final BitSet queue;
    private long modCount;

    /* JADX INFO: Access modifiers changed from: package-private */
    public LargePriorityQueue(List<? extends E> universe, Map<E, Integer> ordinalMap) {
        super(universe, ordinalMap);
        this.modCount = 0L;
        this.queue = new BitSet(this.N);
    }

    @Override // soot.util.PriorityQueue
    boolean add(int ordinal) {
        if (contains(ordinal)) {
            return false;
        }
        this.queue.set(ordinal);
        this.min = Math.min(this.min, ordinal);
        this.modCount++;
        return true;
    }

    @Override // soot.util.PriorityQueue
    void addAll() {
        this.queue.set(0, this.N);
        this.min = 0;
        this.modCount++;
    }

    @Override // soot.util.PriorityQueue
    int nextSetBit(int fromIndex) {
        int i = this.queue.nextSetBit(fromIndex);
        if (i < 0) {
            return Integer.MAX_VALUE;
        }
        return i;
    }

    @Override // soot.util.PriorityQueue
    boolean remove(int ordinal) {
        if (!contains(ordinal)) {
            return false;
        }
        this.queue.clear(ordinal);
        if (this.min == ordinal) {
            this.min = nextSetBit(this.min + 1);
        }
        this.modCount++;
        return true;
    }

    @Override // soot.util.PriorityQueue
    boolean contains(int ordinal) {
        return this.queue.get(ordinal);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    public Iterator<E> iterator() {
        return new PriorityQueue<E>.Itr(this) { // from class: soot.util.LargePriorityQueue.1
            @Override // soot.util.PriorityQueue.Itr
            long getExpected() {
                return LargePriorityQueue.this.modCount;
            }
        };
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public int size() {
        return this.queue.cardinality();
    }
}
