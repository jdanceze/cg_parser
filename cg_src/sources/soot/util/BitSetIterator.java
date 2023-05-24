package soot.util;

import java.util.NoSuchElementException;
/* loaded from: gencallgraphv3.jar:soot/util/BitSetIterator.class */
public class BitSetIterator {
    long[] bits;
    int index = 0;
    long save;
    static final int[] lookup = {-1, 0, 1, 39, 2, 15, 40, 23, 3, 12, 16, 59, 41, 19, 24, 54, 4, -1, 13, 10, 17, 62, 60, 28, 42, 30, 20, 51, 25, 44, 55, 47, 5, 32, -1, 38, 14, 22, 11, 58, 18, 53, -1, 9, 61, 27, 29, 50, 43, 46, 31, 37, 21, 57, 52, 8, 26, 49, 45, 36, 56, 7, 48, 35, 6, 34, 33};

    /* JADX INFO: Access modifiers changed from: package-private */
    public BitSetIterator(long[] bits) {
        this.save = 0L;
        this.bits = bits;
        while (this.index < bits.length && bits[this.index] == 0) {
            this.index++;
        }
        if (this.index < bits.length) {
            this.save = bits[this.index];
        }
    }

    public boolean hasNext() {
        return this.index < this.bits.length;
    }

    public int next() {
        if (this.index >= this.bits.length) {
            throw new NoSuchElementException();
        }
        long k = this.save & (this.save - 1);
        long diff = this.save ^ k;
        this.save = k;
        int result = diff < 0 ? (64 * this.index) + 63 : (64 * this.index) + lookup[(int) (diff % 67)];
        if (this.save == 0) {
            this.index++;
            while (this.index < this.bits.length && this.bits[this.index] == 0) {
                this.index++;
            }
            if (this.index < this.bits.length) {
                this.save = this.bits[this.index];
            }
        }
        return result;
    }
}
