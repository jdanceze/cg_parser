package soot.jimple.toolkits.thread.mhp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/DominatorsFinder.class */
public class DominatorsFinder {
    private final Map<Object, FlowSet> unitToDominators = new HashMap();
    private final DirectedGraph peg;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DominatorsFinder(Chain chain, DirectedGraph pegGraph) {
        this.peg = pegGraph;
        find(chain);
    }

    private void find(Chain chain) {
        boolean change;
        FlowSet fullSet = new ArraySparseSet();
        ArraySparseSet arraySparseSet = new ArraySparseSet();
        for (Object obj : chain) {
            fullSet.add(obj);
        }
        List heads = this.peg.getHeads();
        if (heads.size() != 1) {
            throw new RuntimeException("The size of heads of peg is not equal to 1!");
        }
        FlowSet dominators = new ArraySparseSet();
        Object head = heads.get(0);
        dominators.add(head);
        this.unitToDominators.put(head, dominators);
        for (Object n : chain) {
            if (!heads.contains(n)) {
                ArraySparseSet arraySparseSet2 = new ArraySparseSet();
                fullSet.copy(arraySparseSet2);
                this.unitToDominators.put(n, arraySparseSet2);
            }
        }
        System.out.println("===finish init unitToDominators===");
        System.err.println("===finish init unitToDominators===");
        do {
            change = false;
            for (Object n2 : chain) {
                if (!heads.contains(n2)) {
                    fullSet.copy(arraySparseSet);
                    for (Object p : this.peg.getPredsOf(n2)) {
                        FlowSet dom = getDominatorsOf(p);
                        arraySparseSet.intersection(dom);
                    }
                    ArraySparseSet arraySparseSet3 = new ArraySparseSet();
                    FlowSet nSet = new ArraySparseSet();
                    nSet.add(n2);
                    nSet.union(arraySparseSet, arraySparseSet3);
                    FlowSet dominN = getDominatorsOf(n2);
                    if (!arraySparseSet3.equals(dominN)) {
                        change = true;
                    }
                }
            }
        } while (!change);
    }

    public FlowSet getDominatorsOf(Object s) {
        if (!this.unitToDominators.containsKey(s)) {
            throw new RuntimeException("Invalid stmt" + s);
        }
        return this.unitToDominators.get(s);
    }
}
