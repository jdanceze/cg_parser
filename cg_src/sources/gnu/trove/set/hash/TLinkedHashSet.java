package gnu.trove.set.hash;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.iterator.hash.TObjectHashIterator;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.list.linked.TIntLinkedList;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.io.IOException;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/set/hash/TLinkedHashSet.class */
public class TLinkedHashSet<E> extends THashSet<E> {
    TIntList order;

    public TLinkedHashSet() {
    }

    public TLinkedHashSet(int initialCapacity) {
        super(initialCapacity);
    }

    public TLinkedHashSet(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public TLinkedHashSet(Collection<? extends E> es) {
        super(es);
    }

    @Override // gnu.trove.impl.hash.TObjectHash, gnu.trove.impl.hash.THash
    public int setUp(int initialCapacity) {
        this.order = new TIntArrayList(initialCapacity) { // from class: gnu.trove.set.hash.TLinkedHashSet.1
            @Override // gnu.trove.list.array.TIntArrayList
            public void ensureCapacity(int capacity) {
                if (capacity > this._data.length) {
                    int newCap = Math.max(TLinkedHashSet.this._set.length, capacity);
                    int[] tmp = new int[newCap];
                    System.arraycopy(this._data, 0, tmp, 0, this._data.length);
                    this._data = tmp;
                }
            }
        };
        return super.setUp(initialCapacity);
    }

    @Override // gnu.trove.set.hash.THashSet, gnu.trove.impl.hash.THash, gnu.trove.map.TObjectByteMap
    public void clear() {
        super.clear();
        this.order.clear();
    }

    @Override // gnu.trove.set.hash.THashSet
    public String toString() {
        StringBuilder buf = new StringBuilder("{");
        boolean first = true;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (first) {
                first = false;
            } else {
                buf.append(", ");
            }
            buf.append(it.next());
        }
        buf.append("}");
        return buf.toString();
    }

    @Override // gnu.trove.set.hash.THashSet, java.util.Set, java.util.Collection
    public boolean add(E obj) {
        int index = insertKey(obj);
        if (index < 0) {
            return false;
        }
        if (!this.order.add(index)) {
            throw new IllegalStateException("Order not changed after insert");
        }
        postInsertHook(this.consumeFreeSlot);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // gnu.trove.impl.hash.TObjectHash, gnu.trove.impl.hash.THash
    public void removeAt(int index) {
        this.order.remove(index);
        super.removeAt(index);
    }

    @Override // gnu.trove.set.hash.THashSet, gnu.trove.impl.hash.THash
    protected void rehash(int newCapacity) {
        TIntLinkedList oldOrder = new TIntLinkedList(this.order);
        int oldSize = size();
        Object[] oldSet = this._set;
        this.order.clear();
        this._set = new Object[newCapacity];
        Arrays.fill(this._set, FREE);
        TIntIterator iterator = oldOrder.iterator();
        while (iterator.hasNext()) {
            int i = iterator.next();
            Object obj = oldSet[i];
            if (obj == FREE || obj == REMOVED) {
                throw new IllegalStateException("Iterating over empty location while rehashing");
            }
            if (obj != FREE && obj != REMOVED) {
                int index = insertKey(obj);
                if (index < 0) {
                    throwObjectContractViolation(this._set[(-index) - 1], obj, size(), oldSize, oldSet);
                }
                if (!this.order.add(index)) {
                    throw new IllegalStateException("Order not changed after insert");
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/set/hash/TLinkedHashSet$WriteProcedure.class */
    class WriteProcedure implements TIntProcedure {
        final ObjectOutput output;
        IOException ioException;

        WriteProcedure(ObjectOutput output) {
            this.output = output;
        }

        public IOException getIoException() {
            return this.ioException;
        }

        @Override // gnu.trove.procedure.TIntProcedure
        public boolean execute(int value) {
            try {
                this.output.writeObject(TLinkedHashSet.this._set[value]);
                return true;
            } catch (IOException e) {
                this.ioException = e;
                return false;
            }
        }
    }

    @Override // gnu.trove.set.hash.THashSet
    protected void writeEntries(ObjectOutput out) throws IOException {
        TLinkedHashSet<E>.WriteProcedure writeProcedure = new WriteProcedure(out);
        if (!this.order.forEach(writeProcedure)) {
            throw writeProcedure.getIoException();
        }
    }

    @Override // gnu.trove.set.hash.THashSet, java.util.Set, java.util.Collection, java.lang.Iterable
    public TObjectHashIterator<E> iterator() {
        return new TObjectHashIterator<E>(this) { // from class: gnu.trove.set.hash.TLinkedHashSet.2
            TIntIterator localIterator;
            int lastIndex;

            {
                this.localIterator = TLinkedHashSet.this.order.iterator();
            }

            @Override // gnu.trove.impl.hash.THashIterator, java.util.Iterator
            public E next() {
                this.lastIndex = this.localIterator.next();
                return objectAtIndex(this.lastIndex);
            }

            @Override // gnu.trove.impl.hash.THashIterator, gnu.trove.iterator.TIterator, java.util.Iterator
            public boolean hasNext() {
                return this.localIterator.hasNext();
            }

            @Override // gnu.trove.impl.hash.THashIterator, gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                this.localIterator.remove();
                try {
                    this._hash.tempDisableAutoCompaction();
                    TLinkedHashSet.this.removeAt(this.lastIndex);
                    this._hash.reenableAutoCompaction(false);
                } catch (Throwable th) {
                    this._hash.reenableAutoCompaction(false);
                    throw th;
                }
            }
        };
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/set/hash/TLinkedHashSet$ForEachProcedure.class */
    class ForEachProcedure implements TIntProcedure {
        boolean changed = false;
        final Object[] set;
        final TObjectProcedure<? super E> procedure;

        public ForEachProcedure(Object[] set, TObjectProcedure<? super E> procedure) {
            this.set = set;
            this.procedure = procedure;
        }

        @Override // gnu.trove.procedure.TIntProcedure
        public boolean execute(int value) {
            return this.procedure.execute(this.set[value]);
        }
    }

    @Override // gnu.trove.impl.hash.TObjectHash
    public boolean forEach(TObjectProcedure<? super E> procedure) {
        TLinkedHashSet<E>.ForEachProcedure forEachProcedure = new ForEachProcedure(this._set, procedure);
        return this.order.forEach(forEachProcedure);
    }
}
