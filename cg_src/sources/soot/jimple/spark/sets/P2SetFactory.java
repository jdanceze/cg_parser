package soot.jimple.spark.sets;

import soot.Type;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.sets.PointsToSetInternal;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/sets/P2SetFactory.class */
public abstract class P2SetFactory<T extends PointsToSetInternal> {
    public abstract T newSet(Type type, PAG pag);
}
