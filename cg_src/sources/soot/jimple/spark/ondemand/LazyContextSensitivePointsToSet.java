package soot.jimple.spark.ondemand;

import java.util.Set;
import soot.Local;
import soot.PointsToSet;
import soot.Type;
import soot.jimple.ClassConstant;
import soot.jimple.spark.sets.EqualsSupportingPointsToSet;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/LazyContextSensitivePointsToSet.class */
public class LazyContextSensitivePointsToSet implements EqualsSupportingPointsToSet {
    private EqualsSupportingPointsToSet delegate;
    private final DemandCSPointsTo demandCSPointsTo;
    private final Local local;
    private boolean isContextSensitive = false;

    public boolean isContextSensitive() {
        return this.isContextSensitive;
    }

    public LazyContextSensitivePointsToSet(Local l, EqualsSupportingPointsToSet contextInsensitiveSet, DemandCSPointsTo demandCSPointsTo) {
        this.local = l;
        this.delegate = contextInsensitiveSet;
        this.demandCSPointsTo = demandCSPointsTo;
    }

    @Override // soot.PointsToSet
    public boolean hasNonEmptyIntersection(PointsToSet other) {
        PointsToSet otherInner;
        if (other instanceof LazyContextSensitivePointsToSet) {
            otherInner = ((LazyContextSensitivePointsToSet) other).delegate;
        } else {
            otherInner = other;
        }
        if (this.delegate.hasNonEmptyIntersection(otherInner)) {
            if (other instanceof LazyContextSensitivePointsToSet) {
                ((LazyContextSensitivePointsToSet) other).computeContextSensitiveInfo();
                otherInner = ((LazyContextSensitivePointsToSet) other).delegate;
            }
            computeContextSensitiveInfo();
            return this.delegate.hasNonEmptyIntersection(otherInner);
        }
        return false;
    }

    public void computeContextSensitiveInfo() {
        if (!this.isContextSensitive) {
            this.delegate = (EqualsSupportingPointsToSet) this.demandCSPointsTo.doReachingObjects(this.local);
            this.isContextSensitive = true;
        }
    }

    @Override // soot.PointsToSet
    public boolean isEmpty() {
        return this.delegate.isEmpty();
    }

    @Override // soot.PointsToSet
    public Set<ClassConstant> possibleClassConstants() {
        return this.delegate.possibleClassConstants();
    }

    @Override // soot.PointsToSet
    public Set<String> possibleStringConstants() {
        return this.delegate.possibleStringConstants();
    }

    @Override // soot.PointsToSet
    public Set<Type> possibleTypes() {
        return this.delegate.possibleTypes();
    }

    @Override // soot.jimple.spark.sets.EqualsSupportingPointsToSet
    public boolean pointsToSetEquals(Object other) {
        if (!(other instanceof LazyContextSensitivePointsToSet)) {
            return false;
        }
        return ((LazyContextSensitivePointsToSet) other).delegate.equals(this.delegate);
    }

    @Override // soot.jimple.spark.sets.EqualsSupportingPointsToSet
    public int pointsToSetHashCode() {
        return this.delegate.pointsToSetHashCode();
    }

    public EqualsSupportingPointsToSet getDelegate() {
        return this.delegate;
    }
}
