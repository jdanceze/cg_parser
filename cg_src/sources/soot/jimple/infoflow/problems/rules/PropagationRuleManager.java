package soot.jimple.infoflow.problems.rules;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import soot.SootMethod;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.problems.TaintPropagationResults;
import soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule;
import soot.jimple.infoflow.util.ByReferenceBoolean;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/problems/rules/PropagationRuleManager.class */
public class PropagationRuleManager {
    protected final InfoflowManager manager;
    protected final Abstraction zeroValue;
    protected final TaintPropagationResults results;
    protected final ITaintPropagationRule[] rules;

    public PropagationRuleManager(InfoflowManager manager, Abstraction zeroValue, TaintPropagationResults results, ITaintPropagationRule[] rules) {
        this.manager = manager;
        this.zeroValue = zeroValue;
        this.results = results;
        this.rules = rules;
    }

    public Set<Abstraction> applyNormalFlowFunction(Abstraction d1, Abstraction source, Stmt stmt, Stmt destStmt) {
        return applyNormalFlowFunction(d1, source, stmt, destStmt, null, null);
    }

    public Set<Abstraction> applyNormalFlowFunction(Abstraction d1, Abstraction source, Stmt stmt, Stmt destStmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        ITaintPropagationRule[] iTaintPropagationRuleArr;
        Set<Abstraction> res = null;
        if (killSource == null) {
            killSource = new ByReferenceBoolean();
        }
        for (ITaintPropagationRule rule : this.rules) {
            Collection<Abstraction> ruleOut = rule.propagateNormalFlow(d1, source, stmt, destStmt, killSource, killAll);
            if (killAll != null && killAll.value) {
                return null;
            }
            if (ruleOut != null && !ruleOut.isEmpty()) {
                if (res == null) {
                    res = new HashSet<>(ruleOut);
                } else {
                    res.addAll(ruleOut);
                }
            }
        }
        if ((killAll == null || !killAll.value) && !killSource.value) {
            if (res == null) {
                res = new HashSet<>();
            }
            res.add(source);
        }
        return res;
    }

    public Set<Abstraction> applyCallFlowFunction(Abstraction d1, Abstraction source, Stmt stmt, SootMethod dest, ByReferenceBoolean killAll) {
        ITaintPropagationRule[] iTaintPropagationRuleArr;
        Set<Abstraction> res = null;
        for (ITaintPropagationRule rule : this.rules) {
            Collection<Abstraction> ruleOut = rule.propagateCallFlow(d1, source, stmt, dest, killAll);
            if (killAll.value) {
                return null;
            }
            if (ruleOut != null && !ruleOut.isEmpty()) {
                if (res == null) {
                    res = new HashSet<>(ruleOut);
                } else {
                    res.addAll(ruleOut);
                }
            }
        }
        return res;
    }

    public Set<Abstraction> applyCallToReturnFlowFunction(Abstraction d1, Abstraction source, Stmt stmt) {
        return applyCallToReturnFlowFunction(d1, source, stmt, new ByReferenceBoolean(), null, false);
    }

    public Set<Abstraction> applyCallToReturnFlowFunction(Abstraction d1, Abstraction source, Stmt stmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll, boolean noAddSource) {
        ITaintPropagationRule[] iTaintPropagationRuleArr;
        Set<Abstraction> res = null;
        for (ITaintPropagationRule rule : this.rules) {
            Collection<Abstraction> ruleOut = rule.propagateCallToReturnFlow(d1, source, stmt, killSource, killAll);
            if (killAll != null && killAll.value) {
                return null;
            }
            if (ruleOut != null && !ruleOut.isEmpty()) {
                if (res == null) {
                    res = new HashSet<>(ruleOut);
                } else {
                    res.addAll(ruleOut);
                }
            }
        }
        if (!noAddSource && !killSource.value) {
            if (res == null) {
                res = new HashSet<>();
                res.add(source);
            } else {
                res.add(source);
            }
        }
        return res;
    }

    public Set<Abstraction> applyReturnFlowFunction(Collection<Abstraction> callerD1s, Abstraction calleeD1, Abstraction source, Stmt stmt, Stmt retSite, Stmt callSite, ByReferenceBoolean killAll) {
        ITaintPropagationRule[] iTaintPropagationRuleArr;
        Set<Abstraction> res = null;
        for (ITaintPropagationRule rule : this.rules) {
            Collection<Abstraction> ruleOut = rule.propagateReturnFlow(callerD1s, calleeD1, source, stmt, retSite, callSite, killAll);
            if (killAll != null && killAll.value) {
                return null;
            }
            if (ruleOut != null && !ruleOut.isEmpty()) {
                if (res == null) {
                    res = new HashSet<>(ruleOut);
                } else {
                    res.addAll(ruleOut);
                }
            }
        }
        return res;
    }

    public ITaintPropagationRule[] getRules() {
        return this.rules;
    }
}
