package soot.toolkits.graph;

import java.util.Iterator;
import java.util.List;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/InverseGraph.class */
public class InverseGraph<N> implements DirectedGraph<N> {
    protected final DirectedGraph<N> g;

    public InverseGraph(DirectedGraph<N> g) {
        this.g = g;
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List<N> getHeads() {
        return this.g.getTails();
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List<N> getPredsOf(N s) {
        return this.g.getSuccsOf(s);
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List<N> getSuccsOf(N s) {
        return this.g.getPredsOf(s);
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List<N> getTails() {
        return this.g.getHeads();
    }

    @Override // soot.toolkits.graph.DirectedGraph, java.lang.Iterable
    public Iterator<N> iterator() {
        return this.g.iterator();
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public int size() {
        return this.g.size();
    }
}
