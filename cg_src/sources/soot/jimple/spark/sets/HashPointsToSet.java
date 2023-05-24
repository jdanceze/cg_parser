package soot.jimple.spark.sets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import soot.Type;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/sets/HashPointsToSet.class */
public final class HashPointsToSet extends PointsToSetInternal {
    private final HashSet<Node> s;
    private PAG pag;

    public HashPointsToSet(Type type, PAG pag) {
        super(type);
        this.s = new HashSet<>(4);
        this.pag = null;
        this.pag = pag;
    }

    @Override // soot.PointsToSet
    public final boolean isEmpty() {
        return this.s.isEmpty();
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public final boolean addAll(PointsToSetInternal other, PointsToSetInternal exclude) {
        if ((other instanceof HashPointsToSet) && exclude == null && (this.pag.getTypeManager().getFastHierarchy() == null || this.type == null || this.type.equals(other.type))) {
            return this.s.addAll(((HashPointsToSet) other).s);
        }
        return super.addAll(other, exclude);
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public final boolean forall(P2SetVisitor v) {
        Iterator<Node> it = new ArrayList(this.s).iterator();
        while (it.hasNext()) {
            v.visit(it.next());
        }
        return v.getReturnValue();
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public final boolean add(Node n) {
        if (this.pag.getTypeManager().castNeverFails(n.getType(), this.type)) {
            return this.s.add(n);
        }
        return false;
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public final boolean contains(Node n) {
        return this.s.contains(n);
    }

    public static P2SetFactory getFactory() {
        return new P2SetFactory() { // from class: soot.jimple.spark.sets.HashPointsToSet.1
            @Override // soot.jimple.spark.sets.P2SetFactory
            public PointsToSetInternal newSet(Type type, PAG pag) {
                return new HashPointsToSet(type, pag);
            }
        };
    }
}
