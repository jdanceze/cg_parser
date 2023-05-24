package gnu.trove.decorator;

import gnu.trove.iterator.TFloatIntIterator;
import gnu.trove.map.TFloatIntMap;
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
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/decorator/TFloatIntMapDecorator.class */
public class TFloatIntMapDecorator extends AbstractMap<Float, Integer> implements Map<Float, Integer>, Externalizable, Cloneable {
    static final long serialVersionUID = 1;
    protected TFloatIntMap _map;

    public TFloatIntMapDecorator() {
    }

    public TFloatIntMapDecorator(TFloatIntMap map) {
        this._map = map;
    }

    public TFloatIntMap getMap() {
        return this._map;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Integer put(Float key, Integer value) {
        float k;
        int v;
        if (key == null) {
            k = this._map.getNoEntryKey();
        } else {
            k = unwrapKey(key);
        }
        if (value == null) {
            v = this._map.getNoEntryValue();
        } else {
            v = unwrapValue(value);
        }
        int retval = this._map.put(k, v);
        if (retval == this._map.getNoEntryValue()) {
            return null;
        }
        return wrapValue(retval);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.AbstractMap, java.util.Map
    public Integer get(Object key) {
        float k;
        if (key != null) {
            if (key instanceof Float) {
                k = unwrapKey(key);
            } else {
                return null;
            }
        } else {
            k = this._map.getNoEntryKey();
        }
        int v = this._map.get(k);
        if (v == this._map.getNoEntryValue()) {
            return null;
        }
        return wrapValue(v);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        this._map.clear();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.AbstractMap, java.util.Map
    public Integer remove(Object key) {
        float k;
        if (key != null) {
            if (key instanceof Float) {
                k = unwrapKey(key);
            } else {
                return null;
            }
        } else {
            k = this._map.getNoEntryKey();
        }
        int v = this._map.remove(k);
        if (v == this._map.getNoEntryValue()) {
            return null;
        }
        return wrapValue(v);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: gnu.trove.decorator.TFloatIntMapDecorator$1  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/decorator/TFloatIntMapDecorator$1.class */
    public class AnonymousClass1 extends AbstractSet<Map.Entry<Float, Integer>> {
        AnonymousClass1() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return TFloatIntMapDecorator.this._map.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return TFloatIntMapDecorator.this.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            if (o instanceof Map.Entry) {
                Object k = ((Map.Entry) o).getKey();
                Object v = ((Map.Entry) o).getValue();
                return TFloatIntMapDecorator.this.containsKey(k) && TFloatIntMapDecorator.this.get(k).equals(v);
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<Float, Integer>> iterator() {
            return new Iterator<Map.Entry<Float, Integer>>() { // from class: gnu.trove.decorator.TFloatIntMapDecorator.1.1
                private final TFloatIntIterator it;

                {
                    this.it = TFloatIntMapDecorator.this._map.iterator();
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.Iterator
                public Map.Entry<Float, Integer> next() {
                    this.it.advance();
                    float ik = this.it.key();
                    final Float key = ik == TFloatIntMapDecorator.this._map.getNoEntryKey() ? null : TFloatIntMapDecorator.this.wrapKey(ik);
                    int iv = this.it.value();
                    final Integer v = iv == TFloatIntMapDecorator.this._map.getNoEntryValue() ? null : TFloatIntMapDecorator.this.wrapValue(iv);
                    return new Map.Entry<Float, Integer>() { // from class: gnu.trove.decorator.TFloatIntMapDecorator.1.1.1
                        private Integer val;

                        {
                            this.val = v;
                        }

                        @Override // java.util.Map.Entry
                        public boolean equals(Object o) {
                            return (o instanceof Map.Entry) && ((Map.Entry) o).getKey().equals(key) && ((Map.Entry) o).getValue().equals(this.val);
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.util.Map.Entry
                        public Float getKey() {
                            return key;
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.util.Map.Entry
                        public Integer getValue() {
                            return this.val;
                        }

                        @Override // java.util.Map.Entry
                        public int hashCode() {
                            return key.hashCode() + this.val.hashCode();
                        }

                        @Override // java.util.Map.Entry
                        public Integer setValue(Integer value) {
                            this.val = value;
                            return TFloatIntMapDecorator.this.put(key, value);
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

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean add(Map.Entry<Float, Integer> o) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            boolean modified = false;
            if (contains(o)) {
                Float key = (Float) ((Map.Entry) o).getKey();
                TFloatIntMapDecorator.this._map.remove(TFloatIntMapDecorator.this.unwrapKey(key));
                modified = true;
            }
            return modified;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean addAll(Collection<? extends Map.Entry<Float, Integer>> c) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            TFloatIntMapDecorator.this.clear();
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<Float, Integer>> entrySet() {
        return new AnonymousClass1();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsValue(Object val) {
        return (val instanceof Integer) && this._map.containsValue(unwrapValue(val));
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object key) {
        return key == null ? this._map.containsKey(this._map.getNoEntryKey()) : (key instanceof Float) && this._map.containsKey(unwrapKey(key));
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
    public void putAll(Map<? extends Float, ? extends Integer> map) {
        Iterator<? extends Map.Entry<? extends Float, ? extends Integer>> it = map.entrySet().iterator();
        int i = map.size();
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                Map.Entry<? extends Float, ? extends Integer> e = it.next();
                put(e.getKey(), e.getValue());
            } else {
                return;
            }
        }
    }

    protected Float wrapKey(float k) {
        return Float.valueOf(k);
    }

    protected float unwrapKey(Object key) {
        return ((Float) key).floatValue();
    }

    protected Integer wrapValue(int k) {
        return Integer.valueOf(k);
    }

    protected int unwrapValue(Object value) {
        return ((Integer) value).intValue();
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        this._map = (TFloatIntMap) in.readObject();
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(0);
        out.writeObject(this._map);
    }
}
