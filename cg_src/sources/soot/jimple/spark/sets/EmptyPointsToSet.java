package soot.jimple.spark.sets;

import java.util.Collections;
import java.util.Set;
import soot.G;
import soot.PointsToSet;
import soot.Singletons;
import soot.Type;
import soot.jimple.ClassConstant;
import soot.jimple.spark.pag.Node;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/sets/EmptyPointsToSet.class */
public class EmptyPointsToSet extends PointsToSetInternal {
    public EmptyPointsToSet(Singletons.Global g) {
        super(null);
    }

    public static EmptyPointsToSet v() {
        return G.v().soot_jimple_spark_sets_EmptyPointsToSet();
    }

    public EmptyPointsToSet(Singletons.Global g, Type type) {
        super(type);
    }

    @Override // soot.PointsToSet
    public boolean isEmpty() {
        return true;
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal, soot.PointsToSet
    public boolean hasNonEmptyIntersection(PointsToSet other) {
        return false;
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal, soot.PointsToSet
    public Set<Type> possibleTypes() {
        return Collections.emptySet();
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public boolean addAll(PointsToSetInternal other, PointsToSetInternal exclude) {
        throw new RuntimeException("can't add into empty immutable set");
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public boolean forall(P2SetVisitor v) {
        return false;
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public boolean add(Node n) {
        throw new RuntimeException("can't add into empty immutable set");
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public boolean contains(Node n) {
        return false;
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal, soot.PointsToSet
    public Set<String> possibleStringConstants() {
        return Collections.emptySet();
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal, soot.PointsToSet
    public Set<ClassConstant> possibleClassConstants() {
        return Collections.emptySet();
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public String toString() {
        return "{}";
    }
}
