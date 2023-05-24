package soot.jimple.infoflow.problems.rules.forward;

import java.util.Collection;
import java.util.Collections;
import soot.SootMethod;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.DefinitionStmt;
import soot.jimple.Stmt;
import soot.jimple.ThrowStmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.problems.TaintPropagationResults;
import soot.jimple.infoflow.problems.rules.AbstractTaintPropagationRule;
import soot.jimple.infoflow.util.ByReferenceBoolean;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/problems/rules/forward/ExceptionPropagationRule.class */
public class ExceptionPropagationRule extends AbstractTaintPropagationRule {
    public ExceptionPropagationRule(InfoflowManager manager, Abstraction zeroValue, TaintPropagationResults results) {
        super(manager, zeroValue, results);
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateNormalFlow(Abstraction d1, Abstraction source, Stmt stmt, Stmt destStmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        if (source == getZeroValue()) {
            return null;
        }
        if (source.getExceptionThrown() && (stmt instanceof DefinitionStmt)) {
            DefinitionStmt def = (DefinitionStmt) stmt;
            if (def.getRightOp() instanceof CaughtExceptionRef) {
                killSource.value = true;
                AccessPath ap = getManager().getAccessPathFactory().copyWithNewValue(source.getAccessPath(), def.getLeftOp());
                if (ap == null) {
                    return null;
                }
                return Collections.singleton(source.deriveNewAbstractionOnCatch(ap));
            }
        }
        if (stmt instanceof ThrowStmt) {
            ThrowStmt throwStmt = (ThrowStmt) stmt;
            if (getAliasing().mayAlias(throwStmt.getOp(), source.getAccessPath().getPlainValue())) {
                killSource.value = true;
                return Collections.singleton(source.deriveNewAbstractionOnThrow(throwStmt));
            }
            return null;
        }
        return null;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateCallToReturnFlow(Abstraction d1, Abstraction source, Stmt stmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        return null;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateReturnFlow(Collection<Abstraction> callerD1s, Abstraction calleeD1, Abstraction source, Stmt stmt, Stmt retSite, Stmt callSite, ByReferenceBoolean killAll) {
        if ((stmt instanceof ThrowStmt) && (retSite instanceof DefinitionStmt)) {
            DefinitionStmt defRetStmt = (DefinitionStmt) retSite;
            if (defRetStmt.getRightOp() instanceof CaughtExceptionRef) {
                ThrowStmt throwStmt = (ThrowStmt) stmt;
                if (getAliasing().mayAlias(throwStmt.getOp(), source.getAccessPath().getPlainValue())) {
                    return Collections.singleton(source.deriveNewAbstractionOnThrow(throwStmt));
                }
                return null;
            }
            return null;
        }
        return null;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateCallFlow(Abstraction d1, Abstraction source, Stmt stmt, SootMethod dest, ByReferenceBoolean killAll) {
        return null;
    }
}
