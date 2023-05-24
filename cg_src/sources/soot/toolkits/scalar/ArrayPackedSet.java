package soot.toolkits.scalar;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/ArrayPackedSet.class */
public class ArrayPackedSet<T> extends AbstractBoundedFlowSet<T> {
    protected final ObjectIntMapper<T> map;
    protected final BitSet bits;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ArrayPackedSet.class.desiredAssertionStatus();
    }

    public ArrayPackedSet(FlowUniverse<T> universe) {
        this(new ObjectIntMapper(universe));
    }

    ArrayPackedSet(ObjectIntMapper<T> map) {
        this(map, new BitSet());
    }

    ArrayPackedSet(ObjectIntMapper<T> map, BitSet bits) {
        this.map = map;
        this.bits = bits;
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    /* renamed from: clone */
    public ArrayPackedSet<T> mo2534clone() {
        return new ArrayPackedSet<>(this.map, (BitSet) this.bits.clone());
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public FlowSet<T> emptySet() {
        return new ArrayPackedSet(this.map);
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public int size() {
        return this.bits.cardinality();
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public boolean isEmpty() {
        return this.bits.isEmpty();
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public void clear() {
        this.bits.clear();
    }

    private BitSet copyBitSet(ArrayPackedSet<?> dest) {
        if ($assertionsDisabled || dest.map == this.map) {
            if (this != dest) {
                dest.bits.clear();
                dest.bits.or(this.bits);
            }
            return dest.bits;
        }
        throw new AssertionError();
    }

    private boolean sameType(Object flowSet) {
        return (flowSet instanceof ArrayPackedSet) && ((ArrayPackedSet) flowSet).map == this.map;
    }

    private List<T> toList(BitSet bits, int base) {
        int len = bits.cardinality();
        switch (len) {
            case 0:
                return Collections.emptyList();
            case 1:
                return Collections.singletonList(this.map.getObject((base - 1) + bits.length()));
            default:
                List<T> elements = new ArrayList<>(len);
                int i = bits.nextSetBit(0);
                do {
                    int endOfRun = bits.nextClearBit(i + 1);
                    do {
                        int i2 = i;
                        i++;
                        elements.add(this.map.getObject(base + i2));
                    } while (i < endOfRun);
                    i = bits.nextSetBit(i + 1);
                } while (i >= 0);
                return elements;
        }
    }

    public List<T> toList(int lowInclusive, int highInclusive) {
        if (lowInclusive > highInclusive) {
            return Collections.emptyList();
        }
        if (lowInclusive < 0) {
            throw new IllegalArgumentException();
        }
        int highExclusive = highInclusive + 1;
        return toList(this.bits.get(lowInclusive, highExclusive), lowInclusive);
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public List<T> toList() {
        return toList(this.bits, 0);
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public void add(T obj) {
        this.bits.set(this.map.getInt(obj));
    }

    @Override // soot.toolkits.scalar.AbstractBoundedFlowSet, soot.toolkits.scalar.BoundedFlowSet
    public void complement(FlowSet<T> destFlow) {
        if (sameType(destFlow)) {
            ArrayPackedSet<T> dest = (ArrayPackedSet) destFlow;
            copyBitSet(dest).flip(0, dest.map.size());
            return;
        }
        super.complement(destFlow);
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public void remove(T obj) {
        this.bits.clear(this.map.getInt(obj));
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public boolean isSubSet(FlowSet<T> other) {
        if (other == this) {
            return true;
        }
        if (sameType(other)) {
            ArrayPackedSet<T> o = (ArrayPackedSet) other;
            BitSet tmp = (BitSet) o.bits.clone();
            tmp.andNot(this.bits);
            return tmp.isEmpty();
        }
        return super.isSubSet(other);
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public void union(FlowSet<T> otherFlow, FlowSet<T> destFlow) {
        if (sameType(otherFlow) && sameType(destFlow)) {
            ArrayPackedSet<T> other = (ArrayPackedSet) otherFlow;
            ArrayPackedSet<T> dest = (ArrayPackedSet) destFlow;
            copyBitSet(dest).or(other.bits);
            return;
        }
        super.union(otherFlow, destFlow);
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public void difference(FlowSet<T> otherFlow, FlowSet<T> destFlow) {
        if (sameType(otherFlow) && sameType(destFlow)) {
            ArrayPackedSet<T> other = (ArrayPackedSet) otherFlow;
            ArrayPackedSet<T> dest = (ArrayPackedSet) destFlow;
            copyBitSet(dest).andNot(other.bits);
            return;
        }
        super.difference(otherFlow, destFlow);
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public void intersection(FlowSet<T> otherFlow, FlowSet<T> destFlow) {
        if (sameType(otherFlow) && sameType(destFlow)) {
            ArrayPackedSet<T> other = (ArrayPackedSet) otherFlow;
            ArrayPackedSet<T> dest = (ArrayPackedSet) destFlow;
            copyBitSet(dest).and(other.bits);
            return;
        }
        super.intersection(otherFlow, destFlow);
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public boolean contains(T obj) {
        return this.map.contains(obj) && this.bits.get(this.map.getInt(obj));
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet
    public boolean equals(Object otherFlow) {
        if (sameType(otherFlow)) {
            return this.bits.equals(((ArrayPackedSet) otherFlow).bits);
        }
        return super.equals(otherFlow);
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public void copy(FlowSet<T> destFlow) {
        if (this == destFlow) {
            return;
        }
        if (sameType(destFlow)) {
            ArrayPackedSet<T> dest = (ArrayPackedSet) destFlow;
            copyBitSet(dest);
            return;
        }
        super.copy(destFlow);
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet, java.lang.Iterable
    public Iterator<T> iterator() {
        return new Iterator<T>() { // from class: soot.toolkits.scalar.ArrayPackedSet.1
            int curr = -1;
            int next;

            {
                this.next = ArrayPackedSet.this.bits.nextSetBit(0);
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.next >= 0;
            }

            @Override // java.util.Iterator
            public T next() {
                if (this.next < 0) {
                    throw new NoSuchElementException();
                }
                this.curr = this.next;
                this.next = ArrayPackedSet.this.bits.nextSetBit(this.curr + 1);
                return ArrayPackedSet.this.map.getObject(this.curr);
            }

            @Override // java.util.Iterator
            public void remove() {
                if (this.curr < 0) {
                    throw new IllegalStateException();
                }
                ArrayPackedSet.this.bits.clear(this.curr);
                this.curr = -1;
            }
        };
    }
}
