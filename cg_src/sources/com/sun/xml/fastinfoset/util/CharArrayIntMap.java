package com.sun.xml.fastinfoset.util;

import com.sun.xml.fastinfoset.CommonResourceBundle;
import com.sun.xml.fastinfoset.util.KeyIntMap;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/util/CharArrayIntMap.class */
public class CharArrayIntMap extends KeyIntMap {
    private CharArrayIntMap _readOnlyMap;
    protected int _totalCharacterCount;
    private Entry[] _table;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/util/CharArrayIntMap$Entry.class */
    public static class Entry extends KeyIntMap.BaseEntry {
        final char[] _ch;
        final int _start;
        final int _length;
        Entry _next;

        public Entry(char[] ch, int start, int length, int hash, int value, Entry next) {
            super(hash, value);
            this._ch = ch;
            this._start = start;
            this._length = length;
            this._next = next;
        }

        public final boolean equalsCharArray(char[] ch, int start, int length) {
            int i;
            int i2;
            if (this._length == length) {
                int n = this._length;
                int i3 = this._start;
                int j = start;
                do {
                    int i4 = n;
                    n--;
                    if (i4 == 0) {
                        return true;
                    }
                    i = i3;
                    i3++;
                    i2 = j;
                    j++;
                } while (this._ch[i] == ch[i2]);
                return false;
            }
            return false;
        }
    }

    public CharArrayIntMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        this._table = new Entry[this._capacity];
    }

    public CharArrayIntMap(int initialCapacity) {
        this(initialCapacity, 0.75f);
    }

    public CharArrayIntMap() {
        this(16, 0.75f);
    }

    @Override // com.sun.xml.fastinfoset.util.KeyIntMap
    public final void clear() {
        for (int i = 0; i < this._table.length; i++) {
            this._table[i] = null;
        }
        this._size = 0;
        this._totalCharacterCount = 0;
    }

    @Override // com.sun.xml.fastinfoset.util.KeyIntMap
    public final void setReadOnlyMap(KeyIntMap readOnlyMap, boolean clear) {
        if (!(readOnlyMap instanceof CharArrayIntMap)) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.illegalClass", new Object[]{readOnlyMap}));
        }
        setReadOnlyMap((CharArrayIntMap) readOnlyMap, clear);
    }

    public final void setReadOnlyMap(CharArrayIntMap readOnlyMap, boolean clear) {
        this._readOnlyMap = readOnlyMap;
        if (this._readOnlyMap != null) {
            this._readOnlyMapSize = this._readOnlyMap.size();
            if (clear) {
                clear();
                return;
            }
            return;
        }
        this._readOnlyMapSize = 0;
    }

    public final int get(char[] ch, int start, int length) {
        int hash = hashHash(CharArray.hashCode(ch, start, length));
        return get(ch, start, length, hash);
    }

    public final int obtainIndex(char[] ch, int start, int length, boolean clone) {
        int index;
        int hash = hashHash(CharArray.hashCode(ch, start, length));
        if (this._readOnlyMap != null && (index = this._readOnlyMap.get(ch, start, length, hash)) != -1) {
            return index;
        }
        int tableIndex = indexFor(hash, this._table.length);
        Entry entry = this._table[tableIndex];
        while (true) {
            Entry e = entry;
            if (e != null) {
                if (e._hash != hash || !e.equalsCharArray(ch, start, length)) {
                    entry = e._next;
                } else {
                    return e._value;
                }
            } else {
                if (clone) {
                    char[] chClone = new char[length];
                    System.arraycopy(ch, start, chClone, 0, length);
                    ch = chClone;
                    start = 0;
                }
                addEntry(ch, start, length, hash, this._size + this._readOnlyMapSize, tableIndex);
                return -1;
            }
        }
    }

    public final int getTotalCharacterCount() {
        return this._totalCharacterCount;
    }

    private final int get(char[] ch, int start, int length, int hash) {
        int i;
        if (this._readOnlyMap != null && (i = this._readOnlyMap.get(ch, start, length, hash)) != -1) {
            return i;
        }
        int tableIndex = indexFor(hash, this._table.length);
        Entry entry = this._table[tableIndex];
        while (true) {
            Entry e = entry;
            if (e != null) {
                if (e._hash != hash || !e.equalsCharArray(ch, start, length)) {
                    entry = e._next;
                } else {
                    return e._value;
                }
            } else {
                return -1;
            }
        }
    }

    private final void addEntry(char[] ch, int start, int length, int hash, int value, int bucketIndex) {
        Entry e = this._table[bucketIndex];
        this._table[bucketIndex] = new Entry(ch, start, length, hash, value, e);
        this._totalCharacterCount += length;
        int i = this._size;
        this._size = i + 1;
        if (i >= this._threshold) {
            resize(2 * this._table.length);
        }
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
}
