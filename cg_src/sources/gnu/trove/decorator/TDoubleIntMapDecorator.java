package gnu.trove.decorator;

import gnu.trove.iterator.TDoubleIntIterator;
import gnu.trove.map.TDoubleIntMap;
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
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/decorator/TDoubleIntMapDecorator.class */
public class TDoubleIntMapDecorator extends AbstractMap<Double, Integer> implements Map<Double, Integer>, Externalizable, Cloneable {
    static final long serialVersionUID = 1;
    protected TDoubleIntMap _map;

    public TDoubleIntMapDecorator() {
    }

    public TDoubleIntMapDecorator(TDoubleIntMap map) {
        this._map = map;
    }

    public TDoubleIntMap getMap() {
        return this._map;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Integer put(Double key, Integer value) {
        double k;
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
        double k;
        if (key != null) {
            if (key instanceof Double) {
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
        double k;
        if (key != null) {
            if (key instanceof Double) {
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
    /* renamed from: gnu.trove.decorator.TDoubleIntMapDecorator$1  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/decorator/TDoubleIntMapDecorator$1.class */
    public class AnonymousClass1 extends AbstractSet<Map.Entry<Double, Integer>> {
        AnonymousClass1() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return TDoubleIntMapDecorator.this._map.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return TDoubleIntMapDecorator.this.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            if (o instanceof Map.Entry) {
                Object k = ((Map.Entry) o).getKey();
                Object v = ((Map.Entry) o).getValue();
                return TDoubleIntMapDecorator.this.containsKey(k) && TDoubleIntMapDecorator.this.get(k).equals(v);
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<Double, Integer>> iterator() {
            return new Iterator<Map.Entry<Double, Integer>>() { // from class: gnu.trove.decorator.TDoubleIntMapDecorator.1.1
                private final TDoubleIntIterator it;

                {
                    this.it = TDoubleIntMapDecorator.this._map.iterator();
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.Iterator
                public Map.Entry<Double, Integer> next() {
                    this.it.advance();
                    double ik = this.it.key();
                    final Double key = ik == TDoubleIntMapDecorator.this._map.getNoEntryKey() ? null : TDoubleIntMapDecorator.this.wrapKey(ik);
                    int iv = this.it.value();
                    final Integer v = iv == TDoubleIntMapDecorator.this._map.getNoEntryValue() ? null : TDoubleIntMapDecorator.this.wrapValue(iv);
                    return new Map.Entry<Double, Integer>() { // from class: gnu.trove.decorator.TDoubleIntMapDecorator.1.1.1
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
                        public Double getKey() {
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
                            return TDoubleIntMapDecorator.this.put(key, value);
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
        public boolean add(Map.Entry<Double, Integer> o) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            boolean modified = false;
            if (contains(o)) {
                Double key = (Double) ((Map.Entry) o).getKey();
                TDoubleIntMapDecorator.this._map.remove(TDoubleIntMapDecorator.this.unwrapKey(key));
                modified = true;
            }
            return modified;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean addAll(Collection<? extends Map.Entry<Double, Integer>> c) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            TDoubleIntMapDecorator.this.clear();
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<Double, Integer>> entrySet() {
        return new AnonymousClass1();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsValue(Object val) {
        return (val instanceof Integer) && this._map.containsValue(unwrapValue(val));
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object key) {
        return key == null ? this._map.containsKey(this._map.getNoEntryKey()) : (key instanceof Double) && this._map.containsKey(unwrapKey(key));
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
    public void putAll(Map<? extends Double, ? extends Integer> map) {
        Iterator<? extends Map.Entry<? extends Double, ? extends Integer>> it = map.entrySet().iterator();
        int i = map.size();
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                Map.Entry<? extends Double, ? extends Integer> e = it.next();
                put(e.getKey(), e.getValue());
            } else {
                return;
            }
        }
    }

    protected Double wrapKey(double k) {
        return Double.valueOf(k);
    }

    protected double unwrapKey(Object key) {
        return ((Double) key).doubleValue();
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
        this._map = (TDoubleIntMap) in.readObject();
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(0);
        out.writeObject(this._map);
    }
}
