package soot.jimple.toolkits.scalar;

import soot.SideEffectTester;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.FlowSet;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/PessimisticAvailableExpressionsAnalysis.class */
public class PessimisticAvailableExpressionsAnalysis extends SlowAvailableExpressionsAnalysis {
    public PessimisticAvailableExpressionsAnalysis(DirectedGraph<Unit> dg, SootMethod m, SideEffectTester st) {
        super(dg);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.toolkits.scalar.SlowAvailableExpressionsAnalysis, soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<Value> newInitialFlow() {
        return this.emptySet.mo2534clone();
    }
}
