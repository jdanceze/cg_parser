package soot.jimple.infoflow.taintWrappers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import soot.SootMethod;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AccessPath;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/taintWrappers/IdentityTaintWrapper.class */
public class IdentityTaintWrapper extends AbstractTaintWrapper {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !IdentityTaintWrapper.class.desiredAssertionStatus();
    }

    @Override // soot.jimple.infoflow.taintWrappers.AbstractTaintWrapper
    public Set<AccessPath> getTaintsForMethodInternal(Stmt stmt, AccessPath taintedPath) {
        if ($assertionsDisabled || stmt.containsInvokeExpr()) {
            if (!stmt.getInvokeExpr().getMethod().getDeclaringClass().isLibraryClass()) {
                return null;
            }
            if (taintedPath.isStaticFieldRef()) {
                return Collections.singleton(taintedPath);
            }
            Set<AccessPath> taints = new HashSet<>();
            taints.add(taintedPath);
            if (stmt.getInvokeExpr() instanceof InstanceInvokeExpr) {
                InstanceInvokeExpr iiExpr = (InstanceInvokeExpr) stmt.getInvokeExpr();
                if (taintedPath.getPlainValue().equals(iiExpr.getBase()) && (stmt instanceof AssignStmt)) {
                    taints.add(this.manager.getAccessPathFactory().createAccessPath(((AssignStmt) stmt).getLeftOp(), taintedPath.getTaintSubFields()));
                    return taints;
                }
            }
            for (Value param : stmt.getInvokeExpr().getArgs()) {
                if (taintedPath.getPlainValue().equals(param) && (stmt instanceof AssignStmt)) {
                    taints.add(this.manager.getAccessPathFactory().createAccessPath(((AssignStmt) stmt).getLeftOp(), taintedPath.getTaintSubFields()));
                    return taints;
                }
            }
            return taints;
        }
        throw new AssertionError();
    }

    @Override // soot.jimple.infoflow.taintWrappers.AbstractTaintWrapper
    public boolean isExclusiveInternal(Stmt stmt, AccessPath taintedPath) {
        if ($assertionsDisabled || stmt.containsInvokeExpr()) {
            if (stmt.getInvokeExpr() instanceof InstanceInvokeExpr) {
                InstanceInvokeExpr iiExpr = (InstanceInvokeExpr) stmt.getInvokeExpr();
                if (taintedPath.getPlainValue().equals(iiExpr.getBase())) {
                    return true;
                }
            }
            for (Value param : stmt.getInvokeExpr().getArgs()) {
                if (taintedPath.getPlainValue().equals(param)) {
                    return true;
                }
            }
            return false;
        }
        throw new AssertionError();
    }

    @Override // soot.jimple.infoflow.taintWrappers.ITaintPropagationWrapper
    public boolean supportsCallee(SootMethod method) {
        return true;
    }

    @Override // soot.jimple.infoflow.taintWrappers.ITaintPropagationWrapper
    public boolean supportsCallee(Stmt callSite) {
        return true;
    }

    @Override // soot.jimple.infoflow.taintWrappers.ITaintPropagationWrapper
    public Set<Abstraction> getAliasesForMethod(Stmt stmt, Abstraction d1, Abstraction taintedPath) {
        return null;
    }
}
