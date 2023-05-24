package soot.toolkits.graph;

import java.util.List;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/HashReversibleGraph.class */
public class HashReversibleGraph<N> extends HashMutableDirectedGraph<N> implements ReversibleGraph<N> {
    protected boolean reversed;

    public HashReversibleGraph(DirectedGraph<N> dg) {
        this();
        for (N s : dg) {
            addNode(s);
        }
        for (N s2 : dg) {
            for (N t : dg.getSuccsOf(s2)) {
                addEdge(s2, t);
            }
        }
        this.heads.clear();
        this.heads.addAll(dg.getHeads());
        this.tails.clear();
        this.tails.addAll(dg.getTails());
    }

    public HashReversibleGraph() {
        this.reversed = false;
    }

    @Override // soot.toolkits.graph.ReversibleGraph
    public boolean isReversed() {
        return this.reversed;
    }

    @Override // soot.toolkits.graph.ReversibleGraph
    public ReversibleGraph<N> reverse() {
        this.reversed = !this.reversed;
        return this;
    }

    @Override // soot.toolkits.graph.HashMutableDirectedGraph, soot.toolkits.graph.MutableDirectedGraph
    public void addEdge(N from, N to) {
        if (this.reversed) {
            super.addEdge(to, from);
        } else {
            super.addEdge(from, to);
        }
    }

    @Override // soot.toolkits.graph.HashMutableDirectedGraph, soot.toolkits.graph.MutableDirectedGraph
    public void removeEdge(N from, N to) {
        if (this.reversed) {
            super.removeEdge(to, from);
        } else {
            super.removeEdge(from, to);
        }
    }

    @Override // soot.toolkits.graph.HashMutableDirectedGraph, soot.toolkits.graph.MutableDirectedGraph
    public boolean containsEdge(N from, N to) {
        return this.reversed ? super.containsEdge(to, from) : super.containsEdge(from, to);
    }

    @Override // soot.toolkits.graph.HashMutableDirectedGraph, soot.toolkits.graph.DirectedGraph
    public List<N> getHeads() {
        return this.reversed ? super.getTails() : super.getHeads();
    }

    @Override // soot.toolkits.graph.HashMutableDirectedGraph, soot.toolkits.graph.DirectedGraph
    public List<N> getTails() {
        return this.reversed ? super.getHeads() : super.getTails();
    }

    @Override // soot.toolkits.graph.HashMutableDirectedGraph, soot.toolkits.graph.DirectedGraph
    public List<N> getPredsOf(N s) {
        return this.reversed ? super.getSuccsOf(s) : super.getPredsOf(s);
    }

    @Override // soot.toolkits.graph.HashMutableDirectedGraph, soot.toolkits.graph.DirectedGraph
    public List<N> getSuccsOf(N s) {
        return this.reversed ? super.getPredsOf(s) : super.getSuccsOf(s);
    }
}
