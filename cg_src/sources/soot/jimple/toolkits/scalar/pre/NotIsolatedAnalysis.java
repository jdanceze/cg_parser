package soot.jimple.toolkits.scalar.pre;

import java.util.Map;
import soot.EquivalentValue;
import soot.Unit;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ArrayPackedSet;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.BoundedFlowSet;
import soot.toolkits.scalar.CollectionFlowUniverse;
import soot.toolkits.scalar.FlowSet;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/pre/NotIsolatedAnalysis.class */
public class NotIsolatedAnalysis extends BackwardFlowAnalysis<Unit, FlowSet<EquivalentValue>> {
    private final LatestComputation unitToLatest;
    private final Map<Unit, EquivalentValue> unitToGen;
    private final FlowSet<EquivalentValue> set;

    public NotIsolatedAnalysis(DirectedGraph<Unit> dg, LatestComputation latest, Map<Unit, EquivalentValue> equivRhsMap) {
        this(dg, latest, equivRhsMap, new ArrayPackedSet(new CollectionFlowUniverse(equivRhsMap.values())));
    }

    public NotIsolatedAnalysis(DirectedGraph<Unit> dg, LatestComputation latest, Map<Unit, EquivalentValue> equivRhsMap, BoundedFlowSet<EquivalentValue> set) {
        super(dg);
        this.set = set;
        this.unitToGen = equivRhsMap;
        this.unitToLatest = latest;
        doAnalysis();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<EquivalentValue> newInitialFlow() {
        return this.set.emptySet();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<EquivalentValue> entryInitialFlow() {
        return newInitialFlow();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.FlowAnalysis
    public void flowThrough(FlowSet<EquivalentValue> in, Unit unit, FlowSet<EquivalentValue> out) {
        in.copy(out);
        EquivalentValue rhs = this.unitToGen.get(unit);
        if (rhs != null) {
            out.add(rhs);
        }
        FlowSet<EquivalentValue> latest = this.unitToLatest.getFlowBefore(unit);
        out.difference(latest);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void merge(FlowSet<EquivalentValue> inSet1, FlowSet<EquivalentValue> inSet2, FlowSet<EquivalentValue> outSet) {
        inSet1.union(inSet2, outSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void copy(FlowSet<EquivalentValue> sourceSet, FlowSet<EquivalentValue> destSet) {
        sourceSet.copy(destSet);
    }
}
