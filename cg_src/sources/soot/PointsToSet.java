package soot;

import java.util.Set;
import soot.jimple.ClassConstant;
/* loaded from: gencallgraphv3.jar:soot/PointsToSet.class */
public interface PointsToSet {
    boolean isEmpty();

    boolean hasNonEmptyIntersection(PointsToSet pointsToSet);

    Set<Type> possibleTypes();

    Set<String> possibleStringConstants();

    Set<ClassConstant> possibleClassConstants();
}
