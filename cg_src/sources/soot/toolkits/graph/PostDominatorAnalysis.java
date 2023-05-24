package soot.toolkits.graph;

import java.util.Iterator;
import java.util.List;
import soot.Unit;
import soot.jimple.Stmt;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.FlowSet;
@Deprecated
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/PostDominatorAnalysis.class */
public class PostDominatorAnalysis extends BackwardFlowAnalysis<Unit, FlowSet<Unit>> {
    private UnitGraph g;
    private FlowSet<Unit> allNodes;

    public PostDominatorAnalysis(UnitGraph g) {
        super(g);
        this.g = g;
        initAllNodes();
        doAnalysis();
    }

    private void initAllNodes() {
        this.allNodes = new ArraySparseSet();
        Iterator<Unit> it = this.g.iterator();
        while (it.hasNext()) {
            this.allNodes.add(it.next());
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
        if (isUnitEndNode(s)) {
            out.clear();
            out.add(s);
            return;
        }
        for (Unit succ : this.g.getSuccsOf(s)) {
            FlowSet<Unit> next = getFlowBefore(succ);
            in.intersection(next, in);
        }
        out.intersection(in, out);
        out.add(s);
    }

    private boolean isUnitEndNode(Unit s) {
        if (this.g.getTails().contains(s)) {
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<Unit> entryInitialFlow() {
        FlowSet<Unit> fs = new ArraySparseSet<>();
        List<Unit> tails = this.g.getTails();
        fs.add(tails.get(0));
        return fs;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<Unit> newInitialFlow() {
        return this.allNodes.mo2534clone();
    }

    public boolean postDominates(Stmt s, Stmt t) {
        return getFlowBefore(t).contains(s);
    }
}
