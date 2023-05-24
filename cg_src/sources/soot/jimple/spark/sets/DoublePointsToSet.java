package soot.jimple.spark.sets;

import java.util.HashSet;
import java.util.Set;
import soot.G;
import soot.PointsToSet;
import soot.Type;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/sets/DoublePointsToSet.class */
public class DoublePointsToSet extends PointsToSetInternal {
    private static P2SetFactory defaultP2SetFactory = new P2SetFactory() { // from class: soot.jimple.spark.sets.DoublePointsToSet.1
        @Override // soot.jimple.spark.sets.P2SetFactory
        public PointsToSetInternal newSet(Type type, PAG pag) {
            return new DoublePointsToSet(type, pag);
        }
    };
    private PAG pag;
    protected PointsToSetInternal newSet;
    protected PointsToSetInternal oldSet;

    public DoublePointsToSet(Type type, PAG pag) {
        super(type);
        this.newSet = G.v().newSetFactory.newSet(type, pag);
        this.oldSet = G.v().oldSetFactory.newSet(type, pag);
        this.pag = pag;
    }

    @Override // soot.PointsToSet
    public boolean isEmpty() {
        return this.oldSet.isEmpty() && this.newSet.isEmpty();
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal, soot.PointsToSet
    public boolean hasNonEmptyIntersection(PointsToSet other) {
        return this.oldSet.hasNonEmptyIntersection(other) || this.newSet.hasNonEmptyIntersection(other);
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal, soot.PointsToSet
    public Set<Type> possibleTypes() {
        Set<Type> ret = new HashSet<>();
        ret.addAll(this.oldSet.possibleTypes());
        ret.addAll(this.newSet.possibleTypes());
        return ret;
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public boolean addAll(PointsToSetInternal other, PointsToSetInternal exclude) {
        if (exclude != null) {
            throw new RuntimeException("NYI");
        }
        return this.newSet.addAll(other, this.oldSet);
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public boolean forall(P2SetVisitor v) {
        this.oldSet.forall(v);
        this.newSet.forall(v);
        return v.getReturnValue();
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public boolean add(Node n) {
        if (this.oldSet.contains(n)) {
            return false;
        }
        return this.newSet.add(n);
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public PointsToSetInternal getOldSet() {
        return this.oldSet;
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public PointsToSetInternal getNewSet() {
        return this.newSet;
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public void flushNew() {
        this.oldSet.addAll(this.newSet, null);
        this.newSet = G.v().newSetFactory.newSet(this.type, this.pag);
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public void unFlushNew() {
        this.newSet.addAll(this.oldSet, null);
        this.oldSet = G.v().oldSetFactory.newSet(this.type, this.pag);
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public void mergeWith(PointsToSetInternal other) {
        if (!(other instanceof DoublePointsToSet)) {
            throw new RuntimeException("NYI");
        }
        final DoublePointsToSet o = (DoublePointsToSet) other;
        if ((other.type != null && !other.type.equals(this.type)) || (other.type == null && this.type != null)) {
            throw new RuntimeException("different types " + this.type + " and " + other.type);
        }
        PointsToSetInternal newNewSet = G.v().newSetFactory.newSet(this.type, this.pag);
        final PointsToSetInternal newOldSet = G.v().oldSetFactory.newSet(this.type, this.pag);
        this.oldSet.forall(new P2SetVisitor() { // from class: soot.jimple.spark.sets.DoublePointsToSet.2
            @Override // soot.jimple.spark.sets.P2SetVisitor
            public final void visit(Node n) {
                if (o.oldSet.contains(n)) {
                    newOldSet.add(n);
                }
            }
        });
        newNewSet.addAll(this, newOldSet);
        newNewSet.addAll(o, newOldSet);
        this.newSet = newNewSet;
        this.oldSet = newOldSet;
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public boolean contains(Node n) {
        return this.oldSet.contains(n) || this.newSet.contains(n);
    }

    public static P2SetFactory getFactory(P2SetFactory newFactory, P2SetFactory oldFactory) {
        G.v().newSetFactory = newFactory;
        G.v().oldSetFactory = oldFactory;
        return defaultP2SetFactory;
    }
}
