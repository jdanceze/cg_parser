package com.sun.xml.fastinfoset.util;

import com.sun.xml.fastinfoset.CommonResourceBundle;
import com.sun.xml.fastinfoset.util.StringIntMap;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/util/FixedEntryStringIntMap.class */
public class FixedEntryStringIntMap extends StringIntMap {
    private StringIntMap.Entry _fixedEntry;

    public FixedEntryStringIntMap(String fixedEntry, int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        int hash = hashHash(fixedEntry.hashCode());
        int tableIndex = indexFor(hash, this._table.length);
        StringIntMap.Entry[] entryArr = this._table;
        int i = this._index;
        this._index = i + 1;
        StringIntMap.Entry entry = new StringIntMap.Entry(fixedEntry, hash, i, null);
        this._fixedEntry = entry;
        entryArr[tableIndex] = entry;
        int i2 = this._size;
        this._size = i2 + 1;
        if (i2 >= this._threshold) {
            resize(2 * this._table.length);
        }
    }

    public FixedEntryStringIntMap(String fixedEntry, int initialCapacity) {
        this(fixedEntry, initialCapacity, 0.75f);
    }

    public FixedEntryStringIntMap(String fixedEntry) {
        this(fixedEntry, 16, 0.75f);
    }

    @Override // com.sun.xml.fastinfoset.util.StringIntMap, com.sun.xml.fastinfoset.util.KeyIntMap
    public final void clear() {
        for (int i = 0; i < this._table.length; i++) {
            this._table[i] = null;
        }
        this._lastEntry = NULL_ENTRY;
        if (this._fixedEntry != null) {
            int tableIndex = indexFor(this._fixedEntry._hash, this._table.length);
            this._table[tableIndex] = this._fixedEntry;
            this._fixedEntry._next = null;
            this._size = 1;
            this._index = this._readOnlyMapSize + 1;
            return;
        }
        this._size = 0;
        this._index = this._readOnlyMapSize;
    }

    @Override // com.sun.xml.fastinfoset.util.StringIntMap, com.sun.xml.fastinfoset.util.KeyIntMap
    public final void setReadOnlyMap(KeyIntMap readOnlyMap, boolean clear) {
        if (!(readOnlyMap instanceof FixedEntryStringIntMap)) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.illegalClass", new Object[]{readOnlyMap}));
        }
        setReadOnlyMap((FixedEntryStringIntMap) readOnlyMap, clear);
    }

    public final void setReadOnlyMap(FixedEntryStringIntMap readOnlyMap, boolean clear) {
        this._readOnlyMap = readOnlyMap;
        if (this._readOnlyMap != null) {
            readOnlyMap.removeFixedEntry();
            this._readOnlyMapSize = readOnlyMap.size();
            this._index = this._readOnlyMapSize + this._size;
            if (clear) {
                clear();
                return;
            }
            return;
        }
        this._readOnlyMapSize = 0;
    }

    private final void removeFixedEntry() {
        StringIntMap.Entry previousEntry;
        if (this._fixedEntry != null) {
            int tableIndex = indexFor(this._fixedEntry._hash, this._table.length);
            StringIntMap.Entry firstEntry = this._table[tableIndex];
            if (firstEntry == this._fixedEntry) {
                this._table[tableIndex] = this._fixedEntry._next;
            } else {
                StringIntMap.Entry entry = firstEntry;
                while (true) {
                    previousEntry = entry;
                    if (previousEntry._next == this._fixedEntry) {
                        break;
                    }
                    entry = previousEntry._next;
                }
                previousEntry._next = this._fixedEntry._next;
            }
            this._fixedEntry = null;
            this._size--;
        }
    }
}
