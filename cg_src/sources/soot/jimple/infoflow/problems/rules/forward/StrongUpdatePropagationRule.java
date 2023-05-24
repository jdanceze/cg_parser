package soot.jimple.infoflow.problems.rules.forward;

import java.util.Collection;
import java.util.Iterator;
import soot.Local;
import soot.SootMethod;
import soot.ValueBox;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.aliasing.Aliasing;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.problems.TaintPropagationResults;
import soot.jimple.infoflow.problems.rules.AbstractTaintPropagationRule;
import soot.jimple.infoflow.util.ByReferenceBoolean;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/problems/rules/forward/StrongUpdatePropagationRule.class */
public class StrongUpdatePropagationRule extends AbstractTaintPropagationRule {
    public StrongUpdatePropagationRule(InfoflowManager manager, Abstraction zeroValue, TaintPropagationResults results) {
        super(manager, zeroValue, results);
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateNormalFlow(Abstraction d1, Abstraction source, Stmt stmt, Stmt destStmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        boolean baseAliases;
        if (!(stmt instanceof AssignStmt)) {
            return null;
        }
        AssignStmt assignStmt = (AssignStmt) stmt;
        if (assignStmt.getLeftOp() instanceof ArrayRef) {
            return null;
        }
        if (!source.isAbstractionActive() && source.getCurrentStmt() == stmt) {
            return null;
        }
        if (source.getPredecessor() != null && !source.getPredecessor().isAbstractionActive() && source.isAbstractionActive() && source.getPredecessor().getActivationUnit() == stmt && source.getAccessPath().equals(source.getPredecessor().getAccessPath())) {
            return null;
        }
        if (source.getAccessPath().isInstanceFieldRef()) {
            if (assignStmt.getLeftOp() instanceof InstanceFieldRef) {
                InstanceFieldRef leftRef = (InstanceFieldRef) assignStmt.getLeftOp();
                if (source.isAbstractionActive()) {
                    baseAliases = getAliasing().mustAlias((Local) leftRef.getBase(), source.getAccessPath().getPlainValue(), assignStmt);
                } else {
                    baseAliases = leftRef.getBase() == source.getAccessPath().getPlainValue();
                }
                if (baseAliases && getAliasing().mustAlias(leftRef.getField(), source.getAccessPath().getFirstField())) {
                    killAll.value = true;
                    return null;
                }
                return null;
            } else if ((assignStmt.getLeftOp() instanceof Local) && getAliasing().mustAlias((Local) assignStmt.getLeftOp(), source.getAccessPath().getPlainValue(), stmt)) {
                killAll.value = true;
                return null;
            } else {
                return null;
            }
        } else if (source.getAccessPath().isStaticFieldRef()) {
            if ((assignStmt.getLeftOp() instanceof StaticFieldRef) && getAliasing().mustAlias(((StaticFieldRef) assignStmt.getLeftOp()).getField(), source.getAccessPath().getFirstField())) {
                killAll.value = true;
                return null;
            }
            return null;
        } else if (source.getAccessPath().isLocal() && (assignStmt.getLeftOp() instanceof Local) && assignStmt.getLeftOp() == source.getAccessPath().getPlainValue()) {
            boolean found = false;
            Iterator<ValueBox> it = assignStmt.getRightOp().getUseBoxes().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                ValueBox vb = it.next();
                if (vb.getValue() == source.getAccessPath().getPlainValue()) {
                    found = true;
                    break;
                }
            }
            killAll.value = !found;
            killSource.value = true;
            return null;
        } else {
            return null;
        }
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateCallFlow(Abstraction d1, Abstraction source, Stmt stmt, SootMethod dest, ByReferenceBoolean killAll) {
        return null;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateCallToReturnFlow(Abstraction d1, Abstraction source, Stmt stmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        AccessPath ap;
        if ((stmt instanceof AssignStmt) && (ap = source.getAccessPath()) != null) {
            AssignStmt assignStmt = (AssignStmt) stmt;
            Aliasing aliasing = getAliasing();
            if (aliasing != null && !ap.isStaticFieldRef() && (assignStmt.getLeftOp() instanceof Local) && aliasing.mayAlias(assignStmt.getLeftOp(), ap.getPlainValue())) {
                killSource.value = true;
                return null;
            }
            return null;
        }
        return null;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateReturnFlow(Collection<Abstraction> callerD1s, Abstraction calleeD1, Abstraction source, Stmt stmt, Stmt retSite, Stmt callSite, ByReferenceBoolean killAll) {
        return null;
    }
}
