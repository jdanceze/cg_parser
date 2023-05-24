package com.sun.xml.fastinfoset.util;

import com.sun.xml.fastinfoset.CommonResourceBundle;
import com.sun.xml.fastinfoset.util.KeyIntMap;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/util/StringIntMap.class */
public class StringIntMap extends KeyIntMap {
    protected static final Entry NULL_ENTRY = new Entry(null, 0, -1, null);
    protected StringIntMap _readOnlyMap;
    protected Entry _lastEntry;
    protected Entry[] _table;
    protected int _index;
    protected int _totalCharacterCount;

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/util/StringIntMap$Entry.class */
    public static class Entry extends KeyIntMap.BaseEntry {
        final String _key;
        Entry _next;

        public Entry(String key, int hash, int value, Entry next) {
            super(hash, value);
            this._key = key;
            this._next = next;
        }
    }

    public StringIntMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        this._lastEntry = NULL_ENTRY;
        this._table = new Entry[this._capacity];
    }

    public StringIntMap(int initialCapacity) {
        this(initialCapacity, 0.75f);
    }

    public StringIntMap() {
        this(16, 0.75f);
    }

    @Override // com.sun.xml.fastinfoset.util.KeyIntMap
    public void clear() {
        for (int i = 0; i < this._table.length; i++) {
            this._table[i] = null;
        }
        this._lastEntry = NULL_ENTRY;
        this._size = 0;
        this._index = this._readOnlyMapSize;
        this._totalCharacterCount = 0;
    }

    @Override // com.sun.xml.fastinfoset.util.KeyIntMap
    public void setReadOnlyMap(KeyIntMap readOnlyMap, boolean clear) {
        if (!(readOnlyMap instanceof StringIntMap)) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.illegalClass", new Object[]{readOnlyMap}));
        }
        setReadOnlyMap((StringIntMap) readOnlyMap, clear);
    }

    public final void setReadOnlyMap(StringIntMap readOnlyMap, boolean clear) {
        this._readOnlyMap = readOnlyMap;
        if (this._readOnlyMap != null) {
            this._readOnlyMapSize = this._readOnlyMap.size();
            this._index = this._size + this._readOnlyMapSize;
            if (clear) {
                clear();
                return;
            }
            return;
        }
        this._readOnlyMapSize = 0;
        this._index = this._size;
    }

    public final int getNextIndex() {
        int i = this._index;
        this._index = i + 1;
        return i;
    }

    public final int getIndex() {
        return this._index;
    }

    public final int obtainIndex(String key) {
        int index;
        int hash = hashHash(key.hashCode());
        if (this._readOnlyMap != null && (index = this._readOnlyMap.get(key, hash)) != -1) {
            return index;
        }
        int tableIndex = indexFor(hash, this._table.length);
        Entry entry = this._table[tableIndex];
        while (true) {
            Entry e = entry;
            if (e != null) {
                if (e._hash != hash || !eq(key, e._key)) {
                    entry = e._next;
                } else {
                    return e._value;
                }
            } else {
                addEntry(key, hash, tableIndex);
                return -1;
            }
        }
    }

    public final void add(String key) {
        int hash = hashHash(key.hashCode());
        int tableIndex = indexFor(hash, this._table.length);
        addEntry(key, hash, tableIndex);
    }

    public final int get(String key) {
        if (key == this._lastEntry._key) {
            return this._lastEntry._value;
        }
        return get(key, hashHash(key.hashCode()));
    }

    public final int getTotalCharacterCount() {
        return this._totalCharacterCount;
    }

    private final int get(String key, int hash) {
        int i;
        if (this._readOnlyMap != null && (i = this._readOnlyMap.get(key, hash)) != -1) {
            return i;
        }
        int tableIndex = indexFor(hash, this._table.length);
        Entry entry = this._table[tableIndex];
        while (true) {
            Entry e = entry;
            if (e != null) {
                if (e._hash != hash || !eq(key, e._key)) {
                    entry = e._next;
                } else {
                    this._lastEntry = e;
                    return e._value;
                }
            } else {
                return -1;
            }
        }
    }

    private final void addEntry(String key, int hash, int bucketIndex) {
        Entry e = this._table[bucketIndex];
        Entry[] entryArr = this._table;
        int i = this._index;
        this._index = i + 1;
        entryArr[bucketIndex] = new Entry(key, hash, i, e);
        this._totalCharacterCount += key.length();
        int i2 = this._size;
        this._size = i2 + 1;
        if (i2 >= this._threshold) {
            resize(2 * this._table.length);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void resize(int newCapacity) {
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
