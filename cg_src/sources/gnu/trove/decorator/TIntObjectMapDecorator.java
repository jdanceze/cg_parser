package gnu.trove.decorator;

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.TIntObjectMap;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/decorator/TIntObjectMapDecorator.class */
public class TIntObjectMapDecorator<V> extends AbstractMap<Integer, V> implements Map<Integer, V>, Externalizable, Cloneable {
    static final long serialVersionUID = 1;
    protected TIntObjectMap<V> _map;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractMap, java.util.Map
    public /* bridge */ /* synthetic */ Object put(Integer num, Object x1) {
        return put2(num, (Integer) x1);
    }

    public TIntObjectMapDecorator() {
    }

    public TIntObjectMapDecorator(TIntObjectMap<V> map) {
        this._map = map;
    }

    public TIntObjectMap<V> getMap() {
        return this._map;
    }

    /* renamed from: put  reason: avoid collision after fix types in other method */
    public V put2(Integer key, V value) {
        int k;
        if (key == null) {
            k = this._map.getNoEntryKey();
        } else {
            k = unwrapKey(key);
        }
        return this._map.put(k, value);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V get(Object key) {
        int k;
        if (key != null) {
            if (key instanceof Integer) {
                k = unwrapKey((Integer) key);
            } else {
                return null;
            }
        } else {
            k = this._map.getNoEntryKey();
        }
        return this._map.get(k);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        this._map.clear();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V remove(Object key) {
        int k;
        if (key != null) {
            if (key instanceof Integer) {
                k = unwrapKey((Integer) key);
            } else {
                return null;
            }
        } else {
            k = this._map.getNoEntryKey();
        }
        return this._map.remove(k);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: gnu.trove.decorator.TIntObjectMapDecorator$1  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/decorator/TIntObjectMapDecorator$1.class */
    public class AnonymousClass1 extends AbstractSet<Map.Entry<Integer, V>> {
        AnonymousClass1() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public /* bridge */ /* synthetic */ boolean add(Object x0) {
            return add((Map.Entry) ((Map.Entry) x0));
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return TIntObjectMapDecorator.this._map.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return TIntObjectMapDecorator.this.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            if (o instanceof Map.Entry) {
                Object k = ((Map.Entry) o).getKey();
                Object v = ((Map.Entry) o).getValue();
                return TIntObjectMapDecorator.this.containsKey(k) && TIntObjectMapDecorator.this.get(k).equals(v);
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<Integer, V>> iterator() {
            return new Iterator<Map.Entry<Integer, V>>() { // from class: gnu.trove.decorator.TIntObjectMapDecorator.1.1
                private final TIntObjectIterator<V> it;

                {
                    this.it = TIntObjectMapDecorator.this._map.iterator();
                }

                @Override // java.util.Iterator
                public Map.Entry<Integer, V> next() {
                    this.it.advance();
                    int k = this.it.key();
                    final Integer key = k == TIntObjectMapDecorator.this._map.getNoEntryKey() ? null : TIntObjectMapDecorator.this.wrapKey(k);
                    final V v = this.it.value();
                    return new Map.Entry<Integer, V>() { // from class: gnu.trove.decorator.TIntObjectMapDecorator.1.1.1
                        private V val;

                        {
                            this.val = (V) v;
                        }

                        @Override // java.util.Map.Entry
                        public boolean equals(Object o) {
                            return (o instanceof Map.Entry) && ((Map.Entry) o).getKey().equals(key) && ((Map.Entry) o).getValue().equals(this.val);
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.util.Map.Entry
                        public Integer getKey() {
                            return key;
                        }

                        @Override // java.util.Map.Entry
                        public V getValue() {
                            return this.val;
                        }

                        @Override // java.util.Map.Entry
                        public int hashCode() {
                            return key.hashCode() + this.val.hashCode();
                        }

                        @Override // java.util.Map.Entry
                        public V setValue(V value) {
                            this.val = value;
                            return (V) TIntObjectMapDecorator.this.put2(key, (Integer) value);
                        }
                    };
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.it.hasNext();
                }

                @Override // java.util.Iterator
                public void remove() {
                    this.it.remove();
                }
            };
        }

        public boolean add(Map.Entry<Integer, V> o) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            boolean modified = false;
            if (contains(o)) {
                Integer key = (Integer) ((Map.Entry) o).getKey();
                TIntObjectMapDecorator.this._map.remove(TIntObjectMapDecorator.this.unwrapKey(key));
                modified = true;
            }
            return modified;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean addAll(Collection<? extends Map.Entry<Integer, V>> c) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            TIntObjectMapDecorator.this.clear();
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<Integer, V>> entrySet() {
        return new AnonymousClass1();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsValue(Object val) {
        return this._map.containsValue(val);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object key) {
        return key == null ? this._map.containsKey(this._map.getNoEntryKey()) : (key instanceof Integer) && this._map.containsKey(((Integer) key).intValue());
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        return this._map.size();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void putAll(Map<? extends Integer, ? extends V> map) {
        Iterator<? extends Map.Entry<? extends Integer, ? extends V>> it = map.entrySet().iterator();
        int i = map.size();
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                Map.Entry<? extends Integer, ? extends V> e = it.next();
                put2(e.getKey(), (Integer) e.getValue());
            } else {
                return;
            }
        }
    }

    protected Integer wrapKey(int k) {
        return Integer.valueOf(k);
    }

    protected int unwrapKey(Integer key) {
        return key.intValue();
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        this._map = (TIntObjectMap) in.readObject();
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(0);
        out.writeObject(this._map);
    }
}
