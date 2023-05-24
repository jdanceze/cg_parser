package soot.jimple.infoflow.problems.rules;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import soot.SootMethod;
import soot.jimple.Stmt;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.util.ByReferenceBoolean;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/problems/rules/IdentityPropagationRuleManager.class */
public class IdentityPropagationRuleManager extends PropagationRuleManager {
    public static IdentityPropagationRuleManager INSTANCE = new IdentityPropagationRuleManager();

    private IdentityPropagationRuleManager() {
        super(null, null, null, null);
    }

    @Override // soot.jimple.infoflow.problems.rules.PropagationRuleManager
    public Set<Abstraction> applyCallFlowFunction(Abstraction d1, Abstraction source, Stmt stmt, SootMethod dest, ByReferenceBoolean killAll) {
        return Collections.singleton(source);
    }

    @Override // soot.jimple.infoflow.problems.rules.PropagationRuleManager
    public Set<Abstraction> applyCallToReturnFlowFunction(Abstraction d1, Abstraction source, Stmt stmt) {
        return Collections.singleton(source);
    }

    @Override // soot.jimple.infoflow.problems.rules.PropagationRuleManager
    public Set<Abstraction> applyCallToReturnFlowFunction(Abstraction d1, Abstraction source, Stmt stmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll, boolean noAddSource) {
        return Collections.singleton(source);
    }

    @Override // soot.jimple.infoflow.problems.rules.PropagationRuleManager
    public Set<Abstraction> applyNormalFlowFunction(Abstraction d1, Abstraction source, Stmt stmt, Stmt destStmt) {
        return Collections.singleton(source);
    }

    @Override // soot.jimple.infoflow.problems.rules.PropagationRuleManager
    public Set<Abstraction> applyNormalFlowFunction(Abstraction d1, Abstraction source, Stmt stmt, Stmt destStmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        return Collections.singleton(source);
    }

    @Override // soot.jimple.infoflow.problems.rules.PropagationRuleManager
    public Set<Abstraction> applyReturnFlowFunction(Collection<Abstraction> callerD1s, Abstraction calleeD1, Abstraction source, Stmt stmt, Stmt retSite, Stmt callSite, ByReferenceBoolean killAll) {
        return Collections.singleton(source);
    }
}
