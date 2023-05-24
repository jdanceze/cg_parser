package soot.jimple.toolkits.pointer;

import java.util.Set;
import soot.PointsToSet;
import soot.jimple.ClassConstant;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/Union.class */
public abstract class Union implements PointsToSet {
    public abstract boolean addAll(PointsToSet pointsToSet);

    public static boolean hasNonEmptyIntersection(PointsToSet s1, PointsToSet s2) {
        if (s1 == null) {
            return false;
        }
        if (s1 instanceof Union) {
            return s1.hasNonEmptyIntersection(s2);
        }
        if (s2 == null) {
            return false;
        }
        return s2.hasNonEmptyIntersection(s1);
    }

    @Override // soot.PointsToSet
    public Set<String> possibleStringConstants() {
        return null;
    }

    @Override // soot.PointsToSet
    public Set<ClassConstant> possibleClassConstants() {
        return null;
    }
}
