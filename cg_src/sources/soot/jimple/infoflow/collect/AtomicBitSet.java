package soot.jimple.infoflow.collect;

import java.util.concurrent.atomic.AtomicIntegerArray;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/collect/AtomicBitSet.class */
public class AtomicBitSet {
    private final AtomicIntegerArray array;

    public AtomicBitSet(int length) {
        int intLength = (length + 31) / 32;
        this.array = new AtomicIntegerArray(intLength);
    }

    public boolean set(long n) {
        int num;
        int num2;
        int bit = 1 << ((int) n);
        int idx = (int) (n >>> 5);
        do {
            num = this.array.get(idx);
            num2 = num | bit;
            if (num == num2) {
                return false;
            }
        } while (!this.array.compareAndSet(idx, num, num2));
        return true;
    }

    public int size() {
        return this.array.length();
    }

    public boolean get(long n) {
        int bit = 1 << ((int) n);
        int idx = (int) (n >>> 5);
        int num = this.array.get(idx);
        return (num & bit) != 0;
    }
}
