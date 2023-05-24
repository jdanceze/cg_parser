package soot.jimple.infoflow.problems.rules.forward;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import soot.SootMethod;
import soot.ValueBox;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.problems.TaintPropagationResults;
import soot.jimple.infoflow.problems.rules.AbstractTaintPropagationRule;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.MethodSourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.manager.SourceInfo;
import soot.jimple.infoflow.typing.TypeUtils;
import soot.jimple.infoflow.util.ByReferenceBoolean;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/problems/rules/forward/SourcePropagationRule.class */
public class SourcePropagationRule extends AbstractTaintPropagationRule {
    public SourcePropagationRule(InfoflowManager manager, Abstraction zeroValue, TaintPropagationResults results) {
        super(manager, zeroValue, results);
    }

    private Collection<Abstraction> propagate(Abstraction d1, Abstraction source, Stmt stmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        SourceInfo sourceInfo;
        if (source == getZeroValue()) {
            if (getManager().getSourceSinkManager() != null) {
                sourceInfo = getManager().getSourceSinkManager().getSourceInfo(stmt, getManager());
            } else {
                sourceInfo = null;
            }
            SourceInfo sourceInfo2 = sourceInfo;
            killSource.value = true;
            if (sourceInfo2 != null && !sourceInfo2.getAccessPaths().isEmpty()) {
                Set<Abstraction> res = new HashSet<>();
                for (AccessPath ap : sourceInfo2.getAccessPaths()) {
                    Abstraction abs = new Abstraction(sourceInfo2.getDefinition(), ap, stmt, sourceInfo2.getUserData(), false, false);
                    res.add(abs);
                    for (ValueBox vb : stmt.getUseBoxes()) {
                        if (ap.startsWith(vb.getValue()) && (!TypeUtils.isStringType(vb.getValue().getType()) || ap.getCanHaveImmutableAliases())) {
                            getAliasing().computeAliases(d1, stmt, vb.getValue(), res, getManager().getICFG().getMethodOf(stmt), abs);
                        }
                    }
                    if (stmt.containsInvokeExpr()) {
                        abs.setCorrespondingCallSite(stmt);
                    }
                }
                return res;
            } else if (killAll != null) {
                killAll.value = true;
                return null;
            } else {
                return null;
            }
        }
        return null;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateNormalFlow(Abstraction d1, Abstraction source, Stmt stmt, Stmt destStmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        return propagate(d1, source, stmt, killSource, killAll);
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateCallToReturnFlow(Abstraction d1, Abstraction source, Stmt stmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        return propagate(d1, source, stmt, killSource, null);
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateReturnFlow(Collection<Abstraction> callerD1s, Abstraction calleeD1, Abstraction source, Stmt stmt, Stmt retSite, Stmt callSite, ByReferenceBoolean killAll) {
        return null;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateCallFlow(Abstraction d1, Abstraction source, Stmt stmt, SootMethod dest, ByReferenceBoolean killAll) {
        SourceInfo sourceInfo;
        if (!getManager().getConfig().getInspectSources() && getManager().getSourceSinkManager() != null && (sourceInfo = getManager().getSourceSinkManager().getSourceInfo(stmt, getManager())) != null && !isCallbackOrReturn(sourceInfo.getDefinition())) {
            killAll.value = true;
        }
        if (!getManager().getConfig().getInspectSinks() && getManager().getSourceSinkManager() != null) {
            boolean isSink = getManager().getSourceSinkManager().getSinkInfo(stmt, getManager(), source.getAccessPath()) != null;
            if (isSink) {
                killAll.value = true;
                return null;
            }
            return null;
        }
        return null;
    }

    private boolean isCallbackOrReturn(ISourceSinkDefinition definition) {
        if (definition instanceof MethodSourceSinkDefinition) {
            MethodSourceSinkDefinition methodDef = (MethodSourceSinkDefinition) definition;
            MethodSourceSinkDefinition.CallType callType = methodDef.getCallType();
            return callType == MethodSourceSinkDefinition.CallType.Callback || callType == MethodSourceSinkDefinition.CallType.Return;
        }
        return false;
    }
}
