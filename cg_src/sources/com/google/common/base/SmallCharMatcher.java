package com.google.common.base;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.CharMatcher;
import java.util.BitSet;
@GwtIncompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/base/SmallCharMatcher.class */
final class SmallCharMatcher extends CharMatcher.NamedFastMatcher {
    static final int MAX_SIZE = 1023;
    private final char[] table;
    private final boolean containsZero;
    private final long filter;
    private static final int C1 = -862048943;
    private static final int C2 = 461845907;
    private static final double DESIRED_LOAD_FACTOR = 0.5d;

    private SmallCharMatcher(char[] table, long filter, boolean containsZero, String description) {
        super(description);
        this.table = table;
        this.filter = filter;
        this.containsZero = containsZero;
    }

    static int smear(int hashCode) {
        return C2 * Integer.rotateLeft(hashCode * C1, 15);
    }

    private boolean checkFilter(int c) {
        return 1 == (1 & (this.filter >> c));
    }

    @VisibleForTesting
    static int chooseTableSize(int setSize) {
        if (setSize == 1) {
            return 2;
        }
        int highestOneBit = Integer.highestOneBit(setSize - 1);
        while (true) {
            int tableSize = highestOneBit << 1;
            if (tableSize * DESIRED_LOAD_FACTOR < setSize) {
                highestOneBit = tableSize;
            } else {
                return tableSize;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static CharMatcher from(BitSet chars, String description) {
        int index;
        long filter = 0;
        int size = chars.cardinality();
        boolean containsZero = chars.get(0);
        char[] table = new char[chooseTableSize(size)];
        int mask = table.length - 1;
        int nextSetBit = chars.nextSetBit(0);
        while (true) {
            int c = nextSetBit;
            if (c != -1) {
                filter |= 1 << c;
                int smear = smear(c);
                while (true) {
                    index = smear & mask;
                    if (table[index] == 0) {
                        break;
                    }
                    smear = index + 1;
                }
                table[index] = (char) c;
                nextSetBit = chars.nextSetBit(c + 1);
            } else {
                return new SmallCharMatcher(table, filter, containsZero, description);
            }
        }
    }

    @Override // com.google.common.base.CharMatcher
    public boolean matches(char c) {
        if (c == 0) {
            return this.containsZero;
        }
        if (!checkFilter(c)) {
            return false;
        }
        int mask = this.table.length - 1;
        int startingIndex = smear(c) & mask;
        int index = startingIndex;
        while (this.table[index] != 0) {
            if (this.table[index] == c) {
                return true;
            }
            index = (index + 1) & mask;
            if (index == startingIndex) {
                return false;
            }
        }
        return false;
    }

    @Override // com.google.common.base.CharMatcher
    void setBits(BitSet table) {
        char[] cArr;
        if (this.containsZero) {
            table.set(0);
        }
        for (char c : this.table) {
            if (c != 0) {
                table.set(c);
            }
        }
    }
}
