package com.sun.xml.fastinfoset.util;

import com.sun.xml.fastinfoset.CommonResourceBundle;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/util/KeyIntMap.class */
public abstract class KeyIntMap {
    public static final int NOT_PRESENT = -1;
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final int MAXIMUM_CAPACITY = 1048576;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    int _readOnlyMapSize;
    int _size;
    int _capacity;
    int _threshold;
    final float _loadFactor;

    public abstract void clear();

    public abstract void setReadOnlyMap(KeyIntMap keyIntMap, boolean z);

    /* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/util/KeyIntMap$BaseEntry.class */
    static class BaseEntry {
        final int _hash;
        final int _value;

        public BaseEntry(int hash, int value) {
            this._hash = hash;
            this._value = value;
        }
    }

    public KeyIntMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.illegalInitialCapacity", new Object[]{Integer.valueOf(initialCapacity)}));
        }
        initialCapacity = initialCapacity > 1048576 ? 1048576 : initialCapacity;
        if (loadFactor <= 0.0f || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.illegalLoadFactor", new Object[]{Float.valueOf(loadFactor)}));
        }
        if (initialCapacity != 16) {
            this._capacity = 1;
            while (this._capacity < initialCapacity) {
                this._capacity <<= 1;
            }
            this._loadFactor = loadFactor;
            this._threshold = (int) (this._capacity * this._loadFactor);
            return;
        }
        this._capacity = 16;
        this._loadFactor = DEFAULT_LOAD_FACTOR;
        this._threshold = 12;
    }

    public KeyIntMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public KeyIntMap() {
        this._capacity = 16;
        this._loadFactor = DEFAULT_LOAD_FACTOR;
        this._threshold = 12;
    }

    public final int size() {
        return this._size + this._readOnlyMapSize;
    }

    public static final int hashHash(int h) {
        int h2 = h + ((h << 9) ^ (-1));
        int h3 = h2 ^ (h2 >>> 14);
        int h4 = h3 + (h3 << 4);
        return h4 ^ (h4 >>> 10);
    }

    public static final int indexFor(int h, int length) {
        return h & (length - 1);
    }
}
