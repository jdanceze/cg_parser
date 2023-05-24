package soot.jimple.infoflow.problems.rules.forward;

import java.util.Collection;
import java.util.Collections;
import soot.SootMethod;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.aliasing.Aliasing;
import soot.jimple.infoflow.aliasing.IAliasingStrategy;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.problems.TaintPropagationResults;
import soot.jimple.infoflow.problems.rules.AbstractTaintPropagationRule;
import soot.jimple.infoflow.util.ByReferenceBoolean;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/problems/rules/forward/StaticPropagationRule.class */
public class StaticPropagationRule extends AbstractTaintPropagationRule {
    public StaticPropagationRule(InfoflowManager manager, Abstraction zeroValue, TaintPropagationResults results) {
        super(manager, zeroValue, results);
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateNormalFlow(Abstraction d1, Abstraction source, Stmt stmt, Stmt destStmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        return null;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateCallFlow(Abstraction d1, Abstraction source, Stmt stmt, SootMethod dest, ByReferenceBoolean killAll) {
        Abstraction newAbs;
        InfoflowConfiguration.StaticFieldTrackingMode staticFieldMode = getManager().getConfig().getStaticFieldTrackingMode();
        if (staticFieldMode == InfoflowConfiguration.StaticFieldTrackingMode.None && (dest.isStaticInitializer() || source.getAccessPath().isStaticFieldRef())) {
            killAll.value = true;
            return null;
        }
        AccessPath ap = source.getAccessPath();
        if (ap.isStaticFieldRef()) {
            boolean isLazyAnalysis = false;
            Aliasing aliasing = getAliasing();
            if (aliasing != null) {
                IAliasingStrategy strategy = aliasing.getAliasingStrategy();
                isLazyAnalysis = strategy != null && strategy.isLazyAnalysis();
            }
            if ((isLazyAnalysis || this.manager.getICFG().isStaticFieldRead(dest, ap.getFirstField())) && (newAbs = source.deriveNewAbstraction(ap, stmt)) != null) {
                return Collections.singleton(newAbs);
            }
            return null;
        }
        return null;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateCallToReturnFlow(Abstraction d1, Abstraction source, Stmt stmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        if (getManager().getConfig().getStaticFieldTrackingMode() == InfoflowConfiguration.StaticFieldTrackingMode.None && source.getAccessPath().isStaticFieldRef()) {
            killAll.value = true;
            return null;
        }
        return null;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateReturnFlow(Collection<Abstraction> callerD1s, Abstraction calleeD1, Abstraction source, Stmt stmt, Stmt retSite, Stmt callSite, ByReferenceBoolean killAll) {
        if (!source.getAccessPath().isStaticFieldRef()) {
            return null;
        }
        if (getManager().getConfig().getStaticFieldTrackingMode() == InfoflowConfiguration.StaticFieldTrackingMode.None && source.getAccessPath().isStaticFieldRef()) {
            killAll.value = true;
            return null;
        }
        return Collections.singleton(source.deriveNewAbstraction(source.getAccessPath(), stmt));
    }
}
