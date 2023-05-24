package soot.jimple.infoflow.collect;

import heros.solver.Pair;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/collect/IdentityPair.class */
public class IdentityPair<K, V> extends Pair<K, V> {
    protected int o1Hash;
    protected int o2Hash;

    public IdentityPair(K o1, V o2) {
        super(o1, o2);
        this.o1Hash = System.identityHashCode(o1);
        this.o2Hash = System.identityHashCode(o2);
    }

    @Override // heros.solver.Pair
    public boolean equals(Object other) {
        if (other == null || !(other instanceof IdentityPair)) {
            return false;
        }
        IdentityPair pair = (IdentityPair) other;
        return this.o1 == pair.o1 && this.o2 == pair.o2;
    }

    @Override // heros.solver.Pair
    public int hashCode() {
        return (31 * this.o1Hash) + (7 * this.o2Hash);
    }

    @Override // heros.solver.Pair
    public void setO1(K o1) {
        super.setO1(o1);
        this.o1Hash = System.identityHashCode(o1);
    }

    @Override // heros.solver.Pair
    public void setO2(V o2) {
        super.setO2(o2);
        this.o2Hash = System.identityHashCode(o2);
    }
}
