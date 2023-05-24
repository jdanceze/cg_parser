package soot.jimple.spark.sets;

import soot.jimple.spark.pag.Node;
import soot.util.BitVector;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/sets/PointsToBitVector.class */
public class PointsToBitVector extends BitVector {
    private int refCount;

    public PointsToBitVector(int size) {
        super(size);
        this.refCount = 0;
    }

    public boolean add(Node n) {
        int num = n.getNumber();
        if (!get(num)) {
            set(num);
            return true;
        }
        return false;
    }

    public boolean contains(Node n) {
        return get(n.getNumber());
    }

    public boolean isSubsetOf(PointsToBitVector other) {
        BitVector andResult = BitVector.and(this, other);
        return andResult.equals(this);
    }

    public PointsToBitVector(PointsToBitVector other) {
        super(other);
        this.refCount = 0;
    }

    public void incRefCount() {
        this.refCount++;
    }

    public void decRefCount() {
        this.refCount--;
    }

    public boolean unused() {
        return this.refCount == 0;
    }
}
