package soot.toolkits.graph;

import java.util.Iterator;
import java.util.List;
import soot.Unit;
import soot.jimple.Stmt;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;
@Deprecated
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/DominatorAnalysis.class */
public class DominatorAnalysis extends ForwardFlowAnalysis<Unit, FlowSet<Unit>> {
    private UnitGraph g;
    private FlowSet<Unit> allNodes;

    public DominatorAnalysis(UnitGraph g) {
        super(g);
        this.g = g;
        initAllNodes();
        doAnalysis();
    }

    private void initAllNodes() {
        this.allNodes = new ArraySparseSet();
        Iterator<Unit> it = this.g.iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            this.allNodes.add(u);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void merge(FlowSet<Unit> in1, FlowSet<Unit> in2, FlowSet<Unit> out) {
        in1.intersection(in2, out);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void copy(FlowSet<Unit> source, FlowSet<Unit> dest) {
        source.copy(dest);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.FlowAnalysis
    public void flowThrough(FlowSet<Unit> in, Unit s, FlowSet<Unit> out) {
        if (isUnitStartNode(s)) {
            out.clear();
            out.add(s);
            return;
        }
        for (Unit pred : this.g.getPredsOf(s)) {
            FlowSet<Unit> next = getFlowAfter(pred);
            in.intersection(next, in);
        }
        out.intersection(in, out);
        out.add(s);
    }

    private boolean isUnitStartNode(Unit s) {
        if (s.equals(this.g.getHeads().get(0))) {
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<Unit> entryInitialFlow() {
        FlowSet<Unit> fs = new ArraySparseSet<>();
        List<Unit> heads = this.g.getHeads();
        if (heads.size() != 1) {
            throw new RuntimeException("Expect one start node only.");
        }
        fs.add(heads.get(0));
        return fs;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<Unit> newInitialFlow() {
        return this.allNodes.mo2534clone();
    }

    public boolean dominates(Stmt s, Stmt t) {
        return getFlowBefore(t).contains(s);
    }
}
