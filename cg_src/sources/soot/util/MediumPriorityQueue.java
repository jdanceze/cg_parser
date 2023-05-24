package soot.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:soot/util/MediumPriorityQueue.class */
class MediumPriorityQueue<E> extends PriorityQueue<E> {
    static final int MAX_CAPACITY = 4096;
    private final long[] data;
    private int size;
    private long modCount;
    private long lookup;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !MediumPriorityQueue.class.desiredAssertionStatus();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MediumPriorityQueue(List<? extends E> universe, Map<E, Integer> ordinalMap) {
        super(universe, ordinalMap);
        this.size = 0;
        this.modCount = 0L;
        this.lookup = 0L;
        this.data = new long[((this.N + 64) - 1) >>> 6];
        if (!$assertionsDisabled && this.N <= 64) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && this.N > 4096) {
            throw new AssertionError();
        }
    }

    @Override // soot.util.PriorityQueue
    void addAll() {
        this.size = this.N;
        Arrays.fill(this.data, -1L);
        this.data[this.data.length - 1] = (-1) >>> (-this.size);
        this.lookup = (-1) >>> (-this.data.length);
        this.min = 0;
        this.modCount++;
    }

    @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection
    public void clear() {
        this.size = 0;
        Arrays.fill(this.data, 0L);
        this.lookup = 0L;
        this.min = Integer.MAX_VALUE;
        this.modCount++;
    }

    @Override // soot.util.PriorityQueue
    int nextSetBit(int fromIndex) {
        if ($assertionsDisabled || fromIndex >= 0) {
            int bb = fromIndex >>> 6;
            while (fromIndex < this.N) {
                long m1 = (-1) << fromIndex;
                long t1 = this.data[bb] & m1;
                if ((t1 & (-m1)) != 0) {
                    return fromIndex;
                }
                if (t1 != 0) {
                    return (bb << 6) + Long.numberOfTrailingZeros(t1);
                }
                bb++;
                long m0 = (-1) << bb;
                long t0 = this.lookup & m0;
                if ((t0 & (-m0)) == 0) {
                    bb = Long.numberOfTrailingZeros(t0);
                }
                fromIndex = bb << 6;
            }
            return fromIndex;
        }
        throw new AssertionError();
    }

    @Override // soot.util.PriorityQueue
    boolean add(int ordinal) {
        int bucket = ordinal >>> 6;
        long prv = this.data[bucket];
        long now = prv | (1 << ordinal);
        if (prv == now) {
            return false;
        }
        this.data[bucket] = now;
        this.lookup |= 1 << bucket;
        this.size++;
        this.modCount++;
        this.min = Math.min(this.min, ordinal);
        return true;
    }

    @Override // soot.util.PriorityQueue
    boolean contains(int ordinal) {
        if ($assertionsDisabled || ordinal >= 0) {
            if ($assertionsDisabled || ordinal < this.N) {
                return ((this.data[ordinal >>> 6] >>> ordinal) & 1) == 1;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    @Override // soot.util.PriorityQueue
    boolean remove(int index) {
        if ($assertionsDisabled || index >= 0) {
            if ($assertionsDisabled || index < this.N) {
                int bucket = index >>> 6;
                long old = this.data[bucket];
                long now = old & ((1 << index) ^ (-1));
                if (old == now) {
                    return false;
                }
                if (0 == now) {
                    this.lookup &= (1 << bucket) ^ (-1);
                }
                this.size--;
                this.modCount++;
                this.data[bucket] = now;
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

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    public Iterator<E> iterator() {
        return new PriorityQueue<E>.Itr(this) { // from class: soot.util.MediumPriorityQueue.1
            @Override // soot.util.PriorityQueue.Itr
            long getExpected() {
                return MediumPriorityQueue.this.modCount;
            }
        };
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public int size() {
        return this.size;
    }
}
