package gnu.trove.decorator;

import gnu.trove.iterator.TByteObjectIterator;
import gnu.trove.map.TByteObjectMap;
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
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/decorator/TByteObjectMapDecorator.class */
public class TByteObjectMapDecorator<V> extends AbstractMap<Byte, V> implements Map<Byte, V>, Externalizable, Cloneable {
    static final long serialVersionUID = 1;
    protected TByteObjectMap<V> _map;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractMap, java.util.Map
    public /* bridge */ /* synthetic */ Object put(Byte b, Object x1) {
        return put2(b, (Byte) x1);
    }

    public TByteObjectMapDecorator() {
    }

    public TByteObjectMapDecorator(TByteObjectMap<V> map) {
        this._map = map;
    }

    public TByteObjectMap<V> getMap() {
        return this._map;
    }

    /* renamed from: put  reason: avoid collision after fix types in other method */
    public V put2(Byte key, V value) {
        byte k;
        if (key == null) {
            k = this._map.getNoEntryKey();
        } else {
            k = unwrapKey(key);
        }
        return this._map.put(k, value);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V get(Object key) {
        byte k;
        if (key != null) {
            if (key instanceof Byte) {
                k = unwrapKey((Byte) key);
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
        byte k;
        if (key != null) {
            if (key instanceof Byte) {
                k = unwrapKey((Byte) key);
            } else {
                return null;
            }
        } else {
            k = this._map.getNoEntryKey();
        }
        return this._map.remove(k);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: gnu.trove.decorator.TByteObjectMapDecorator$1  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/decorator/TByteObjectMapDecorator$1.class */
    public class AnonymousClass1 extends AbstractSet<Map.Entry<Byte, V>> {
        AnonymousClass1() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public /* bridge */ /* synthetic */ boolean add(Object x0) {
            return add((Map.Entry) ((Map.Entry) x0));
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return TByteObjectMapDecorator.this._map.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return TByteObjectMapDecorator.this.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            if (o instanceof Map.Entry) {
                Object k = ((Map.Entry) o).getKey();
                Object v = ((Map.Entry) o).getValue();
                return TByteObjectMapDecorator.this.containsKey(k) && TByteObjectMapDecorator.this.get(k).equals(v);
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<Byte, V>> iterator() {
            return new Iterator<Map.Entry<Byte, V>>() { // from class: gnu.trove.decorator.TByteObjectMapDecorator.1.1
                private final TByteObjectIterator<V> it;

                {
                    this.it = TByteObjectMapDecorator.this._map.iterator();
                }

                @Override // java.util.Iterator
                public Map.Entry<Byte, V> next() {
                    this.it.advance();
                    byte k = this.it.key();
                    final Byte key = k == TByteObjectMapDecorator.this._map.getNoEntryKey() ? null : TByteObjectMapDecorator.this.wrapKey(k);
                    final V v = this.it.value();
                    return new Map.Entry<Byte, V>() { // from class: gnu.trove.decorator.TByteObjectMapDecorator.1.1.1
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
                        public Byte getKey() {
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
                            return (V) TByteObjectMapDecorator.this.put2(key, (Byte) value);
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

        public boolean add(Map.Entry<Byte, V> o) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            boolean modified = false;
            if (contains(o)) {
                Byte key = (Byte) ((Map.Entry) o).getKey();
                TByteObjectMapDecorator.this._map.remove(TByteObjectMapDecorator.this.unwrapKey(key));
                modified = true;
            }
            return modified;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean addAll(Collection<? extends Map.Entry<Byte, V>> c) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            TByteObjectMapDecorator.this.clear();
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<Byte, V>> entrySet() {
        return new AnonymousClass1();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsValue(Object val) {
        return this._map.containsValue(val);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object key) {
        return key == null ? this._map.containsKey(this._map.getNoEntryKey()) : (key instanceof Byte) && this._map.containsKey(((Byte) key).byteValue());
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
    public void putAll(Map<? extends Byte, ? extends V> map) {
        Iterator<? extends Map.Entry<? extends Byte, ? extends V>> it = map.entrySet().iterator();
        int i = map.size();
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                Map.Entry<? extends Byte, ? extends V> e = it.next();
                put2(e.getKey(), (Byte) e.getValue());
            } else {
                return;
            }
        }
    }

    protected Byte wrapKey(byte k) {
        return Byte.valueOf(k);
    }

    protected byte unwrapKey(Byte key) {
        return key.byteValue();
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        this._map = (TByteObjectMap) in.readObject();
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(0);
        out.writeObject(this._map);
    }
}
