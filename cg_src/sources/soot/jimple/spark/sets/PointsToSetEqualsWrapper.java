package soot.jimple.spark.sets;

import java.util.Set;
import soot.PointsToSet;
import soot.Type;
import soot.jimple.ClassConstant;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/sets/PointsToSetEqualsWrapper.class */
public class PointsToSetEqualsWrapper implements PointsToSet {
    protected EqualsSupportingPointsToSet pts;

    public PointsToSetEqualsWrapper(EqualsSupportingPointsToSet pts) {
        this.pts = pts;
    }

    public EqualsSupportingPointsToSet unwarp() {
        return this.pts;
    }

    public int hashCode() {
        return this.pts.pointsToSetHashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj || this.pts == obj) {
            return true;
        }
        return this.pts.pointsToSetEquals(unwrapIfNecessary(obj));
    }

    @Override // soot.PointsToSet
    public boolean hasNonEmptyIntersection(PointsToSet other) {
        return this.pts.hasNonEmptyIntersection((PointsToSet) unwrapIfNecessary(other));
    }

    @Override // soot.PointsToSet
    public boolean isEmpty() {
        return this.pts.isEmpty();
    }

    @Override // soot.PointsToSet
    public Set<ClassConstant> possibleClassConstants() {
        return this.pts.possibleClassConstants();
    }

    @Override // soot.PointsToSet
    public Set<String> possibleStringConstants() {
        return this.pts.possibleStringConstants();
    }

    @Override // soot.PointsToSet
    public Set<Type> possibleTypes() {
        return this.pts.possibleTypes();
    }

    protected Object unwrapIfNecessary(Object obj) {
        if (obj instanceof PointsToSetEqualsWrapper) {
            PointsToSetEqualsWrapper wrapper = (PointsToSetEqualsWrapper) obj;
            obj = wrapper.pts;
        }
        return obj;
    }

    public String toString() {
        return this.pts.toString();
    }
}
