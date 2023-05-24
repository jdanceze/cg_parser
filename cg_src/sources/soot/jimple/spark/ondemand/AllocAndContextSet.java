package soot.jimple.spark.ondemand;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import soot.PointsToSet;
import soot.Type;
import soot.jimple.ClassConstant;
import soot.jimple.spark.ondemand.genericutil.ArraySet;
import soot.jimple.spark.ondemand.genericutil.ImmutableStack;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.ClassConstantNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.StringConstantNode;
import soot.jimple.spark.sets.EqualsSupportingPointsToSet;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/AllocAndContextSet.class */
public final class AllocAndContextSet extends ArraySet<AllocAndContext> implements EqualsSupportingPointsToSet {
    @Override // soot.PointsToSet
    public boolean hasNonEmptyIntersection(PointsToSet other) {
        if (other instanceof AllocAndContextSet) {
            return nonEmptyHelper((AllocAndContextSet) other);
        }
        if (other instanceof WrappedPointsToSet) {
            return hasNonEmptyIntersection(((WrappedPointsToSet) other).getWrapped());
        }
        if (other instanceof PointsToSetInternal) {
            return ((PointsToSetInternal) other).forall(new P2SetVisitor() { // from class: soot.jimple.spark.ondemand.AllocAndContextSet.1
                @Override // soot.jimple.spark.sets.P2SetVisitor
                public void visit(Node n) {
                    if (!this.returnValue) {
                        Iterator<AllocAndContext> it = AllocAndContextSet.this.iterator();
                        while (it.hasNext()) {
                            AllocAndContext allocAndContext = it.next();
                            if (n.equals(allocAndContext.alloc)) {
                                this.returnValue = true;
                                return;
                            }
                        }
                    }
                }
            });
        }
        throw new UnsupportedOperationException("can't check intersection with set of type " + other.getClass());
    }

    private boolean nonEmptyHelper(AllocAndContextSet other) {
        Iterator<AllocAndContext> it = other.iterator();
        while (it.hasNext()) {
            AllocAndContext otherAllocAndContext = it.next();
            Iterator<AllocAndContext> it2 = iterator();
            while (it2.hasNext()) {
                AllocAndContext myAllocAndContext = it2.next();
                if (otherAllocAndContext.alloc.equals(myAllocAndContext.alloc)) {
                    ImmutableStack<Integer> myContext = myAllocAndContext.context;
                    ImmutableStack<Integer> otherContext = otherAllocAndContext.context;
                    if (myContext.topMatches(otherContext) || otherContext.topMatches(myContext)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override // soot.PointsToSet
    public Set<ClassConstant> possibleClassConstants() {
        Set<ClassConstant> res = new HashSet<>();
        Iterator<AllocAndContext> it = iterator();
        while (it.hasNext()) {
            AllocAndContext allocAndContext = it.next();
            AllocNode n = allocAndContext.alloc;
            if (n instanceof ClassConstantNode) {
                res.add(((ClassConstantNode) n).getClassConstant());
            } else {
                return null;
            }
        }
        return res;
    }

    @Override // soot.PointsToSet
    public Set<String> possibleStringConstants() {
        Set<String> res = new HashSet<>();
        Iterator<AllocAndContext> it = iterator();
        while (it.hasNext()) {
            AllocAndContext allocAndContext = it.next();
            AllocNode n = allocAndContext.alloc;
            if (n instanceof StringConstantNode) {
                res.add(((StringConstantNode) n).getString());
            } else {
                return null;
            }
        }
        return res;
    }

    @Override // soot.PointsToSet
    public Set<Type> possibleTypes() {
        Set res = new HashSet();
        Iterator<AllocAndContext> it = iterator();
        while (it.hasNext()) {
            AllocAndContext allocAndContext = it.next();
            res.add(allocAndContext.alloc.getType());
        }
        return res;
    }

    @Override // soot.jimple.spark.sets.EqualsSupportingPointsToSet
    public int pointsToSetHashCode() {
        int result = 1;
        Iterator<AllocAndContext> it = iterator();
        while (it.hasNext()) {
            AllocAndContext elem = it.next();
            result = (31 * result) + elem.hashCode();
        }
        return result;
    }

    @Override // soot.jimple.spark.sets.EqualsSupportingPointsToSet
    public boolean pointsToSetEquals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AllocAndContextSet)) {
            return false;
        }
        AllocAndContextSet otherPts = (AllocAndContextSet) other;
        return superSetOf(otherPts, this) && superSetOf(this, otherPts);
    }

    private boolean superSetOf(AllocAndContextSet onePts, AllocAndContextSet otherPts) {
        return onePts.containsAll(otherPts);
    }
}
