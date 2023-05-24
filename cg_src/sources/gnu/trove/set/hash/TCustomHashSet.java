package gnu.trove.set.hash;

import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.TCustomObjectHash;
import gnu.trove.iterator.hash.TObjectHashIterator;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.procedure.array.ToObjectArrayProceedure;
import gnu.trove.strategy.HashingStrategy;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/set/hash/TCustomHashSet.class */
public class TCustomHashSet<E> extends TCustomObjectHash<E> implements Set<E>, Iterable<E>, Externalizable {
    static final long serialVersionUID = 1;

    public TCustomHashSet() {
    }

    public TCustomHashSet(HashingStrategy<? super E> strategy) {
        super(strategy);
    }

    public TCustomHashSet(HashingStrategy<? super E> strategy, int initialCapacity) {
        super(strategy, initialCapacity);
    }

    public TCustomHashSet(HashingStrategy<? super E> strategy, int initialCapacity, float loadFactor) {
        super(strategy, initialCapacity, loadFactor);
    }

    public TCustomHashSet(HashingStrategy<? super E> strategy, Collection<? extends E> collection) {
        this(strategy, collection.size());
        addAll(collection);
    }

    @Override // java.util.Set, java.util.Collection
    public boolean add(E obj) {
        int index = insertKey(obj);
        if (index < 0) {
            return false;
        }
        postInsertHook(this.consumeFreeSlot);
        return true;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean equals(Object other) {
        if (!(other instanceof Set)) {
            return false;
        }
        Set that = (Set) other;
        if (that.size() != size()) {
            return false;
        }
        return containsAll(that);
    }

    @Override // java.util.Set, java.util.Collection
    public int hashCode() {
        TCustomHashSet<E>.HashProcedure p = new HashProcedure();
        forEach(p);
        return p.getHashCode();
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/set/hash/TCustomHashSet$HashProcedure.class */
    private final class HashProcedure implements TObjectProcedure<E> {
        private int h;

        private HashProcedure() {
            this.h = 0;
        }

        public int getHashCode() {
            return this.h;
        }

        @Override // gnu.trove.procedure.TObjectProcedure
        public final boolean execute(E key) {
            this.h += HashFunctions.hash(key);
            return true;
        }
    }

    @Override // gnu.trove.impl.hash.THash
    protected void rehash(int newCapacity) {
        int index;
        int oldCapacity = this._set.length;
        int oldSize = size();
        Object[] oldSet = this._set;
        this._set = new Object[newCapacity];
        Arrays.fill(this._set, FREE);
        int i = oldCapacity;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                Object obj = oldSet[i];
                if (obj != FREE && obj != REMOVED && (index = insertKey(obj)) < 0) {
                    throwObjectContractViolation(this._set[(-index) - 1], obj, size(), oldSize, oldSet);
                }
            } else {
                return;
            }
        }
    }

    @Override // java.util.Set, java.util.Collection
    public Object[] toArray() {
        Object[] result = new Object[size()];
        forEach(new ToObjectArrayProceedure(result));
        return result;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v15, types: [java.lang.Object[]] */
    @Override // java.util.Set, java.util.Collection
    public <T> T[] toArray(T[] a) {
        int size = size();
        if (a.length < size) {
            a = (Object[]) Array.newInstance(a.getClass().getComponentType(), size);
        }
        forEach(new ToObjectArrayProceedure(a));
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override // gnu.trove.impl.hash.THash, gnu.trove.map.TObjectByteMap
    public void clear() {
        super.clear();
        Arrays.fill(this._set, 0, this._set.length, FREE);
    }

    @Override // java.util.Set, java.util.Collection
    public boolean remove(Object obj) {
        int index = index(obj);
        if (index >= 0) {
            removeAt(index);
            return true;
        }
        return false;
    }

    @Override // java.util.Set, java.util.Collection, java.lang.Iterable
    public TObjectHashIterator<E> iterator() {
        return new TObjectHashIterator<>(this);
    }

    @Override // java.util.Set, java.util.Collection
    public boolean containsAll(Collection<?> collection) {
        Iterator i = collection.iterator();
        while (i.hasNext()) {
            if (!contains(i.next())) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean addAll(Collection<? extends E> collection) {
        boolean changed = false;
        int size = collection.size();
        ensureCapacity(size);
        Iterator<? extends E> it = collection.iterator();
        while (true) {
            int i = size;
            size--;
            if (i > 0) {
                if (add(it.next())) {
                    changed = true;
                }
            } else {
                return changed;
            }
        }
    }

    @Override // java.util.Set, java.util.Collection
    public boolean removeAll(Collection<?> collection) {
        boolean changed = false;
        int size = collection.size();
        Iterator it = collection.iterator();
        while (true) {
            int i = size;
            size--;
            if (i > 0) {
                if (remove(it.next())) {
                    changed = true;
                }
            } else {
                return changed;
            }
        }
    }

    @Override // java.util.Set, java.util.Collection
    public boolean retainAll(Collection<?> collection) {
        boolean changed = false;
        int size = size();
        Iterator<E> it = iterator();
        while (true) {
            int i = size;
            size--;
            if (i > 0) {
                if (!collection.contains(it.next())) {
                    it.remove();
                    changed = true;
                }
            } else {
                return changed;
            }
        }
    }

    public String toString() {
        final StringBuilder buf = new StringBuilder("{");
        forEach((TObjectProcedure<E>) new TObjectProcedure<E>() { // from class: gnu.trove.set.hash.TCustomHashSet.1
            private boolean first = true;

            @Override // gnu.trove.procedure.TObjectProcedure
            public boolean execute(Object value) {
                if (this.first) {
                    this.first = false;
                } else {
                    buf.append(", ");
                }
                buf.append(value);
                return true;
            }
        });
        buf.append("}");
        return buf.toString();
    }

    @Override // gnu.trove.impl.hash.TCustomObjectHash, gnu.trove.impl.hash.TObjectHash, gnu.trove.impl.hash.THash, java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(1);
        super.writeExternal(out);
        out.writeInt(this._size);
        int i = this._set.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (this._set[i] != REMOVED && this._set[i] != FREE) {
                    out.writeObject(this._set[i]);
                }
            } else {
                return;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // gnu.trove.impl.hash.TCustomObjectHash, gnu.trove.impl.hash.TObjectHash, gnu.trove.impl.hash.THash, java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        byte version = in.readByte();
        if (version != 0) {
            super.readExternal(in);
        }
        int size = in.readInt();
        setUp(size);
        while (true) {
            int i = size;
            size--;
            if (i > 0) {
                add(in.readObject());
            } else {
                return;
            }
        }
    }
}
