package soot.toolkits.graph;

import java.util.Iterator;
import java.util.List;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/DirectedGraph.class */
public interface DirectedGraph<N> extends Iterable<N> {
    List<N> getHeads();

    List<N> getTails();

    List<N> getPredsOf(N n);

    List<N> getSuccsOf(N n);

    int size();

    @Override // java.lang.Iterable
    Iterator<N> iterator();
}
