package gnu.trove.map.hash;

import gnu.trove.function.TObjectFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.TCustomObjectHash;
import gnu.trove.iterator.hash.TObjectHashIterator;
import gnu.trove.map.TMap;
import gnu.trove.procedure.TObjectObjectProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.strategy.HashingStrategy;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Array;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TCustomHashMap.class */
public class TCustomHashMap<K, V> extends TCustomObjectHash<K> implements TMap<K, V>, Externalizable {
    static final long serialVersionUID = 1;
    protected transient V[] _values;

    public TCustomHashMap() {
    }

    public TCustomHashMap(HashingStrategy<? super K> strategy) {
        super(strategy);
    }

    public TCustomHashMap(HashingStrategy<? super K> strategy, int initialCapacity) {
        super(strategy, initialCapacity);
    }

    public TCustomHashMap(HashingStrategy<? super K> strategy, int initialCapacity, float loadFactor) {
        super(strategy, initialCapacity, loadFactor);
    }

    public TCustomHashMap(HashingStrategy<? super K> strategy, Map<? extends K, ? extends V> map) {
        this(strategy, map.size());
        putAll(map);
    }

    public TCustomHashMap(HashingStrategy<? super K> strategy, TCustomHashMap<? extends K, ? extends V> map) {
        this(strategy, map.size());
        putAll(map);
    }

    @Override // gnu.trove.impl.hash.TObjectHash, gnu.trove.impl.hash.THash
    public int setUp(int initialCapacity) {
        int capacity = super.setUp(initialCapacity);
        this._values = (V[]) new Object[capacity];
        return capacity;
    }

    @Override // java.util.Map
    public V put(K key, V value) {
        int index = insertKey(key);
        return doPut(value, index);
    }

    @Override // gnu.trove.map.TMap, java.util.Map
    public V putIfAbsent(K key, V value) {
        int index = insertKey(key);
        if (index < 0) {
            return this._values[(-index) - 1];
        }
        return doPut(value, index);
    }

    private V doPut(V value, int index) {
        V previous = null;
        boolean isNewMapping = true;
        if (index < 0) {
            index = (-index) - 1;
            previous = this._values[index];
            isNewMapping = false;
        }
        this._values[index] = value;
        if (isNewMapping) {
            postInsertHook(this.consumeFreeSlot);
        }
        return previous;
    }

    @Override // java.util.Map
    public boolean equals(Object other) {
        if (!(other instanceof Map)) {
            return false;
        }
        Map<K, V> that = (Map) other;
        if (that.size() != size()) {
            return false;
        }
        return forEachEntry(new EqProcedure(that));
    }

    @Override // java.util.Map
    public int hashCode() {
        TCustomHashMap<K, V>.HashProcedure p = new HashProcedure();
        forEachEntry(p);
        return p.getHashCode();
    }

    public String toString() {
        final StringBuilder buf = new StringBuilder("{");
        forEachEntry(new TObjectObjectProcedure<K, V>() { // from class: gnu.trove.map.hash.TCustomHashMap.1
            private boolean first = true;

            @Override // gnu.trove.procedure.TObjectObjectProcedure
            public boolean execute(K key, V value) {
                if (this.first) {
                    this.first = false;
                } else {
                    buf.append(", ");
                }
                buf.append(key);
                buf.append("=");
                buf.append(value);
                return true;
            }
        });
        buf.append("}");
        return buf.toString();
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TCustomHashMap$HashProcedure.class */
    private final class HashProcedure implements TObjectObjectProcedure<K, V> {
        private int h;

        private HashProcedure() {
            this.h = 0;
        }

        public int getHashCode() {
            return this.h;
        }

        @Override // gnu.trove.procedure.TObjectObjectProcedure
        public final boolean execute(K key, V value) {
            this.h += HashFunctions.hash(key) ^ (value == null ? 0 : value.hashCode());
            return true;
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TCustomHashMap$EqProcedure.class */
    private static final class EqProcedure<K, V> implements TObjectObjectProcedure<K, V> {
        private final Map<K, V> _otherMap;

        EqProcedure(Map<K, V> otherMap) {
            this._otherMap = otherMap;
        }

        @Override // gnu.trove.procedure.TObjectObjectProcedure
        public final boolean execute(K key, V value) {
            if (value == null && !this._otherMap.containsKey(key)) {
                return false;
            }
            V oValue = this._otherMap.get(key);
            return oValue == value || (oValue != null && oValue.equals(value));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // gnu.trove.map.TMap
    public boolean forEachKey(TObjectProcedure<? super K> procedure) {
        return forEach(procedure);
    }

    @Override // gnu.trove.map.TMap
    public boolean forEachValue(TObjectProcedure<? super V> procedure) {
        V[] values = this._values;
        Object[] set = this._set;
        int i = values.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (set[i] != FREE && set[i] != REMOVED && !procedure.execute((Object) values[i])) {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    @Override // gnu.trove.map.TMap
    public boolean forEachEntry(TObjectObjectProcedure<? super K, ? super V> procedure) {
        Object[] keys = this._set;
        V[] values = this._values;
        int i = keys.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (keys[i] != FREE && keys[i] != REMOVED && !procedure.execute(keys[i], (Object) values[i])) {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    @Override // gnu.trove.map.TMap
    public boolean retainEntries(TObjectObjectProcedure<? super K, ? super V> procedure) {
        boolean modified = false;
        Object[] keys = this._set;
        V[] values = this._values;
        tempDisableAutoCompaction();
        try {
            int i = keys.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (keys[i] != FREE && keys[i] != REMOVED && !procedure.execute(keys[i], (Object) values[i])) {
                        removeAt(i);
                        modified = true;
                    }
                } else {
                    return modified;
                }
            }
        } finally {
            reenableAutoCompaction(true);
        }
    }

    @Override // gnu.trove.map.TMap
    public void transformValues(TObjectFunction<V, V> function) {
        V[] values = this._values;
        Object[] set = this._set;
        int i = values.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (set[i] != FREE && set[i] != REMOVED) {
                    values[i] = function.execute(values[i]);
                }
            } else {
                return;
            }
        }
    }

    @Override // gnu.trove.impl.hash.THash
    protected void rehash(int newCapacity) {
        int oldCapacity = this._set.length;
        int oldSize = size();
        Object[] oldKeys = this._set;
        V[] oldVals = this._values;
        this._set = new Object[newCapacity];
        Arrays.fill(this._set, FREE);
        this._values = (V[]) new Object[newCapacity];
        int i = oldCapacity;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                Object o = oldKeys[i];
                if (o != FREE && o != REMOVED) {
                    int index = insertKey(o);
                    if (index < 0) {
                        throwObjectContractViolation(this._set[(-index) - 1], o, size(), oldSize, oldKeys);
                    }
                    this._values[index] = oldVals[i];
                }
            } else {
                return;
            }
        }
    }

    @Override // java.util.Map
    public V get(Object key) {
        int index = index(key);
        if (index < 0 || !this.strategy.equals(this._set[index], key)) {
            return null;
        }
        return this._values[index];
    }

    @Override // gnu.trove.impl.hash.THash, gnu.trove.map.TObjectByteMap
    public void clear() {
        if (size() == 0) {
            return;
        }
        super.clear();
        Arrays.fill(this._set, 0, this._set.length, FREE);
        Arrays.fill(this._values, 0, this._values.length, (Object) null);
    }

    @Override // java.util.Map
    public V remove(Object key) {
        V prev = null;
        int index = index(key);
        if (index >= 0) {
            prev = this._values[index];
            removeAt(index);
        }
        return prev;
    }

    @Override // gnu.trove.impl.hash.TObjectHash, gnu.trove.impl.hash.THash
    public void removeAt(int index) {
        this._values[index] = null;
        super.removeAt(index);
    }

    @Override // java.util.Map
    public Collection<V> values() {
        return new ValueView();
    }

    @Override // java.util.Map
    public Set<K> keySet() {
        return new KeyView();
    }

    @Override // java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        return new EntryView();
    }

    @Override // java.util.Map
    public boolean containsValue(Object val) {
        Object[] set = this._set;
        V[] vals = this._values;
        if (null == val) {
            int i = vals.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    if (set[i] != FREE && set[i] != REMOVED && val == vals[i]) {
                        return true;
                    }
                } else {
                    return false;
                }
            }
        } else {
            int i3 = vals.length;
            while (true) {
                int i4 = i3;
                i3--;
                if (i4 > 0) {
                    if (set[i3] != FREE && set[i3] != REMOVED && (val == vals[i3] || this.strategy.equals(val, vals[i3]))) {
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }
    }

    @Override // java.util.Map
    public boolean containsKey(Object key) {
        return contains(key);
    }

    @Override // java.util.Map
    public void putAll(Map<? extends K, ? extends V> map) {
        ensureCapacity(map.size());
        for (Map.Entry<? extends K, ? extends V> e : map.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TCustomHashMap$ValueView.class */
    protected class ValueView extends TCustomHashMap<K, V>.MapBackedView<V> {
        protected ValueView() {
            super();
        }

        @Override // gnu.trove.map.hash.TCustomHashMap.MapBackedView, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<V> iterator() {
            return new TObjectHashIterator(TCustomHashMap.this) { // from class: gnu.trove.map.hash.TCustomHashMap.ValueView.1
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // gnu.trove.iterator.hash.TObjectHashIterator, gnu.trove.impl.hash.THashIterator
                public V objectAtIndex(int index) {
                    return TCustomHashMap.this._values[index];
                }
            };
        }

        @Override // gnu.trove.map.hash.TCustomHashMap.MapBackedView
        public boolean containsElement(V value) {
            return TCustomHashMap.this.containsValue(value);
        }

        /* JADX WARN: Code restructure failed: missing block: B:15:0x0054, code lost:
            r4.this$0.removeAt(r8);
         */
        /* JADX WARN: Code restructure failed: missing block: B:16:0x005e, code lost:
            return true;
         */
        @Override // gnu.trove.map.hash.TCustomHashMap.MapBackedView
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public boolean removeElement(V r5) {
            /*
                r4 = this;
                r0 = r4
                gnu.trove.map.hash.TCustomHashMap r0 = gnu.trove.map.hash.TCustomHashMap.this
                V[] r0 = r0._values
                r6 = r0
                r0 = r4
                gnu.trove.map.hash.TCustomHashMap r0 = gnu.trove.map.hash.TCustomHashMap.this
                java.lang.Object[] r0 = r0._set
                r7 = r0
                r0 = r6
                int r0 = r0.length
                r8 = r0
            L14:
                r0 = r8
                int r8 = r8 + (-1)
                if (r0 <= 0) goto L5f
                r0 = r7
                r1 = r8
                r0 = r0[r1]
                java.lang.Object r1 = gnu.trove.impl.hash.TObjectHash.FREE
                if (r0 == r1) goto L38
                r0 = r7
                r1 = r8
                r0 = r0[r1]
                java.lang.Object r1 = gnu.trove.impl.hash.TObjectHash.REMOVED
                if (r0 == r1) goto L38
                r0 = r5
                r1 = r6
                r2 = r8
                r1 = r1[r2]
                if (r0 == r1) goto L54
            L38:
                r0 = 0
                r1 = r6
                r2 = r8
                r1 = r1[r2]
                if (r0 == r1) goto L14
                r0 = r4
                gnu.trove.map.hash.TCustomHashMap r0 = gnu.trove.map.hash.TCustomHashMap.this
                gnu.trove.strategy.HashingStrategy r0 = gnu.trove.map.hash.TCustomHashMap.access$200(r0)
                r1 = r6
                r2 = r8
                r1 = r1[r2]
                r2 = r5
                boolean r0 = r0.equals(r1, r2)
                if (r0 == 0) goto L14
            L54:
                r0 = r4
                gnu.trove.map.hash.TCustomHashMap r0 = gnu.trove.map.hash.TCustomHashMap.this
                r1 = r8
                r0.removeAt(r1)
                r0 = 1
                return r0
            L5f:
                r0 = 0
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: gnu.trove.map.hash.TCustomHashMap.ValueView.removeElement(java.lang.Object):boolean");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TCustomHashMap$EntryView.class */
    public class EntryView extends TCustomHashMap<K, V>.MapBackedView<Map.Entry<K, V>> {
        protected EntryView() {
            super();
        }

        @Override // gnu.trove.map.hash.TCustomHashMap.MapBackedView
        public /* bridge */ /* synthetic */ boolean containsElement(Object x0) {
            return containsElement((Map.Entry) ((Map.Entry) x0));
        }

        @Override // gnu.trove.map.hash.TCustomHashMap.MapBackedView
        public /* bridge */ /* synthetic */ boolean removeElement(Object x0) {
            return removeElement((Map.Entry) ((Map.Entry) x0));
        }

        /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TCustomHashMap$EntryView$EntryIterator.class */
        private final class EntryIterator extends TObjectHashIterator {
            EntryIterator(TCustomHashMap<K, V> map) {
                super(map);
            }

            @Override // gnu.trove.iterator.hash.TObjectHashIterator, gnu.trove.impl.hash.THashIterator
            public TCustomHashMap<K, V>.Entry objectAtIndex(int index) {
                return new Entry(TCustomHashMap.this._set[index], TCustomHashMap.this._values[index], index);
            }
        }

        @Override // gnu.trove.map.hash.TCustomHashMap.MapBackedView, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator(TCustomHashMap.this);
        }

        public boolean removeElement(Map.Entry<K, V> entry) {
            int index = TCustomHashMap.this.index(keyForEntry(entry));
            if (index >= 0) {
                Object val = valueForEntry(entry);
                if (val == TCustomHashMap.this._values[index] || (null != val && TCustomHashMap.this.strategy.equals(val, TCustomHashMap.this._values[index]))) {
                    TCustomHashMap.this.removeAt(index);
                    return true;
                }
                return false;
            }
            return false;
        }

        public boolean containsElement(Map.Entry<K, V> entry) {
            Object val = TCustomHashMap.this.get(keyForEntry(entry));
            V value = entry.getValue();
            return value == val || (null != val && TCustomHashMap.this.strategy.equals(val, value));
        }

        protected V valueForEntry(Map.Entry<K, V> entry) {
            return entry.getValue();
        }

        protected K keyForEntry(Map.Entry<K, V> entry) {
            return entry.getKey();
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TCustomHashMap$MapBackedView.class */
    private abstract class MapBackedView<E> extends AbstractSet<E> implements Set<E>, Iterable<E> {
        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public abstract Iterator<E> iterator();

        public abstract boolean removeElement(E e);

        public abstract boolean containsElement(E e);

        private MapBackedView() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object key) {
            return containsElement(key);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            return removeElement(o);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            TCustomHashMap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean add(E obj) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return TCustomHashMap.this.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public Object[] toArray() {
            Object[] result = new Object[size()];
            Iterator<E> e = iterator();
            int i = 0;
            while (e.hasNext()) {
                result[i] = e.next();
                i++;
            }
            return result;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v19, types: [java.lang.Object[]] */
        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public <T> T[] toArray(T[] a) {
            int size = size();
            if (a.length < size) {
                a = (Object[]) Array.newInstance(a.getClass().getComponentType(), size);
            }
            Iterator<E> it = iterator();
            Object[] result = a;
            for (int i = 0; i < size; i++) {
                result[i] = it.next();
            }
            if (a.length > size) {
                a[size] = null;
            }
            return a;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return TCustomHashMap.this.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean addAll(Collection<? extends E> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean retainAll(Collection<?> collection) {
            boolean changed = false;
            Iterator<E> i = iterator();
            while (i.hasNext()) {
                if (!collection.contains(i.next())) {
                    i.remove();
                    changed = true;
                }
            }
            return changed;
        }

        @Override // java.util.AbstractCollection
        public String toString() {
            Iterator<E> i = iterator();
            if (!i.hasNext()) {
                return "{}";
            }
            StringBuilder sb = new StringBuilder();
            sb.append('{');
            while (true) {
                E e = i.next();
                sb.append(e == this ? "(this Collection)" : e);
                if (!i.hasNext()) {
                    return sb.append('}').toString();
                }
                sb.append(", ");
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TCustomHashMap$KeyView.class */
    protected class KeyView extends TCustomHashMap<K, V>.MapBackedView<K> {
        protected KeyView() {
            super();
        }

        @Override // gnu.trove.map.hash.TCustomHashMap.MapBackedView, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<K> iterator() {
            return new TObjectHashIterator(TCustomHashMap.this);
        }

        @Override // gnu.trove.map.hash.TCustomHashMap.MapBackedView
        public boolean removeElement(K key) {
            return null != TCustomHashMap.this.remove(key);
        }

        @Override // gnu.trove.map.hash.TCustomHashMap.MapBackedView
        public boolean containsElement(K key) {
            return TCustomHashMap.this.contains(key);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/map/hash/TCustomHashMap$Entry.class */
    public final class Entry implements Map.Entry<K, V> {
        private K key;
        private V val;
        private final int index;

        Entry(K key, V value, int index) {
            this.key = key;
            this.val = value;
            this.index = index;
        }

        @Override // java.util.Map.Entry
        public K getKey() {
            return this.key;
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            return this.val;
        }

        @Override // java.util.Map.Entry
        public V setValue(V o) {
            if (TCustomHashMap.this._values[this.index] != this.val) {
                throw new ConcurrentModificationException();
            }
            V retval = this.val;
            TCustomHashMap.this._values[this.index] = o;
            this.val = o;
            return retval;
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object o) {
            if (o instanceof Map.Entry) {
                Map.Entry e2 = (Map.Entry) o;
                if (getKey() != null ? TCustomHashMap.this.strategy.equals(getKey(), e2.getKey()) : e2.getKey() == null) {
                    if (getValue() != null ? getValue().equals(e2.getValue()) : e2.getValue() == null) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return (getKey() == null ? 0 : getKey().hashCode()) ^ (getValue() == null ? 0 : getValue().hashCode());
        }

        public String toString() {
            return this.key + "=" + this.val;
        }
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
                    out.writeObject(this._values[i]);
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
                put(in.readObject(), in.readObject());
            } else {
                return;
            }
        }
    }
}
