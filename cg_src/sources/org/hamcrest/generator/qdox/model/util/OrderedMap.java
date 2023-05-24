package org.hamcrest.generator.qdox.model.util;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/util/OrderedMap.class */
public class OrderedMap extends AbstractMap {
    private Set _entrySet = new OrderedSet();

    @Override // java.util.AbstractMap, java.util.Map
    public Set entrySet() {
        return this._entrySet;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Object put(Object key, Object value) {
        Entry existingEntry = getEntryWithKey(key);
        if (existingEntry == null) {
            entrySet().add(new Entry(key, value));
            return null;
        }
        Object previousValue = existingEntry.getValue();
        existingEntry.setValue(value);
        return previousValue;
    }

    private Entry getEntryWithKey(Object key) {
        for (Entry e : entrySet()) {
            if (eq(e.getKey(), key)) {
                return e;
            }
        }
        return null;
    }

    /* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/util/OrderedMap$OrderedSet.class */
    static class OrderedSet extends AbstractSet {
        private List _elementList = new LinkedList();

        OrderedSet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this._elementList.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator iterator() {
            return this._elementList.iterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean add(Object o) {
            this._elementList.add(o);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/util/OrderedMap$Entry.class */
    public static class Entry implements Map.Entry {
        Object _key;
        Object _value;

        public Entry(Object key, Object value) {
            this._key = key;
            this._value = value;
        }

        @Override // java.util.Map.Entry
        public Object getKey() {
            return this._key;
        }

        @Override // java.util.Map.Entry
        public Object getValue() {
            return this._value;
        }

        @Override // java.util.Map.Entry
        public Object setValue(Object value) {
            Object oldValue = this._value;
            this._value = value;
            return oldValue;
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e = (Map.Entry) o;
            return OrderedMap.eq(this._key, e.getKey()) && OrderedMap.eq(this._value, e.getValue());
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return (this._key == null ? 0 : this._key.hashCode()) ^ (this._value == null ? 0 : this._value.hashCode());
        }

        public String toString() {
            return new StringBuffer().append(this._key).append("=").append(this._value).toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean eq(Object o1, Object o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }
}
