package soot.jimple.infoflow.problems.rules.backward;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.problems.TaintPropagationResults;
import soot.jimple.infoflow.problems.rules.AbstractTaintPropagationRule;
import soot.jimple.infoflow.sourcesSinks.manager.IReversibleSourceSinkManager;
import soot.jimple.infoflow.sourcesSinks.manager.SourceInfo;
import soot.jimple.infoflow.util.ByReferenceBoolean;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/problems/rules/backward/BackwardsSinkPropagationRule.class */
public class BackwardsSinkPropagationRule extends AbstractTaintPropagationRule {
    public BackwardsSinkPropagationRule(InfoflowManager manager, Abstraction zeroValue, TaintPropagationResults results) {
        super(manager, zeroValue, results);
    }

    private Collection<Abstraction> propagate(Abstraction source, Stmt stmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        if (source != getZeroValue() || !(this.manager.getSourceSinkManager() instanceof IReversibleSourceSinkManager)) {
            return null;
        }
        IReversibleSourceSinkManager ssm = (IReversibleSourceSinkManager) this.manager.getSourceSinkManager();
        SourceInfo sinkInfo = ssm.getInverseSinkInfo(stmt, getManager());
        killSource.value = true;
        if (sinkInfo != null && !sinkInfo.getAccessPaths().isEmpty()) {
            Optional<Unit> caller = this.manager.getICFG().getCallersOf(this.manager.getICFG().getMethodOf(stmt)).stream().findAny();
            if (caller.isPresent()) {
                Stmt callerStmt = (Stmt) caller.get();
                if (callerStmt.containsInvokeExpr() && this.manager.getTaintWrapper() != null && this.manager.getTaintWrapper().isExclusive(callerStmt, this.zeroValue)) {
                    return null;
                }
            }
            Set<Abstraction> res = new HashSet<>();
            for (AccessPath ap : sinkInfo.getAccessPaths()) {
                Abstraction abs = new Abstraction(sinkInfo.getDefinition(), ap, stmt, sinkInfo.getUserData(), false, false);
                abs.setCorrespondingCallSite(stmt);
                res.add(abs.deriveNewAbstractionWithTurnUnit(stmt));
            }
            return res;
        } else if (killAll != null) {
            killAll.value = true;
            return null;
        } else {
            return null;
        }
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateNormalFlow(Abstraction d1, Abstraction source, Stmt stmt, Stmt destStmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        return propagate(source, stmt, killSource, killAll);
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateCallToReturnFlow(Abstraction d1, Abstraction source, Stmt stmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        return propagate(source, stmt, killSource, null);
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateReturnFlow(Collection<Abstraction> callerD1s, Abstraction calleeD1, Abstraction source, Stmt stmt, Stmt retSite, Stmt callSite, ByReferenceBoolean killAll) {
        return null;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateCallFlow(Abstraction d1, Abstraction source, Stmt stmt, SootMethod dest, ByReferenceBoolean killAll) {
        if (!(this.manager.getSourceSinkManager() instanceof IReversibleSourceSinkManager)) {
            return null;
        }
        IReversibleSourceSinkManager ssm = (IReversibleSourceSinkManager) this.manager.getSourceSinkManager();
        if (!getManager().getConfig().getInspectSources() && getManager().getSourceSinkManager() != null) {
            SourceInfo sinkInfo = ssm.getInverseSinkInfo(stmt, getManager());
            if (sinkInfo != null) {
                killAll.value = true;
            }
        }
        if (!getManager().getConfig().getInspectSinks() && getManager().getSourceSinkManager() != null) {
            boolean isSource = ssm.getInverseSourceInfo(stmt, getManager(), source.getAccessPath()) != null;
            if (isSource) {
                killAll.value = true;
                return null;
            }
            return null;
        }
        return null;
    }
}
