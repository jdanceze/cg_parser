package soot.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/util/SmallPriorityQueue.class */
public class SmallPriorityQueue<E> extends PriorityQueue<E> {
    static final int MAX_CAPACITY = 64;
    private long queue;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SmallPriorityQueue.class.desiredAssertionStatus();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SmallPriorityQueue(List<? extends E> universe, Map<E, Integer> ordinalMap) {
        super(universe, ordinalMap);
        this.queue = 0L;
        if (!$assertionsDisabled && universe.size() > 64) {
            throw new AssertionError();
        }
    }

    @Override // soot.util.PriorityQueue
    void addAll() {
        if (this.N == 0) {
            return;
        }
        this.queue = (-1) >>> (-this.N);
        this.min = 0;
    }

    @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection
    public void clear() {
        this.queue = 0L;
        this.min = Integer.MAX_VALUE;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    public Iterator<E> iterator() {
        return new PriorityQueue<E>.Itr(this) { // from class: soot.util.SmallPriorityQueue.1
            @Override // soot.util.PriorityQueue.Itr
            long getExpected() {
                return SmallPriorityQueue.this.queue;
            }
        };
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public int size() {
        return Long.bitCount(this.queue);
    }

    @Override // soot.util.PriorityQueue
    int nextSetBit(int fromIndex) {
        if ($assertionsDisabled || fromIndex >= 0) {
            if (fromIndex > this.N) {
                return fromIndex;
            }
            long m0 = (-1) << fromIndex;
            long t0 = this.queue & m0;
            if ((t0 & (-m0)) != 0) {
                return fromIndex;
            }
            return Long.numberOfTrailingZeros(t0);
        }
        throw new AssertionError();
    }

    @Override // soot.util.PriorityQueue
    boolean add(int ordinal) {
        long old = this.queue;
        this.queue |= 1 << ordinal;
        if (old == this.queue) {
            return false;
        }
        this.min = Math.min(this.min, ordinal);
        return true;
    }

    @Override // soot.util.PriorityQueue
    boolean contains(int ordinal) {
        if ($assertionsDisabled || ordinal >= 0) {
            if ($assertionsDisabled || ordinal < this.N) {
                return ((this.queue >>> ordinal) & 1) == 1;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    @Override // soot.util.PriorityQueue
    boolean remove(int index) {
        if ($assertionsDisabled || index >= 0) {
            if ($assertionsDisabled || index < this.N) {
                long old = this.queue;
                this.queue &= (1 << index) ^ (-1);
                if (old == this.queue) {
                    return false;
                }
                if (this.min == index) {
                    this.min = nextSetBit(this.min + 1);
                    return true;
                }
                return true;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean removeAll(Collection<?> c) {
        long mask = 0;
        for (Object o : c) {
            mask |= 1 << getOrdinal(o);
        }
        long old = this.queue;
        this.queue &= mask ^ (-1);
        this.min = nextSetBit(this.min);
        return old != this.queue;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean retainAll(Collection<?> c) {
        long mask = 0;
        for (Object o : c) {
            mask |= 1 << getOrdinal(o);
        }
        long old = this.queue;
        this.queue &= mask;
        this.min = nextSetBit(this.min);
        return old != this.queue;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean containsAll(Collection<?> c) {
        long mask = 0;
        for (Object o : c) {
            mask |= 1 << getOrdinal(o);
        }
        return (mask & (this.queue ^ (-1))) == 0;
    }

    @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection
    public boolean addAll(Collection<? extends E> c) {
        long mask = 0;
        for (Object o : c) {
            mask |= 1 << getOrdinal(o);
        }
        long old = this.queue;
        this.queue |= mask;
        if (old == this.queue) {
            return false;
        }
        this.min = nextSetBit(0);
        return true;
    }
}
