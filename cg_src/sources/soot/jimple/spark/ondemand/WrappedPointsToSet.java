package soot.jimple.spark.ondemand;

import java.util.Set;
import soot.PointsToSet;
import soot.Type;
import soot.jimple.ClassConstant;
import soot.jimple.spark.sets.EqualsSupportingPointsToSet;
import soot.jimple.spark.sets.PointsToSetInternal;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/WrappedPointsToSet.class */
public class WrappedPointsToSet implements EqualsSupportingPointsToSet {
    final PointsToSetInternal wrapped;

    public PointsToSetInternal getWrapped() {
        return this.wrapped;
    }

    public WrappedPointsToSet(PointsToSetInternal wrapped) {
        this.wrapped = wrapped;
    }

    @Override // soot.PointsToSet
    public boolean hasNonEmptyIntersection(PointsToSet other) {
        if (other instanceof AllocAndContextSet) {
            return other.hasNonEmptyIntersection(this);
        }
        if (other instanceof WrappedPointsToSet) {
            return hasNonEmptyIntersection(((WrappedPointsToSet) other).getWrapped());
        }
        return this.wrapped.hasNonEmptyIntersection(other);
    }

    @Override // soot.PointsToSet
    public boolean isEmpty() {
        return this.wrapped.isEmpty();
    }

    @Override // soot.PointsToSet
    public Set<ClassConstant> possibleClassConstants() {
        return this.wrapped.possibleClassConstants();
    }

    @Override // soot.PointsToSet
    public Set<String> possibleStringConstants() {
        return this.wrapped.possibleStringConstants();
    }

    @Override // soot.PointsToSet
    public Set<Type> possibleTypes() {
        return this.wrapped.possibleTypes();
    }

    public String toString() {
        return this.wrapped.toString();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof WrappedPointsToSet) {
            WrappedPointsToSet wrapper = (WrappedPointsToSet) obj;
            return this.wrapped.equals(wrapper.wrapped);
        }
        return obj.equals(this.wrapped);
    }

    public int hashCode() {
        return this.wrapped.hashCode();
    }

    @Override // soot.jimple.spark.sets.EqualsSupportingPointsToSet
    public boolean pointsToSetEquals(Object other) {
        if (!(other instanceof EqualsSupportingPointsToSet)) {
            return false;
        }
        EqualsSupportingPointsToSet otherPts = (EqualsSupportingPointsToSet) unwrapIfNecessary(other);
        return this.wrapped.pointsToSetEquals(otherPts);
    }

    @Override // soot.jimple.spark.sets.EqualsSupportingPointsToSet
    public int pointsToSetHashCode() {
        return this.wrapped.pointsToSetHashCode();
    }

    protected Object unwrapIfNecessary(Object obj) {
        if (obj instanceof WrappedPointsToSet) {
            WrappedPointsToSet wrapper = (WrappedPointsToSet) obj;
            obj = wrapper.wrapped;
        }
        return obj;
    }
}
