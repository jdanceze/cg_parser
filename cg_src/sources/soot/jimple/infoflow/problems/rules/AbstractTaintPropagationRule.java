package soot.jimple.infoflow.problems.rules;

import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.aliasing.Aliasing;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.problems.TaintPropagationResults;
import soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/problems/rules/AbstractTaintPropagationRule.class */
public abstract class AbstractTaintPropagationRule implements ITaintPropagationRule {
    protected final InfoflowManager manager;
    protected final Abstraction zeroValue;
    protected final TaintPropagationResults results;

    public AbstractTaintPropagationRule(InfoflowManager manager, Abstraction zeroValue, TaintPropagationResults results) {
        this.manager = manager;
        this.zeroValue = zeroValue;
        this.results = results;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public InfoflowManager getManager() {
        return this.manager;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Aliasing getAliasing() {
        return this.manager.getAliasing();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Abstraction getZeroValue() {
        return this.zeroValue;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public TaintPropagationResults getResults() {
        return this.results;
    }
}
