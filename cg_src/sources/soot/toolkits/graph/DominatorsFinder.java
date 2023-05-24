package soot.toolkits.graph;

import java.util.Collection;
import java.util.List;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/DominatorsFinder.class */
public interface DominatorsFinder<N> {
    DirectedGraph<N> getGraph();

    List<N> getDominators(N n);

    N getImmediateDominator(N n);

    boolean isDominatedBy(N n, N n2);

    boolean isDominatedByAll(N n, Collection<N> collection);
}
