package soot.util;
/* loaded from: gencallgraphv3.jar:soot/util/BitVector.class */
public class BitVector {
    private long[] bits;

    public BitVector() {
        this(64);
    }

    public BitVector(BitVector other) {
        long[] otherBits = other.bits;
        this.bits = new long[otherBits.length];
        System.arraycopy(otherBits, 0, this.bits, 0, otherBits.length);
    }

    public BitVector(int numBits) {
        int lastIndex = indexOf(numBits - 1);
        this.bits = new long[lastIndex + 1];
    }

    private int indexOf(int bit) {
        return bit >> 6;
    }

    private long mask(int bit) {
        return 1 << (bit & 63);
    }

    public void and(BitVector other) {
        if (this == other) {
            return;
        }
        long[] otherBits = other.bits;
        int numToAnd = otherBits.length;
        if (this.bits.length < numToAnd) {
            numToAnd = this.bits.length;
        }
        int i = 0;
        while (i < numToAnd) {
            this.bits[i] = this.bits[i] & otherBits[i];
            i++;
        }
        while (i < this.bits.length) {
            this.bits[i] = 0;
            i++;
        }
    }

    public void andNot(BitVector other) {
        long[] otherBits = other.bits;
        int numToAnd = otherBits.length;
        if (this.bits.length < numToAnd) {
            numToAnd = this.bits.length;
        }
        for (int i = 0; i < numToAnd; i++) {
            this.bits[i] = this.bits[i] & (otherBits[i] ^ (-1));
        }
    }

    public void clear(int bit) {
        if (indexOf(bit) < this.bits.length) {
            long[] jArr = this.bits;
            int indexOf = indexOf(bit);
            jArr[indexOf] = jArr[indexOf] & (mask(bit) ^ (-1));
        }
    }

    public Object clone() {
        try {
            BitVector ret = (BitVector) super.clone();
            System.arraycopy(this.bits, 0, ret.bits, 0, ret.bits.length);
            return ret;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean equals(Object o) {
        if (!(o instanceof BitVector)) {
            return false;
        }
        long[] otherBits = ((BitVector) o).bits;
        long[] longer = otherBits;
        int min = this.bits.length;
        if (otherBits.length < min) {
            min = otherBits.length;
            longer = this.bits;
        }
        int i = 0;
        while (i < min) {
            if (this.bits[i] == otherBits[i]) {
                i++;
            } else {
                return false;
            }
        }
        while (i < longer.length) {
            if (longer[i] == 0) {
                i++;
            } else {
                return false;
            }
        }
        return true;
    }

    public boolean get(int bit) {
        return indexOf(bit) < this.bits.length && (this.bits[indexOf(bit)] & mask(bit)) != 0;
    }

    public int hashCode() {
        long[] jArr;
        long ret = 0;
        for (long element : this.bits) {
            ret ^= element;
        }
        return (int) ((ret >> 32) ^ ret);
    }

    public int length() {
        int i = this.bits.length - 1;
        while (i >= 0 && this.bits[i] == 0) {
            i--;
        }
        if (i < 0) {
            return 0;
        }
        long j = this.bits[i];
        int i2 = (i + 1) << 6;
        long k = Long.MIN_VALUE;
        while ((k & j) == 0) {
            k >>= 1;
            i2--;
        }
        return i2;
    }

    public void copyFrom(BitVector other) {
        if (this == other) {
            return;
        }
        long[] otherBits = other.bits;
        int j = otherBits.length - 1;
        while (j >= 0 && otherBits[j] == 0) {
            j--;
        }
        expand(j << 6);
        while (j >= 0) {
            this.bits[j] = otherBits[j];
            j--;
        }
        for (int i = j + 1; i < this.bits.length; i++) {
            this.bits[i] = 0;
        }
    }

    public void or(BitVector other) {
        if (this == other) {
            return;
        }
        long[] otherBits = other.bits;
        int j = otherBits.length - 1;
        while (j >= 0 && otherBits[j] == 0) {
            j--;
        }
        expand(j << 6);
        while (j >= 0) {
            long[] jArr = this.bits;
            int i = j;
            jArr[i] = jArr[i] | otherBits[j];
            j--;
        }
    }

    public int cardinality() {
        int c = 0;
        long[] jArr = this.bits;
        int length = jArr.length;
        for (int i = 0; i < length; i++) {
            long v = jArr[i];
            while (v != 0) {
                v &= v - 1;
                c++;
            }
        }
        return c;
    }

    public boolean intersects(BitVector other) {
        long[] otherBits = other.bits;
        int numToCheck = otherBits.length;
        if (this.bits.length < numToCheck) {
            numToCheck = this.bits.length;
        }
        for (int i = 0; i < numToCheck; i++) {
            if ((this.bits[i] & otherBits[i]) != 0) {
                return true;
            }
        }
        return false;
    }

    private void expand(int bit) {
        int n = indexOf(bit) + 1;
        if (n <= this.bits.length) {
            return;
        }
        if (this.bits.length * 2 > n) {
            n = this.bits.length * 2;
        }
        long[] newBits = new long[n];
        System.arraycopy(this.bits, 0, newBits, 0, this.bits.length);
        this.bits = newBits;
    }

    public void xor(BitVector other) {
        if (this == other) {
            return;
        }
        long[] otherBits = other.bits;
        int j = otherBits.length - 1;
        while (j >= 0 && otherBits[j] == 0) {
            j--;
        }
        expand(j << 6);
        while (j >= 0) {
            long[] jArr = this.bits;
            int i = j;
            jArr[i] = jArr[i] ^ otherBits[j];
            j--;
        }
    }

    public boolean set(int bit) {
        expand(bit);
        boolean ret = !get(bit);
        long[] jArr = this.bits;
        int indexOf = indexOf(bit);
        jArr[indexOf] = jArr[indexOf] | mask(bit);
        return ret;
    }

    public int size() {
        return this.bits.length << 6;
    }

    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append('{');
        boolean start = true;
        BitSetIterator it = new BitSetIterator(this.bits);
        while (it.hasNext()) {
            int bit = it.next();
            if (start) {
                start = false;
            } else {
                ret.append(", ");
            }
            ret.append(bit);
        }
        ret.append('}');
        return ret.toString();
    }

    public boolean orAndAndNot(BitVector orset, BitVector andset, BitVector andnotset) {
        long[] b;
        int bl;
        long[] c;
        int cl;
        long[] d;
        int dl;
        long[] e;
        long[] a = this.bits;
        int al = a.length;
        if (orset == null) {
            b = null;
            bl = 0;
        } else {
            b = orset.bits;
            bl = b.length;
        }
        if (andset == null) {
            c = null;
            cl = 0;
        } else {
            c = andset.bits;
            cl = c.length;
        }
        if (andnotset == null) {
            d = null;
            dl = 0;
        } else {
            d = andnotset.bits;
            dl = d.length;
        }
        if (al < bl) {
            e = new long[bl];
            System.arraycopy(a, 0, e, 0, al);
            this.bits = e;
        } else {
            e = a;
        }
        boolean ret = false;
        if (c == null) {
            if (dl <= bl) {
                int i = 0;
                while (i < dl) {
                    long l = b[i] & (d[i] ^ (-1));
                    if ((l & (e[i] ^ (-1))) != 0) {
                        ret = true;
                    }
                    long[] jArr = e;
                    int i2 = i;
                    jArr[i2] = jArr[i2] | l;
                    i++;
                }
                while (i < bl) {
                    long l2 = b[i];
                    if ((l2 & (e[i] ^ (-1))) != 0) {
                        ret = true;
                    }
                    long[] jArr2 = e;
                    int i3 = i;
                    jArr2[i3] = jArr2[i3] | l2;
                    i++;
                }
            } else {
                for (int i4 = 0; i4 < bl; i4++) {
                    long l3 = b[i4] & (d[i4] ^ (-1));
                    if ((l3 & (e[i4] ^ (-1))) != 0) {
                        ret = true;
                    }
                    long[] jArr3 = e;
                    int i5 = i4;
                    jArr3[i5] = jArr3[i5] | l3;
                }
            }
        } else if (bl <= cl && bl <= dl) {
            for (int i6 = 0; i6 < bl; i6++) {
                long l4 = b[i6] & c[i6] & (d[i6] ^ (-1));
                if ((l4 & (e[i6] ^ (-1))) != 0) {
                    ret = true;
                }
                long[] jArr4 = e;
                int i7 = i6;
                jArr4[i7] = jArr4[i7] | l4;
            }
        } else if (cl <= bl && cl <= dl) {
            for (int i8 = 0; i8 < cl; i8++) {
                long l5 = b[i8] & c[i8] & (d[i8] ^ (-1));
                if ((l5 & (e[i8] ^ (-1))) != 0) {
                    ret = true;
                }
                long[] jArr5 = e;
                int i9 = i8;
                jArr5[i9] = jArr5[i9] | l5;
            }
        } else {
            int i10 = 0;
            while (i10 < dl) {
                long l6 = b[i10] & c[i10] & (d[i10] ^ (-1));
                if ((l6 & (e[i10] ^ (-1))) != 0) {
                    ret = true;
                }
                long[] jArr6 = e;
                int i11 = i10;
                jArr6[i11] = jArr6[i11] | l6;
                i10++;
            }
            int shorter = bl < cl ? bl : cl;
            while (i10 < shorter) {
                long l7 = b[i10] & c[i10];
                if ((l7 & (e[i10] ^ (-1))) != 0) {
                    ret = true;
                }
                long[] jArr7 = e;
                int i12 = i10;
                jArr7[i12] = jArr7[i12] | l7;
                i10++;
            }
        }
        return ret;
    }

    public static BitVector and(BitVector set1, BitVector set2) {
        int min = set1.size();
        int max = set2.size();
        if (min > max) {
            min = max;
        }
        BitVector ret = new BitVector(min);
        long[] retbits = ret.bits;
        long[] bits1 = set1.bits;
        long[] bits2 = set2.bits;
        int min2 = min >> 6;
        for (int i = 0; i < min2; i++) {
            retbits[i] = bits1[i] & bits2[i];
        }
        return ret;
    }

    public static BitVector or(BitVector set1, BitVector set2) {
        int min = set1.size();
        int max = set2.size();
        if (min > max) {
            min = max;
            max = set1.size();
        }
        BitVector ret = new BitVector(max);
        long[] retbits = ret.bits;
        long[] bits1 = set1.bits;
        long[] bits2 = set2.bits;
        int min2 = min >> 6;
        int max2 = max >> 6;
        for (int i = 0; i < min2; i++) {
            retbits[i] = bits1[i] | bits2[i];
        }
        if (bits1.length == min2) {
            System.arraycopy(bits2, min2, retbits, min2, max2 - min2);
        } else {
            System.arraycopy(bits1, min2, retbits, min2, max2 - min2);
        }
        return ret;
    }

    public BitSetIterator iterator() {
        return new BitSetIterator(this.bits);
    }
}
