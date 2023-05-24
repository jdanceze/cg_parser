package com.sun.xml.fastinfoset.util;

import com.sun.xml.fastinfoset.CommonResourceBundle;
import com.sun.xml.fastinfoset.EncodingConstants;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.jvnet.fastinfoset.FastInfosetException;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/util/PrefixArray.class */
public class PrefixArray extends ValueArray {
    public static final int PREFIX_MAP_SIZE = 64;
    private int _initialCapacity;
    public String[] _array;
    private PrefixArray _readOnlyArray;
    private PrefixEntry[] _prefixMap;
    private PrefixEntry _prefixPool;
    private NamespaceEntry _namespacePool;
    private NamespaceEntry[] _inScopeNamespaces;
    public int[] _currentInScope;
    public int _declarationId;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/util/PrefixArray$PrefixEntry.class */
    public static class PrefixEntry {
        private PrefixEntry next;
        private int prefixId;

        private PrefixEntry() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/util/PrefixArray$NamespaceEntry.class */
    public static class NamespaceEntry {
        private NamespaceEntry next;
        private int declarationId;
        private int namespaceIndex;
        private String prefix;
        private String namespaceName;
        private int prefixEntryIndex;

        private NamespaceEntry() {
        }
    }

    public PrefixArray(int initialCapacity, int maximumCapacity) {
        this._prefixMap = new PrefixEntry[64];
        this._initialCapacity = initialCapacity;
        this._maximumCapacity = maximumCapacity;
        this._array = new String[initialCapacity];
        this._inScopeNamespaces = new NamespaceEntry[initialCapacity + 2];
        this._currentInScope = new int[initialCapacity + 2];
        increaseNamespacePool(initialCapacity);
        increasePrefixPool(initialCapacity);
        initializeEntries();
    }

    public PrefixArray() {
        this(10, Integer.MAX_VALUE);
    }

    private final void initializeEntries() {
        this._inScopeNamespaces[0] = this._namespacePool;
        this._namespacePool = this._namespacePool.next;
        this._inScopeNamespaces[0].next = null;
        this._inScopeNamespaces[0].prefix = "";
        this._inScopeNamespaces[0].namespaceName = "";
        NamespaceEntry namespaceEntry = this._inScopeNamespaces[0];
        this._currentInScope[0] = 0;
        namespaceEntry.namespaceIndex = 0;
        int index = KeyIntMap.indexFor(KeyIntMap.hashHash(this._inScopeNamespaces[0].prefix.hashCode()), this._prefixMap.length);
        this._prefixMap[index] = this._prefixPool;
        this._prefixPool = this._prefixPool.next;
        this._prefixMap[index].next = null;
        this._prefixMap[index].prefixId = 0;
        this._inScopeNamespaces[1] = this._namespacePool;
        this._namespacePool = this._namespacePool.next;
        this._inScopeNamespaces[1].next = null;
        this._inScopeNamespaces[1].prefix = EncodingConstants.XML_NAMESPACE_PREFIX;
        this._inScopeNamespaces[1].namespaceName = "http://www.w3.org/XML/1998/namespace";
        NamespaceEntry namespaceEntry2 = this._inScopeNamespaces[1];
        this._currentInScope[1] = 1;
        namespaceEntry2.namespaceIndex = 1;
        int index2 = KeyIntMap.indexFor(KeyIntMap.hashHash(this._inScopeNamespaces[1].prefix.hashCode()), this._prefixMap.length);
        if (this._prefixMap[index2] == null) {
            this._prefixMap[index2] = this._prefixPool;
            this._prefixPool = this._prefixPool.next;
            this._prefixMap[index2].next = null;
        } else {
            PrefixEntry e = this._prefixMap[index2];
            this._prefixMap[index2] = this._prefixPool;
            this._prefixPool = this._prefixPool.next;
            this._prefixMap[index2].next = e;
        }
        this._prefixMap[index2].prefixId = 1;
    }

    private final void increaseNamespacePool(int capacity) {
        if (this._namespacePool == null) {
            this._namespacePool = new NamespaceEntry();
        }
        for (int i = 0; i < capacity; i++) {
            NamespaceEntry ne = new NamespaceEntry();
            ne.next = this._namespacePool;
            this._namespacePool = ne;
        }
    }

    private final void increasePrefixPool(int capacity) {
        if (this._prefixPool == null) {
            this._prefixPool = new PrefixEntry();
        }
        for (int i = 0; i < capacity; i++) {
            PrefixEntry pe = new PrefixEntry();
            pe.next = this._prefixPool;
            this._prefixPool = pe;
        }
    }

    public int countNamespacePool() {
        int i = 0;
        NamespaceEntry namespaceEntry = this._namespacePool;
        while (true) {
            NamespaceEntry e = namespaceEntry;
            if (e != null) {
                i++;
                namespaceEntry = e.next;
            } else {
                return i;
            }
        }
    }

    public int countPrefixPool() {
        int i = 0;
        PrefixEntry prefixEntry = this._prefixPool;
        while (true) {
            PrefixEntry e = prefixEntry;
            if (e != null) {
                i++;
                prefixEntry = e.next;
            } else {
                return i;
            }
        }
    }

    @Override // com.sun.xml.fastinfoset.util.ValueArray
    public final void clear() {
        for (int i = this._readOnlyArraySize; i < this._size; i++) {
            this._array[i] = null;
        }
        this._size = this._readOnlyArraySize;
    }

    public final void clearCompletely() {
        this._prefixPool = null;
        this._namespacePool = null;
        for (int i = 0; i < this._size + 2; i++) {
            this._currentInScope[i] = 0;
            this._inScopeNamespaces[i] = null;
        }
        for (int i2 = 0; i2 < this._prefixMap.length; i2++) {
            this._prefixMap[i2] = null;
        }
        increaseNamespacePool(this._initialCapacity);
        increasePrefixPool(this._initialCapacity);
        initializeEntries();
        this._declarationId = 0;
        clear();
    }

    public final String[] getArray() {
        if (this._array == null) {
            return null;
        }
        String[] clonedArray = new String[this._array.length];
        System.arraycopy(this._array, 0, clonedArray, 0, this._array.length);
        return clonedArray;
    }

    @Override // com.sun.xml.fastinfoset.util.ValueArray
    public final void setReadOnlyArray(ValueArray readOnlyArray, boolean clear) {
        if (!(readOnlyArray instanceof PrefixArray)) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.illegalClass", new Object[]{readOnlyArray}));
        }
        setReadOnlyArray((PrefixArray) readOnlyArray, clear);
    }

    public final void setReadOnlyArray(PrefixArray readOnlyArray, boolean clear) {
        if (readOnlyArray != null) {
            this._readOnlyArray = readOnlyArray;
            this._readOnlyArraySize = readOnlyArray.getSize();
            clearCompletely();
            this._inScopeNamespaces = new NamespaceEntry[this._readOnlyArraySize + this._inScopeNamespaces.length];
            this._currentInScope = new int[this._readOnlyArraySize + this._currentInScope.length];
            initializeEntries();
            if (clear) {
                clear();
            }
            this._array = getCompleteArray();
            this._size = this._readOnlyArraySize;
        }
    }

    public final String[] getCompleteArray() {
        if (this._readOnlyArray == null) {
            return getArray();
        }
        String[] ra = this._readOnlyArray.getCompleteArray();
        String[] a = new String[this._readOnlyArraySize + this._array.length];
        System.arraycopy(ra, 0, a, 0, this._readOnlyArraySize);
        return a;
    }

    public final String get(int i) {
        return this._array[i];
    }

    public final int add(String s) {
        if (this._size == this._array.length) {
            resize();
        }
        String[] strArr = this._array;
        int i = this._size;
        this._size = i + 1;
        strArr[i] = s;
        return this._size;
    }

    protected final void resize() {
        if (this._size == this._maximumCapacity) {
            throw new ValueArrayResourceException(CommonResourceBundle.getInstance().getString("message.arrayMaxCapacity"));
        }
        int newSize = ((this._size * 3) / 2) + 1;
        if (newSize > this._maximumCapacity) {
            newSize = this._maximumCapacity;
        }
        String[] newArray = new String[newSize];
        System.arraycopy(this._array, 0, newArray, 0, this._size);
        this._array = newArray;
        int newSize2 = newSize + 2;
        NamespaceEntry[] newInScopeNamespaces = new NamespaceEntry[newSize2];
        System.arraycopy(this._inScopeNamespaces, 0, newInScopeNamespaces, 0, this._inScopeNamespaces.length);
        this._inScopeNamespaces = newInScopeNamespaces;
        int[] newCurrentInScope = new int[newSize2];
        System.arraycopy(this._currentInScope, 0, newCurrentInScope, 0, this._currentInScope.length);
        this._currentInScope = newCurrentInScope;
    }

    public final void clearDeclarationIds() {
        for (int i = 0; i < this._size; i++) {
            NamespaceEntry e = this._inScopeNamespaces[i];
            if (e != null) {
                e.declarationId = 0;
            }
        }
        this._declarationId = 1;
    }

    public final void pushScope(int prefixIndex, int namespaceIndex) throws FastInfosetException {
        if (this._namespacePool == null) {
            increaseNamespacePool(16);
        }
        NamespaceEntry e = this._namespacePool;
        this._namespacePool = e.next;
        int prefixIndex2 = prefixIndex + 1;
        NamespaceEntry current = this._inScopeNamespaces[prefixIndex2];
        if (current == null) {
            e.declarationId = this._declarationId;
            int namespaceIndex2 = namespaceIndex + 1;
            this._currentInScope[prefixIndex2] = namespaceIndex2;
            e.namespaceIndex = namespaceIndex2;
            e.next = null;
            this._inScopeNamespaces[prefixIndex2] = e;
        } else if (current.declarationId < this._declarationId) {
            e.declarationId = this._declarationId;
            int namespaceIndex3 = namespaceIndex + 1;
            this._currentInScope[prefixIndex2] = namespaceIndex3;
            e.namespaceIndex = namespaceIndex3;
            e.next = current;
            current.declarationId = 0;
            this._inScopeNamespaces[prefixIndex2] = e;
        } else {
            throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.duplicateNamespaceAttribute"));
        }
    }

    public final void pushScopeWithPrefixEntry(String prefix, String namespaceName, int prefixIndex, int namespaceIndex) throws FastInfosetException {
        if (this._namespacePool == null) {
            increaseNamespacePool(16);
        }
        if (this._prefixPool == null) {
            increasePrefixPool(16);
        }
        NamespaceEntry e = this._namespacePool;
        this._namespacePool = e.next;
        int prefixIndex2 = prefixIndex + 1;
        NamespaceEntry current = this._inScopeNamespaces[prefixIndex2];
        if (current == null) {
            e.declarationId = this._declarationId;
            int namespaceIndex2 = namespaceIndex + 1;
            this._currentInScope[prefixIndex2] = namespaceIndex2;
            e.namespaceIndex = namespaceIndex2;
            e.next = null;
            this._inScopeNamespaces[prefixIndex2] = e;
        } else if (current.declarationId < this._declarationId) {
            e.declarationId = this._declarationId;
            int namespaceIndex3 = namespaceIndex + 1;
            this._currentInScope[prefixIndex2] = namespaceIndex3;
            e.namespaceIndex = namespaceIndex3;
            e.next = current;
            current.declarationId = 0;
            this._inScopeNamespaces[prefixIndex2] = e;
        } else {
            throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.duplicateNamespaceAttribute"));
        }
        PrefixEntry p = this._prefixPool;
        this._prefixPool = this._prefixPool.next;
        p.prefixId = prefixIndex2;
        e.prefix = prefix;
        e.namespaceName = namespaceName;
        e.prefixEntryIndex = KeyIntMap.indexFor(KeyIntMap.hashHash(prefix.hashCode()), this._prefixMap.length);
        PrefixEntry pCurrent = this._prefixMap[e.prefixEntryIndex];
        p.next = pCurrent;
        this._prefixMap[e.prefixEntryIndex] = p;
    }

    public final void popScope(int prefixIndex) {
        int prefixIndex2 = prefixIndex + 1;
        NamespaceEntry e = this._inScopeNamespaces[prefixIndex2];
        this._inScopeNamespaces[prefixIndex2] = e.next;
        this._currentInScope[prefixIndex2] = e.next != null ? e.next.namespaceIndex : 0;
        e.next = this._namespacePool;
        this._namespacePool = e;
    }

    public final void popScopeWithPrefixEntry(int prefixIndex) {
        int prefixIndex2 = prefixIndex + 1;
        NamespaceEntry e = this._inScopeNamespaces[prefixIndex2];
        this._inScopeNamespaces[prefixIndex2] = e.next;
        this._currentInScope[prefixIndex2] = e.next != null ? e.next.namespaceIndex : 0;
        e.prefix = e.namespaceName = null;
        e.next = this._namespacePool;
        this._namespacePool = e;
        PrefixEntry current = this._prefixMap[e.prefixEntryIndex];
        if (current.prefixId == prefixIndex2) {
            this._prefixMap[e.prefixEntryIndex] = current.next;
            current.next = this._prefixPool;
            this._prefixPool = current;
            return;
        }
        PrefixEntry prev = current;
        PrefixEntry prefixEntry = current.next;
        while (true) {
            PrefixEntry current2 = prefixEntry;
            if (current2 != null) {
                if (current2.prefixId == prefixIndex2) {
                    prev.next = current2.next;
                    current2.next = this._prefixPool;
                    this._prefixPool = current2;
                    return;
                }
                prev = current2;
                prefixEntry = current2.next;
            } else {
                return;
            }
        }
    }

    public final String getNamespaceFromPrefix(String prefix) {
        NamespaceEntry ne;
        int index = KeyIntMap.indexFor(KeyIntMap.hashHash(prefix.hashCode()), this._prefixMap.length);
        PrefixEntry prefixEntry = this._prefixMap[index];
        while (true) {
            PrefixEntry pe = prefixEntry;
            if (pe != null) {
                ne = this._inScopeNamespaces[pe.prefixId];
                if (prefix == ne.prefix || prefix.equals(ne.prefix)) {
                    break;
                }
                prefixEntry = pe.next;
            } else {
                return null;
            }
        }
        return ne.namespaceName;
    }

    public final String getPrefixFromNamespace(String namespaceName) {
        int position = 0;
        while (true) {
            position++;
            if (position < this._size + 2) {
                NamespaceEntry ne = this._inScopeNamespaces[position];
                if (ne != null && namespaceName.equals(ne.namespaceName)) {
                    return ne.prefix;
                }
            } else {
                return null;
            }
        }
    }

    public final Iterator getPrefixes() {
        return new Iterator() { // from class: com.sun.xml.fastinfoset.util.PrefixArray.1
            int _position = 1;
            NamespaceEntry _ne;

            {
                this._ne = PrefixArray.this._inScopeNamespaces[this._position];
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this._ne != null;
            }

            @Override // java.util.Iterator
            public Object next() {
                if (this._position != PrefixArray.this._size + 2) {
                    String prefix = this._ne.prefix;
                    moveToNext();
                    return prefix;
                }
                throw new NoSuchElementException();
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }

            private final void moveToNext() {
                do {
                    int i = this._position + 1;
                    this._position = i;
                    if (i < PrefixArray.this._size + 2) {
                        this._ne = PrefixArray.this._inScopeNamespaces[this._position];
                    } else {
                        this._ne = null;
                        return;
                    }
                } while (this._ne == null);
            }
        };
    }

    public final Iterator getPrefixesFromNamespace(final String namespaceName) {
        return new Iterator() { // from class: com.sun.xml.fastinfoset.util.PrefixArray.2
            String _namespaceName;
            int _position = 0;
            NamespaceEntry _ne;

            {
                this._namespaceName = namespaceName;
                moveToNext();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this._ne != null;
            }

            @Override // java.util.Iterator
            public Object next() {
                if (this._position != PrefixArray.this._size + 2) {
                    String prefix = this._ne.prefix;
                    moveToNext();
                    return prefix;
                }
                throw new NoSuchElementException();
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }

            private final void moveToNext() {
                while (true) {
                    int i = this._position + 1;
                    this._position = i;
                    if (i < PrefixArray.this._size + 2) {
                        this._ne = PrefixArray.this._inScopeNamespaces[this._position];
                        if (this._ne != null && this._namespaceName.equals(this._ne.namespaceName)) {
                            return;
                        }
                    } else {
                        this._ne = null;
                        return;
                    }
                }
            }
        };
    }
}
