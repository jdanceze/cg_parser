package gnu.trove.decorator;

import gnu.trove.iterator.TShortDoubleIterator;
import gnu.trove.map.TShortDoubleMap;
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
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/decorator/TShortDoubleMapDecorator.class */
public class TShortDoubleMapDecorator extends AbstractMap<Short, Double> implements Map<Short, Double>, Externalizable, Cloneable {
    static final long serialVersionUID = 1;
    protected TShortDoubleMap _map;

    public TShortDoubleMapDecorator() {
    }

    public TShortDoubleMapDecorator(TShortDoubleMap map) {
        this._map = map;
    }

    public TShortDoubleMap getMap() {
        return this._map;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Double put(Short key, Double value) {
        short k;
        double v;
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
        double retval = this._map.put(k, v);
        if (retval == this._map.getNoEntryValue()) {
            return null;
        }
        return wrapValue(retval);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.AbstractMap, java.util.Map
    public Double get(Object key) {
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
        double v = this._map.get(k);
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
    public Double remove(Object key) {
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
        double v = this._map.remove(k);
        if (v == this._map.getNoEntryValue()) {
            return null;
        }
        return wrapValue(v);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: gnu.trove.decorator.TShortDoubleMapDecorator$1  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/decorator/TShortDoubleMapDecorator$1.class */
    public class AnonymousClass1 extends AbstractSet<Map.Entry<Short, Double>> {
        AnonymousClass1() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return TShortDoubleMapDecorator.this._map.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return TShortDoubleMapDecorator.this.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            if (o instanceof Map.Entry) {
                Object k = ((Map.Entry) o).getKey();
                Object v = ((Map.Entry) o).getValue();
                return TShortDoubleMapDecorator.this.containsKey(k) && TShortDoubleMapDecorator.this.get(k).equals(v);
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<Short, Double>> iterator() {
            return new Iterator<Map.Entry<Short, Double>>() { // from class: gnu.trove.decorator.TShortDoubleMapDecorator.1.1
                private final TShortDoubleIterator it;

                {
                    this.it = TShortDoubleMapDecorator.this._map.iterator();
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.Iterator
                public Map.Entry<Short, Double> next() {
                    this.it.advance();
                    short ik = this.it.key();
                    final Short key = ik == TShortDoubleMapDecorator.this._map.getNoEntryKey() ? null : TShortDoubleMapDecorator.this.wrapKey(ik);
                    double iv = this.it.value();
                    final Double v = iv == TShortDoubleMapDecorator.this._map.getNoEntryValue() ? null : TShortDoubleMapDecorator.this.wrapValue(iv);
                    return new Map.Entry<Short, Double>() { // from class: gnu.trove.decorator.TShortDoubleMapDecorator.1.1.1
                        private Double val;

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
                        public Double getValue() {
                            return this.val;
                        }

                        @Override // java.util.Map.Entry
                        public int hashCode() {
                            return key.hashCode() + this.val.hashCode();
                        }

                        @Override // java.util.Map.Entry
                        public Double setValue(Double value) {
                            this.val = value;
                            return TShortDoubleMapDecorator.this.put(key, value);
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
        public boolean add(Map.Entry<Short, Double> o) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            boolean modified = false;
            if (contains(o)) {
                Short key = (Short) ((Map.Entry) o).getKey();
                TShortDoubleMapDecorator.this._map.remove(TShortDoubleMapDecorator.this.unwrapKey(key));
                modified = true;
            }
            return modified;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean addAll(Collection<? extends Map.Entry<Short, Double>> c) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            TShortDoubleMapDecorator.this.clear();
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<Short, Double>> entrySet() {
        return new AnonymousClass1();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsValue(Object val) {
        return (val instanceof Double) && this._map.containsValue(unwrapValue(val));
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
    public void putAll(Map<? extends Short, ? extends Double> map) {
        Iterator<? extends Map.Entry<? extends Short, ? extends Double>> it = map.entrySet().iterator();
        int i = map.size();
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                Map.Entry<? extends Short, ? extends Double> e = it.next();
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

    protected Double wrapValue(double k) {
        return Double.valueOf(k);
    }

    protected double unwrapValue(Object value) {
        return ((Double) value).doubleValue();
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        this._map = (TShortDoubleMap) in.readObject();
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(0);
        out.writeObject(this._map);
    }
}
