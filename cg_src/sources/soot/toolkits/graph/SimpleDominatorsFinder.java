package soot.toolkits.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.toolkits.scalar.FlowSet;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/SimpleDominatorsFinder.class */
public class SimpleDominatorsFinder<N> implements DominatorsFinder<N> {
    private static final Logger logger = LoggerFactory.getLogger(SimpleDominatorsFinder.class);
    protected final DirectedGraph<N> graph;
    protected final Map<N, FlowSet<N>> nodeToDominators;

    public SimpleDominatorsFinder(DirectedGraph<N> graph) {
        this.graph = graph;
        this.nodeToDominators = new HashMap((graph.size() * 2) + 1, 0.7f);
        SimpleDominatorsAnalysis<N> analysis = new SimpleDominatorsAnalysis<>(graph);
        for (N node : graph) {
            this.nodeToDominators.put(node, analysis.getFlowAfter(node));
        }
    }

    @Override // soot.toolkits.graph.DominatorsFinder
    public DirectedGraph<N> getGraph() {
        return this.graph;
    }

    @Override // soot.toolkits.graph.DominatorsFinder
    public List<N> getDominators(N node) {
        return this.nodeToDominators.get(node).toList();
    }

    @Override // soot.toolkits.graph.DominatorsFinder
    public N getImmediateDominator(N node) {
        if (getGraph().getHeads().contains(node)) {
            return null;
        }
        FlowSet<N> head = this.nodeToDominators.get(node).mo2534clone();
        head.remove(node);
        for (N dominator : head) {
            if (this.nodeToDominators.get(dominator).isSubSet(head)) {
                return dominator;
            }
        }
        return null;
    }

    @Override // soot.toolkits.graph.DominatorsFinder
    public boolean isDominatedBy(N node, N dominator) {
        return this.nodeToDominators.get(node).contains(dominator);
    }

    @Override // soot.toolkits.graph.DominatorsFinder
    public boolean isDominatedByAll(N node, Collection<N> dominators) {
        FlowSet<N> f = this.nodeToDominators.get(node);
        for (N n : dominators) {
            if (!f.contains(n)) {
                return false;
            }
        }
        return true;
    }
}
