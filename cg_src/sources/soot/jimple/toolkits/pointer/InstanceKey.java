package soot.jimple.toolkits.pointer;

import soot.Local;
import soot.PointsToAnalysis;
import soot.PointsToSet;
import soot.RefLikeType;
import soot.Scene;
import soot.SootMethod;
import soot.jimple.Stmt;
import soot.jimple.spark.sets.EqualsSupportingPointsToSet;
import soot.jimple.spark.sets.PointsToSetEqualsWrapper;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/InstanceKey.class */
public class InstanceKey {
    protected final Local assignedLocal;
    protected final LocalMustAliasAnalysis lmaa;
    protected final LocalMustNotAliasAnalysis lnma;
    protected final Stmt stmtAfterAssignStmt;
    protected final SootMethod owner;
    protected final int hashCode;
    protected final PointsToSet pts;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !InstanceKey.class.desiredAssertionStatus();
    }

    public InstanceKey(Local local, Stmt stmt, SootMethod owner, LocalMustAliasAnalysis lmaa, LocalMustNotAliasAnalysis lmna) {
        this.assignedLocal = local;
        this.owner = owner;
        this.stmtAfterAssignStmt = stmt;
        this.lmaa = lmaa;
        this.lnma = lmna;
        PointsToAnalysis pta = Scene.v().getPointsToAnalysis();
        this.pts = new PointsToSetEqualsWrapper((EqualsSupportingPointsToSet) pta.reachingObjects(local));
        this.hashCode = computeHashCode();
    }

    public boolean mustAlias(InstanceKey otherKey) {
        if (this.stmtAfterAssignStmt == null || otherKey.stmtAfterAssignStmt == null) {
            return false;
        }
        return this.lmaa.mustAlias(this.assignedLocal, this.stmtAfterAssignStmt, otherKey.assignedLocal, otherKey.stmtAfterAssignStmt);
    }

    public boolean mayNotAlias(InstanceKey otherKey) {
        if (!this.owner.equals(otherKey.owner) || this.stmtAfterAssignStmt == null || otherKey.stmtAfterAssignStmt == null || !this.lnma.notMayAlias(this.assignedLocal, this.stmtAfterAssignStmt, otherKey.assignedLocal, otherKey.stmtAfterAssignStmt)) {
            return (Scene.v().getPointsToAnalysis() == null || this.pts.hasNonEmptyIntersection(otherKey.pts)) ? false : true;
        }
        return true;
    }

    public PointsToSet getPointsToSet() {
        return this.pts;
    }

    public Local getLocal() {
        return this.assignedLocal;
    }

    public boolean haveLocalInformation() {
        return this.stmtAfterAssignStmt != null;
    }

    public String toString() {
        String instanceKeyString = this.stmtAfterAssignStmt != null ? this.lmaa.instanceKeyString(this.assignedLocal, this.stmtAfterAssignStmt) : "pts(" + this.hashCode + ")";
        return String.valueOf(instanceKeyString) + "(" + this.assignedLocal.getName() + ")";
    }

    public int hashCode() {
        return this.hashCode;
    }

    protected int computeHashCode() {
        int result = (31 * 1) + (this.owner == null ? 0 : this.owner.hashCode());
        if (this.stmtAfterAssignStmt != null && (this.assignedLocal.getType() instanceof RefLikeType)) {
            result = (31 * result) + this.lmaa.instanceKeyString(this.assignedLocal, this.stmtAfterAssignStmt).hashCode();
        } else if (this.stmtAfterAssignStmt == null) {
            result = (31 * result) + this.pts.hashCode();
        }
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        InstanceKey other = (InstanceKey) obj;
        if (this.owner == null) {
            if (other.owner != null) {
                return false;
            }
        } else if (!this.owner.equals(other.owner)) {
            return false;
        }
        if (mustAlias(other)) {
            return true;
        }
        return this.stmtAfterAssignStmt == null && other.stmtAfterAssignStmt == null && this.pts.equals(other.pts);
    }

    public boolean isOfReferenceType() {
        if ($assertionsDisabled || (this.assignedLocal.getType() instanceof RefLikeType)) {
            return true;
        }
        throw new AssertionError();
    }

    public SootMethod getOwner() {
        return this.owner;
    }

    public Stmt getStmt() {
        return this.stmtAfterAssignStmt;
    }
}
