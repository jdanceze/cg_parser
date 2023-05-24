package soot.jimple.toolkits.scalar.pre;

import java.util.Map;
import soot.EquivalentValue;
import soot.Unit;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ArrayPackedSet;
import soot.toolkits.scalar.BoundedFlowSet;
import soot.toolkits.scalar.CollectionFlowUniverse;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/pre/DelayabilityAnalysis.class */
public class DelayabilityAnalysis extends ForwardFlowAnalysis<Unit, FlowSet<EquivalentValue>> {
    private final EarliestnessComputation earliest;
    private final Map<Unit, EquivalentValue> unitToKillValue;
    private final BoundedFlowSet<EquivalentValue> set;

    @Deprecated
    public DelayabilityAnalysis(DirectedGraph<Unit> dg) {
        super(dg);
        throw new RuntimeException("Don't use this Constructor!");
    }

    public DelayabilityAnalysis(DirectedGraph<Unit> dg, EarliestnessComputation earliest, Map<Unit, EquivalentValue> equivRhsMap) {
        this(dg, earliest, equivRhsMap, new ArrayPackedSet(new CollectionFlowUniverse(equivRhsMap.values())));
    }

    public DelayabilityAnalysis(DirectedGraph<Unit> dg, EarliestnessComputation earliest, Map<Unit, EquivalentValue> equivRhsMap, BoundedFlowSet<EquivalentValue> set) {
        super(dg);
        this.set = set;
        this.unitToKillValue = equivRhsMap;
        this.earliest = earliest;
        doAnalysis();
        for (Unit currentUnit : dg) {
            getFlowBefore(currentUnit).union(earliest.getFlowBefore(currentUnit));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<EquivalentValue> newInitialFlow() {
        return this.set.topSet();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<EquivalentValue> entryInitialFlow() {
        return this.set.emptySet();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.FlowAnalysis
    public void flowThrough(FlowSet<EquivalentValue> in, Unit u, FlowSet<EquivalentValue> out) {
        in.copy(out);
        out.union(this.earliest.getFlowBefore(u));
        EquivalentValue equiVal = this.unitToKillValue.get(u);
        if (equiVal != null) {
            out.remove(equiVal);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void merge(FlowSet<EquivalentValue> inSet1, FlowSet<EquivalentValue> inSet2, FlowSet<EquivalentValue> outSet) {
        inSet1.intersection(inSet2, outSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void copy(FlowSet<EquivalentValue> sourceSet, FlowSet<EquivalentValue> destSet) {
        sourceSet.copy(destSet);
    }
}
