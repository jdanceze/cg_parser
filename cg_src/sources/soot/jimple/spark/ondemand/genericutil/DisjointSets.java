package soot.jimple.spark.ondemand.genericutil;

import java.util.Arrays;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/genericutil/DisjointSets.class */
public final class DisjointSets {
    private int[] array;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DisjointSets.class.desiredAssertionStatus();
    }

    public DisjointSets(int numElements) {
        this.array = new int[numElements];
        Arrays.fill(this.array, -1);
    }

    public void union(int root1, int root2) {
        if (!$assertionsDisabled && this.array[root1] >= 0) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && this.array[root2] >= 0) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && root1 == root2) {
            throw new AssertionError();
        }
        if (this.array[root2] < this.array[root1]) {
            int[] iArr = this.array;
            iArr[root2] = iArr[root2] + this.array[root1];
            this.array[root1] = root2;
            return;
        }
        int[] iArr2 = this.array;
        iArr2[root1] = iArr2[root1] + this.array[root2];
        this.array[root2] = root1;
    }

    public int find(int x) {
        if (this.array[x] < 0) {
            return x;
        }
        this.array[x] = find(this.array[x]);
        return this.array[x];
    }
}
