package soot.toolkits.scalar;

import java.util.BitSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Timers;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.Orderer;
import soot.toolkits.graph.PseudoTopologicalOrderer;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/ForwardFlowAnalysisExtended.class */
public abstract class ForwardFlowAnalysisExtended<N, A> {
    protected Map<N, Map<N, A>> unitToBeforeFlow;
    protected Map<N, Map<N, A>> unitToAfterFlow;
    protected DirectedGraph<N> graph;

    protected abstract A newInitialFlow();

    protected abstract A entryInitialFlow();

    protected abstract void copy(A a, A a2);

    protected abstract void merge(A a, A a2, A a3);

    protected abstract void flowThrough(A a, N n, N n2, A a2);

    public ForwardFlowAnalysisExtended(DirectedGraph<N> graph) {
        this.graph = graph;
        this.unitToBeforeFlow = new IdentityHashMap((graph.size() * 2) + 1);
        this.unitToAfterFlow = new IdentityHashMap((graph.size() * 2) + 1);
    }

    protected Orderer<N> constructOrderer() {
        return new PseudoTopologicalOrderer();
    }

    protected void merge(N succNode, A in1, A in2, A out) {
        merge(in1, in2, out);
    }

    protected void mergeInto(N succNode, A inout, A in) {
        A tmp = newInitialFlow();
        merge(succNode, inout, in, tmp);
        copy(tmp, inout);
    }

    public A getFromMap(Map<N, Map<N, A>> map, N s, N t) {
        Map<N, A> m = map.get(s);
        if (m != null) {
            return m.get(t);
        }
        return null;
    }

    public void putToMap(Map<N, Map<N, A>> map, N s, N t, A val) {
        Map<N, A> m = map.get(s);
        if (m == null) {
            m = new IdentityHashMap<>();
            map.put(s, m);
        }
        m.put(t, val);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void doAnalysis() {
        List<N> orderedUnits = constructOrderer().newList(this.graph, false);
        int n = orderedUnits.size();
        BitSet head = new BitSet();
        BitSet work = new BitSet(n);
        work.set(0, n);
        Map<N, Integer> index = new IdentityHashMap<>((n * 2) + 1);
        int i = 0;
        for (N s : orderedUnits) {
            int i2 = i;
            i++;
            index.put(s, Integer.valueOf(i2));
            for (N v : this.graph.getSuccsOf(s)) {
                putToMap(this.unitToBeforeFlow, s, v, newInitialFlow());
                putToMap(this.unitToAfterFlow, s, v, newInitialFlow());
            }
        }
        for (N s2 : this.graph.getHeads()) {
            head.set(index.get(s2).intValue());
            for (N v2 : this.graph.getSuccsOf(s2)) {
                putToMap(this.unitToBeforeFlow, s2, v2, entryInitialFlow());
            }
        }
        int numComputations = 0;
        A previousFlow = newInitialFlow();
        int nextSetBit = work.nextSetBit(0);
        while (true) {
            int i3 = nextSetBit;
            if (i3 >= 0) {
                work.clear(i3);
                N s3 = orderedUnits.get(i3);
                for (N v3 : this.graph.getSuccsOf(s3)) {
                    A beforeFlow = getFromMap(this.unitToBeforeFlow, s3, v3);
                    A afterFlow = getFromMap(this.unitToAfterFlow, s3, v3);
                    copy(afterFlow, previousFlow);
                    Iterator<N> it = this.graph.getPredsOf(s3).iterator();
                    if (it.hasNext()) {
                        copy(getFromMap(this.unitToAfterFlow, it.next(), s3), beforeFlow);
                        while (it.hasNext()) {
                            mergeInto(s3, beforeFlow, getFromMap(this.unitToAfterFlow, it.next(), s3));
                        }
                        if (head.get(i3)) {
                            mergeInto(s3, beforeFlow, entryInitialFlow());
                        }
                    }
                    flowThrough(beforeFlow, s3, v3, afterFlow);
                    boolean hasChanged = !previousFlow.equals(afterFlow);
                    if (hasChanged) {
                        int j = index.get(v3).intValue();
                        work.set(j);
                        i3 = Math.min(i3, j - 1);
                    }
                    numComputations++;
                }
                nextSetBit = work.nextSetBit(i3 + 1);
            } else {
                Timers.v().totalFlowNodes += n;
                Timers.v().totalFlowComputations += numComputations;
                return;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public A getFlowBefore(N s) {
        A beforeFlow = null;
        Iterator<N> it = this.graph.getPredsOf(s).iterator();
        if (it.hasNext()) {
            beforeFlow = getFromMap(this.unitToAfterFlow, it.next(), s);
            while (it.hasNext()) {
                mergeInto(s, beforeFlow, getFromMap(this.unitToAfterFlow, it.next(), s));
            }
        }
        return beforeFlow;
    }
}
