package soot.util;
/* loaded from: gencallgraphv3.jar:soot/util/SharedBitSet.class */
public final class SharedBitSet {
    BitVector value;
    boolean own;

    public SharedBitSet(int i) {
        this.own = true;
        this.value = new BitVector(i);
    }

    public SharedBitSet() {
        this(32);
    }

    private void acquire() {
        if (this.own) {
            return;
        }
        this.own = true;
        this.value = (BitVector) this.value.clone();
    }

    private void canonicalize() {
        this.value = SharedBitSetCache.v().canonicalize(this.value);
        this.own = false;
    }

    public boolean set(int bit) {
        acquire();
        return this.value.set(bit);
    }

    public void clear(int bit) {
        acquire();
        this.value.clear(bit);
    }

    public boolean get(int bit) {
        return this.value.get(bit);
    }

    public void and(SharedBitSet other) {
        if (this.own) {
            this.value.and(other.value);
        } else {
            this.value = BitVector.and(this.value, other.value);
            this.own = true;
        }
        canonicalize();
    }

    public void or(SharedBitSet other) {
        if (this.own) {
            this.value.or(other.value);
        } else {
            this.value = BitVector.or(this.value, other.value);
            this.own = true;
        }
        canonicalize();
    }

    public boolean orAndAndNot(SharedBitSet orset, SharedBitSet andset, SharedBitSet andnotset) {
        acquire();
        boolean ret = this.value.orAndAndNot(orset.value, andset.value, andnotset.value);
        canonicalize();
        return ret;
    }

    public boolean orAndAndNot(SharedBitSet orset, BitVector andset, SharedBitSet andnotset) {
        acquire();
        boolean ret = this.value.orAndAndNot(orset.value, andset, andnotset == null ? null : andnotset.value);
        canonicalize();
        return ret;
    }

    public BitSetIterator iterator() {
        return this.value.iterator();
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        BitSetIterator it = iterator();
        while (it.hasNext()) {
            int next = it.next();
            b.append(next);
            if (it.hasNext()) {
                b.append(',');
            }
        }
        return b.toString();
    }
}
