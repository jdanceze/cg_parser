package soot.util;

import soot.G;
import soot.Singletons;
/* loaded from: gencallgraphv3.jar:soot/util/SharedBitSetCache.class */
public final class SharedBitSetCache {
    public static final int size = 32749;
    public BitVector[] cache = new BitVector[size];
    public BitVector[] orAndAndNotCache = new BitVector[size];

    public SharedBitSetCache(Singletons.Global g) {
    }

    public static SharedBitSetCache v() {
        return G.v().soot_util_SharedBitSetCache();
    }

    public BitVector canonicalize(BitVector set) {
        int hash = set.hashCode();
        if (hash < 0) {
            hash = -hash;
        }
        int hash2 = hash % size;
        BitVector hashed = this.cache[hash2];
        if (hashed != null && hashed.equals(set)) {
            return hashed;
        }
        this.cache[hash2] = set;
        return set;
    }
}
