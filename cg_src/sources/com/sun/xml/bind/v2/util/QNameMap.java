package com.sun.xml.bind.v2.util;

import com.sun.xml.bind.v2.runtime.Name;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.xml.namespace.QName;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/util/QNameMap.class */
public final class QNameMap<V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int MAXIMUM_CAPACITY = 1073741824;
    transient Entry<V>[] table;
    transient int size;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final /* synthetic */ boolean $assertionsDisabled;
    private Set<Entry<V>> entrySet = null;
    private int threshold = 12;

    static {
        $assertionsDisabled = !QNameMap.class.desiredAssertionStatus();
    }

    public QNameMap() {
        this.table = new Entry[16];
        this.table = new Entry[16];
    }

    public void put(String namespaceUri, String localname, V value) {
        if (!$assertionsDisabled && localname == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && namespaceUri == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && localname != localname.intern()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && namespaceUri != namespaceUri.intern()) {
            throw new AssertionError();
        }
        int hash = hash(localname);
        int i = indexFor(hash, this.table.length);
        Entry<V> entry = this.table[i];
        while (true) {
            Entry<V> e = entry;
            if (e != null) {
                if (e.hash != hash || localname != e.localName || namespaceUri != e.nsUri) {
                    entry = e.next;
                } else {
                    e.value = value;
                    return;
                }
            } else {
                addEntry(hash, namespaceUri, localname, value, i);
                return;
            }
        }
    }

    public void put(QName name, V value) {
        put(name.getNamespaceURI(), name.getLocalPart(), value);
    }

    public void put(Name name, V value) {
        put(name.nsUri, name.localName, value);
    }

    public V get(String nsUri, String localPart) {
        Entry<V> e = getEntry(nsUri, localPart);
        if (e == null) {
            return null;
        }
        return e.value;
    }

    public V get(QName name) {
        return get(name.getNamespaceURI(), name.getLocalPart());
    }

    public int size() {
        return this.size;
    }

    public QNameMap<V> putAll(QNameMap<? extends V> map) {
        int newCapacity;
        int numKeysToBeAdded = map.size();
        if (numKeysToBeAdded == 0) {
            return this;
        }
        if (numKeysToBeAdded > this.threshold) {
            int targetCapacity = numKeysToBeAdded;
            if (targetCapacity > 1073741824) {
                targetCapacity = 1073741824;
            }
            int length = this.table.length;
            while (true) {
                newCapacity = length;
                if (newCapacity >= targetCapacity) {
                    break;
                }
                length = newCapacity << 1;
            }
            if (newCapacity > this.table.length) {
                resize(newCapacity);
            }
        }
        for (Entry<? extends V> e : map.entrySet()) {
            put(e.nsUri, e.localName, e.getValue());
        }
        return this;
    }

    private static int hash(String x) {
        int h = x.hashCode();
        int h2 = h + ((h << 9) ^ (-1));
        int h3 = h2 ^ (h2 >>> 14);
        int h4 = h3 + (h3 << 4);
        return h4 ^ (h4 >>> 10);
    }

    private static int indexFor(int h, int length) {
        return h & (length - 1);
    }

    private void addEntry(int hash, String nsUri, String localName, V value, int bucketIndex) {
        Entry<V> e = this.table[bucketIndex];
        this.table[bucketIndex] = new Entry<>(hash, nsUri, localName, value, e);
        int i = this.size;
        this.size = i + 1;
        if (i >= this.threshold) {
            resize(2 * this.table.length);
        }
    }

    private void resize(int newCapacity) {
        Entry[] oldTable = this.table;
        int oldCapacity = oldTable.length;
        if (oldCapacity == 1073741824) {
            this.threshold = Integer.MAX_VALUE;
            return;
        }
        Entry[] newTable = new Entry[newCapacity];
        transfer(newTable);
        this.table = newTable;
        this.threshold = newCapacity;
    }

    private void transfer(Entry<V>[] newTable) {
        Entry<V>[] src = this.table;
        int newCapacity = newTable.length;
        for (int j = 0; j < src.length; j++) {
            Entry<V> e = src[j];
            if (e != null) {
                src[j] = null;
                do {
                    Entry<V> next = e.next;
                    int i = indexFor(e.hash, newCapacity);
                    e.next = newTable[i];
                    newTable[i] = e;
                    e = next;
                } while (e != null);
            }
        }
    }

    public Entry<V> getOne() {
        Entry<V>[] entryArr;
        for (Entry<V> e : this.table) {
            if (e != null) {
                return e;
            }
        }
        return null;
    }

    public Collection<QName> keySet() {
        Set<QName> r = new HashSet<>();
        for (Entry<V> e : entrySet()) {
            r.add(e.createQName());
        }
        return r;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/util/QNameMap$HashIterator.class */
    public abstract class HashIterator<E> implements Iterator<E> {
        Entry<V> next;
        int index;

        HashIterator() {
            Entry<V>[] t = QNameMap.this.table;
            int i = t.length;
            Entry<V> n = null;
            if (QNameMap.this.size != 0) {
                while (i > 0) {
                    i--;
                    Entry<V> entry = t[i];
                    n = entry;
                    if (entry != null) {
                        break;
                    }
                }
            }
            this.next = n;
            this.index = i;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.next != null;
        }

        Entry<V> nextEntry() {
            Entry<V> e = this.next;
            if (e == null) {
                throw new NoSuchElementException();
            }
            Entry<V> n = e.next;
            Entry<V>[] t = QNameMap.this.table;
            int i = this.index;
            while (n == null && i > 0) {
                i--;
                n = t[i];
            }
            this.index = i;
            this.next = n;
            return e;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public boolean containsKey(String nsUri, String localName) {
        return getEntry(nsUri, localName) != null;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/util/QNameMap$Entry.class */
    public static final class Entry<V> {
        public final String nsUri;
        public final String localName;
        V value;
        final int hash;
        Entry<V> next;

        Entry(int h, String nsUri, String localName, V v, Entry<V> n) {
            this.value = v;
            this.next = n;
            this.nsUri = nsUri;
            this.localName = localName;
            this.hash = h;
        }

        public QName createQName() {
            return new QName(this.nsUri, this.localName);
        }

        public V getValue() {
            return this.value;
        }

        public V setValue(V newValue) {
            V oldValue = this.value;
            this.value = newValue;
            return oldValue;
        }

        public boolean equals(Object o) {
            if (!(o instanceof Entry)) {
                return false;
            }
            Entry e = (Entry) o;
            String k1 = this.nsUri;
            String k2 = e.nsUri;
            String k3 = this.localName;
            String k4 = e.localName;
            if (k1 != k2) {
                if (k1 != null && k1.equals(k2)) {
                    if (k3 != k4 && (k3 == null || !k3.equals(k4))) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            Object v1 = getValue();
            Object v2 = e.getValue();
            if (v1 != v2) {
                if (v1 != null && v1.equals(v2)) {
                    return true;
                }
                return false;
            }
            return true;
        }

        public int hashCode() {
            return this.localName.hashCode() ^ (this.value == null ? 0 : this.value.hashCode());
        }

        public String toString() {
            return '\"' + this.nsUri + "\",\"" + this.localName + "\"=" + getValue();
        }
    }

    public Set<Entry<V>> entrySet() {
        Set<Entry<V>> es = this.entrySet;
        if (es != null) {
            return es;
        }
        EntrySet entrySet = new EntrySet();
        this.entrySet = entrySet;
        return entrySet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Iterator<Entry<V>> newEntryIterator() {
        return new EntryIterator();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/util/QNameMap$EntryIterator.class */
    public class EntryIterator extends QNameMap<V>.HashIterator<Entry<V>> {
        private EntryIterator() {
            super();
        }

        @Override // java.util.Iterator
        public Entry<V> next() {
            return nextEntry();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/util/QNameMap$EntrySet.class */
    public class EntrySet extends AbstractSet<Entry<V>> {
        private EntrySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Entry<V>> iterator() {
            return QNameMap.this.newEntryIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            if (!(o instanceof Entry)) {
                return false;
            }
            Entry<V> e = (Entry) o;
            Entry<V> candidate = QNameMap.this.getEntry(e.nsUri, e.localName);
            return candidate != null && candidate.equals(e);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return QNameMap.this.size;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Entry<V> getEntry(String nsUri, String localName) {
        Entry<V> e;
        if ($assertionsDisabled || nsUri == nsUri.intern()) {
            if ($assertionsDisabled || localName == localName.intern()) {
                int hash = hash(localName);
                int i = indexFor(hash, this.table.length);
                Entry<V> entry = this.table[i];
                while (true) {
                    e = entry;
                    if (e == null || (localName == e.localName && nsUri == e.nsUri)) {
                        break;
                    }
                    entry = e.next;
                }
                return e;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append('{');
        for (Entry<V> e : entrySet()) {
            if (buf.length() > 1) {
                buf.append(',');
            }
            buf.append('[');
            buf.append(e);
            buf.append(']');
        }
        buf.append('}');
        return buf.toString();
    }
}
