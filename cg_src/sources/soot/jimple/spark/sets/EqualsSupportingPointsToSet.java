package soot.jimple.spark.sets;

import soot.PointsToSet;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/sets/EqualsSupportingPointsToSet.class */
public interface EqualsSupportingPointsToSet extends PointsToSet {
    int pointsToSetHashCode();

    boolean pointsToSetEquals(Object obj);
}
