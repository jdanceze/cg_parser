package com.sun.xml.fastinfoset.util;

import com.sun.xml.fastinfoset.CommonResourceBundle;
import org.jvnet.fastinfoset.FastInfosetException;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/util/DuplicateAttributeVerifier.class */
public class DuplicateAttributeVerifier {
    public static final int MAP_SIZE = 256;
    public int _currentIteration;
    private Entry[] _map;
    public final Entry _poolHead;
    public Entry _poolCurrent;
    private Entry _poolTail;

    /* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/util/DuplicateAttributeVerifier$Entry.class */
    public static class Entry {
        private int iteration;
        private int value;
        private Entry hashNext;
        private Entry poolNext;
    }

    public DuplicateAttributeVerifier() {
        Entry entry = new Entry();
        this._poolHead = entry;
        this._poolTail = entry;
    }

    public final void clear() {
        this._currentIteration = 0;
        Entry entry = this._poolHead;
        while (true) {
            Entry e = entry;
            if (e != null) {
                e.iteration = 0;
                entry = e.poolNext;
            } else {
                reset();
                return;
            }
        }
    }

    public final void reset() {
        this._poolCurrent = this._poolHead;
        if (this._map == null) {
            this._map = new Entry[256];
        }
    }

    private final void increasePool(int capacity) {
        if (this._map == null) {
            this._map = new Entry[256];
            this._poolCurrent = this._poolHead;
            return;
        }
        Entry tail = this._poolTail;
        for (int i = 0; i < capacity; i++) {
            Entry e = new Entry();
            this._poolTail.poolNext = e;
            this._poolTail = e;
        }
        this._poolCurrent = tail.poolNext;
    }

    public final void checkForDuplicateAttribute(int hash, int value) throws FastInfosetException {
        if (this._poolCurrent == null) {
            increasePool(16);
        }
        Entry newEntry = this._poolCurrent;
        this._poolCurrent = this._poolCurrent.poolNext;
        Entry head = this._map[hash];
        if (head == null || head.iteration < this._currentIteration) {
            newEntry.hashNext = null;
            this._map[hash] = newEntry;
            newEntry.iteration = this._currentIteration;
            newEntry.value = value;
            return;
        }
        Entry e = head;
        while (e.value != value) {
            Entry entry = e.hashNext;
            e = entry;
            if (entry == null) {
                newEntry.hashNext = head;
                this._map[hash] = newEntry;
                newEntry.iteration = this._currentIteration;
                newEntry.value = value;
                return;
            }
        }
        reset();
        throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.duplicateAttribute"));
    }
}
