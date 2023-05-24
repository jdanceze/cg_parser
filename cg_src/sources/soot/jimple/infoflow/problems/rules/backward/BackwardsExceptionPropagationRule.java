package soot.jimple.infoflow.problems.rules.backward;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.IdentityStmt;
import soot.jimple.Stmt;
import soot.jimple.ThrowStmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.aliasing.Aliasing;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.problems.TaintPropagationResults;
import soot.jimple.infoflow.problems.rules.AbstractTaintPropagationRule;
import soot.jimple.infoflow.util.ByReferenceBoolean;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/problems/rules/backward/BackwardsExceptionPropagationRule.class */
public class BackwardsExceptionPropagationRule extends AbstractTaintPropagationRule {
    public BackwardsExceptionPropagationRule(InfoflowManager manager, Abstraction zeroValue, TaintPropagationResults results) {
        super(manager, zeroValue, results);
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateNormalFlow(Abstraction d1, Abstraction source, Stmt stmt, Stmt destStmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        Aliasing aliasing = getAliasing();
        if (aliasing == null) {
            return null;
        }
        if (stmt instanceof IdentityStmt) {
            IdentityStmt id = (IdentityStmt) stmt;
            if ((id.getRightOp() instanceof CaughtExceptionRef) && (aliasing.mayAlias(id.getLeftOp(), source.getAccessPath().getPlainValue()) || source.getAccessPath().isEmpty())) {
                killSource.value = true;
                return Collections.singleton(source.deriveNewAbstractionOnThrow(id));
            }
        }
        if (source.getExceptionThrown() && (stmt instanceof ThrowStmt)) {
            killSource.value = true;
            AccessPath ap = this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), ((ThrowStmt) stmt).getOp());
            if (ap != null) {
                Abstraction abs = source.deriveNewAbstractionOnCatch(ap);
                if (this.manager.getConfig().getImplicitFlowMode().trackControlFlowDependencies() && abs.getDominator() == null) {
                    HashSet<Abstraction> res = new HashSet<>();
                    res.add(abs);
                    List<Unit> condUnits = this.manager.getICFG().getConditionalBranchIntraprocedural(stmt);
                    if (condUnits.size() >= 1) {
                        abs.setDominator(condUnits.get(0));
                        for (int i = 1; i < condUnits.size(); i++) {
                            res.add(abs.deriveNewAbstractionWithDominator(condUnits.get(i)));
                        }
                    }
                    return res;
                }
                return Collections.singleton(abs);
            }
            return null;
        }
        return null;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateCallToReturnFlow(Abstraction d1, Abstraction source, Stmt stmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        if (source.getExceptionThrown()) {
            killSource.value = true;
            return null;
        }
        return null;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateReturnFlow(Collection<Abstraction> callerD1s, Abstraction calleeD1, Abstraction source, Stmt stmt, Stmt retSite, Stmt callSite, ByReferenceBoolean killAll) {
        return null;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateCallFlow(Abstraction d1, Abstraction source, Stmt stmt, SootMethod dest, ByReferenceBoolean killAll) {
        if (source.getExceptionThrown()) {
            HashSet<Abstraction> res = new HashSet<>();
            Iterator<Unit> it = dest.getActiveBody().getUnits().iterator();
            while (it.hasNext()) {
                Unit unit = it.next();
                if (unit instanceof ThrowStmt) {
                    Value op = ((ThrowStmt) unit).getOp();
                    if (this.manager.getTypeUtils().checkCast(source.getAccessPath(), op.getType())) {
                        AccessPath ap = this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), op);
                        res.add(source.deriveNewAbstractionOnCatch(ap));
                    }
                }
            }
            if (res.isEmpty()) {
                return null;
            }
            return res;
        }
        return null;
    }
}
