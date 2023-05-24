package soot.jimple.infoflow.problems.rules.backward;

import heros.solver.PathEdge;
import java.util.Collection;
import soot.JavaMethods;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.NewExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.aliasing.Aliasing;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.problems.TaintPropagationResults;
import soot.jimple.infoflow.problems.rules.AbstractTaintPropagationRule;
import soot.jimple.infoflow.util.BaseSelector;
import soot.jimple.infoflow.util.ByReferenceBoolean;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/problems/rules/backward/BackwardsClinitRule.class */
public class BackwardsClinitRule extends AbstractTaintPropagationRule {
    public BackwardsClinitRule(InfoflowManager manager, Abstraction zeroValue, TaintPropagationResults results) {
        super(manager, zeroValue, results);
    }

    private void propagateToClinit(Abstraction d1, Abstraction abs, SootMethod callee) {
        Collection<Unit> startPoints = this.manager.getICFG().getStartPointsOf(callee);
        for (Unit startPoint : startPoints) {
            this.manager.getForwardSolver().processEdge(new PathEdge<>(d1, startPoint, abs));
        }
    }

    private boolean containsStaticField(SootMethod callee, Abstraction abs) {
        return this.manager.getICFG().isStaticFieldUsed(callee, abs.getAccessPath().getFirstField()) || this.manager.getICFG().isStaticFieldRead(callee, abs.getAccessPath().getFirstField());
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateNormalFlow(Abstraction d1, Abstraction source, Stmt stmt, Stmt destStmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        if (!(stmt instanceof AssignStmt)) {
            return null;
        }
        AssignStmt assignStmt = (AssignStmt) stmt;
        AccessPath ap = source.getAccessPath();
        Aliasing aliasing = getAliasing();
        if (aliasing == null) {
            return null;
        }
        Collection<SootMethod> callees = this.manager.getICFG().getCalleesOfCallAt(stmt);
        SootMethod callee = callees.stream().filter(c -> {
            return c.hasActiveBody() && c.getSubSignature().equals(JavaMethods.SIG_CLINIT);
        }).findAny().orElse(null);
        if (callee == null) {
            return null;
        }
        Value leftOp = assignStmt.getLeftOp();
        boolean leftSideMatches = Aliasing.baseMatches(BaseSelector.selectBase(leftOp, false), source);
        Value rightOp = assignStmt.getRightOp();
        Value rightVal = BaseSelector.selectBase(assignStmt.getRightOp(), false);
        SootClass declaringClassMethod = this.manager.getICFG().getMethodOf(assignStmt).getDeclaringClass();
        Abstraction newAbs = null;
        if (leftSideMatches && (rightOp instanceof StaticFieldRef)) {
            SootClass declaringClassOp = ((StaticFieldRef) rightOp).getField().getDeclaringClass();
            if (declaringClassMethod == declaringClassOp) {
                return null;
            }
            AccessPath newAp = this.manager.getAccessPathFactory().copyWithNewValue(ap, rightVal, rightVal.getType(), false);
            newAbs = source.deriveNewAbstraction(newAp, stmt);
        } else if (ap.isStaticFieldRef() && (rightOp instanceof NewExpr)) {
            SootClass declaringClassOp2 = ((NewExpr) rightOp).getBaseType().getSootClass();
            if (declaringClassMethod == declaringClassOp2) {
                return null;
            }
            newAbs = source.deriveNewAbstraction(source.getAccessPath(), stmt);
        }
        if (newAbs != null && containsStaticField(callee, newAbs)) {
            newAbs.setCorrespondingCallSite(assignStmt);
            propagateToClinit(d1, newAbs, callee);
            return null;
        }
        return null;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateCallFlow(Abstraction d1, Abstraction source, Stmt stmt, SootMethod dest, ByReferenceBoolean killAll) {
        return null;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateCallToReturnFlow(Abstraction d1, Abstraction source, Stmt stmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        return null;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateReturnFlow(Collection<Abstraction> callerD1s, Abstraction calleeD1, Abstraction source, Stmt stmt, Stmt retSite, Stmt callSite, ByReferenceBoolean killAll) {
        Stmt callStmt = source.getCorrespondingCallSite();
        if (this.manager.getICFG().getMethodOf(stmt).getSubSignature().equals(JavaMethods.SIG_CLINIT) && (callStmt instanceof AssignStmt) && (((AssignStmt) callStmt).getRightOp() instanceof StaticFieldRef)) {
            killAll.value = true;
            return null;
        }
        return null;
    }
}
