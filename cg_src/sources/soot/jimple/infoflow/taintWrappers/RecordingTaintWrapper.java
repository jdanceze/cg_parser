package soot.jimple.infoflow.taintWrappers;

import java.util.HashSet;
import java.util.Set;
import soot.SootMethod;
import soot.jimple.Stmt;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AccessPath;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/taintWrappers/RecordingTaintWrapper.class */
public class RecordingTaintWrapper extends AbstractTaintWrapper {
    private final Set<SootMethod> targetMethods = new HashSet();

    @Override // soot.jimple.infoflow.taintWrappers.ITaintPropagationWrapper
    public Set<Abstraction> getAliasesForMethod(Stmt stmt, Abstraction d1, Abstraction taintedPath) {
        return null;
    }

    @Override // soot.jimple.infoflow.taintWrappers.ITaintPropagationWrapper
    public boolean supportsCallee(SootMethod method) {
        return false;
    }

    @Override // soot.jimple.infoflow.taintWrappers.ITaintPropagationWrapper
    public boolean supportsCallee(Stmt callSite) {
        return false;
    }

    @Override // soot.jimple.infoflow.taintWrappers.AbstractTaintWrapper
    protected boolean isExclusiveInternal(Stmt stmt, AccessPath taintedPath) {
        return false;
    }

    @Override // soot.jimple.infoflow.taintWrappers.AbstractTaintWrapper
    public Set<AccessPath> getTaintsForMethodInternal(Stmt stmt, AccessPath taintedPath) {
        if (stmt.containsInvokeExpr()) {
            this.targetMethods.add(stmt.getInvokeExpr().getMethod());
            return null;
        }
        return null;
    }

    public Set<SootMethod> getCallees() {
        return this.targetMethods;
    }
}
