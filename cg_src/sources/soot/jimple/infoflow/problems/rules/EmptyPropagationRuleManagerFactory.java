package soot.jimple.infoflow.problems.rules;

import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.problems.TaintPropagationResults;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/problems/rules/EmptyPropagationRuleManagerFactory.class */
public class EmptyPropagationRuleManagerFactory implements IPropagationRuleManagerFactory {
    public static EmptyPropagationRuleManagerFactory INSTANCE = new EmptyPropagationRuleManagerFactory();

    @Override // soot.jimple.infoflow.problems.rules.IPropagationRuleManagerFactory
    public PropagationRuleManager createRuleManager(InfoflowManager manager, Abstraction zeroValue, TaintPropagationResults results) {
        return IdentityPropagationRuleManager.INSTANCE;
    }
}
