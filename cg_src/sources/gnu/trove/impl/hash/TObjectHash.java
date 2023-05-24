package gnu.trove.impl.hash;

import gnu.trove.procedure.TObjectProcedure;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/hash/TObjectHash.class */
public abstract class TObjectHash<T> extends THash {
    static final long serialVersionUID = -3461112548087185871L;
    public transient Object[] _set;
    public static final Object REMOVED = new Object();
    public static final Object FREE = new Object();
    protected boolean consumeFreeSlot;

    public TObjectHash() {
    }

    public TObjectHash(int initialCapacity) {
        super(initialCapacity);
    }

    public TObjectHash(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    @Override // gnu.trove.impl.hash.THash
    public int capacity() {
        return this._set.length;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // gnu.trove.impl.hash.THash
    public void removeAt(int index) {
        this._set[index] = REMOVED;
        super.removeAt(index);
    }

    @Override // gnu.trove.impl.hash.THash
    public int setUp(int initialCapacity) {
        int capacity = super.setUp(initialCapacity);
        this._set = new Object[capacity];
        Arrays.fill(this._set, FREE);
        return capacity;
    }

    public boolean forEach(TObjectProcedure<? super T> procedure) {
        Object[] set = this._set;
        int i = set.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (set[i] != FREE && set[i] != REMOVED && !procedure.execute(set[i])) {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    public boolean contains(Object obj) {
        return index(obj) >= 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int index(Object obj) {
        if (obj == null) {
            return indexForNull();
        }
        int hash = hash(obj) & Integer.MAX_VALUE;
        int index = hash % this._set.length;
        Object cur = this._set[index];
        if (cur == FREE) {
            return -1;
        }
        if (cur == obj || equals(obj, cur)) {
            return index;
        }
        return indexRehashed(obj, index, hash, cur);
    }

    private int indexRehashed(Object obj, int index, int hash, Object cur) {
        Object[] set = this._set;
        int length = set.length;
        int probe = 1 + (hash % (length - 2));
        do {
            index -= probe;
            if (index < 0) {
                index += length;
            }
            Object cur2 = set[index];
            if (cur2 == FREE) {
                return -1;
            }
            if (cur2 == obj || equals(obj, cur2)) {
                return index;
            }
        } while (index != index);
        return -1;
    }

    private int indexForNull() {
        int index = 0;
        Object[] arr$ = this._set;
        for (Object o : arr$) {
            if (o == null) {
                return index;
            }
            if (o == FREE) {
                return -1;
            }
            index++;
        }
        return -1;
    }

    @Deprecated
    protected int insertionIndex(T obj) {
        return insertKey(obj);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int insertKey(T key) {
        this.consumeFreeSlot = false;
        if (key == null) {
            return insertKeyForNull();
        }
        int hash = hash(key) & Integer.MAX_VALUE;
        int index = hash % this._set.length;
        Object cur = this._set[index];
        if (cur == FREE) {
            this.consumeFreeSlot = true;
            this._set[index] = key;
            return index;
        } else if (cur == key || equals(key, cur)) {
            return (-index) - 1;
        } else {
            return insertKeyRehash(key, index, hash, cur);
        }
    }

    private int insertKeyRehash(T key, int index, int hash, Object cur) {
        Object[] set = this._set;
        int length = set.length;
        int probe = 1 + (hash % (length - 2));
        int firstRemoved = -1;
        do {
            if (cur == REMOVED && firstRemoved == -1) {
                firstRemoved = index;
            }
            index -= probe;
            if (index < 0) {
                index += length;
            }
            cur = set[index];
            if (cur == FREE) {
                if (firstRemoved != -1) {
                    this._set[firstRemoved] = key;
                    return firstRemoved;
                }
                this.consumeFreeSlot = true;
                this._set[index] = key;
                return index;
            } else if (cur == key || equals(key, cur)) {
                return (-index) - 1;
            }
        } while (index != index);
        if (firstRemoved != -1) {
            this._set[firstRemoved] = key;
            return firstRemoved;
        }
        throw new IllegalStateException("No free or removed slots available. Key set full?!!");
    }

    private int insertKeyForNull() {
        int index = 0;
        int firstRemoved = -1;
        Object[] arr$ = this._set;
        for (Object o : arr$) {
            if (o == REMOVED && firstRemoved == -1) {
                firstRemoved = index;
            }
            if (o == FREE) {
                if (firstRemoved != -1) {
                    this._set[firstRemoved] = null;
                    return firstRemoved;
                }
                this.consumeFreeSlot = true;
                this._set[index] = null;
                return index;
            } else if (o == null) {
                return (-index) - 1;
            } else {
                index++;
            }
        }
        if (firstRemoved != -1) {
            this._set[firstRemoved] = null;
            return firstRemoved;
        }
        throw new IllegalStateException("Could not find insertion index for null key. Key set full!?!!");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void throwObjectContractViolation(Object o1, Object o2) throws IllegalArgumentException {
        throw buildObjectContractViolation(o1, o2, "");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void throwObjectContractViolation(Object o1, Object o2, int size, int oldSize, Object[] oldKeys) throws IllegalArgumentException {
        String extra = dumpExtraInfo(o1, o2, size(), oldSize, oldKeys);
        throw buildObjectContractViolation(o1, o2, extra);
    }

    protected final IllegalArgumentException buildObjectContractViolation(Object o1, Object o2, String extra) {
        return new IllegalArgumentException("Equal objects must have equal hashcodes. During rehashing, Trove discovered that the following two objects claim to be equal (as in java.lang.Object.equals()) but their hashCodes (or those calculated by your TObjectHashingStrategy) are not equal.This violates the general contract of java.lang.Object.hashCode().  See bullet point two in that method's documentation. object #1 =" + objectInfo(o1) + "; object #2 =" + objectInfo(o2) + "\n" + extra);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean equals(Object notnull, Object two) {
        if (two == null || two == REMOVED) {
            return false;
        }
        return notnull.equals(two);
    }

    protected int hash(Object notnull) {
        return notnull.hashCode();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static String reportPotentialConcurrentMod(int newSize, int oldSize) {
        if (newSize != oldSize) {
            return "[Warning] apparent concurrent modification of the key set. Size before and after rehash() do not match " + oldSize + " vs " + newSize;
        }
        return "";
    }

    protected String dumpExtraInfo(Object newVal, Object oldVal, int currentSize, int oldSize, Object[] oldKeys) {
        StringBuilder b = new StringBuilder();
        b.append(dumpKeyTypes(newVal, oldVal));
        b.append(reportPotentialConcurrentMod(currentSize, oldSize));
        b.append(detectKeyLoss(oldKeys, oldSize));
        if (newVal == oldVal) {
            b.append("Inserting same object twice, rehashing bug. Object= ").append(oldVal);
        }
        return b.toString();
    }

    private static String detectKeyLoss(Object[] keys, int oldSize) {
        StringBuilder buf = new StringBuilder();
        Set<Object> k = makeKeySet(keys);
        if (k.size() != oldSize) {
            buf.append("\nhashCode() and/or equals() have inconsistent implementation");
            buf.append("\nKey set lost entries, now got ").append(k.size()).append(" instead of ").append(oldSize);
            buf.append(". This can manifest itself as an apparent duplicate key.");
        }
        return buf.toString();
    }

    private static Set<Object> makeKeySet(Object[] keys) {
        Set<Object> types = new HashSet<>();
        for (Object o : keys) {
            if (o != FREE && o != REMOVED) {
                types.add(o);
            }
        }
        return types;
    }

    private static String equalsSymmetryInfo(Object a, Object b) {
        StringBuilder buf = new StringBuilder();
        if (a == b) {
            return "a == b";
        }
        if (a.getClass() != b.getClass()) {
            buf.append("Class of objects differ a=").append(a.getClass()).append(" vs b=").append(b.getClass());
            boolean aEb = a.equals(b);
            boolean bEa = b.equals(a);
            if (aEb != bEa) {
                buf.append("\nequals() of a or b object are asymmetric");
                buf.append("\na.equals(b) =").append(aEb);
                buf.append("\nb.equals(a) =").append(bEa);
            }
        }
        return buf.toString();
    }

    protected static String objectInfo(Object o) {
        return (o == null ? "class null" : o.getClass()) + " id= " + System.identityHashCode(o) + " hashCode= " + (o == null ? 0 : o.hashCode()) + " toString= " + String.valueOf(o);
    }

    private String dumpKeyTypes(Object newVal, Object oldVal) {
        StringBuilder buf = new StringBuilder();
        Set<Class<?>> types = new HashSet<>();
        Object[] arr$ = this._set;
        for (Object o : arr$) {
            if (o != FREE && o != REMOVED) {
                if (o != null) {
                    types.add(o.getClass());
                } else {
                    types.add(null);
                }
            }
        }
        if (types.size() > 1) {
            buf.append("\nMore than one type used for keys. Watch out for asymmetric equals(). Read about the 'Liskov substitution principle' and the implications for equals() in java.");
            buf.append("\nKey types: ").append(types);
            buf.append(equalsSymmetryInfo(newVal, oldVal));
        }
        return buf.toString();
    }

    @Override // gnu.trove.impl.hash.THash, java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(0);
        super.writeExternal(out);
    }

    @Override // gnu.trove.impl.hash.THash, java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        super.readExternal(in);
    }
}
