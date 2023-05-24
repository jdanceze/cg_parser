package soot.jimple.infoflow.problems.rules;

import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.problems.TaintPropagationResults;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/problems/rules/IPropagationRuleManagerFactory.class */
public interface IPropagationRuleManagerFactory {
    PropagationRuleManager createRuleManager(InfoflowManager infoflowManager, Abstraction abstraction, TaintPropagationResults taintPropagationResults);
}
