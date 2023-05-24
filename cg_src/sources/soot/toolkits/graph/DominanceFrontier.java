package soot.toolkits.graph;

import java.util.List;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/DominanceFrontier.class */
public interface DominanceFrontier<N> {
    List<DominatorNode<N>> getDominanceFrontierOf(DominatorNode<N> dominatorNode);
}
