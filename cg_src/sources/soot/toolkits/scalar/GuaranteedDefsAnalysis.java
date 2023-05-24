package soot.toolkits.scalar;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.toolkits.graph.DominatorsFinder;
import soot.toolkits.graph.MHGDominatorsFinder;
import soot.toolkits.graph.UnitGraph;
/* compiled from: GuaranteedDefs.java */
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/GuaranteedDefsAnalysis.class */
class GuaranteedDefsAnalysis extends ForwardFlowAnalysis<Unit, FlowSet<Value>> {
    private static final FlowSet<Value> EMPTY_SET = new ArraySparseSet();
    private final Map<Unit, FlowSet<Value>> unitToGenerateSet;

    /* JADX INFO: Access modifiers changed from: package-private */
    public GuaranteedDefsAnalysis(UnitGraph graph) {
        super(graph);
        this.unitToGenerateSet = new HashMap((graph.size() * 2) + 1, 0.7f);
        DominatorsFinder<Unit> df = new MHGDominatorsFinder<>(graph);
        Iterator<Unit> it = graph.iterator();
        while (it.hasNext()) {
            Unit s = it.next();
            FlowSet<Value> genSet = EMPTY_SET.mo2534clone();
            for (Unit dom : df.getDominators(s)) {
                for (ValueBox box : dom.getDefBoxes()) {
                    Value val = box.getValue();
                    if (val instanceof Local) {
                        genSet.add(val, genSet);
                    }
                }
            }
            this.unitToGenerateSet.put(s, genSet);
        }
        doAnalysis();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<Value> newInitialFlow() {
        return EMPTY_SET.mo2534clone();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<Value> entryInitialFlow() {
        return EMPTY_SET.mo2534clone();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.FlowAnalysis
    public void flowThrough(FlowSet<Value> in, Unit unit, FlowSet<Value> out) {
        in.union(this.unitToGenerateSet.get(unit), out);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void merge(FlowSet<Value> in1, FlowSet<Value> in2, FlowSet<Value> out) {
        in1.intersection(in2, out);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void copy(FlowSet<Value> source, FlowSet<Value> dest) {
        source.copy(dest);
    }
}
