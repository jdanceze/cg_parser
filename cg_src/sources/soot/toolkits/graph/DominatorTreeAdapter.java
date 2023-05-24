package soot.toolkits.graph;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/DominatorTreeAdapter.class */
public class DominatorTreeAdapter<N> implements DirectedGraph<DominatorNode<N>> {
    protected DominatorTree<N> dt;

    @Override // soot.toolkits.graph.DirectedGraph
    public /* bridge */ /* synthetic */ List getSuccsOf(Object obj) {
        return getSuccsOf((DominatorNode) ((DominatorNode) obj));
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public /* bridge */ /* synthetic */ List getPredsOf(Object obj) {
        return getPredsOf((DominatorNode) ((DominatorNode) obj));
    }

    public DominatorTreeAdapter(DominatorTree<N> dt) {
        this.dt = dt;
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List<DominatorNode<N>> getHeads() {
        return this.dt.getHeads();
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List<DominatorNode<N>> getTails() {
        return this.dt.getTails();
    }

    public List<DominatorNode<N>> getPredsOf(DominatorNode<N> node) {
        return Collections.singletonList(this.dt.getParentOf(node));
    }

    public List<DominatorNode<N>> getSuccsOf(DominatorNode<N> node) {
        return this.dt.getChildrenOf(node);
    }

    @Override // soot.toolkits.graph.DirectedGraph, java.lang.Iterable
    public Iterator<DominatorNode<N>> iterator() {
        return this.dt.iterator();
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public int size() {
        return this.dt.size();
    }
}
