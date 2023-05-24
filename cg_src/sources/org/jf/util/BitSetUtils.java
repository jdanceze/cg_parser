package org.jf.util;

import java.util.BitSet;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/util/BitSetUtils.class */
public class BitSetUtils {
    public static BitSet bitSetOfIndexes(int... indexes) {
        BitSet bitSet = new BitSet();
        for (int index : indexes) {
            bitSet.set(index);
        }
        return bitSet;
    }
}
