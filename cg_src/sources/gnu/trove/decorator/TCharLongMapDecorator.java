package gnu.trove.decorator;

import gnu.trove.iterator.TCharLongIterator;
import gnu.trove.map.TCharLongMap;
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
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/decorator/TCharLongMapDecorator.class */
public class TCharLongMapDecorator extends AbstractMap<Character, Long> implements Map<Character, Long>, Externalizable, Cloneable {
    static final long serialVersionUID = 1;
    protected TCharLongMap _map;

    public TCharLongMapDecorator() {
    }

    public TCharLongMapDecorator(TCharLongMap map) {
        this._map = map;
    }

    public TCharLongMap getMap() {
        return this._map;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Long put(Character key, Long value) {
        char k;
        long v;
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
        long retval = this._map.put(k, v);
        if (retval == this._map.getNoEntryValue()) {
            return null;
        }
        return wrapValue(retval);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.AbstractMap, java.util.Map
    public Long get(Object key) {
        char k;
        if (key != null) {
            if (key instanceof Character) {
                k = unwrapKey(key);
            } else {
                return null;
            }
        } else {
            k = this._map.getNoEntryKey();
        }
        long v = this._map.get(k);
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
    public Long remove(Object key) {
        char k;
        if (key != null) {
            if (key instanceof Character) {
                k = unwrapKey(key);
            } else {
                return null;
            }
        } else {
            k = this._map.getNoEntryKey();
        }
        long v = this._map.remove(k);
        if (v == this._map.getNoEntryValue()) {
            return null;
        }
        return wrapValue(v);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: gnu.trove.decorator.TCharLongMapDecorator$1  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/decorator/TCharLongMapDecorator$1.class */
    public class AnonymousClass1 extends AbstractSet<Map.Entry<Character, Long>> {
        AnonymousClass1() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return TCharLongMapDecorator.this._map.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return TCharLongMapDecorator.this.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            if (o instanceof Map.Entry) {
                Object k = ((Map.Entry) o).getKey();
                Object v = ((Map.Entry) o).getValue();
                return TCharLongMapDecorator.this.containsKey(k) && TCharLongMapDecorator.this.get(k).equals(v);
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<Character, Long>> iterator() {
            return new Iterator<Map.Entry<Character, Long>>() { // from class: gnu.trove.decorator.TCharLongMapDecorator.1.1
                private final TCharLongIterator it;

                {
                    this.it = TCharLongMapDecorator.this._map.iterator();
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.Iterator
                public Map.Entry<Character, Long> next() {
                    this.it.advance();
                    char ik = this.it.key();
                    final Character key = ik == TCharLongMapDecorator.this._map.getNoEntryKey() ? null : TCharLongMapDecorator.this.wrapKey(ik);
                    long iv = this.it.value();
                    final Long v = iv == TCharLongMapDecorator.this._map.getNoEntryValue() ? null : TCharLongMapDecorator.this.wrapValue(iv);
                    return new Map.Entry<Character, Long>() { // from class: gnu.trove.decorator.TCharLongMapDecorator.1.1.1
                        private Long val;

                        {
                            this.val = v;
                        }

                        @Override // java.util.Map.Entry
                        public boolean equals(Object o) {
                            return (o instanceof Map.Entry) && ((Map.Entry) o).getKey().equals(key) && ((Map.Entry) o).getValue().equals(this.val);
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.util.Map.Entry
                        public Character getKey() {
                            return key;
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.util.Map.Entry
                        public Long getValue() {
                            return this.val;
                        }

                        @Override // java.util.Map.Entry
                        public int hashCode() {
                            return key.hashCode() + this.val.hashCode();
                        }

                        @Override // java.util.Map.Entry
                        public Long setValue(Long value) {
                            this.val = value;
                            return TCharLongMapDecorator.this.put(key, value);
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
        public boolean add(Map.Entry<Character, Long> o) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            boolean modified = false;
            if (contains(o)) {
                Character key = (Character) ((Map.Entry) o).getKey();
                TCharLongMapDecorator.this._map.remove(TCharLongMapDecorator.this.unwrapKey(key));
                modified = true;
            }
            return modified;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean addAll(Collection<? extends Map.Entry<Character, Long>> c) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            TCharLongMapDecorator.this.clear();
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<Character, Long>> entrySet() {
        return new AnonymousClass1();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsValue(Object val) {
        return (val instanceof Long) && this._map.containsValue(unwrapValue(val));
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object key) {
        return key == null ? this._map.containsKey(this._map.getNoEntryKey()) : (key instanceof Character) && this._map.containsKey(unwrapKey(key));
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
    public void putAll(Map<? extends Character, ? extends Long> map) {
        Iterator<? extends Map.Entry<? extends Character, ? extends Long>> it = map.entrySet().iterator();
        int i = map.size();
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                Map.Entry<? extends Character, ? extends Long> e = it.next();
                put(e.getKey(), e.getValue());
            } else {
                return;
            }
        }
    }

    protected Character wrapKey(char k) {
        return Character.valueOf(k);
    }

    protected char unwrapKey(Object key) {
        return ((Character) key).charValue();
    }

    protected Long wrapValue(long k) {
        return Long.valueOf(k);
    }

    protected long unwrapValue(Object value) {
        return ((Long) value).longValue();
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        this._map = (TCharLongMap) in.readObject();
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(0);
        out.writeObject(this._map);
    }
}
