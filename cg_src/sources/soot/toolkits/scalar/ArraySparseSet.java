package soot.toolkits.scalar;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/ArraySparseSet.class */
public class ArraySparseSet<T> extends AbstractFlowSet<T> {
    protected static final int DEFAULT_SIZE = 8;
    protected int numElements;
    protected int maxElements;
    protected T[] elements;

    public ArraySparseSet() {
        this.maxElements = 8;
        this.elements = (T[]) new Object[8];
        this.numElements = 0;
    }

    private ArraySparseSet(ArraySparseSet<T> other) {
        this.numElements = other.numElements;
        this.maxElements = other.maxElements;
        this.elements = (T[]) ((Object[]) other.elements.clone());
    }

    private boolean sameType(Object flowSet) {
        return flowSet instanceof ArraySparseSet;
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    /* renamed from: clone */
    public ArraySparseSet<T> mo2534clone() {
        return new ArraySparseSet<>(this);
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public FlowSet<T> emptySet() {
        return new ArraySparseSet();
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public void clear() {
        this.numElements = 0;
        Arrays.fill(this.elements, (Object) null);
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public int size() {
        return this.numElements;
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public boolean isEmpty() {
        return this.numElements == 0;
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public List<T> toList() {
        return Arrays.asList(Arrays.copyOf(this.elements, this.numElements));
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public void add(T e) {
        if (!contains(e)) {
            if (this.numElements == this.maxElements) {
                doubleCapacity();
            }
            T[] tArr = this.elements;
            int i = this.numElements;
            this.numElements = i + 1;
            tArr[i] = e;
        }
    }

    private void doubleCapacity() {
        int newSize = this.maxElements * 2;
        T[] tArr = (T[]) new Object[newSize];
        System.arraycopy(this.elements, 0, tArr, 0, this.numElements);
        this.elements = tArr;
        this.maxElements = newSize;
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public void remove(Object obj) {
        for (int i = 0; i < this.numElements; i++) {
            if (this.elements[i].equals(obj)) {
                remove(i);
                return;
            }
        }
    }

    public void remove(int idx) {
        this.numElements--;
        this.elements[idx] = this.elements[this.numElements];
        this.elements[this.numElements] = null;
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public void union(FlowSet<T> otherFlow, FlowSet<T> destFlow) {
        if (sameType(otherFlow) && sameType(destFlow)) {
            ArraySparseSet<T> other = (ArraySparseSet) otherFlow;
            ArraySparseSet<T> dest = (ArraySparseSet) destFlow;
            if (dest == other) {
                for (int i = 0; i < this.numElements; i++) {
                    dest.add(this.elements[i]);
                }
                return;
            }
            if (this != dest) {
                copy(dest);
            }
            for (int i2 = 0; i2 < other.numElements; i2++) {
                dest.add(other.elements[i2]);
            }
            return;
        }
        super.union(otherFlow, destFlow);
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public void intersection(FlowSet<T> otherFlow, FlowSet<T> destFlow) {
        ArraySparseSet<T> workingSet;
        if (sameType(otherFlow) && sameType(destFlow)) {
            ArraySparseSet<T> other = (ArraySparseSet) otherFlow;
            ArraySparseSet<T> dest = (ArraySparseSet) destFlow;
            if (dest == other || dest == this) {
                workingSet = new ArraySparseSet<>();
            } else {
                workingSet = dest;
                workingSet.clear();
            }
            for (int i = 0; i < this.numElements; i++) {
                if (other.contains(this.elements[i])) {
                    workingSet.add(this.elements[i]);
                }
            }
            if (workingSet != dest) {
                workingSet.copy(dest);
                return;
            }
            return;
        }
        super.intersection(otherFlow, destFlow);
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public void difference(FlowSet<T> otherFlow, FlowSet<T> destFlow) {
        ArraySparseSet<T> workingSet;
        if (sameType(otherFlow) && sameType(destFlow)) {
            ArraySparseSet<T> other = (ArraySparseSet) otherFlow;
            ArraySparseSet<T> dest = (ArraySparseSet) destFlow;
            if (dest == other || dest == this) {
                workingSet = new ArraySparseSet<>();
            } else {
                workingSet = dest;
                workingSet.clear();
            }
            for (int i = 0; i < this.numElements; i++) {
                if (!other.contains(this.elements[i])) {
                    workingSet.add(this.elements[i]);
                }
            }
            if (workingSet != dest) {
                workingSet.copy(dest);
                return;
            }
            return;
        }
        super.difference(otherFlow, destFlow);
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    @Deprecated
    public boolean contains(Object obj) {
        for (int i = 0; i < this.numElements; i++) {
            if (this.elements[i].equals(obj)) {
                return true;
            }
        }
        return false;
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet
    public boolean equals(Object otherFlow) {
        if (sameType(otherFlow)) {
            ArraySparseSet<T> other = (ArraySparseSet) otherFlow;
            int size = this.numElements;
            if (other.numElements != size) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (!other.contains(this.elements[i])) {
                    return false;
                }
            }
            return true;
        }
        return super.equals(otherFlow);
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public void copy(FlowSet<T> destFlow) {
        if (sameType(destFlow)) {
            ArraySparseSet<T> dest = (ArraySparseSet) destFlow;
            while (dest.maxElements < this.maxElements) {
                dest.doubleCapacity();
            }
            dest.numElements = this.numElements;
            System.arraycopy(this.elements, 0, dest.elements, 0, this.numElements);
            return;
        }
        super.copy(destFlow);
    }

    @Override // soot.toolkits.scalar.FlowSet
    public void copyFreshToExisting(FlowSet<T> destFlow) {
        if (sameType(destFlow)) {
            ArraySparseSet<T> dest = (ArraySparseSet) destFlow;
            dest.maxElements = this.maxElements;
            dest.elements = this.elements;
            dest.numElements = this.numElements;
            return;
        }
        super.copy(destFlow);
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet, java.lang.Iterable
    public Iterator<T> iterator() {
        return new Iterator<T>() { // from class: soot.toolkits.scalar.ArraySparseSet.1
            int nextIdx = 0;

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.nextIdx < ArraySparseSet.this.numElements;
            }

            @Override // java.util.Iterator
            public T next() {
                T[] tArr = ArraySparseSet.this.elements;
                int i = this.nextIdx;
                this.nextIdx = i + 1;
                return tArr[i];
            }

            @Override // java.util.Iterator
            public void remove() {
                if (this.nextIdx == 0) {
                    throw new IllegalStateException("'next' has not been called yet.");
                }
                ArraySparseSet.this.remove(this.nextIdx - 1);
                this.nextIdx--;
            }
        };
    }
}
