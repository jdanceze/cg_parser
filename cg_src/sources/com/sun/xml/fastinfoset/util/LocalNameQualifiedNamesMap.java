package com.sun.xml.fastinfoset.util;

import com.sun.xml.fastinfoset.CommonResourceBundle;
import com.sun.xml.fastinfoset.QualifiedName;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/util/LocalNameQualifiedNamesMap.class */
public class LocalNameQualifiedNamesMap extends KeyIntMap {
    private LocalNameQualifiedNamesMap _readOnlyMap;
    private int _index;
    private Entry[] _table;

    /* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/util/LocalNameQualifiedNamesMap$Entry.class */
    public static class Entry {
        final String _key;
        final int _hash;
        public QualifiedName[] _value = new QualifiedName[1];
        public int _valueIndex;
        Entry _next;

        public Entry(String key, int hash, Entry next) {
            this._key = key;
            this._hash = hash;
            this._next = next;
        }

        public void addQualifiedName(QualifiedName name) {
            if (this._valueIndex < this._value.length) {
                QualifiedName[] qualifiedNameArr = this._value;
                int i = this._valueIndex;
                this._valueIndex = i + 1;
                qualifiedNameArr[i] = name;
            } else if (this._valueIndex == this._value.length) {
                QualifiedName[] newValue = new QualifiedName[((this._valueIndex * 3) / 2) + 1];
                System.arraycopy(this._value, 0, newValue, 0, this._valueIndex);
                this._value = newValue;
                QualifiedName[] qualifiedNameArr2 = this._value;
                int i2 = this._valueIndex;
                this._valueIndex = i2 + 1;
                qualifiedNameArr2[i2] = name;
            }
        }
    }

    public LocalNameQualifiedNamesMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        this._table = new Entry[this._capacity];
    }

    public LocalNameQualifiedNamesMap(int initialCapacity) {
        this(initialCapacity, 0.75f);
    }

    public LocalNameQualifiedNamesMap() {
        this(16, 0.75f);
    }

    @Override // com.sun.xml.fastinfoset.util.KeyIntMap
    public final void clear() {
        for (int i = 0; i < this._table.length; i++) {
            this._table[i] = null;
        }
        this._size = 0;
        if (this._readOnlyMap != null) {
            this._index = this._readOnlyMap.getIndex();
        } else {
            this._index = 0;
        }
    }

    @Override // com.sun.xml.fastinfoset.util.KeyIntMap
    public final void setReadOnlyMap(KeyIntMap readOnlyMap, boolean clear) {
        if (!(readOnlyMap instanceof LocalNameQualifiedNamesMap)) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.illegalClass", new Object[]{readOnlyMap}));
        }
        setReadOnlyMap((LocalNameQualifiedNamesMap) readOnlyMap, clear);
    }

    public final void setReadOnlyMap(LocalNameQualifiedNamesMap readOnlyMap, boolean clear) {
        this._readOnlyMap = readOnlyMap;
        if (this._readOnlyMap != null) {
            this._readOnlyMapSize = this._readOnlyMap.size();
            this._index = this._readOnlyMap.getIndex();
            if (clear) {
                clear();
                return;
            }
            return;
        }
        this._readOnlyMapSize = 0;
        this._index = 0;
    }

    public final boolean isQNameFromReadOnlyMap(QualifiedName name) {
        return this._readOnlyMap != null && name.index <= this._readOnlyMap.getIndex();
    }

    public final int getNextIndex() {
        int i = this._index;
        this._index = i + 1;
        return i;
    }

    public final int getIndex() {
        return this._index;
    }

    public final Entry obtainEntry(String key) {
        Entry entry;
        int hash = hashHash(key.hashCode());
        if (this._readOnlyMap != null && (entry = this._readOnlyMap.getEntry(key, hash)) != null) {
            return entry;
        }
        int tableIndex = indexFor(hash, this._table.length);
        Entry entry2 = this._table[tableIndex];
        while (true) {
            Entry e = entry2;
            if (e != null) {
                if (e._hash != hash || !eq(key, e._key)) {
                    entry2 = e._next;
                } else {
                    return e;
                }
            } else {
                return addEntry(key, hash, tableIndex);
            }
        }
    }

    public final Entry obtainDynamicEntry(String key) {
        int hash = hashHash(key.hashCode());
        int tableIndex = indexFor(hash, this._table.length);
        Entry entry = this._table[tableIndex];
        while (true) {
            Entry e = entry;
            if (e != null) {
                if (e._hash != hash || !eq(key, e._key)) {
                    entry = e._next;
                } else {
                    return e;
                }
            } else {
                return addEntry(key, hash, tableIndex);
            }
        }
    }

    private final Entry getEntry(String key, int hash) {
        Entry entry;
        if (this._readOnlyMap != null && (entry = this._readOnlyMap.getEntry(key, hash)) != null) {
            return entry;
        }
        int tableIndex = indexFor(hash, this._table.length);
        Entry entry2 = this._table[tableIndex];
        while (true) {
            Entry e = entry2;
            if (e != null) {
                if (e._hash != hash || !eq(key, e._key)) {
                    entry2 = e._next;
                } else {
                    return e;
                }
            } else {
                return null;
            }
        }
    }

    private final Entry addEntry(String key, int hash, int bucketIndex) {
        Entry e = this._table[bucketIndex];
        this._table[bucketIndex] = new Entry(key, hash, e);
        Entry e2 = this._table[bucketIndex];
        int i = this._size;
        this._size = i + 1;
        if (i >= this._threshold) {
            resize(2 * this._table.length);
        }
        return e2;
    }

    private final void resize(int newCapacity) {
        this._capacity = newCapacity;
        Entry[] oldTable = this._table;
        int oldCapacity = oldTable.length;
        if (oldCapacity == 1048576) {
            this._threshold = Integer.MAX_VALUE;
            return;
        }
        Entry[] newTable = new Entry[this._capacity];
        transfer(newTable);
        this._table = newTable;
        this._threshold = (int) (this._capacity * this._loadFactor);
    }

    private final void transfer(Entry[] newTable) {
        Entry[] src = this._table;
        int newCapacity = newTable.length;
        for (int j = 0; j < src.length; j++) {
            Entry e = src[j];
            if (e != null) {
                src[j] = null;
                do {
                    Entry next = e._next;
                    int i = indexFor(e._hash, newCapacity);
                    e._next = newTable[i];
                    newTable[i] = e;
                    e = next;
                } while (e != null);
            }
        }
    }

    private final boolean eq(String x, String y) {
        return x == y || x.equals(y);
    }
}
