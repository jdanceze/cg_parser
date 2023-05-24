package gnu.trove.decorator;

import gnu.trove.iterator.TShortFloatIterator;
import gnu.trove.map.TShortFloatMap;
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
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/decorator/TShortFloatMapDecorator.class */
public class TShortFloatMapDecorator extends AbstractMap<Short, Float> implements Map<Short, Float>, Externalizable, Cloneable {
    static final long serialVersionUID = 1;
    protected TShortFloatMap _map;

    public TShortFloatMapDecorator() {
    }

    public TShortFloatMapDecorator(TShortFloatMap map) {
        this._map = map;
    }

    public TShortFloatMap getMap() {
        return this._map;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Float put(Short key, Float value) {
        short k;
        float v;
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
        float retval = this._map.put(k, v);
        if (retval == this._map.getNoEntryValue()) {
            return null;
        }
        return wrapValue(retval);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.AbstractMap, java.util.Map
    public Float get(Object key) {
        short k;
        if (key != null) {
            if (key instanceof Short) {
                k = unwrapKey(key);
            } else {
                return null;
            }
        } else {
            k = this._map.getNoEntryKey();
        }
        float v = this._map.get(k);
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
    public Float remove(Object key) {
        short k;
        if (key != null) {
            if (key instanceof Short) {
                k = unwrapKey(key);
            } else {
                return null;
            }
        } else {
            k = this._map.getNoEntryKey();
        }
        float v = this._map.remove(k);
        if (v == this._map.getNoEntryValue()) {
            return null;
        }
        return wrapValue(v);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: gnu.trove.decorator.TShortFloatMapDecorator$1  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/decorator/TShortFloatMapDecorator$1.class */
    public class AnonymousClass1 extends AbstractSet<Map.Entry<Short, Float>> {
        AnonymousClass1() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return TShortFloatMapDecorator.this._map.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return TShortFloatMapDecorator.this.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            if (o instanceof Map.Entry) {
                Object k = ((Map.Entry) o).getKey();
                Object v = ((Map.Entry) o).getValue();
                return TShortFloatMapDecorator.this.containsKey(k) && TShortFloatMapDecorator.this.get(k).equals(v);
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<Short, Float>> iterator() {
            return new Iterator<Map.Entry<Short, Float>>() { // from class: gnu.trove.decorator.TShortFloatMapDecorator.1.1
                private final TShortFloatIterator it;

                {
                    this.it = TShortFloatMapDecorator.this._map.iterator();
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.Iterator
                public Map.Entry<Short, Float> next() {
                    this.it.advance();
                    short ik = this.it.key();
                    final Short key = ik == TShortFloatMapDecorator.this._map.getNoEntryKey() ? null : TShortFloatMapDecorator.this.wrapKey(ik);
                    float iv = this.it.value();
                    final Float v = iv == TShortFloatMapDecorator.this._map.getNoEntryValue() ? null : TShortFloatMapDecorator.this.wrapValue(iv);
                    return new Map.Entry<Short, Float>() { // from class: gnu.trove.decorator.TShortFloatMapDecorator.1.1.1
                        private Float val;

                        {
                            this.val = v;
                        }

                        @Override // java.util.Map.Entry
                        public boolean equals(Object o) {
                            return (o instanceof Map.Entry) && ((Map.Entry) o).getKey().equals(key) && ((Map.Entry) o).getValue().equals(this.val);
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.util.Map.Entry
                        public Short getKey() {
                            return key;
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.util.Map.Entry
                        public Float getValue() {
                            return this.val;
                        }

                        @Override // java.util.Map.Entry
                        public int hashCode() {
                            return key.hashCode() + this.val.hashCode();
                        }

                        @Override // java.util.Map.Entry
                        public Float setValue(Float value) {
                            this.val = value;
                            return TShortFloatMapDecorator.this.put(key, value);
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
        public boolean add(Map.Entry<Short, Float> o) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            boolean modified = false;
            if (contains(o)) {
                Short key = (Short) ((Map.Entry) o).getKey();
                TShortFloatMapDecorator.this._map.remove(TShortFloatMapDecorator.this.unwrapKey(key));
                modified = true;
            }
            return modified;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean addAll(Collection<? extends Map.Entry<Short, Float>> c) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            TShortFloatMapDecorator.this.clear();
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<Short, Float>> entrySet() {
        return new AnonymousClass1();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsValue(Object val) {
        return (val instanceof Float) && this._map.containsValue(unwrapValue(val));
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object key) {
        return key == null ? this._map.containsKey(this._map.getNoEntryKey()) : (key instanceof Short) && this._map.containsKey(unwrapKey(key));
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
    public void putAll(Map<? extends Short, ? extends Float> map) {
        Iterator<? extends Map.Entry<? extends Short, ? extends Float>> it = map.entrySet().iterator();
        int i = map.size();
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                Map.Entry<? extends Short, ? extends Float> e = it.next();
                put(e.getKey(), e.getValue());
            } else {
                return;
            }
        }
    }

    protected Short wrapKey(short k) {
        return Short.valueOf(k);
    }

    protected short unwrapKey(Object key) {
        return ((Short) key).shortValue();
    }

    protected Float wrapValue(float k) {
        return Float.valueOf(k);
    }

    protected float unwrapValue(Object value) {
        return ((Float) value).floatValue();
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        this._map = (TShortFloatMap) in.readObject();
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(0);
        out.writeObject(this._map);
    }
}
