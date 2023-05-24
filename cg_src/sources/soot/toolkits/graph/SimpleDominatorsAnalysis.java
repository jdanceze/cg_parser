package soot.toolkits.graph;

import java.util.ArrayList;
import java.util.List;
import soot.toolkits.scalar.ArrayPackedSet;
import soot.toolkits.scalar.BoundedFlowSet;
import soot.toolkits.scalar.CollectionFlowUniverse;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;
/* compiled from: SimpleDominatorsFinder.java */
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/SimpleDominatorsAnalysis.class */
class SimpleDominatorsAnalysis<N> extends ForwardFlowAnalysis<N, FlowSet<N>> {
    private final BoundedFlowSet<N> emptySet;
    private final BoundedFlowSet<N> fullSet;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public /* bridge */ /* synthetic */ void copy(Object obj, Object obj2) {
        copy((FlowSet) ((FlowSet) obj), (FlowSet) ((FlowSet) obj2));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.toolkits.scalar.FlowAnalysis
    protected /* bridge */ /* synthetic */ void flowThrough(Object obj, Object obj2, Object obj3) {
        flowThrough((FlowSet<FlowSet>) obj, (FlowSet) obj2, (FlowSet<FlowSet>) obj3);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public /* bridge */ /* synthetic */ void mergeInto(Object obj, Object obj2, Object obj3) {
        mergeInto((SimpleDominatorsAnalysis<N>) obj, (FlowSet<SimpleDominatorsAnalysis<N>>) obj2, (FlowSet<SimpleDominatorsAnalysis<N>>) obj3);
    }

    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    protected /* bridge */ /* synthetic */ void merge(Object obj, Object obj2, Object obj3) {
        merge((FlowSet) ((FlowSet) obj), (FlowSet) ((FlowSet) obj2), (FlowSet) ((FlowSet) obj3));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SimpleDominatorsAnalysis(DirectedGraph<N> graph) {
        super(graph);
        List<N> nodes = new ArrayList<>(graph.size());
        for (N n : graph) {
            nodes.add(n);
        }
        this.emptySet = new ArrayPackedSet(new CollectionFlowUniverse(nodes));
        this.fullSet = (BoundedFlowSet) this.emptySet.mo2534clone();
        this.fullSet.complement();
        doAnalysis();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<N> newInitialFlow() {
        return this.fullSet.mo2534clone();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<N> entryInitialFlow() {
        FlowSet<N> initSet = this.emptySet.mo2534clone();
        for (N h : this.graph.getHeads()) {
            initSet.add(h);
        }
        return initSet;
    }

    protected void flowThrough(FlowSet<N> in, N block, FlowSet<N> out) {
        in.copy(out);
        out.add(block);
    }

    protected void merge(FlowSet<N> in1, FlowSet<N> in2, FlowSet<N> out) {
        in1.intersection(in2, out);
    }

    protected void mergeInto(N block, FlowSet<N> inout, FlowSet<N> in) {
        inout.intersection(in);
    }

    protected void copy(FlowSet<N> source, FlowSet<N> dest) {
        source.copy(dest);
    }
}
